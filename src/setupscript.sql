-- DROP 旧表（如果存在）
DROP TABLE IF EXISTS OrderItems;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS Payments;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
                                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     name TEXT,
                                     email TEXT,
                                     password TEXT,
                                     dob TEXT,
                                     gender TEXT
);

-- 示例账号
INSERT INTO users (name, email, password, dob, gender) VALUES
    ('John Doe', 'john@example.com', 'securepassword123', '1990-01-01', 'Male');

-- 创建订单表
CREATE TABLE IF NOT EXISTS Orders (
                                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                                      userId INTEGER NOT NULL,
                                      totalPrice REAL,
                                      status TEXT,
                                      orderDate DATETIME DEFAULT CURRENT_TIMESTAMP,
                                      FOREIGN KEY(userId) REFERENCES users(id)
);

-- 创建订单明细表
CREATE TABLE IF NOT EXISTS OrderItems (
                                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                                          orderId INTEGER NOT NULL,
                                          productId INTEGER,
                                          quantity INTEGER,
                                          unitPrice REAL,
                                          FOREIGN KEY(orderId) REFERENCES Orders(id)
);

-- 创建支付表
CREATE TABLE IF NOT EXISTS Payments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    orderId INTEGER NOT NULL,
    paymentMethod TEXT NOT NULL,
    cardNumber TEXT NOT NULL,
    cardHolderName TEXT NOT NULL,
    expiryDate TEXT NOT NULL,
    cvv TEXT NOT NULL,
    amount REAL NOT NULL,
    paymentDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    status TEXT NOT NULL,
    FOREIGN KEY(orderId) REFERENCES Orders(id)
);

-- 示例订单
INSERT INTO Orders (userId, totalPrice, status) VALUES
    (1, 199.99, 'Pending');

-- 示例订单明细
INSERT INTO OrderItems (orderId, productId, quantity, unitPrice) VALUES
                                                                     (1, 101, 2, 49.99),
                                                                     (1, 102, 1, 99.99);

-- 示例支付记录
INSERT INTO Payments (orderId, paymentMethod, cardNumber, cardHolderName, expiryDate, cvv, amount, status) VALUES
    (1, 'Credit Card', '4111111111111111', 'John Doe', '12/25', '123', 199.99, 'Pending');
