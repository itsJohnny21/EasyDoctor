INSERT INTO users (username, password, role)
VALUES ('john123', SHA2('123', 256), 'DOCTOR');
SET @userID = LAST_INSERT_ID();
INSERT INTO employees (userID, firstName, lastName, gender, birthDate, hireDate, email, phone, address, managerID)
VALUES (@userID, 'John', 'Smith', 'MALE', '1980-01-01', CURRENT_TIMESTAMP, 'johnsmith123@gmail.com', '1234567890', '123 Main St', NULL);

select * from users;
select * from employees;

INSERT INTO users (username, password, role)
VALUES ('peter', SHA2('123', 256), 'DOCTOR');
SET @userID = LAST_INSERT_ID();
INSERT INTO employees (userID, firstName, lastName, gender, birthDate, hireDate, email, phone, address, managerID)
VALUES (@userID, 'Peter', 'Davidson', 'MALE', '1979-12-31', CURRENT_TIMESTAMP, 'peterdavidson123@gmail.com', '1234567890', '123 Main St', NULL);

select * from users;
select * from employees;