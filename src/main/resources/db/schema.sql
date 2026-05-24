CREATE DATABASE IF NOT EXISTS agro_erp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE agro_erp;

CREATE TABLE companies (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  company_code VARCHAR(40) NOT NULL UNIQUE,
  company_name VARCHAR(160) NOT NULL,
  trade_license_no VARCHAR(120),
  tax_registration_no VARCHAR(120),
  mobile_number VARCHAR(30),
  email VARCHAR(120),
  address VARCHAR(500),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL
);

CREATE TABLE roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(60) NOT NULL UNIQUE,
  description VARCHAR(255),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL
);

CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(80) NOT NULL UNIQUE,
  email VARCHAR(120) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(120) NOT NULL,
  company_id BIGINT NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  CONSTRAINT fk_user_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE user_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE territories (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(40) NOT NULL UNIQUE,
  name VARCHAR(120) NOT NULL,
  market_name VARCHAR(120),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL
);

CREATE TABLE retailers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  retailer_code VARCHAR(40) NOT NULL UNIQUE,
  retailer_name VARCHAR(160) NOT NULL,
  owner_name VARCHAR(160),
  mobile_number VARCHAR(30) NOT NULL,
  email VARCHAR(120),
  address VARCHAR(500),
  territory_id BIGINT,
  market_name VARCHAR(120),
  credit_limit DECIMAL(14,2) NOT NULL DEFAULT 0,
  opening_balance DECIMAL(14,2) NOT NULL DEFAULT 0,
  current_due_balance DECIMAL(14,2) NOT NULL DEFAULT 0,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  INDEX idx_retailer_mobile (mobile_number),
  INDEX idx_retailer_territory (territory_id),
  CONSTRAINT fk_retailer_territory FOREIGN KEY (territory_id) REFERENCES territories(id)
);

CREATE TABLE categories (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(80) NOT NULL UNIQUE,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL
);

CREATE TABLE sub_categories (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  category_id BIGINT NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  INDEX idx_sub_category_category (category_id),
  CONSTRAINT fk_sub_category_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE brands (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(80) NOT NULL UNIQUE,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL
);

CREATE TABLE units (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(30) NOT NULL UNIQUE,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL
);

CREATE TABLE products (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_code VARCHAR(50) NOT NULL UNIQUE,
  product_name VARCHAR(180) NOT NULL,
  material_type ENUM('RAW_MATERIALS','SEMIFINISHED_PRODUCTS','FINISHED_PRODUCTS','TRADING_PRODUCT') NOT NULL DEFAULT 'FINISHED_PRODUCTS',
  category_id BIGINT,
  sub_category_id BIGINT,
  brand_id BIGINT,
  unit_id BIGINT,
  pack_size VARCHAR(80),
  purchase_price DECIMAL(14,2) NOT NULL DEFAULT 0,
  sales_price DECIMAL(14,2) NOT NULL DEFAULT 0,
  retailer_price DECIMAL(14,2) NOT NULL DEFAULT 0,
  current_price DECIMAL(14,2) NOT NULL DEFAULT 0,
  total_stock_quantity DECIMAL(14,3) NOT NULL DEFAULT 0,
  current_inventory_value DECIMAL(16,2) NOT NULL DEFAULT 0,
  vat_percent DECIMAL(8,2) NOT NULL DEFAULT 0,
  discount_percent DECIMAL(8,2) NOT NULL DEFAULT 0,
  batch_number VARCHAR(80),
  expiry_date DATE,
  image_url VARCHAR(500),
  reorder_level DECIMAL(14,3) NOT NULL DEFAULT 0,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  INDEX idx_product_name (product_name),
  INDEX idx_product_category (category_id),
  CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id),
  CONSTRAINT fk_product_sub_category FOREIGN KEY (sub_category_id) REFERENCES sub_categories(id),
  CONSTRAINT fk_product_brand FOREIGN KEY (brand_id) REFERENCES brands(id),
  CONSTRAINT fk_product_unit FOREIGN KEY (unit_id) REFERENCES units(id)
);

