DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    userID INT NOT NULL,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    birthDate DATE NOT NULL,
    hireDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    managerID INT,
    FOREIGN KEY (userID) REFERENCES users(ID),
    FOREIGN KEY (managerID) REFERENCES users(ID)
);

INSERT INTO users (username, password, userType)
VALUES ('test', 'test', 'EMPLOYEE');

INSERT INTO employees (userID, firstName, lastName, gender, birthDate, email, phone, address, managerID) 
VALUES (3, 'test', 'test', 'OTHER', '2000-01-01', 'test@test.com', '1234567890', '123 Test St', 1);

DELETE FROM employees WHERE userID = 1;
DELETE FROM users WHERE ID = 1;