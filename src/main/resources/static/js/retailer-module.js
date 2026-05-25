let retailerRows = [];
let retailerTerritories = [];
let editingRetailerId = null;
let editingRetailerActive = true;

function retailerMoney(value) {
  return Number(value || 0).toFixed(2);
}

function retailerText(value) {
  return value || '-';
}

function retailerTerritoryName(id) {
  const territory = retailerTerritories.find(t => Number(t.id) === Number(id));
  return territory ? territory.name : '-';
}

function retailerSearchText(r) {
  return [
    r.retailerCode,
    r.retailerName,
    r.ownerName,
    r.mobileNumber,
    r.email,
    r.marketName,
    retailerTerritoryName(r.territoryId),
    r.currentDueBalance
  ].join(' ').toLowerCase();
}

function retailerOptionValue(r) {
  return [r.retailerName, r.retailerCode, r.mobileNumber, retailerTerritoryName(r.territoryId)]
    .filter(Boolean)
    .join(' | ');
}

function retailerActionButtons(r) {
  return `<div class="row-actions">
    <button class="btn btn-sm btn-outline-primary" title="Details" onclick="showEncodedRecordDetails('Retailer Details', '${encodeURIComponent(JSON.stringify(r))}')"><i class="bi bi-eye"></i></button>
    <a class="btn btn-sm btn-outline-warning" title="Edit" href="/pages/retailer/create.html?id=${r.id}"><i class="bi bi-pencil-square"></i></a>
    <a class="btn btn-sm btn-outline-success" title="Create order" href="/pages/orders/create.html?retailerId=${r.id}"><i class="bi bi-cart-plus"></i></a>
    <a class="btn btn-sm btn-outline-secondary" title="Journal" href="/pages/retailer/ledger.html?retailerId=${r.id}"><i class="bi bi-journal-text"></i></a>
    <button class="btn btn-sm btn-outline-danger" title="Deactivate" onclick="deleteRetailer(${r.id})"><i class="bi bi-slash-circle"></i></button>
  </div>`;
}

async function loadRetailerLookups() {
  retailerTerritories = await api('/api/setup/territories');
  document.querySelectorAll('[data-territory-select]').forEach(select => {
    select.innerHTML = '<option value="">Select</option>' + retailerTerritories.map(t => `<option value="${t.id}">${t.name} (${t.code || '-'})</option>`).join('');
  });
}

async function loadRetailers() {
  const page = await api('/api/retailers?size=200');
  retailerRows = page.content || [];
  return retailerRows;
}

async function initRetailerList() {
  await loadRetailerLookups();
  await loadRetailers();
  retailerAutoList.innerHTML = retailerRows.map(r => `<option value="${retailerOptionValue(r)}"></option>`).join('');
  renderRetailerList();
}

function filteredRetailers() {
  const q = (retailerSearch.value || '').toLowerCase().trim();
  const territory = territoryFilter.value;
  const status = statusFilter.value;
  return retailerRows.filter(r =>
    (!q || retailerSearchText(r).includes(q)) &&
    (!territory || Number(r.territoryId) === Number(territory)) &&
    (!status || String(r.active) === status)
  );
}

function renderRetailerSummary(rows) {
  if (!document.getElementById('totalRetailers')) return;
  totalRetailers.textContent = rows.length;
  activeRetailers.textContent = rows.filter(r => r.active).length;
  dueRetailers.textContent = rows.filter(r => Number(r.currentDueBalance || 0) > 0).length;
  totalDue.textContent = retailerMoney(rows.reduce((sum, r) => sum + Number(r.currentDueBalance || 0), 0));
}

function renderRetailerList() {
  const rows = filteredRetailers();
  renderRetailerSummary(rows);
  retailerListMessage.textContent = rows.length ? `${rows.length} retailer(s) found` : 'No retailer found';
  retailerRowsBody.innerHTML = rows.map(r => `
    <tr>
      <td><strong>${r.retailerCode}</strong><div class="text-muted small">${retailerTerritoryName(r.territoryId)}</div></td>
      <td>${r.retailerName}<div class="text-muted small">${retailerText(r.address)}</div></td>
      <td>${retailerText(r.ownerName)}<div class="text-muted small">${retailerText(r.mobileNumber)}</div></td>
      <td>${retailerText(r.email)}<div class="text-muted small">${retailerText(r.marketName)}</div></td>
      <td class="text-end">${retailerMoney(r.creditLimit)}</td>
      <td class="text-end">${retailerMoney(r.currentDueBalance)}</td>
      <td>${appStatusBadge(null, r.active)}</td>
      <td class="text-end">${retailerActionButtons(r)}</td>
    </tr>`).join('') || '<tr><td colspan="8">No retailer found.</td></tr>';
}

async function initRetailerCreate() {
  await loadRetailerLookups();
  const id = new URLSearchParams(window.location.search).get('id');
  if (id) await loadRetailerForEdit(id);
  else await generateRetailerCode();
}

async function generateRetailerCode() {
  if (!document.getElementById('retailerCode') || editingRetailerId) return;
  retailerCode.value = 'Generating...';
  try {
    retailerCode.value = await api('/api/retailers/next-code');
  } catch (error) {
    retailerCode.value = '';
    retailerMessage.className = 'small text-danger mt-2';
    retailerMessage.textContent = error.message;
  }
}

