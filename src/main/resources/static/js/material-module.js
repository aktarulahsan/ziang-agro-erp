let materialRows = [];
let materialUnits = {};
let materialWarehouses = [];
let editingMaterialId = null;
const materialMovementTypes = [
  { code: '101', label: '101 - Goods Receipt' },
  { code: '102', label: '102 - Reversal of Goods Receipt' },
  { code: '201', label: '201 - Goods Issue' },
  { code: '261', label: '261 - Issue to Production' },
  { code: '309', label: '309 - Transfer / Adjustment' },
  { code: '501', label: '501 - Initial Entry of Stock' },
  { code: '551', label: '551 - Scrapping / Damage' },
  { code: '653', label: '653 - Customer Return to Stock' }
];

function materialMoney(value) {
  return Number(value || 0).toFixed(2);
}

function materialQty(value) {
  return Number(value || 0).toFixed(3);
} 

function materialDateTime(value) {
  return value ? new Date(value).toLocaleString() : '-';
}

function materialTypeLabel(value) {
  return (value || '').replaceAll('_', ' ');
}

function materialOptions(rows = materialRows) {
  return rows.map(m => `<option value="${m.id}">${m.productCode} - ${m.productName}</option>`).join('');
}

function materialActionButtons(m) {
  return `<div class="row-actions">
    <button class="btn btn-sm btn-outline-primary" title="Details" onclick="showEncodedRecordDetails('Material Details', '${encodeURIComponent(JSON.stringify(m))}')"><i class="bi bi-eye"></i></button>
    <a class="btn btn-sm btn-outline-warning" title="Edit" href="/pages/material/create.html?id=${m.id}"><i class="bi bi-pencil-square"></i></a>
    <a class="btn btn-sm btn-outline-success" title="Stock view" href="/pages/material/stock.html?productId=${m.id}"><i class="bi bi-boxes"></i></a>
    <a class="btn btn-sm btn-outline-secondary" title="Price setup" href="/pages/material/price-posting.html?productCode=${encodeURIComponent(m.productCode)}"><i class="bi bi-cash-coin"></i></a>
  </div>`;
}

async function loadMaterialLookups() {
  const [units, warehouses] = await Promise.all([api('/api/setup/units'), api('/api/setup/warehouses')]);
  materialUnits = Object.fromEntries(units.map(u => [u.id, u.name]));
  materialWarehouses = warehouses;
  document.querySelectorAll('[data-warehouse-select]').forEach(select => {
    select.innerHTML = materialWarehouses.map(w => `<option value="${w.id}">${w.name}</option>`).join('');
  });
  return { units, warehouses };
}

async function loadMaterials(type = '') {
  const suffix = type ? `?materialType=${type}` : '';
  materialRows = await api(`/api/materials${suffix}`);
  return materialRows;
}

async function loadMaterialStock(warehouseId, type = '') {
  const suffix = type ? `&materialType=${type}` : '';
  materialRows = await api(`/api/materials/stock-summary?warehouseId=${warehouseId}${suffix}`);
  return materialRows;
}

function bindMaterialModuleNav(activeHref) {
  document.querySelectorAll('.module-nav a').forEach(a => {
    if (a.getAttribute('href') === activeHref) a.classList.add('active');
  });
}

async function initMaterialList() {
  await loadMaterialLookups();
  await refreshMaterialList();
}

async function refreshMaterialList() {
  const rows = await loadMaterials(document.getElementById('materialTypeFilter')?.value || '');
  materialListRows.innerHTML = rows.map(m => `
    <tr>
      <td>${m.productCode}</td><td>${m.productName}</td><td>${materialTypeLabel(m.materialType)}</td>
      <td>${materialUnits[m.unitId] || '-'}</td><td>${m.packSize || '-'}</td>
      <td class="text-end">${materialMoney(m.purchasePrice)}</td><td class="text-end">${materialMoney(m.salesPrice)}</td>
      <td class="text-end">${materialMoney(m.retailerPrice)}</td><td>${appStatusBadge(null, m.active)}</td><td class="text-end">${materialActionButtons(m)}</td>
    </tr>`).join('') || '<tr><td colspan="10">No material found.</td></tr>';
}

