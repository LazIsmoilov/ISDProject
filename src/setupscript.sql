-- Drop existing tables in the correct order
DROP TABLE IF EXISTS shipments;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS orderItems;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
                       userId INTEGER PRIMARY KEY AUTOINCREMENT,
                       fullName VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL, -- Should store hashed passwords
                       dob DATE,
                       gender VARCHAR(20) CHECK(gender IN ('Male', 'Female', 'Other')),
                       type VARCHAR(20) NOT NULL CHECK(type IN ('customer', 'staff', 'admin')),
                       phoneNumber VARCHAR(15),
                       isActive INTEGER DEFAULT 1 CHECK(isActive IN (0, 1))
);

-- Insert initial admin user
INSERT INTO users (fullName, email, password, dob, gender, type, phoneNumber, isActive)
VALUES ('Admin User', 'admin@system.com', 'hashed_admin123', '1980-01-01', 'Other', 'admin', '1234567890', 1);

-- Insert example users
INSERT INTO users (fullName, email, password, dob, gender, type, phoneNumber, isActive)
VALUES
    ('John Doe', 'john@example.com', 'hashed_securepassword123', '1990-01-01', 'Male', 'customer', '0987654321', 1),
    ('Jane Smith', 'jane@example.com', 'hashed_password456', '1985-05-15', 'Female', 'staff', '1122334455', 1);

-- Create products table (if needed)
CREATE TABLE products (
                          productId INTEGER PRIMARY KEY AUTOINCREMENT,
                          name VARCHAR(100) NOT NULL,
                          price REAL NOT NULL,
                          description TEXT
);

-- Create orders table
CREATE TABLE orders (
                        orderId INTEGER PRIMARY KEY AUTOINCREMENT,
                        userId INTEGER NOT NULL,
                        orderDate DATE NOT NULL,
                        status VARCHAR(30) DEFAULT 'Pending' CHECK(status IN ('Pending', 'Confirmed', 'Shipped', 'Delivered', 'Cancelled')),
                        totalAmount REAL DEFAULT 0,
                        FOREIGN KEY (userId) REFERENCES users(userId)
);

-- Create orderItems table
CREATE TABLE orderItems (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                    vv        orderId INTEGER NOT NULL,
                            productId INTEGER NOT NULL,
                            quantity INTEGER NOT NULL CHECK(quantity > 0),
                            unitPrice REAL NOT NULL CHECK(unitPrice >= 0),
                            FOREIGN KEY (orderId) REFERENCES orders(orderId),
                            FOREIGN KEY (productId) REFERENCES products(productId)
);

-- Create payments table
CREATE TABLE payments (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          orderId INTEGER NOT NULL,
                          userId INTEGER NOT NULL, -- Added to match Payment.java
                          paymentMethod VARCHAR(30) NOT NULL CHECK(paymentMethod IN ('Credit Card', 'PayPal', 'Bank Transfer')),
                          cardNumber VARCHAR(30),
                          cardHolderName VARCHAR(100),
                          expiryDate VARCHAR(10),
                          cvv VARCHAR(10),
                          amount REAL NOT NULL CHECK(amount >= 0),
                          paymentDate DATE,
                          status VARCHAR(20) DEFAULT 'Pending' CHECK(status IN ('Pending', 'Completed', 'Failed')),
                          FOREIGN KEY (orderId) REFERENCES orders(orderId),
                          FOREIGN KEY (userId) REFERENCES users(userId)
);

-- Create shipments table
CREATE TABLE shipments (
                           shipmentId INTEGER PRIMARY KEY AUTOINCREMENT,
                           orderId INTEGER NOT NULL,
                           method VARCHAR(50) NOT NULL CHECK(method IN ('Standard', 'Express', 'Overnight')),
                           shipmentDate DATE,
                           address TEXT NOT NULL,
                           FOREIGN KEY (orderId) REFERENCES orders(orderId)
);


-- DEVICES TABLE
CREATE TABLE Devices (
                         device_id INTEGER PRIMARY KEY AUTOINCREMENT,
                         name TEXT NOT NULL,
                         type TEXT NOT NULL,
                         description TEXT NOT NULL,
                         unit TEXT NOT NULL,
                         price REAL NOT NULL,
                         quantity INTEGER NOT NULL,
                         image TEXT
);l

