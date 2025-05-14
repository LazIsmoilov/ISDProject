-- DROP 旧表（如果存在）
DROP TABLE IF EXISTS OrderItems;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS users;

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

-- 示例订单
INSERT INTO Orders (userId, totalPrice, status) VALUES
    (1, 199.99, 'Pending');

-- 示例订单明细
INSERT INTO OrderItems (orderId, productId, quantity, unitPrice) VALUES
                                                                     (1, 101, 2, 49.99),
                                                                     (1, 102, 1, 99.99);