CREATE TABLE product_prices (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL,
  retailer_id BIGINT NULL,
  territory_id BIGINT NULL,
  purchase_price DECIMAL(14,2) NOT NULL DEFAULT 0,
  sales_price DECIMAL(14,2) NOT NULL DEFAULT 0,
  retailer_price DECIMAL(14,2) NOT NULL DEFAULT 0,
  valid_from DATE NOT NULL,
  valid_to DATE,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  INDEX idx_price_product_validity (product_id, valid_from, valid_to),
  CONSTRAINT fk_price_product FOREIGN KEY (product_id) REFERENCES products(id),
  CONSTRAINT fk_price_retailer FOREIGN KEY (retailer_id) REFERENCES retailers(id),
  CONSTRAINT fk_price_territory FOREIGN KEY (territory_id) REFERENCES territories(id)
);

CREATE TABLE warehouses (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(40) NOT NULL UNIQUE,
  name VARCHAR(120) NOT NULL,
  address VARCHAR(255),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL
);

CREATE TABLE stock_transactions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL,
  warehouse_id BIGINT NOT NULL,
  transaction_type ENUM('OPENING','RECEIVE','ISSUE','SALES_OUT','RETURN_IN','DAMAGE','ADJUSTMENT') NOT NULL,
  quantity DECIMAL(14,3) NOT NULL,
  batch_number VARCHAR(80),
  expiry_date DATE,
  reference_type VARCHAR(50),
  reference_id BIGINT,
  remarks VARCHAR(255),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  INDEX idx_stock_product_warehouse (product_id, warehouse_id),
  INDEX idx_stock_reference (reference_type, reference_id),
  CONSTRAINT fk_stock_product FOREIGN KEY (product_id) REFERENCES products(id),
  CONSTRAINT fk_stock_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(id)
);

CREATE TABLE discounts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  product_id BIGINT NULL,
  category_id BIGINT NULL,
  retailer_id BIGINT NULL,
  discount_type ENUM('PERCENTAGE','FIXED') NOT NULL,
  discount_value DECIMAL(14,2) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  INDEX idx_discount_validity (start_date, end_date, active)
);

CREATE TABLE offers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  offer_type ENUM('SEASONAL','BOGO','QUANTITY_BASED') NOT NULL,
  product_id BIGINT NULL,
  min_quantity DECIMAL(14,3),
  free_product_id BIGINT NULL,
  free_quantity DECIMAL(14,3),
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL
);

CREATE TABLE orders (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_number VARCHAR(40) NOT NULL UNIQUE,
  order_date DATE NOT NULL,
  retailer_id BIGINT NOT NULL,
  status ENUM('DRAFT','PENDING','APPROVED','INVOICED','DELIVERED','CANCELLED','RETURNED') NOT NULL,
  gross_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
  discount_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
  vat_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
  net_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  INDEX idx_order_date_status (order_date, status),
  CONSTRAINT fk_order_retailer FOREIGN KEY (retailer_id) REFERENCES retailers(id)
);

