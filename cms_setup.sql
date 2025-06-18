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