async function initMaterialCreate() {
  const [{ units }] = await Promise.all([loadMaterialLookups(), loadMaterialSetup()]);
  unitId.innerHTML = units.map(u => `<option value="${u.id}">${u.name}</option>`).join('');
  const editId = new URLSearchParams(window.location.search).get('id');
  if (editId) await loadMaterialForEdit(editId);
  else await generateMaterialCode();
}

async function generateMaterialCode() {
  if (!document.getElementById('productCode') || editingMaterialId) return;
  productCode.value = 'Generating...';
  try {
    productCode.value = await api(`/api/products/next-code?materialType=${encodeURIComponent(materialType.value || 'FINISHED_PRODUCTS')}`);
  } catch (error) {
    productCode.value = '';
    materialMessage.className = 'small text-danger mt-2';
    materialMessage.textContent = error.message;
  }
}

async function loadMaterialSetup() {
  const [categories, subCategories, brands] = await Promise.all([
    api('/api/setup/categories'),
    api('/api/setup/sub-categories'),
    api('/api/setup/brands')
  ]);
  categoryId.innerHTML = '<option value="">Select</option>' + categories.map(c => `<option value="${c.id}">${c.name}</option>`).join('');
  subCategoryId.innerHTML = '<option value="">Select</option>' + subCategories.map(s => `<option value="${s.id}">${s.name}</option>`).join('');
  brandId.innerHTML = '<option value="">Select</option>' + brands.map(b => `<option value="${b.id}">${b.name}</option>`).join('');
}

async function saveMaterialMaster() {
  try {
    materialMessage.className = 'small text-muted mt-2';
    materialMessage.textContent = 'Saving material...';
    const payload = {
      productCode: productCode.value,
      productName: productName.value,
      materialType: materialType.value,
      categoryId: categoryId.value ? Number(categoryId.value) : null,
      subCategoryId: subCategoryId.value ? Number(subCategoryId.value) : null,
      brandId: brandId.value ? Number(brandId.value) : null,
      unitId: unitId.value ? Number(unitId.value) : null,
      packSize: packSize.value,
      purchasePrice: Number(purchasePrice.value || 0),
      salesPrice: Number(salesPrice.value || 0),
      retailerPrice: Number(retailerPrice.value || 0),
      vatPercent: Number(vatPercent.value || 0),
      discountPercent: Number(discountPercent.value || 0),
      batchNumber: batchNumber.value,
      expiryDate: expiryDate.value || null,
      reorderLevel: Number(reorderLevel.value || 0),
      imageUrl: imageUrl.value,
      active: true
    };
    const saved = await api(editingMaterialId ? `/api/products/${editingMaterialId}` : '/api/products', {
      method: editingMaterialId ? 'PUT' : 'POST',
      body: JSON.stringify(payload)
    });
    materialMessage.className = 'small text-success mt-2';
    materialMessage.textContent = `${saved.productCode} ${editingMaterialId ? 'updated' : 'created'}.`;
    clearMaterialMasterForm();
  } catch (error) {
    materialMessage.className = 'small text-danger mt-2';
    materialMessage.textContent = error.message;
  }
}

async function loadMaterialForEdit(id) {
  const m = await api(`/api/products/${id}`);
  editingMaterialId = m.id;
  const title = document.getElementById('materialFormTitle');
  const buttonText = document.getElementById('materialSaveText');
  if (title) title.textContent = 'Edit Material';
  if (buttonText) buttonText.textContent = 'Update Material';
  productCode.value = m.productCode || '';
  productName.value = m.productName || '';
  materialType.value = m.materialType || 'FINISHED_PRODUCTS';
  categoryId.value = m.categoryId || '';
  subCategoryId.value = m.subCategoryId || '';
  brandId.value = m.brandId || '';
  unitId.value = m.unitId || '';
  packSize.value = m.packSize || '';
  purchasePrice.value = Number(m.purchasePrice || 0);
  salesPrice.value = Number(m.salesPrice || 0);
  retailerPrice.value = Number(m.retailerPrice || 0);
  vatPercent.value = Number(m.vatPercent || 0);
  discountPercent.value = Number(m.discountPercent || 0);
  reorderLevel.value = Number(m.reorderLevel || 0);
  batchNumber.value = m.batchNumber || '';
  expiryDate.value = m.expiryDate || '';
  imageUrl.value = m.imageUrl || '';
}