CREATE TABLE order_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity DECIMAL(14,3) NOT NULL,
  unit_price DECIMAL(14,2) NOT NULL,
  discount_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
  vat_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
  line_total DECIMAL(14,2) NOT NULL DEFAULT 0,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id),
  CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE invoices (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  invoice_number VARCHAR(40) NOT NULL UNIQUE,
  invoice_date DATE NOT NULL,
  retailer_id BIGINT NOT NULL,
  order_id BIGINT NOT NULL UNIQUE,
  status ENUM('PENDING','PARTIALLY_PAID','PAID','CANCELLED') NOT NULL,
  net_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
  paid_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
  due_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  INDEX idx_invoice_date_status (invoice_date, status),
  CONSTRAINT fk_invoice_retailer FOREIGN KEY (retailer_id) REFERENCES retailers(id),
  CONSTRAINT fk_invoice_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE invoice_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  invoice_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity DECIMAL(14,3) NOT NULL,
  unit_price DECIMAL(14,2) NOT NULL,
  line_total DECIMAL(14,2) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  CONSTRAINT fk_invoice_item_invoice FOREIGN KEY (invoice_id) REFERENCES invoices(id),
  CONSTRAINT fk_invoice_item_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE deliveries (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  delivery_number VARCHAR(40) NOT NULL UNIQUE,
  delivery_date DATE NOT NULL,
  invoice_id BIGINT NOT NULL,
  retailer_id BIGINT NOT NULL,
  delivery_address VARCHAR(500),
  vehicle_details VARCHAR(160),
  delivery_person VARCHAR(120),
  delivery_contact_no VARCHAR(40),
  route_name VARCHAR(120),
  transport_cost DECIMAL(14,2) NOT NULL DEFAULT 0,
  remarks VARCHAR(255),
  status ENUM('PENDING','READY_FOR_DELIVERY','IN_TRANSIT','DELIVERED','PARTIALLY_DELIVERED','CANCELLED') NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  INDEX idx_delivery_date_status (delivery_date, status),
  CONSTRAINT fk_delivery_invoice FOREIGN KEY (invoice_id) REFERENCES invoices(id),
  CONSTRAINT fk_delivery_retailer FOREIGN KEY (retailer_id) REFERENCES retailers(id)
);

CREATE TABLE delivery_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  delivery_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  delivered_quantity DECIMAL(14,3) NOT NULL,
  pending_quantity DECIMAL(14,3) NOT NULL DEFAULT 0,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  CONSTRAINT fk_delivery_item_delivery FOREIGN KEY (delivery_id) REFERENCES deliveries(id),
  CONSTRAINT fk_delivery_item_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE payments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  receipt_number VARCHAR(40) NOT NULL UNIQUE,
  payment_date DATE NOT NULL,
  retailer_id BIGINT NOT NULL,
  invoice_id BIGINT NULL,
  payment_method ENUM('CASH','BANK','MOBILE_BANKING','CHEQUE','ONLINE_PAYMENT','ADJUSTMENT') NOT NULL,
  amount DECIMAL(14,2) NOT NULL,
  reference_number VARCHAR(120),
  approved BOOLEAN NOT NULL DEFAULT FALSE,
  remarks VARCHAR(255),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  INDEX idx_payment_date (payment_date),
  CONSTRAINT fk_payment_retailer FOREIGN KEY (retailer_id) REFERENCES retailers(id),
  CONSTRAINT fk_payment_invoice FOREIGN KEY (invoice_id) REFERENCES invoices(id)
);

CREATE TABLE retailer_ledger (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  retailer_id BIGINT NOT NULL,
  transaction_date DATE NOT NULL,
  ledger_type ENUM('OPENING','INVOICE','PAYMENT','RETURN_ADJUSTMENT','DISCOUNT_ADJUSTMENT') NOT NULL,
  debit_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
  credit_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
  running_balance DECIMAL(14,2) NOT NULL DEFAULT 0,
  reference_type VARCHAR(50),
  reference_id BIGINT,
  narration VARCHAR(255),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  INDEX idx_ledger_retailer_date (retailer_id, transaction_date),
  CONSTRAINT fk_ledger_retailer FOREIGN KEY (retailer_id) REFERENCES retailers(id)
);

CREATE TABLE chart_of_accounts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  account_code VARCHAR(40) NOT NULL UNIQUE,
  account_name VARCHAR(160) NOT NULL,
  account_type ENUM('ASSET','LIABILITY','EQUITY','INCOME','EXPENSE') NOT NULL,
  parent_id BIGINT NULL,
  system_account BOOLEAN NOT NULL DEFAULT FALSE,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  CONSTRAINT fk_account_parent FOREIGN KEY (parent_id) REFERENCES chart_of_accounts(id)
);

