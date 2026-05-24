const apiBase = '';

function token() {
  return localStorage.getItem('agroToken');
}

function requireAuth() {
  if (!token()) {
    window.location.href = '/login.html';
    return;
  }
  applyMenuPermissions();
  guardCurrentPage();
}

function userRoles() {
  const stored = localStorage.getItem('agroRoles');
  if (stored) {
    try { return JSON.parse(stored); } catch (_) { return []; }
  }
  const jwt = token();
  if (!jwt || !jwt.includes('.')) return [];
  try {
    const payload = JSON.parse(atob(jwt.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
    return payload.roles || [];
  } catch (_) {
    return [];
  }
}

function hasRole(role) {
  return userRoles().includes(role);
}

function menuRules() {
  return [
    { key: 'dashboard', roles: ['ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_SALES_MANAGER','ROLE_SALES_OFFICER','ROLE_STORE_USER','ROLE_DELIVERY_USER','ROLE_ACCOUNTS_USER','ROLE_RETAILER'], match: href => href === '/index.html' || href === '/' },
    { key: 'retailers', roles: ['ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_SALES_MANAGER','ROLE_SALES_OFFICER'], match: href => href.includes('/pages/retailers') || href.includes('/pages/retailer') },
    { key: 'products', roles: ['ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_SALES_MANAGER','ROLE_SALES_OFFICER','ROLE_STORE_USER'], match: href => href.includes('/pages/products') },
    { key: 'material', roles: ['ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_STORE_USER'], match: href => href.includes('/pages/material') || href.includes('/pages/materials') },
    { key: 'orders', roles: ['ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_SALES_MANAGER','ROLE_SALES_OFFICER','ROLE_RETAILER'], match: href => href.includes('/pages/orders') },
    { key: 'invoices', roles: ['ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_SALES_MANAGER','ROLE_ACCOUNTS_USER'], match: href => href.includes('/pages/invoices') || href.includes('/pages/invoice-print') },
    { key: 'deliveries', roles: ['ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_STORE_USER','ROLE_DELIVERY_USER'], match: href => href.includes('/pages/deliveries') },
    { key: 'accounts', roles: ['ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTS_USER'], match: href => href.includes('/pages/accounts') },
    { key: 'settings', roles: ['ROLE_SUPER_ADMIN'], match: href => href.includes('/pages/settings') || href.includes('/swagger-ui') || href.includes('/v3/api-docs') }
  ];
}

function canAccessHref(href) {
  if (!href || href.startsWith('#')) return true;
  const url = new URL(href, window.location.origin);
  const path = url.pathname;
  const rule = menuRules().find(item => item.match(path));
  if (!rule) return true;
  return rule.roles.some(hasRole);
}

function applyMenuPermissions() {
  const nav = document.querySelector('.sidebar nav');
  if (!nav) return;
  if (hasRole('ROLE_SUPER_ADMIN') && !nav.querySelector('[href="/pages/settings/users.html"]')) {
    const settings = document.createElement('a');
    settings.href = '/pages/settings/users.html';
    settings.innerHTML = '<i class="bi bi-gear"></i>Settings';
    nav.appendChild(settings);
  }
  nav.querySelectorAll('a[href]').forEach(link => {
    link.hidden = !canAccessHref(link.getAttribute('href'));
  });
  nav.querySelectorAll('.sidebar-group').forEach(group => {
    group.hidden = [...group.querySelectorAll('a[href]')].every(link => link.hidden);
  });
  document.querySelectorAll('[data-menu-href]').forEach(el => {
    el.hidden = !canAccessHref(el.getAttribute('data-menu-href'));
  });
}

function guardCurrentPage() {
  const path = window.location.pathname;
  if (path === '/' || path === '/index.html') return;
  if (!canAccessHref(path)) {
    window.location.href = '/index.html';
  }
}

function headers() {
  const h = { 'Content-Type': 'application/json' };
  if (token()) h.Authorization = `Bearer ${token()}`;
  return h;
}

async function api(path, options = {}) {
  const response = await fetch(apiBase + path, { ...options, headers: { ...headers(), ...(options.headers || {}) } });
  const json = await response.json();
  if (!response.ok || !json.success) throw new Error(json.message || 'Request failed');
  return json.data;
}

async function login() {
  const message = document.getElementById('loginMessage');
  try {
    const data = await api('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify({
        username: document.getElementById('username').value,
        password: document.getElementById('password').value
      })
    });
    localStorage.setItem('agroToken', data.token);
    localStorage.setItem('agroCompanyId', data.companyId);
    localStorage.setItem('agroCompanyName', data.companyName);
    localStorage.setItem('agroUsername', data.username);
    localStorage.setItem('agroRoles', JSON.stringify([...(data.roles || [])]));
    message.textContent = `Logged in as ${data.username}`;
    window.location.href = '/index.html';
  } catch (error) {
    message.textContent = error.message;
  }
}