-- SAMPLE DEVICES
INSERT INTO Devices (name, type, description, unit, price, quantity)
VALUES
    ('Wearable Health', 'Health', 'Wearable Blood Pressure Monitor', 'pcs', 169.21, 100),
    ('Smart Energy', 'Energy', 'Smart Light Bulb with dimmer', 'pcs', 252.56, 101),
    ('Solar Energy', 'Energy', 'Solar Power Bank with USB-C', 'pcs', 249.7, 68),
    ('Smart Home Automation', 'Home Automation', 'Smart Thermostat with mobile app', 'pcs', 145.9, 91),
    ('Air Sensors', 'Sensors', 'Temperature and Humidity Sensor', 'pcs', 283.02, 139),
    ('Voice Home Automation', 'Home Automation', 'Voice-Controlled Ceiling Fan', 'pcs', 267.13, 91),
    ('Smart Energy', 'Energy', 'Smart Power Strip with surge protection', 'pcs', 265.55, 30),
    ('Indoor Security', 'Security', 'Smart Indoor Camera with night vision', 'pcs', 198.72, 67),
    ('Soil Sensors', 'Sensors', 'Smart Soil Moisture Sensor', 'pcs', 107.11, 91),
    ('Motion Security', 'Security', 'Motion Detector with alerts', 'pcs', 42.77, 117),
    ('Smart Sensors', 'Sensors', 'Air Quality Sensor', 'pcs', 91.4, 126),
    ('Lock Security', 'Security', 'Smart Lock with fingerprint reader', 'pcs', 67.67, 147),
    ('Thermo Health', 'Health', 'Digital Thermometer with app sync', 'pcs', 25.85, 129),
    ('Fan Home Automation', 'Home Automation', 'Voice-Controlled Ceiling Fan', 'pcs', 125.39, 68),
    ('Camera Security', 'Security', 'Smart Indoor Camera with night vision', 'pcs', 293.74, 81),
    ('Soil Sensors', 'Sensors', 'Smart Soil Moisture Sensor', 'pcs', 139.18, 38),
    ('Curtains Home Automation', 'Home Automation', 'Smart Curtains with timer', 'pcs', 200.36, 145),
    ('Air Sensors', 'Sensors', 'Air Quality Sensor', 'pcs', 248.86, 113),
    ('Water Sensors', 'Sensors', 'Water Leak Detector', 'pcs', 22.33, 141),
    ('Plug Energy', 'Energy', 'Smart Plug for remote control', 'pcs', 104.14, 58),
    ('Heart Health', 'Health', 'Bluetooth Heart Monitor', 'pcs', 136.42, 132),
    ('Lock Security', 'Security', 'Smart Lock with fingerprint reader', 'pcs', 217.8, 50),
    ('Motion Security', 'Security', 'Motion Detector with alerts', 'pcs', 109.64, 134),
    ('Indoor Security', 'Security', 'Smart Indoor Camera with night vision', 'pcs', 199.76, 116),
    ('Plug Energy', 'Energy', 'WiFi Smart Plug for remote control', 'pcs', 23.45, 117),
    ('Curtains Home Automation', 'Home Automation', 'Smart Curtains with timer', 'pcs', 288.52, 43),
    ('Thermostat Home Automation', 'Home Automation', 'Smart Thermostat with mobile app', 'pcs', 261.83, 73),
    ('Thermo Health', 'Health', 'Digital Thermometer with app sync', 'pcs', 110.32, 96),
    ('Door Security', 'Security', 'Door Sensor for open/close state', 'pcs', 100.68, 15),
    ('Water Sensors', 'Sensors', 'Water Leak Detector', 'pcs', 248.52, 45),
    ('Curtains Home Automation', 'Home Automation', 'Smart Curtains with timer', 'pcs', 189.9, 96),
    ('Camera Security', 'Security', 'Smart Indoor Camera with night vision', 'pcs', 218.13, 142),
    ('Power Energy', 'Energy', 'Solar Power Bank with USB-C', 'pcs', 101.38, 116),
    ('Thermo Health', 'Health', 'Digital Thermometer with app sync', 'pcs', 134.25, 21),
    ('Thermostat Home Automation', 'Home Automation', 'Smart Thermostat with mobile app', 'pcs', 58.46, 125),
    ('Smart Health', 'Health', 'Wearable Blood Pressure Monitor', 'pcs', 73.07, 79),
    ('Scale Health', 'Health', 'Smart Scale with BMI tracking', 'pcs', 142.87, 105),
    ('Smart Sensors', 'Sensors', 'Air Quality Sensor', 'pcs', 218.25, 149),
    ('Plug Energy', 'Energy', 'Smart Plug for remote control', 'pcs', 132.56, 111),
    ('Motion Security', 'Security', 'Motion Detector with alerts', 'pcs', 137.95, 44),
    ('Smart Energy', 'Energy', 'WiFi Smart Power Strip', 'pcs', 242.87, 87),
    ('Scale Health', 'Health', 'Smart Scale with BMI tracking', 'pcs', 113.39, 59),
    ('Soil Sensors', 'Sensors', 'Smart Soil Moisture Sensor', 'pcs', 276.45, 94),
    ('Smart Lock Security', 'Security', 'Smart Lock with fingerprint reader', 'pcs', 293.49, 106),
    ('Temperature Sensors', 'Sensors', 'Temperature and Humidity Sensor', 'pcs', 289.88, 37),
    ('Curtains Home Automation', 'Home Automation', 'Smart Curtains with timer', 'pcs', 268.32, 93),
    ('Smart Health', 'Health', 'Wearable Blood Pressure Monitor', 'pcs', 253.64, 144),
    ('Plug Energy', 'Energy', 'WiFi Smart Plug for remote control', 'pcs', 165.8, 20),
    ('Motion Security', 'Security', 'Motion Detector with alerts', 'pcs', 197.41, 123);
df\\\\\
-- Select all users to verify
SELECT * FROM users;