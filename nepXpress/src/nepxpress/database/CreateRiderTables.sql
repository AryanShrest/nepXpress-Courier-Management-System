-- Create table for My Deliveries (matches frontend UI columns)
CREATE TABLE IF NOT EXISTS rider_deliveries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rider_id INT NOT NULL,
    parcel_id VARCHAR(50) NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (rider_id) REFERENCES riders(id) ON DELETE CASCADE
);

-- Create table for Cash Collected (matches frontend UI columns)
CREATE TABLE IF NOT EXISTS rider_cash_collections (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rider_id INT NOT NULL,
    parcel_id VARCHAR(50) NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (rider_id) REFERENCES riders(id) ON DELETE CASCADE
);

-- Create table for My Pickup (matches frontend UI columns)
CREATE TABLE IF NOT EXISTS rider_pickups (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rider_id INT NOT NULL,
    pickup_id VARCHAR(50) NOT NULL,
    merchant_name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (rider_id) REFERENCES riders(id) ON DELETE CASCADE
);

-- Sample data for rider_deliveries
-- INSERT INTO rider_deliveries (rider_id, parcel_id, customer_name, address, status) VALUES
-- (3, 'DLV1001', 'Ramesh Shrestha', 'Baneshwor, KTM', 'Pending'),
-- (3, 'DLV1002', 'Sita Gurung', 'Patan, KTM', 'Delivered'),
-- (4, 'DLV1003', 'Bikash Lama', 'Bhaktapur', 'Pending'),
-- (4, 'DLV1004', 'Sunita Karki', 'Kalanki, KTM', 'Failed'),
-- (3, 'DLV1005', 'Niraj Thapa', 'Lagankhel, KTM', 'Delivered');

-- Sample data for rider_cash_collections
-- INSERT INTO rider_cash_collections (rider_id, parcel_id, customer_name, amount, status) VALUES
-- (3, 'DLV1002', 'Sita Gurung', 1200.00, 'Collected'),
-- (3, 'DLV1005', 'Niraj Thapa', 1800.00, 'Collected'),
-- (4, 'DLV1006', 'Asha KC', 950.00, 'Delivered'),
-- (4, 'DLV1007', 'Bimal Rai', 2100.00, 'Collected'),
-- (3, 'DLV1008', 'Rita Sharma', 700.00, 'Delivered');

-- Sample data for rider_pickups
-- INSERT INTO rider_pickups (rider_id, pickup_id, merchant_name, address, status) VALUES
-- (3, 'PKP00156', 'Smart Deals', 'Kalanki, KTM', 'Assigned'),
-- (3, 'PKP00157', 'Trendy Store', 'Patan, KTM', 'Pending'),
-- (4, 'PKP00158', 'Fashion Hub', 'Bhaktapur', 'Completed'),
-- (4, 'PKP00159', 'Electro World', 'Baneshwor, KTM', 'Assigned'),
-- (3, 'PKP00160', 'Home Decor', 'Lagankhel, KTM', 'Pending'); 