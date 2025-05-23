-- Drop the existing users table if it exists
DROP TABLE IF EXISTS users;

-- Create the users table with updated schema
CREATE TABLE users (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       name VARCHAR(30) NOT NULL,
                       email VARCHAR(30) NOT NULL UNIQUE,
                       password VARCHAR(30) NOT NULL,
                       dob VARCHAR(30),
                       gender VARCHAR(30),
                       type VARCHAR(20) NOT NULL, -- 'customer', 'staff', 'admin'
                       phoneNumber VARCHAR(15), -- e.g., 10-digit phone number
                       isActive INTEGER DEFAULT 1 -- 1 for active, 0 for inactive
);

-- Insert a built-in admin user
INSERT INTO users (name, email, password, dob, gender, type, phoneNumber, isActive)
VALUES ('Admin User', 'admin@system.com', 'admin123', '1980-01-01', 'Other', 'admin', '1234567890', 1);

-- Insert example users
INSERT INTO users (name, email, password, dob, gender, type, phoneNumber, isActive)
VALUES
    ('John Doe', 'john@example.com', 'securepassword123', '1990-01-01', 'Male', 'customer', '0987654321', 1),
    ('Jane Smith', 'jane@example.com', 'password456', '1985-05-15', 'Female', 'staff', '1122334455', 1);

-- Select all users to verify
SELECT * FROM users;