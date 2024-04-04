INSERT INTO users (username, password, role)
VALUES ('john1234', SHA2('123', 256), 'DOCTOR');
SET @userID = LAST_INSERT_ID();
INSERT INTO employees (ID, firstName, lastName, sex, birthDate, hireDate, email, phone, address, managerID, race, ethnicity)
VALUES (@userID, 'John', 'Smith', 'MALE', '1980-01-01', CURRENT_TIMESTAMP, 'johnsmith123@gmail.com', '1234567890', '123 Main St', NULL, "WHITE", "HISPANIC");

INSERT INTO users (username, password, role)
VALUES ('uhh', SHA2('123', 256), 'DOCTOR');
SET @userID = LAST_INSERT_ID();
INSERT INTO employees (ID, firstName, lastName, sex, birthDate, hireDate, email, phone, address, managerID, race, ethnicity)
VALUES (@userID, 'uhhh', 'Davidson', 'MALE', '1979-12-31', CURRENT_TIMESTAMP, 'peterdavidson123@gmail.com', '1234567890', '123 Main St', NULL, "WHITE", "HISPANIC");


select * from users;
select * from employees;