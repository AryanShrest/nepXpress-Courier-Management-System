-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS cms;
USE cms;

-- Users table for authentication
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role ENUM('admin', 'staff', 'customer') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Parcels table
CREATE TABLE IF NOT EXISTS parcels (
    parcel_id INT PRIMARY KEY AUTO_INCREMENT,
    tracking_number VARCHAR(20) UNIQUE NOT NULL,
    sender_id INT NOT NULL,
    receiver_name VARCHAR(100) NOT NULL,
    receiver_phone VARCHAR(20) NOT NULL,
    receiver_address TEXT NOT NULL,
    weight DECIMAL(10,2) NOT NULL,
    status ENUM('pending', 'in_transit', 'delivered', 'cancelled') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(user_id)
);

-- Complaints table
CREATE TABLE IF NOT EXISTS complaints (
    complaint_id INT PRIMARY KEY AUTO_INCREMENT,
    parcel_id INT NOT NULL,
    user_id INT NOT NULL,
    nature VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    status ENUM('pending', 'in_progress', 'resolved') NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parcel_id) REFERENCES parcels(parcel_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Tracking history table
CREATE TABLE IF NOT EXISTS tracking_history (
    history_id INT PRIMARY KEY AUTO_INCREMENT,
    parcel_id INT NOT NULL,
    status ENUM('pending', 'in_transit', 'delivered', 'cancelled') NOT NULL,
    location VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parcel_id) REFERENCES parcels(parcel_id)
);

-- Dispatch table
CREATE TABLE IF NOT EXISTS dispatch (
    dispatch_id INT PRIMARY KEY AUTO_INCREMENT,
    parcel_id INT NOT NULL,
    courier_id INT NOT NULL,
    dispatch_date TIMESTAMP NOT NULL,
    status ENUM('assigned', 'picked_up', 'in_transit', 'delivered') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parcel_id) REFERENCES parcels(parcel_id),
    FOREIGN KEY (courier_id) REFERENCES users(user_id)
);

-- Switch to cms database
USE cms;

-- View all tables
SHOW TABLES;

-- View data from each table
SELECT * FROM users;
SELECT * FROM parcels;
SELECT * FROM complaints;
SELECT * FROM tracking_history;
SELECT * FROM dispatch;

-- View parcels with sender information
SELECT p.*, u.first_name, u.last_name as sender_name 
FROM parcels p 
JOIN users u ON p.sender_id = u.user_id;

-- View complaints with parcel and user information
SELECT c.*, p.tracking_number, u.first_name, u.last_name
FROM complaints c
JOIN parcels p ON c.parcel_id = p.parcel_id
JOIN users u ON c.user_id = u.user_id;

-- Count records in each table
SELECT 'users' as table_name, COUNT(*) as count FROM users
UNION ALL
SELECT 'parcels', COUNT(*) FROM parcels
UNION ALL
SELECT 'complaints', COUNT(*) FROM complaints
UNION ALL
SELECT 'tracking_history', COUNT(*) FROM tracking_history
UNION ALL
SELECT 'dispatch', COUNT(*) FROM dispatch;

DESCRIBE users;
DESCRIBE parcels;
DESCRIBE complaints;
DESCRIBE tracking_history;
DESCRIBE dispatch;

-- Filter users by role
SELECT * FROM users WHERE role = 'customer';

-- Filter parcels by status
SELECT * FROM parcels WHERE status = 'pending';

-- Filter complaints by status
SELECT * FROM complaints WHERE status = 'pending';

-- Filter by date range
SELECT * FROM parcels 
WHERE created_at BETWEEN '2024-01-01' AND '2024-12-31';

-- Filter by multiple conditions
SELECT * FROM parcels 
WHERE status = 'pending' 
AND weight > 5.0;

-- Filter with IN clause
SELECT * FROM parcels 
WHERE status IN ('pending', 'in_transit');

-- Sort users by name
SELECT * FROM users 
ORDER BY first_name, last_name;

-- Sort parcels by date (newest first)
SELECT * FROM parcels 
ORDER BY created_at DESC;

-- Sort parcels by status and date
SELECT * FROM parcels 
ORDER BY status, created_at DESC;

-- Sort with limit (top 10 newest parcels)
SELECT * FROM parcels 
ORDER BY created_at DESC 
LIMIT 10;

-- Sort with multiple columns
SELECT p.*, u.first_name, u.last_name 
FROM parcels p 
JOIN users u ON p.sender_id = u.user_id 
ORDER BY p.status, p.created_at DESC;

-- Search by tracking number
SELECT * FROM parcels 
WHERE tracking_number LIKE '%123%';

-- Search by name (partial match)
SELECT * FROM users 
WHERE first_name LIKE '%John%' 
OR last_name LIKE '%Smith%';

-- Search by email domain
SELECT * FROM users 
WHERE email LIKE '%@gmail.com';

-- Search in multiple tables
SELECT p.*, u.first_name, u.last_name 
FROM parcels p 
JOIN users u ON p.sender_id = u.user_id 
WHERE p.tracking_number LIKE '%123%' 
OR u.first_name LIKE '%John%';

-- Search with multiple conditions
SELECT * FROM complaints 
WHERE nature LIKE '%damage%' 
AND status = 'pending';

EXIT; 