function logout() {
  localStorage.removeItem('agroToken');
  localStorage.removeItem('agroCompanyId');
  localStorage.removeItem('agroCompanyName');
  localStorage.removeItem('agroUsername');
  localStorage.removeItem('agroRoles');
  location.reload();
}

async function loadDashboard() {
  if (!token()) return;
  try {
    const data = await api('/api/dashboard/summary');
    Object.keys(data).forEach(key => {
      const el = document.getElementById(key);
      if (el) el.textContent = data[key];
    });
    return data;
  } catch (error) {
    console.warn(error.message);
    return null;
  }
}

async function loadTable(path, tableBodyId, renderRow) {
  const body = document.getElementById(tableBodyId);
  body.innerHTML = '<tr><td colspan="10">Loading...</td></tr>';
  try {
    const page = await api(path);
    body.innerHTML = page.content.map(renderRow).join('');
  } catch (error) {
    body.innerHTML = `<tr><td colspan="10">${error.message}</td></tr>`;
  }
}

function resetEntryForm(root = document, defaults = {}) {
  const scope = typeof root === 'string' ? document.querySelector(root) : root;
  if (!scope) return;
  scope.querySelectorAll('input, textarea, select').forEach(field => {
    if (!field.id || field.readOnly) return;
    if (Object.prototype.hasOwnProperty.call(defaults, field.id)) {
      field.value = defaults[field.id];
    } else if (field.type === 'checkbox' || field.type === 'radio') {
      field.checked = false;
    } else if (field.tagName === 'SELECT') {
      field.selectedIndex = 0;
    } else {
      field.value = field.type === 'number' ? '0' : '';
    }
  });
}

function appStatusBadge(status, active = true) {
  const label = status || (active ? 'ACTIVE' : 'INACTIVE');
  const tone = {
    ACTIVE: 'success',
    INACTIVE: 'secondary',
    PENDING: 'warning',
    APPROVED: 'success',
    INVOICED: 'primary',
    DELIVERED: 'info',
    PAID: 'success',
    PARTIALLY_PAID: 'warning',
    READY_FOR_DELIVERY: 'primary',
    IN_TRANSIT: 'info',
    LOW_STOCK: 'danger',
    OPENING: 'secondary',
    INVOICE: 'primary',
    PAYMENT: 'success',
    RETURN_ADJUSTMENT: 'warning',
    DISCOUNT_ADJUSTMENT: 'info',
    CANCELLED: 'secondary',
    RETURNED: 'danger'
  }[label] || 'light text-dark';
  return `<span class="badge text-bg-${tone}">${String(label).replaceAll('_', ' ')}</span>`;
}

function showRecordDetails(title, record) {
  let modal = document.getElementById('recordDetailsModal');
  if (!modal) {
    document.body.insertAdjacentHTML('beforeend', `
      <div class="modal fade" id="recordDetailsModal" tabindex="-1">
        <div class="modal-dialog modal-lg"><div class="modal-content">
          <div class="modal-header"><h5 class="modal-title" id="recordDetailsTitle">Details</h5><button class="btn-close" data-bs-dismiss="modal"></button></div>
          <div class="modal-body"><div class="table-panel"><table class="table table-sm align-middle"><tbody id="recordDetailsRows"></tbody></table></div></div>
        </div></div>
      </div>`);
    modal = document.getElementById('recordDetailsModal');
  }
  recordDetailsTitle.textContent = title;
  recordDetailsRows.innerHTML = Object.entries(record || {}).map(([key, value]) =>
    `<tr><th>${key.replace(/([A-Z])/g, ' $1')}</th><td>${value === null || value === undefined || value === '' ? '-' : value}</td></tr>`
  ).join('');
  bootstrap.Modal.getOrCreateInstance(modal).show();
}

function showEncodedRecordDetails(title, encodedRecord) {
  showRecordDetails(title, JSON.parse(decodeURIComponent(encodedRecord)));
}