function clearMaterialMasterForm() {
  editingMaterialId = null;
  resetEntryForm(document, {
    materialType: 'RAW_MATERIALS',
    purchasePrice: '0',
    salesPrice: '0',
    retailerPrice: '0',
    vatPercent: '0',
    discountPercent: '0',
    reorderLevel: '0'
  });
  const title = document.getElementById('materialFormTitle');
  const buttonText = document.getElementById('materialSaveText');
  if (title) title.textContent = 'Basic Data and Valuation';
  if (buttonText) buttonText.textContent = 'Save Material';
  if (window.location.search) history.replaceState(null, '', '/pages/material/create.html');
  generateMaterialCode();
}

async function initMaterialStock() {
  await loadMaterialLookups();
  await refreshMaterialStock();
}

async function refreshMaterialStock() {
  const rows = await loadMaterialStock(warehouseId.value, materialTypeFilter.value);
  materialStockRows.innerHTML = rows.map(m => {
    const low = Number(m.currentStock || 0) <= Number(m.reorderLevel || 0);
    return `<tr class="${low ? 'stock-over-row' : ''}">
      <td>${m.productCode}</td><td>${m.productName}</td><td>${materialTypeLabel(m.materialType)}</td>
      <td>${m.packSize || '-'}</td><td class="text-end">${materialQty(m.currentStock)}</td>
      <td class="text-end">${materialQty(m.reorderLevel)}</td><td>${low ? appStatusBadge('LOW_STOCK') : appStatusBadge('ACTIVE')}</td>
      <td class="text-end">${materialActionButtons(m)}</td>
    </tr>`;
  }).join('') || '<tr><td colspan="8">No stock found.</td></tr>';
}

async function initStockPosting() {
  await loadMaterialLookups();
  await loadMaterials();
  stockProductId.innerHTML = materialOptions();
  movementTypeCode.innerHTML = materialMovementTypes.map(m => `<option value="${m.code}">${m.label}</option>`).join('');
}

async function postMaterialStock() {
  try {
    stockMessage.className = 'small text-muted mt-2';
    stockMessage.textContent = 'Posting stock...';
    await api('/api/stock/transactions', { method: 'POST', body: JSON.stringify({
      productId: Number(stockProductId.value),
      warehouseId: Number(warehouseId.value),
      movementTypeCode: movementTypeCode.value,
      quantity: Number(stockQuantity.value || 0),
      batchNumber: stockBatch.value,
      expiryDate: stockExpiry.value || null,
      remarks: stockRemarks.value
    })});
    stockMessage.className = 'small text-success mt-2';
    stockMessage.textContent = 'Stock posted.';
    resetEntryForm(document, { stockQuantity: '1' });
  } catch (error) {
    stockMessage.className = 'small text-danger mt-2';
    stockMessage.textContent = error.message;
  }
}

async function initPricePosting() {
  await loadMaterialLookups();
  await loadMaterials();
  priceProductId.innerHTML = materialOptions();
  const productCode = new URLSearchParams(window.location.search).get('productCode');
  if (productCode) {
    const selected = materialRows.find(m => m.productCode === productCode);
    if (selected) priceProductId.value = selected.id;
  }
  fillMaterialPrice();
  await refreshSelectedPriceAudit();
}

function selectedMaterialPrice() {
  return materialRows.find(m => Number(m.id) === Number(priceProductId.value));
}

