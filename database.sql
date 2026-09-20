-- ============================================================
-- HealthFirst Pharmacy Inventory Management System
-- Database Script
-- Student: Tshimologo Molotsi
-- Student Number: 402204239
-- Module: Programming 732
-- ============================================================

DROP DATABASE IF EXISTS healthfirst_pharmacy;

CREATE DATABASE healthfirst_pharmacy
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE healthfirst_pharmacy;

-- ============================================================
-- TABLE 1: USERS
-- Stores login credentials and system roles.
-- ============================================================

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'Cashier') NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABLE 2: SUPPLIERS
-- Stores supplier contact information.
-- ============================================================

CREATE TABLE suppliers (
    supplier_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    address TEXT NOT NULL
);

-- ============================================================
-- TABLE 3: MEDICINES
-- Stores HealthFirst Pharmacy inventory.
-- ============================================================

CREATE TABLE medicines (
    medicine_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    company VARCHAR(100) NOT NULL,
    medicine_type VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity_in_stock INT NOT NULL DEFAULT 0,
    reorder_level INT NOT NULL DEFAULT 10,
    expiry_date DATE NOT NULL,
    supplier_id INT NOT NULL,

    CONSTRAINT chk_medicine_price
        CHECK (price >= 0),

    CONSTRAINT chk_medicine_quantity
        CHECK (quantity_in_stock >= 0),

    CONSTRAINT chk_reorder_level
        CHECK (reorder_level >= 0),

    CONSTRAINT fk_medicine_supplier
        FOREIGN KEY (supplier_id)
        REFERENCES suppliers(supplier_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- ============================================================
-- TABLE 4: SALES
-- Stores the main information for each sale.
-- ============================================================

CREATE TABLE sales (
    sale_id INT PRIMARY KEY AUTO_INCREMENT,
    sale_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    user_id INT NOT NULL,

    CONSTRAINT chk_sale_total
        CHECK (total_amount >= 0),

    CONSTRAINT fk_sale_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- ============================================================
-- TABLE 5: SALE ITEMS
-- Stores individual medicines included in each sale.
-- ============================================================

CREATE TABLE sale_items (
    sale_item_id INT PRIMARY KEY AUTO_INCREMENT,
    sale_id INT NOT NULL,
    medicine_id INT NOT NULL,
    quantity_sold INT NOT NULL,
    price_at_sale DECIMAL(10, 2) NOT NULL,

    CONSTRAINT chk_quantity_sold
        CHECK (quantity_sold > 0),

    CONSTRAINT chk_price_at_sale
        CHECK (price_at_sale >= 0),

    CONSTRAINT fk_sale_item_sale
        FOREIGN KEY (sale_id)
        REFERENCES sales(sale_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_sale_item_medicine
        FOREIGN KEY (medicine_id)
        REFERENCES medicines(medicine_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- ============================================================
-- INDEXES
-- Improve the speed of common searches and reports.
-- ============================================================

CREATE INDEX idx_medicine_name
    ON medicines(name);

CREATE INDEX idx_medicine_expiry
    ON medicines(expiry_date);

CREATE INDEX idx_sale_date
    ON sales(sale_date);

-- ============================================================
-- SAMPLE USERS
-- Passwords are stored as SHA-256 hashes.
--
-- Admin password: admin123
-- Cashier password: cash123
-- ============================================================

INSERT INTO users (
    username,
    password,
    role,
    full_name
)
VALUES
(
    'admin',
    SHA2('admin123', 256),
    'Admin',
    'Tshimologo Molotsi'
),
(
    'cashier',
    SHA2('cash123', 256),
    'Cashier',
    'Lerato Mokoena'
);

-- ============================================================
-- SAMPLE SUPPLIERS
-- ============================================================

INSERT INTO suppliers (
    name,
    contact_person,
    phone,
    email,
    address
)
VALUES
(
    'Ubuntu Medical Distributors',
    'Naledi Khumalo',
    '012 555 0141',
    'orders@ubuntumedical.co.za',
    '88 Francis Baard Street, Pretoria Central'
),
(
    'Emerald Health Supplies',
    'Karabo Molefe',
    '012 555 0186',
    'sales@emeraldhealth.co.za',
    '14 Lenchen Avenue, Centurion'
),
(
    'Golden Cross Pharmaceuticals',
    'Thabo Ndlovu',
    '012 555 0224',
    'support@goldencross.co.za',
    '27 Burnett Street, Hatfield, Pretoria'
);

-- ============================================================
-- SAMPLE MEDICINES
-- Some dates are dynamic so expiry reports always have data.
-- ============================================================

INSERT INTO medicines (
    name,
    company,
    medicine_type,
    price,
    quantity_in_stock,
    reorder_level,
    expiry_date,
    supplier_id
)
VALUES
(
    'Paracetamol 500mg',
    'Adcock Ingram',
    'Tablet',
    34.99,
    120,
    25,
    DATE_ADD(CURDATE(), INTERVAL 12 MONTH),
    1
),
(
    'Amoxicillin 500mg',
    'Aspen Pharmacare',
    'Capsule',
    89.50,
    45,
    15,
    DATE_ADD(CURDATE(), INTERVAL 8 MONTH),
    2
),
(
    'Cough Relief Syrup',
    'HealthFirst Wellness',
    'Syrup',
    67.95,
    28,
    10,
    DATE_ADD(CURDATE(), INTERVAL 20 DAY),
    3
),
(
    'Hydrocortisone Cream',
    'Dermacare Laboratories',
    'Cream',
    54.75,
    8,
    12,
    DATE_ADD(CURDATE(), INTERVAL 6 MONTH),
    2
),
(
    'Vitamin C 1000mg',
    'Vital Health Foods',
    'Tablet',
    79.99,
    65,
    20,
    DATE_ADD(CURDATE(), INTERVAL 15 MONTH),
    1
),
(
    'Allergy Relief 10mg',
    'PharmaCare SA',
    'Tablet',
    42.50,
    6,
    10,
    DATE_ADD(CURDATE(), INTERVAL 25 DAY),
    3
);

-- ============================================================
-- SAMPLE SALE
-- ============================================================

INSERT INTO sales (
    total_amount,
    user_id
)
VALUES
(
    104.98,
    2
);

INSERT INTO sale_items (
    sale_id,
    medicine_id,
    quantity_sold,
    price_at_sale
)
VALUES
(
    1,
    1,
    1,
    34.99
),
(
    1,
    5,
    1,
    69.99
);

-- ============================================================
-- VERIFICATION QUERIES
-- ============================================================

SELECT 'HealthFirst database created successfully' AS result;

SELECT TABLE_NAME
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'healthfirst_pharmacy'
ORDER BY TABLE_NAME;

SELECT
    medicine_id,
    name,
    quantity_in_stock,
    reorder_level,
    expiry_date
FROM medicines
ORDER BY name;
