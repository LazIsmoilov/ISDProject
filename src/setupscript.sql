-- Drop existing tables in the correct order (due to foreign key dependencies)
DROP TABLE IF EXISTS shipments;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS orderItems;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
                       userId INTEGER PRIMARY KEY AUTOINCREMENT,
                       fullName VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       dob DATE,
                       gender VARCHAR(20),
                       type VARCHAR(20) NOT NULL CHECK(type IN ('customer', 'staff', 'admin')),
                       phoneNumber VARCHAR(15),
                       isActive INTEGER DEFAULT 1 CHECK(isActive IN (0, 1))
);

-- Insert initial admin user
INSERT INTO users (fullName, email, password, dob, gender, type, phoneNumber, isActive)
VALUES ('Admin User', 'admin@system.com', 'admin123', '1980-01-01', 'Other', 'admin', '1234567890', 1);

-- Insert example users
INSERT INTO users (fullName, email, password, dob, gender, type, phoneNumber, isActive)
VALUES
    ('John Doe', 'john@example.com', 'securepassword123', '1990-01-01', 'Male', 'customer', '0987654321', 1),
    ('Jane Smith', 'jane@example.com', 'password456', '1985-05-15', 'Female', 'staff', '1122334455', 1);

-- Create orders table
CREATE TABLE orders (
                        orderId INTEGER PRIMARY KEY AUTOINCREMENT,
                        userId INTEGER NOT NULL,
                        orderDate DATE NOT NULL,
                        status VARCHAR(30) DEFAULT 'Pending',
                        totalAmount REAL DEFAULT 0,
                        FOREIGN KEY (userId) REFERENCES users(userId)
);

-- Create orderItems table
CREATE TABLE orderItems (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            orderId INTEGER NOT NULL,
                            productId INTEGER NOT NULL,
                            quantity INTEGER NOT NULL,
                            unitPrice REAL NOT NULL,
                            FOREIGN KEY (orderId) REFERENCES orders(orderId)
    -- Add FOREIGN KEY (productId) REFERENCES products(productId) if you have a products table
);

-- Create payments table
CREATE TABLE payments (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          orderId INTEGER NOT NULL,
                          paymentMethod VARCHAR(30) NOT NULL,
                          cardNumber VARCHAR(30),
                          cardHolderName VARCHAR(100),
                          expiryDate VARCHAR(10),
                          cvv VARCHAR(10),
                          amount REAL NOT NULL,
                          paymentDate DATE,
                          status VARCHAR(20) DEFAULT 'Pending',
                          FOREIGN KEY (orderId) REFERENCES orders(orderId)
);

-- Create shipments table
CREATE TABLE shipments (
                           shipmentId INTEGER PRIMARY KEY AUTOINCREMENT,
                           orderId INTEGER NOT NULL,
                           method VARCHAR(50) NOT NULL,
                           shipmentDate DATE,
                           address TEXT NOT NULL,
                           FOREIGN KEY (orderId) REFERENCES orders(orderId)
);

-- Select all users to verify
SELECT * FROM users;