function fillMaterialPrice() {
  const m = selectedMaterialPrice();
  if (!m) return;
  pricePurchase.value = Number(m.purchasePrice || 0);
  priceSales.value = Number(m.salesPrice || 0);
  priceRetailer.value = Number(m.retailerPrice || 0);
  priceVat.value = Number(m.vatPercent || 0);
  priceDiscount.value = Number(m.discountPercent || 0);
  const surcharge = document.getElementById('priceSurcharge');
  const discountAmount = document.getElementById('priceDiscountAmount');
  if (surcharge) surcharge.value = 0;
  if (discountAmount) discountAmount.value = 0;
  previewMaterialPrice();
  refreshSelectedPriceAudit();
}

function previewMaterialPrice() {
  const purchase = Number(pricePurchase.value || 0);
  const retailer = Number(priceRetailer.value || 0);
  const surcharge = Number(document.getElementById('priceSurcharge')?.value || 0);
  const discountAmount = Number(document.getElementById('priceDiscountAmount')?.value || 0);
  const discount = retailer * Number(priceDiscount.value || 0) / 100 + discountAmount;
  const vat = (retailer + surcharge - discount) * Number(priceVat.value || 0) / 100;
  priceMargin.textContent = materialMoney(retailer - purchase);
  priceNet.textContent = materialMoney(retailer + surcharge - discount + vat);
}

function priceConditionsFromInputs() {
  const surcharge = Number(document.getElementById('priceSurcharge')?.value || 0);
  const discountAmount = Number(document.getElementById('priceDiscountAmount')?.value || 0);
  const conditions = [];
  if (surcharge > 0) conditions.push({ conditionType: 'SURCHARGE', description: 'Surcharge condition', amount: surcharge });
  if (discountAmount > 0) conditions.push({ conditionType: 'DISCOUNT', description: 'Discount condition', amount: discountAmount });
  return conditions;
}

function renderPricePostResult(result) {
  const stock = document.getElementById('priceStockQty');
  const valueChange = document.getElementById('priceValueChange');
  const documentNumber = document.getElementById('priceDocumentNumber');
  if (stock) stock.textContent = materialQty(result?.totalStockQuantity);
  if (valueChange) valueChange.textContent = materialMoney(result?.inventoryValueChange);
  if (documentNumber) documentNumber.textContent = result?.materialDocumentNumber || 'Not posted';
}

async function saveMaterialPrice() {
  try {
    const selected = selectedMaterialPrice();
    if (!selected?.productCode) throw new Error('Select a material first.');
    priceMessage.className = 'small text-muted mt-2';
    priceMessage.textContent = 'Posting price...';
    const updated = await api(`/api/products/${encodeURIComponent(selected.productCode)}/price`, { method: 'PUT', body: JSON.stringify({
      newPrice: Number(priceRetailer.value || 0),
      reason: document.getElementById('priceRemarks')?.value || 'Material price posting',
      changedBy: localStorage.getItem('agroUsername') || 'system',
      conditions: priceConditionsFromInputs()
    })});
    renderPricePostResult(updated);
    priceMessage.className = 'small text-success mt-2';
    priceMessage.textContent = `${updated.productCode} price posted. ${materialMoney(updated.oldPrice)} -> ${materialMoney(updated.newPrice)} | Inventory value change ${materialMoney(updated.inventoryValueChange)} | Doc ${updated.materialDocumentNumber}`;
    priceRemarks.value = '';
    if (document.getElementById('priceSurcharge')) priceSurcharge.value = '0';
    if (document.getElementById('priceDiscountAmount')) priceDiscountAmount.value = '0';
    await loadMaterials();
    await refreshSelectedPriceAudit();
  } catch (error) {
    priceMessage.className = 'small text-danger mt-2';
    priceMessage.textContent = error.message;
  }
}