function retailerPayload() {
  return {
    retailerCode: retailerCode.value,
    retailerName: retailerName.value,
    ownerName: ownerName.value,
    mobileNumber: mobileNumber.value,
    email: email.value,
    address: address.value,
    territoryId: territoryId.value ? Number(territoryId.value) : null,
    marketName: marketName.value,
    creditLimit: Number(creditLimit.value || 0),
    openingBalance: Number(openingBalance.value || 0),
    active: editingRetailerId ? editingRetailerActive : true
  };
}

async function saveRetailerMaster() {
  try {
    retailerMessage.className = 'small text-muted mt-2';
    retailerMessage.textContent = 'Saving retailer...';
    const saved = await api(editingRetailerId ? `/api/retailers/${editingRetailerId}` : '/api/retailers', {
      method: editingRetailerId ? 'PUT' : 'POST',
      body: JSON.stringify(retailerPayload())
    });
    retailerMessage.className = 'small text-success mt-2';
    retailerMessage.textContent = `${saved.retailerCode} ${editingRetailerId ? 'updated' : 'created'}.`;
    clearRetailerMasterForm();
  } catch (error) {
    retailerMessage.className = 'small text-danger mt-2';
    retailerMessage.textContent = error.message;
  }
}

async function loadRetailerForEdit(id) {
  const r = await api(`/api/retailers/${id}`);
  editingRetailerId = r.id;
  editingRetailerActive = r.active;
  if (document.getElementById('retailerFormTitle')) retailerFormTitle.textContent = 'Edit Retailer';
  if (document.getElementById('retailerSaveText')) retailerSaveText.textContent = 'Update Retailer';
  retailerCode.value = r.retailerCode || '';
  retailerName.value = r.retailerName || '';
  ownerName.value = r.ownerName || '';
  mobileNumber.value = r.mobileNumber || '';
  email.value = r.email || '';
  territoryId.value = r.territoryId || '';
  marketName.value = r.marketName || '';
  creditLimit.value = Number(r.creditLimit || 0);
  openingBalance.value = Number(r.openingBalance || 0);
  currentDueBalance.value = retailerMoney(r.currentDueBalance);
  address.value = r.address || '';
}

function clearRetailerMasterForm() {
  editingRetailerId = null;
  editingRetailerActive = true;
  resetEntryForm(document, { creditLimit: '0', openingBalance: '0', currentDueBalance: '0.00' });
  if (document.getElementById('currentDueBalance')) currentDueBalance.value = '0.00';
  if (document.getElementById('retailerFormTitle')) retailerFormTitle.textContent = 'Create Retailer';
  if (document.getElementById('retailerSaveText')) retailerSaveText.textContent = 'Save Retailer';
  if (window.location.search) history.replaceState(null, '', '/pages/retailer/create.html');
  generateRetailerCode();
}

async function deleteRetailer(id) {
  if (!confirm('Deactivate this retailer?')) return;
  await api(`/api/retailers/${id}`, { method: 'DELETE' });
  await loadRetailers();
  renderRetailerList();
}

async function initRetailerLedger() {
  await loadRetailerLookups();
  await loadRetailers();
  ledgerRetailerList.innerHTML = retailerRows.map(r => `<option value="${retailerOptionValue(r)}"></option>`).join('');
  const retailerId = new URLSearchParams(window.location.search).get('retailerId');
  if (retailerId) {
    const r = retailerRows.find(row => Number(row.id) === Number(retailerId));
    if (r) {
      ledgerRetailerSearch.value = retailerOptionValue(r);
      selectedLedgerRetailerId.value = r.id;
    }
  }
  resolveLedgerRetailer();
  if (selectedLedgerRetailerId.value) await loadRetailerLedger();
}

function resolveLedgerRetailer() {
  const q = (ledgerRetailerSearch.value || '').toLowerCase();
  const r = retailerRows.find(row =>
    retailerOptionValue(row).toLowerCase() === q ||
    [row.retailerCode, row.retailerName, row.mobileNumber].some(v => String(v || '').toLowerCase() && q.includes(String(v || '').toLowerCase()))
  );
  selectedLedgerRetailerId.value = r ? r.id : '';
  ledgerRetailerName.textContent = r ? r.retailerName : '-';
  ledgerRetailerCode.textContent = r ? r.retailerCode : '-';
  ledgerRetailerDue.textContent = r ? retailerMoney(r.currentDueBalance) : '0.00';
}

async function loadRetailerLedger() {
  resolveLedgerRetailer();
  if (!selectedLedgerRetailerId.value) {
    ledgerRows.innerHTML = '<tr><td colspan="8">Select a retailer first.</td></tr>';
    return;
  }
  const rows = await api(`/api/ledger/retailers/${selectedLedgerRetailerId.value}`);
  ledgerRows.innerHTML = rows.map(row => `
    <tr>
      <td>${row.transactionDate || '-'}</td>
      <td>${appStatusBadge(row.ledgerType)}</td>
      <td class="text-end">${retailerMoney(row.debitAmount)}</td>
      <td class="text-end">${retailerMoney(row.creditAmount)}</td>
      <td class="text-end">${retailerMoney(row.runningBalance)}</td>
      <td>${row.referenceType || '-'} #${row.referenceId || '-'}</td>
      <td>${row.narration || ''}</td>
      <td class="text-end"><button class="btn btn-sm btn-outline-primary" onclick="showEncodedRecordDetails('Retailer Journal Details', '${encodeURIComponent(JSON.stringify(row))}')"><i class="bi bi-eye"></i></button></td>
    </tr>`).join('') || '<tr><td colspan="8">No journal history found.</td></tr>';
}
