-- Create the database
CREATE DATABASE IF NOT EXISTS courier_management_system;
USE courier_management_system;

-- Users table for authentication and user management
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    role ENUM('admin', 'staff', 'customer') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Customers table for storing customer information
CREATE TABLE IF NOT EXISTS customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50),
    postal_code VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Parcels table for managing parcel information
CREATE TABLE IF NOT EXISTS parcels (
    parcel_id INT PRIMARY KEY AUTO_INCREMENT,
    reference_number VARCHAR(50) UNIQUE NOT NULL,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    weight DECIMAL(10,2) NOT NULL,
    dimensions VARCHAR(50),
    parcel_type VARCHAR(50) NOT NULL,
    description TEXT,
    declared_value DECIMAL(10,2),
    status ENUM('pending', 'collected', 'in_transit', 'delivered', 'returned', 'cancelled') NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES customers(customer_id),
    FOREIGN KEY (receiver_id) REFERENCES customers(customer_id)
);

-- Tracking table for parcel tracking history
CREATE TABLE IF NOT EXISTS tracking (
    tracking_id INT PRIMARY KEY AUTO_INCREMENT,
    parcel_id INT NOT NULL,
    status ENUM('pending', 'collected', 'in_transit', 'delivered', 'returned', 'cancelled') NOT NULL,
    location VARCHAR(100),
    remarks TEXT,
    updated_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parcel_id) REFERENCES parcels(parcel_id),
    FOREIGN KEY (updated_by) REFERENCES users(user_id)
);

-- Complaints table for managing customer complaints
CREATE TABLE IF NOT EXISTS complaints (
    complaint_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    parcel_id INT,
    subject VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    status ENUM('pending', 'in_progress', 'resolved', 'closed') NOT NULL DEFAULT 'pending',
    priority ENUM('low', 'medium', 'high') NOT NULL DEFAULT 'medium',
    assigned_to INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (parcel_id) REFERENCES parcels(parcel_id),
    FOREIGN KEY (assigned_to) REFERENCES users(user_id)
);

-- Dispatch table for managing parcel dispatches
CREATE TABLE IF NOT EXISTS dispatch (
    dispatch_id INT PRIMARY KEY AUTO_INCREMENT,
    parcel_id INT NOT NULL,
    driver_id INT NOT NULL,
    vehicle_number VARCHAR(20) NOT NULL,
    dispatch_date TIMESTAMP NOT NULL,
    expected_delivery_date TIMESTAMP,
    actual_delivery_date TIMESTAMP,
    status ENUM('pending', 'in_transit', 'delivered', 'failed') NOT NULL DEFAULT 'pending',
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parcel_id) REFERENCES parcels(parcel_id),
    FOREIGN KEY (driver_id) REFERENCES users(user_id)
);

-- Rates table for storing shipping rates
CREATE TABLE IF NOT EXISTS rates (
    rate_id INT PRIMARY KEY AUTO_INCREMENT,
    source_city VARCHAR(50) NOT NULL,
    destination_city VARCHAR(50) NOT NULL,
    weight_range_start DECIMAL(10,2) NOT NULL,
    weight_range_end DECIMAL(10,2) NOT NULL,
    base_rate DECIMAL(10,2) NOT NULL,
    per_kg_rate DECIMAL(10,2) NOT NULL,
    service_type ENUM('standard', 'express', 'same_day') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Billing table for managing payments
CREATE TABLE IF NOT EXISTS billing (
    billing_id INT PRIMARY KEY AUTO_INCREMENT,
    parcel_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    tax_amount DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_status ENUM('pending', 'paid', 'failed', 'refunded') NOT NULL DEFAULT 'pending',
    payment_method VARCHAR(50),
    payment_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parcel_id) REFERENCES parcels(parcel_id)
);

-- Insert default admin user
INSERT INTO users (username, password, full_name, email, role)
VALUES ('admin', SHA2('admin123', 256), 'System Administrator', 'admin@nepxpress.com', 'admin');

-- Create indexes for better performance
CREATE INDEX idx_parcels_reference ON parcels(reference_number);
CREATE INDEX idx_parcels_status ON parcels(status);
CREATE INDEX idx_tracking_parcel ON tracking(parcel_id);
CREATE INDEX idx_complaints_status ON complaints(status);
CREATE INDEX idx_dispatch_status ON dispatch(status);
CREATE INDEX idx_billing_status ON billing(payment_status); 