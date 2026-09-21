# HealthFirst Pharmacy Inventory Management System

A desktop Pharmacy Inventory Management System developed using Java Swing, JDBC and MySQL.

The system provides secure role-based access for administrators and cashiers. It supports medicine inventory management, suppliers, system users, point-of-sale transactions, receipts and operational reports.

## Student Information

- Student: Tshimologo Molotsi
- Student number: 402204239
- Module: Programming 732
- Project: Pharmacy Inventory Management System

## Main Features

### Secure Login

- Database-based authentication
- SHA-256 password hashing
- Administrator and Cashier roles
- Invalid credential handling
- Role-based dashboard redirection

### Administrator Dashboard

- Live medicine, supplier and user totals
- Daily sales summary
- Medicine management
- Supplier management
- User management
- Business reports

### Medicine Management

- Add medicine records
- Update medicine information
- Delete eligible medicine records
- Search medicine records
- Track stock and reorder levels
- Track expiry dates
- Assign suppliers

### Supplier Management

- Add suppliers
- Update supplier information
- Delete eligible suppliers
- Search supplier records
- Store contact and address information

### User Management

- Create Admin and Cashier accounts
- Update account details and passwords
- Delete eligible accounts
- Search users
- Prevent deletion of the final Administrator
- Store passwords as SHA-256 hashes

### Cashier Point of Sale

- Search available medicines
- Add medicines to a customer cart
- Select sale quantities
- Remove cart items
- Clear the cart
- Calculate the total automatically
- Complete checkout
- Record sales and sale items
- Reduce medicine stock automatically
- Display a customer receipt

### Reports

- Sales Report
- Item-Wise Sales Report
- Low Stock Report
- Expiry Report

## Technologies Used

- Java
- Java Swing and AWT
- JDBC
- MySQL 8
- MySQL Connector/J
- Apache Maven
- Apache NetBeans
- Git and GitHub

## Database Structure

The system uses the following MySQL tables:

- `users`
- `suppliers`
- `medicines`
- `sales`
- `sale_items`

The complete schema and sample data are provided in:

```text
database.sql

## GitHub Repository

https://github.com/Tshimologo-tm/PRO732-Pharmacy-Inventory-System