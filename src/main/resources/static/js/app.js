const apiBase = '';

const ROLE = {
  SUPER_ADMIN: 'ROLE_SUPER_ADMIN',
  ADMIN: 'ROLE_ADMIN',
  SALES_MANAGER: 'ROLE_SALES_MANAGER',
  SALES_OFFICER: 'ROLE_SALES_OFFICER',
  ACCOUNTS_USER: 'ROLE_ACCOUNTS_USER',
  STORE_USER: 'ROLE_STORE_USER',
  DELIVERY_USER: 'ROLE_DELIVERY_USER',
  RETAILER: 'ROLE_RETAILER'
};

const ALL_APP_ROLES = Object.values(ROLE);
const SALES_ROLES = [ROLE.SUPER_ADMIN, ROLE.ADMIN, ROLE.SALES_MANAGER, ROLE.SALES_OFFICER];
const STOCK_ROLES = [ROLE.SUPER_ADMIN, ROLE.ADMIN, ROLE.STORE_USER];
const BILLING_ROLES = [ROLE.SUPER_ADMIN, ROLE.ADMIN, ROLE.SALES_MANAGER, ROLE.ACCOUNTS_USER];
const DELIVERY_ROLES = [ROLE.SUPER_ADMIN, ROLE.ADMIN, ROLE.SALES_MANAGER, ROLE.STORE_USER, ROLE.DELIVERY_USER];

const APP_MENU = [
  { key: 'dashboard', label: 'Dashboard', icon: 'bi-speedometer2', href: '/index.html', roles: ALL_APP_ROLES, match: href => href === '/index.html' || href === '/' },
  { key: 'retailers', label: 'Retailer', icon: 'bi-shop', href: '/pages/retailer/index.html', roles: SALES_ROLES, match: href => href.includes('/pages/retailers') || href.includes('/pages/retailer') },
  { key: 'products', label: 'Products', icon: 'bi-box-seam', href: '/pages/products.html', roles: STOCK_ROLES, match: href => href.includes('/pages/products') },
  { key: 'material', label: 'Material', icon: 'bi-boxes', href: '/pages/material/index.html', roles: STOCK_ROLES, match: href => href.includes('/pages/material') || href.includes('/pages/materials') },
  {
    key: 'orders',
    label: 'Orders',
    icon: 'bi-cart-check',
    roles: [ROLE.SUPER_ADMIN, ROLE.ADMIN, ROLE.SALES_MANAGER, ROLE.SALES_OFFICER, ROLE.RETAILER],
    match: href => href.includes('/pages/orders'),
    children: [
      { key: 'orders-list', label: 'Order List', icon: 'bi-list-ul', href: '/pages/orders/list.html' },
      { key: 'orders-create', label: 'Create Order', icon: 'bi-plus-circle', href: '/pages/orders/create.html' }
    ]
  },
  {
    key: 'invoices',
    label: 'Invoices',
    icon: 'bi-receipt',
    roles: BILLING_ROLES,
    match: href => href.includes('/pages/invoices') || href.includes('/pages/invoice-print'),
    children: [
      { key: 'invoices-list', label: 'Invoice List', icon: 'bi-list-ul', href: '/pages/invoices/list.html' },
      { key: 'invoices-create', label: 'Create Invoice', icon: 'bi-plus-circle', href: '/pages/invoices/create.html' }
    ]
  },
  {
    key: 'deliveries',
    label: 'Deliveries',
    icon: 'bi-truck',
    roles: DELIVERY_ROLES,
    match: href => href.includes('/pages/deliveries'),
    children: [
      { key: 'deliveries-list', label: 'Delivery List', icon: 'bi-list-ul', href: '/pages/deliveries/list.html' },
      { key: 'deliveries-create', label: 'Create Delivery', icon: 'bi-plus-circle', href: '/pages/deliveries/create.html' }
    ]
  },
  { key: 'accounts', label: 'Accounts', icon: 'bi-journal-text', href: '/pages/accounts.html', roles: [ROLE.SUPER_ADMIN, ROLE.ACCOUNTS_USER], match: href => href.includes('/pages/accounts') },
  { key: 'reports', label: 'Reports', icon: 'bi-graph-up-arrow', href: '/pages/reports.html', roles: [ROLE.SUPER_ADMIN, ROLE.ADMIN, ROLE.ACCOUNTS_USER, ROLE.STORE_USER], match: href => href.includes('/pages/reports') },
  { key: 'settings', label: 'Settings', icon: 'bi-person-gear', href: '/pages/settings/users.html', roles: [ROLE.SUPER_ADMIN], match: href => href.includes('/pages/settings') || href.includes('/swagger-ui') || href.includes('/v3/api-docs') }
];

function token() {
  return localStorage.getItem('agroToken');
}

function requireAuth() {
  if (!token()) {
    window.location.href = '/login.html';
    return;
  }
  renderSidebarMenu();
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
  return APP_MENU.map(({ key, roles, match }) => ({ key, roles, match }));
}

function canAccessMenuItem(item) {
  return !item.roles || item.roles.some(hasRole);
}

function currentPath() {
  return window.location.pathname === '/' ? '/index.html' : window.location.pathname;
}

function isMenuItemActive(item) {
  if (item.match && item.match(currentPath())) return true;
  if (item.href && new URL(item.href, window.location.origin).pathname === currentPath()) return true;
  return (item.children || []).some(isMenuItemActive);
}

function escapeHtml(value) {
  return String(value || '').replace(/[&<>"']/g, char => ({
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#39;'
  }[char]));
}

function roleLabel(role) {
  return String(role || 'USER').replace('ROLE_', '').replaceAll('_', ' ');
}

function menuLink(item, extraClass = '') {
  const activeClass = isMenuItemActive(item) ? ' active' : '';
  return `<a class="${extraClass}${activeClass}" href="${item.href}"><i class="bi ${item.icon}"></i><span>${escapeHtml(item.label)}</span></a>`;
}

function renderSidebarMenu() {
  const sidebar = document.querySelector('.sidebar');
  if (!sidebar) return;

  const menuHtml = APP_MENU.map(item => {
    if (item.children) {
      const visibleChildren = item.children.filter(child => canAccessMenuItem({ ...child, roles: child.roles || item.roles }));
      if (!canAccessMenuItem(item) || !visibleChildren.length) return '';
      const openClass = isMenuItemActive(item) ? ' active' : '';
      return `<div class="sidebar-group${openClass}">
        <div class="sidebar-section-title"><i class="bi ${item.icon}"></i><span>${escapeHtml(item.label)}</span></div>
        <div class="sidebar-submenu">${visibleChildren.map(child => menuLink(child)).join('')}</div>
      </div>`;
    }
    return canAccessMenuItem(item) ? menuLink(item) : '';
  }).join('');

  const roles = userRoles().map(roleLabel).join(', ') || 'User';
  const username = localStorage.getItem('agroUsername') || 'Signed in';

  sidebar.innerHTML = `
    <div class="brand"><i class="bi bi-flower1"></i><span>Agro ERP</span></div>
    <nav class="sidebar-menu" aria-label="Main menu">${menuHtml}</nav>
    <div class="sidebar-user">
      <div>
        <strong>${escapeHtml(username)}</strong>
        <span>${escapeHtml(roles)}</span>
      </div>
      <button class="sidebar-logout" type="button" title="Logout" aria-label="Logout"><i class="bi bi-box-arrow-right"></i></button>
    </div>`;

  sidebar.querySelector('.sidebar-logout')?.addEventListener('click', logout);
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
