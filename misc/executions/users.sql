DROP TABLE IF EXISTS users;

CREATE TABLE users (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    username VARCHAR(100) NOT NULL UNIQUE CHECK(LENGTH(username) >= 5),
    password VARCHAR(100) NOT NULL CHECK(LENGTH(username) >= 8),
    role ENUM('PATIENT', 'NURSE', 'PATIENT') NOT NULL
);
use easydoctor;
INSERT INTO users (username, password, role)
VALUES ('jonis6421', SHA2('123', 256), 'ADMIN');


use easydoctor;
SELECT * FROM users;
SELECT * FROM patients;
SELECT * FROM employees;

SELECT username FROM patients JOIN users ON users.ID = patients.ID WHERE patients.ID = 184;