CREATE TABLE account_ledger (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  account_id BIGINT NOT NULL,
  transaction_date DATE NOT NULL,
  debit_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
  credit_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
  running_balance DECIMAL(14,2) NOT NULL DEFAULT 0,
  reference_type VARCHAR(50),
  reference_id BIGINT,
  narration VARCHAR(255),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  INDEX idx_account_ledger_date (transaction_date, account_id),
  CONSTRAINT fk_account_ledger_account FOREIGN KEY (account_id) REFERENCES chart_of_accounts(id)
);

CREATE TABLE return_policies (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  return_allowed_days INT NOT NULL,
  product_condition VARCHAR(160),
  batch_expiry_validation BOOLEAN NOT NULL DEFAULT TRUE,
  exchange_allowed BOOLEAN NOT NULL DEFAULT TRUE,
  refund_method ENUM('REFUND','DUE_ADJUSTMENT','EXCHANGE') NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL
);

CREATE TABLE order_returns (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  return_number VARCHAR(40) NOT NULL UNIQUE,
  invoice_id BIGINT NOT NULL,
  retailer_id BIGINT NOT NULL,
  return_date DATE NOT NULL,
  reason VARCHAR(255),
  total_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
  status ENUM('PENDING','APPROVED','REJECTED','ADJUSTED') NOT NULL DEFAULT 'PENDING',
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  CONSTRAINT fk_return_invoice FOREIGN KEY (invoice_id) REFERENCES invoices(id),
  CONSTRAINT fk_return_retailer FOREIGN KEY (retailer_id) REFERENCES retailers(id)
);

CREATE TABLE order_return_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_return_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity DECIMAL(14,3) NOT NULL,
  unit_price DECIMAL(14,2) NOT NULL,
  line_total DECIMAL(14,2) NOT NULL,
  condition_note VARCHAR(255),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  CONSTRAINT fk_return_item_return FOREIGN KEY (order_return_id) REFERENCES order_returns(id),
  CONSTRAINT fk_return_item_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE audit_logs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  module_name VARCHAR(100),
  action_name VARCHAR(100),
  record_id BIGINT,
  details VARCHAR(1000),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  INDEX idx_audit_module_record (module_name, record_id)
);

CREATE TABLE price_change_history (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL,
  old_price DECIMAL(14,2) NOT NULL DEFAULT 0,
  new_price DECIMAL(14,2) NOT NULL DEFAULT 0,
  requested_price DECIMAL(14,2) NOT NULL DEFAULT 0,
  change_reason VARCHAR(500),
  changed_by VARCHAR(100),
  change_date_time TIMESTAMP NOT NULL,
  inventory_value_before DECIMAL(16,2) NOT NULL DEFAULT 0,
  inventory_value_after DECIMAL(16,2) NOT NULL DEFAULT 0,
  inventory_value_change DECIMAL(16,2) NOT NULL DEFAULT 0,
  condition_summary VARCHAR(1000),
  material_document_number VARCHAR(50),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  INDEX idx_price_change_product_time (product_id, change_date_time),
  CONSTRAINT fk_price_change_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE accounting_entries (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL,
  entry_type ENUM('DEBIT','CREDIT') NOT NULL,
  amount DECIMAL(16,2) NOT NULL DEFAULT 0,
  description VARCHAR(500),
  posting_date DATE NOT NULL,
  material_document_number VARCHAR(50) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_by VARCHAR(100), created_at TIMESTAMP NULL,
  updated_by VARCHAR(100), updated_at TIMESTAMP NULL,
  INDEX idx_accounting_entry_product_date (product_id, posting_date),
  INDEX idx_accounting_entry_document (material_document_number),
  CONSTRAINT fk_accounting_entry_product FOREIGN KEY (product_id) REFERENCES products(id)
);
