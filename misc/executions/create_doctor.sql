INSERT INTO users (username, password, role)
VALUES ('john1234', SHA2('123', 256), 'DOCTOR');
SET @userID = LAST_INSERT_ID();
INSERT INTO employees (ID, firstName, lastName, sex, birthDate, hireDate, email, phone, address, managerID, race, ethnicity)
VALUES (@userID, 'John', 'Smith', 'MALE', '1980-01-01', CURRENT_TIMESTAMP, 'johnsmith123@gmail.com', '1234567890', '123 Main St', NULL, "WHITE", "HISPANIC");

INSERT INTO users (username, password, role)
VALUES ('itsJohnnyDoctor21', SHA2('hehe1!', 256), 'DOCTOR');
SET @userID = LAST_INSERT_ID();
INSERT INTO employees (ID, firstName, lastName, sex, birthDate, hireDate, email, phone, address, managerID, race, ethnicity)
VALUES (@userID, 'Johnny', 'Salazar', 'MALE', '1999-02-21', CURRENT_TIMESTAMP, 'jsalazar6421@gmail.com', '4804663466', '123 Main St', NULL, "WHITE", "HISPANIC");

select * from users;
select * from employees;
