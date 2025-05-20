-- DROP TABLES IF THEY EXIST (clean start)
DROP TABLE IF EXISTS OrderItems;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Devices;
DROP TABLE IF EXISTS users;

-- USERS TABLE
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fullName TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    dob TEXT,
    gender TEXT,
    phone TEXT,
    role TEXT CHECK(role IN ('staff', 'customer')) NOT NULL
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
);

-- ORDERS TABLE
CREATE TABLE Orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER NOT NULL,
    totalPrice REAL,
    status TEXT,
    orderDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(userId) REFERENCES users(id)
);

-- ORDER ITEMS TABLE
CREATE TABLE OrderItems (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    orderId INTEGER NOT NULL,
    productId INTEGER,
    quantity INTEGER,
    unitPrice REAL,
    FOREIGN KEY(orderId) REFERENCES Orders(id),
    FOREIGN KEY(productId) REFERENCES Devices(device_id)
);

-- SAMPLE USERS
INSERT INTO users (fullName, email, password, dob, gender, phone, role)
VALUES
('Jane Doe', 'jane@example.com', 'test12345', '2000-01-01', 'Female', '0412345678', 'customer'),
('Staff Admin', 'staff@example.com', 'admin123', '1990-05-05', 'Male', '0499999999', 'staff');

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


-- SAMPLE ORDER
INSERT INTO Orders (userId, totalPrice, status)
VALUES (1, 199.97, 'Pending');

-- SAMPLE ORDER ITEMS
INSERT INTO OrderItems (orderId, productId, quantity, unitPrice)
VALUES
(1, 1, 2, 19.99),
(1, 2, 1, 9.99);
