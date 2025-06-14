-- Create database
CREATE DATABASE IF NOT EXISTS riderdashboard;
USE riderdashboard;

-- Deliveries table
CREATE TABLE IF NOT EXISTS deliveries (
    parcel_id VARCHAR(10) PRIMARY KEY,
    customer_name VARCHAR(100),
    address VARCHAR(200),
    status VARCHAR(20)
);

INSERT INTO deliveries (parcel_id, customer_name, address, status) VALUES
('P001', 'John Doe', 'New Road, KTM', 'Pending'),
('P002', 'Jane Smith', 'Kupondole, KTM', 'Delivered'),
('P003', 'Alice Brown', 'Durbar Marg, KTM', 'Failed'),
('P004', 'Bob White', 'Sundhara, KTM', 'Pending'),
('P005', 'Charlie Black', 'Kalimati, KTM', 'Delivered');

-- Pickups table
CREATE TABLE IF NOT EXISTS pickups (
    pickup_id VARCHAR(10) PRIMARY KEY,
    merchant_name VARCHAR(100),
    location VARCHAR(200),
    time VARCHAR(20),
    status VARCHAR(20)
);

INSERT INTO pickups (pickup_id, merchant_name, location, time, status) VALUES
('PU00123', 'Trendy Store', 'New Road, KTM', '10:00 AM', 'Pending'),
('PU00124', 'RB Electronics', 'Kupondole, KTM', '11:30 AM', 'Pending'),
('PU00120', 'Fashion Hub', 'Durbar Marg, KTM', '9:00 AM', 'Picked Up'),
('PU00118', 'ABC Store', 'Sundhara, KTM', '2:00 PM', 'Picked Up'),
('PU00115', 'XYZ Traders', 'Kalimati, KTM', '4:30 PM', 'Cancelled');

-- Cash Collected table
CREATE TABLE IF NOT EXISTS cash_collected (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE,
    merchant VARCHAR(100),
    amount DECIMAL(10,2),
    status VARCHAR(20)
);

INSERT INTO cash_collected (date, merchant, amount, status) VALUES
('2024-03-20', 'Trendy Store', 1500.00, 'Collected'),
('2024-03-20', 'RB Electronics', 2300.00, 'Pending'),
('2024-03-19', 'ABC Store', 1800.00, 'Collected'),
('2024-03-19', 'Fashion Hub', 3200.00, 'Collected'),
('2024-03-18', 'XYZ Traders', 1950.00, 'Pending');

-- Issues table
CREATE TABLE IF NOT EXISTS issues (
    id INT AUTO_INCREMENT PRIMARY KEY,
    parcel_id VARCHAR(10),
    issue_type VARCHAR(100),
    description TEXT,
    FOREIGN KEY (parcel_id) REFERENCES deliveries(parcel_id)
);

-- Example issue
INSERT INTO issues (parcel_id, issue_type, description) VALUES
('P003', 'Package damaged', 'Box was torn on delivery.'); 