async function postBulkMaterialPrice() {
  try {
    bulkPriceMessage.className = 'small text-muted mt-2';
    bulkPriceMessage.textContent = 'Posting bulk prices...';
    const parsed = JSON.parse(bulkPriceJson.value || '[]');
    const items = Array.isArray(parsed) ? parsed : parsed.items;
    if (!Array.isArray(items) || !items.length) throw new Error('Add at least one bulk item.');
    const rows = await api('/api/products/prices/bulk', { method: 'POST', body: JSON.stringify({ items }) });
    renderBulkPriceRows(rows);
    bulkPriceMessage.className = 'small text-success mt-2';
    bulkPriceMessage.textContent = `${rows.length} price row posted.`;
    bulkPriceJson.value = '[]';
    await loadMaterials();
    fillMaterialPrice();
  } catch (error) {
    bulkPriceMessage.className = 'small text-danger mt-2';
    bulkPriceMessage.textContent = error.message;
  }
}

async function uploadBulkMaterialPrice() {
  try {
    const file = bulkPriceExcel.files?.[0];
    if (!file) throw new Error('Select an Excel file first.');
    bulkPriceMessage.className = 'small text-muted mt-2';
    bulkPriceMessage.textContent = 'Uploading Excel prices...';
    const formData = new FormData();
    formData.append('file', file);
    formData.append('changedBy', localStorage.getItem('agroUsername') || 'system');
    const response = await fetch('/api/products/prices/bulk-upload', {
      method: 'POST',
      headers: token() ? { Authorization: `Bearer ${token()}` } : {},
      body: formData
    });
    const json = await response.json();
    if (!response.ok || !json.success) throw new Error(json.message || 'Upload failed');
    renderBulkPriceRows(json.data || []);
    bulkPriceMessage.className = 'small text-success mt-2';
    bulkPriceMessage.textContent = `${(json.data || []).length} Excel row posted.`;
    bulkPriceExcel.value = '';
    await loadMaterials();
    fillMaterialPrice();
  } catch (error) {
    bulkPriceMessage.className = 'small text-danger mt-2';
    bulkPriceMessage.textContent = error.message;
  }
}

function renderBulkPriceRows(rows = []) {
  bulkPriceRows.innerHTML = rows.map(row => `
    <tr>
      <td>${row.message || 'Price updated successfully'}</td>
      <td class="text-end">${materialMoney(row.oldPrice)}</td>
      <td class="text-end">${materialMoney(row.newPrice)}</td>
      <td class="text-end">${materialQty(row.totalStockQuantity)}</td>
      <td class="text-end">${materialMoney(row.inventoryValueChange)}</td>
      <td>${row.materialDocumentNumber || '-'}</td>
    </tr>`).join('') || '<tr><td colspan="6">No bulk posting yet.</td></tr>';
}

async function refreshSelectedPriceAudit() {
  const selected = selectedMaterialPrice();
  if (!selected?.productCode) return;
  try {
    const code = encodeURIComponent(selected.productCode);
    const [history, entries] = await Promise.all([
      api(`/api/products/${code}/price-history`),
      api(`/api/products/${code}/accounting-entries`)
    ]);
    renderSelectedPriceHistory(history);
    renderSelectedAccountingEntries(entries);
  } catch (error) {
    if (document.getElementById('selectedPriceHistoryRows')) {
      selectedPriceHistoryRows.innerHTML = `<tr><td colspan="6">${error.message}</td></tr>`;
    }
    if (document.getElementById('selectedAccountingRows')) {
      selectedAccountingRows.innerHTML = `<tr><td colspan="5">${error.message}</td></tr>`;
    }
  }
}

function renderSelectedPriceHistory(rows = []) {
  if (!document.getElementById('selectedPriceHistoryRows')) return;
  selectedPriceHistoryRows.innerHTML = rows.map(row => `
    <tr>
      <td>${materialDateTime(row.changeDateTime)}</td>
      <td class="text-end">${materialMoney(row.oldPrice)}</td>
      <td class="text-end">${materialMoney(row.newPrice)}</td>
      <td class="text-end">${materialMoney(row.inventoryValueChange)}</td>
      <td>${row.changedBy || '-'}</td>
      <td>${row.changeReason || ''}</td>
    </tr>`).join('') || '<tr><td colspan="6">No price change history for this material.</td></tr>';
}

