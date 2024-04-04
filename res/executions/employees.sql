DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    userID INT NOT NULL,
    firstName VARCHAR(100) NOT NULL,
    middleName VARCHAR(100) DEFAULT NULL,
    lastName VARCHAR(100) NOT NULL,
    race ENUM('WHITE', 'BLACK', 'HISPANIC', 'ASIAN', 'NATIVE_AMERICAN', 'PACIFIC_ISLANDER', 'OTHER') NOT NULL,
    ethnicity ENUM('HISPANIC', 'NON_HISPANIC') NOT NULL,
    sex ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    birthDate DATE NOT NULL,
    hireDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    managerID INT,
    FOREIGN KEY (userID) REFERENCES users(ID) ON CASCADE DELETE,
    FOREIGN KEY (managerID) REFERENCES users(ID) ON CASCADE DELETE
);


INSERT INTO users (username, password, userType)
VALUES ('test', 'test', 'EMPLOYEE');

INSERT INTO employees (userID, firstName, lastName, sex, birthDate, email, phone, address, managerID, race, ethnicity) 
VALUES (3, 'test', 'test', 'OTHER', '2000-01-01', 'test@test.com', '1234567890', '123 Test St', 1, "WHITE", "HISPANIC");

use easydoctor;
SELECT * FROM users;
delete from users where ID = 104;
SELECT * FROM employees;