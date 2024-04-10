DROP TABLE IF EXISTS users;

CREATE TABLE users (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    username VARCHAR(100) NOT NULL UNIQUE CHECK(LENGTH(username) >= 5),
    password VARCHAR(100) NOT NULL CHECK(LENGTH(username) >= 8),
    role ENUM('PATIENT', 'NURSE', 'PATIENT') NOT NULL
);

INSERT INTO users (username, password, role)
VALUES ('jonis6421', SHA2('123', 256), 'ADMIN');

use easydoctor;
SELECT * FROM users;
SELECT * FROM patients;
SELECT * FROM employees;
delete from users where ID = 108;

SELECT users.ID, email, users.username FROM users JOIN patients ON users.ID = patients.ID WHERE username = 'jsalazar6421@gmail.com' AND role = 'PATIENT' UNION SELECT patients.ID, email, users.username FROM patients JOIN users on users.ID = patients.ID WHERE email = 'jsalazar6421@gmail.com' AND role = 'PATIENT' UNION SELECT employees.ID, email, users.username FROM employees JOIN users ON users.ID = employees.ID WHERE email = 'jsalazar6421@gmail.com' AND role = 'PATIENT';