function renderSelectedAccountingEntries(rows = []) {
  if (!document.getElementById('selectedAccountingRows')) return;
  selectedAccountingRows.innerHTML = rows.map(row => `
    <tr>
      <td>${row.postingDate || '-'}</td>
      <td><span class="badge ${row.entryType === 'DEBIT' ? 'text-bg-success' : 'text-bg-secondary'}">${row.entryType}</span></td>
      <td class="text-end">${materialMoney(row.amount)}</td>
      <td>${row.description || ''}</td>
      <td>${row.materialDocumentNumber || '-'}</td>
    </tr>`).join('') || '<tr><td colspan="5">No accounting entry for this material.</td></tr>';
}

async function initMovementHistory() {
  await loadMaterialLookups();
  await loadMaterials();
  movementProductId.innerHTML = '<option value="">All Materials</option>' + materialOptions();
  movementTypeFilter.innerHTML = '<option value="">All Movement Types</option>' + materialMovementTypes.map(m => `<option value="${m.code}">${m.label}</option>`).join('');
  await refreshMovementHistory();
}

async function refreshMovementHistory() {
  const product = movementProductId.value ? `productId=${movementProductId.value}` : '';
  const movement = movementTypeFilter.value ? `movementTypeCode=${movementTypeFilter.value}` : '';
  const query = [product, movement].filter(Boolean).join('&');
  const rows = await api(`/api/materials/movements${query ? `?${query}` : ''}`);
  movementRows.innerHTML = rows.map(row => `
    <tr>
      <td>${new Date(row.createdAt).toLocaleString()}</td>
      <td>${row.productCode}</td>
      <td>${row.productName}</td>
      <td>${row.warehouseName}</td>
      <td>${row.movementTypeCode || '-'}</td>
      <td>${row.movementTypeName || row.transactionType}</td>
      <td class="text-end">${materialQty(row.quantity)}</td>
      <td>${row.batchNumber || '-'}</td>
      <td>${row.referenceType || 'MANUAL'}</td>
      <td>${row.remarks || ''}</td>
      <td class="text-end"><button class="btn btn-sm btn-outline-primary" title="Details" onclick="showEncodedRecordDetails('Movement Details', '${encodeURIComponent(JSON.stringify(row))}')"><i class="bi bi-eye"></i></button></td>
    </tr>`).join('') || '<tr><td colspan="11">No movement found.</td></tr>';
}

async function initPriceHistory() {
  await loadMaterials();
  priceHistoryProductId.innerHTML = '<option value="">All Materials</option>' + materialOptions();
  await refreshPriceHistory();
}

async function refreshPriceHistory() {
  const selectedRows = priceHistoryProductId.value
    ? materialRows.filter(m => Number(m.id) === Number(priceHistoryProductId.value))
    : materialRows;
  const historyGroups = await Promise.all(selectedRows.map(m =>
    api(`/api/products/${encodeURIComponent(m.productCode)}/price-history`).catch(() => [])
  ));
  const rows = historyGroups.flat().sort((a, b) => new Date(b.changeDateTime || 0) - new Date(a.changeDateTime || 0));
  priceHistoryRows.innerHTML = rows.map(row => `
    <tr>
      <td>${materialDateTime(row.changeDateTime)}</td>
      <td>${row.productCode}</td>
      <td>${row.productName}</td>
      <td class="text-end">${materialMoney(row.oldPrice)}</td>
      <td class="text-end">${materialMoney(row.newPrice)}</td>
      <td class="text-end">${materialMoney(row.inventoryValueBefore)}</td>
      <td class="text-end">${materialMoney(row.inventoryValueAfter)}</td>
      <td class="text-end">${materialMoney(row.inventoryValueChange)}</td>
      <td>${row.materialDocumentNumber || '-'}</td>
      <td>${row.changeReason || ''}</td>
      <td class="text-end"><button class="btn btn-sm btn-outline-primary" title="Details" onclick="showEncodedRecordDetails('Price History Details', '${encodeURIComponent(JSON.stringify(row))}')"><i class="bi bi-eye"></i></button></td>
    </tr>`).join('') || '<tr><td colspan="11">No price history found.</td></tr>';
}
