DROP TABLE IF EXISTS users;

CREATE TABLE users (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    username VARCHAR(100) NOT NULL UNIQUE CHECK(LENGTH(username) >= 5),
    passwordHash VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role ENUM('DOCTOR', 'NURSE', 'PATIENT') NOT NULL
);

INSERT INTO users (username, password, role)
VALUES ('jonis6421', SHA2('123', 256), 'ADMIN');


SELECT * FROM users;
SELECT * FROM patients;

delete from users where ID = 97;
