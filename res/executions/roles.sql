CREATE ROLE 'patient';
GRANT UPDATE (gender, email, phone, address, race, ethnicity) ON patients TO 'patient';
GRANT SELECT ON conditions TO 'patient';
GRANT SELECT ON immunization TO 'patient';
GRANT SELECT ON prescriptions TO 'patient';
GRANT SELECT ON surgeries TO 'patient';
GRANT SELECT ON allergies TO 'patient';
GRANT SELECT ON visits TO 'patient';
GRANT SELECT ON conversations TO 'patient';
CREATE USER 'patient1' IDENTIFIED BY 'patient';
DELETE FROM mysql.user WHERE User = 'patient1';


create ROLE 'doctor';
GRANT UPDATE ON prescriptions TO 'doctor';
GRANT UPDATE ON drugs TO 'doctor';
GRANT UPDATE ON allergies TO 'doctor';
GRANT UPDATE ON immunizations TO 'doctor';
GRANT UPDATE ON surgeries TO 'doctor';

GRANT SELECT ON patients TO 'doctor';
GRANT SELECT ON visits TO 'doctor';
GRANT SELECT ON prescriptions TO 'doctor';
GRANT SELECT ON drugs TO 'doctor';
GRANT SELECT ON employees TO 'doctor';
GRANT SELECT ON patients TO 'doctor';
GRANT SELECT ON tasks TO 'doctor';
GRANT SELECT ON conversations TO 'doctor';
GRANT SELECT ON surgeries TO 'doctor';

CREATE ROLE 'nurse';
GRANT UPDATE ON visits TO 'nurse';
GRANT UPDATE ON allergies TO 'nurse';
GRANT UPDATE ON immunizations TO 'nurse';
GRANT UPDATE ON surgeries TO 'nurse';

GRANT SELECT ON patients TO 'nurse';
GRANT SELECT ON visits TO 'nurse';
GRANT SELECT ON prescriptions TO 'nurse';
GRANT SELECT ON drugs TO 'nurse';
GRANT SELECT ON employees TO 'nurse';
GRANT SELECT ON patients TO 'nurse';
GRANT SELECT ON tasks TO 'nurse';
GRANT SELECT ON conversations TO 'nurse';
GRANT SELECT ON surgeries TO 'nurse';

CREATE USER 'neutral' IDENTIFIED BY 'neutral123';
GRANT SELECT ON users TO 'neutral';

SELECT DISTINCT User FROM mysql.user;
DROP USER 'admin'@'%';
SELECT CURRENT_ROLE();
SELECT CURRENT_USER();

ALTER USER 'doctor'@'%' IDENTIFIED BY 'doctor123';
ALTER USER 'nurse'@'%' IDENTIFIED BY 'nurse123';
ALTER USER 'patient'@'%' IDENTIFIED BY 'patient123';
ALTER USER 'neutral'@'%' IDENTIFIED BY 'neutral123';
ALTER USER 'doctor'@'%' ACCOUNT UNLOCK;
ALTER USER 'nurse'@'%' ACCOUNT UNLOCK;
ALTER USER 'patient'@'%' ACCOUNT UNLOCK;
ALTER USER 'neutral'@'%' ACCOUNT UNLOCK;
SHOW GRANTS FOR 'doctor';
SHOW GRANTS FOR 'nurse';
SHOW GRANTS FOR 'patient';
SHOW GRANTS FOR 'neutral';

CREATE USER 'joniboi21' IDENTIFIED BY 'Grapes6421!';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, RELOAD, PROCESS, REFERENCES, INDEX, ALTER, SHOW DATABASES, CREATE TEMPORARY TABLES, LOCK TABLES, EXECUTE, REPLICATION SLAVE, REPLICATION CLIENT, CREATE VIEW, SHOW VIEW, CREATE ROUTINE, ALTER ROUTINE, CREATE USER, EVENT, TRIGGER ON *.* TO `joniboi21`@`%` WITH GRANT OPTION;

GRANT SELECT ON employees TO 'neutral';
REVOKE SELECT ON users FROM 'neutral';
GRANT SELECT (ID, username, role) ON users TO 'neutral'@'%';
GRANT SELECT (passwordHash) ON users TO 'neutral'@'%';

REVOKE ALL PRIVILEGES ON *.* FROM 'patient'@'%';
SHOW GRANTS FOR 'neutral';


SHOW GRANTS FOR 'nurse'@'%';
REVOKE ALL PRIVILEGES ON *.* FROM 'nurse'@'%';
REVOKE INSERT ON logbook FROM 'neutral'@'%';

GRANT SELECT ON patients TO 'doctor';
GRANT SELECT, UPDATE ON employees TO 'doctor';
GRANT SELECT, INSERT ON conversations TO 'doctor';
GRANT UPDATE, INSERT, SELECT ON allergies TO 'doctor';
GRANT UPDATE, INSERT, SELECT ON surgeries TO 'doctor';
GRANT UPDATE, INSERT, SELECT ON visits TO 'doctor';
GRANT INSERT, SELECT ON logbook TO 'doctor';
GRANT UPDATE, INSERT ON drugs TO 'doctor';
GRANT SELECT, UPDATE, INSERT ON immunizations TO 'doctor';
GRANT UPDATE, SELECT, INSERT on healthConditions TO 'doctor';
GRANT SELECT (ID, role, username) ON users TO 'doctor';


GRANT UPDATE, SELECT, INSERT on prescriptions TO 'doctor';
GRANT UPDATE, SELECT, INSERT on drugs TO 'doctor';
GRANT UPDATE (preferredDoctorID, bloodType, weight, height, insuranceProvider, insuranceID) ON patients TO 'nurse';

GRANT SELECT on prescriptions TO 'nurse';
GRANT SELECT on drugs TO 'nurse';
GRANT SELECT (ID, role, username) ON users TO 'nurse';



GRANT SELECT (firstName, lastName, phone, email) ON employees TO 'patient';
GRANT SELECT ON patients TO 'patient';
GRANT UPDATE (firstName, lastName, sex, phone, email, address, preferredDoctorID, insuranceProvider, insuranceID) ON patients TO 'patient';
GRANT SELECT ON allergies TO 'patient';
GRANT SELECT ON surgeries TO 'patient';
GRANT SELECT ON immunizations TO 'patient';
GRANT INSERT, SELECT ON conversations TO 'patient';
GRANT INSERT, SELECT ON visits TO 'patient';
GRANT INSERT ON logbook TO 'patient';
GRANT SELECT ON healthConditions TO 'patient';
GRANT SELECT ON drugs TO 'patient';
GRANT SELECT ON prescriptions TO 'patient';
GRANT SELECT (ID, role, username) ON users TO 'patient';
GRANT SELECT (ID) ON visits TO 'patient';

SHOW TABLES;

SELECT COLUMN_NAME 
FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES 
WHERE TABLE_NAME = 'patients' 
AND PRIVILEGE_TYPE = 'UPDATE' 
AND GRANTEE = "'patient'@'%'";

SHOW GRANTS FOR 'doctor'@'%';
SHOW GRANTS FOR 'nurse'@'%';
SHOW GRANTS FOR 'patient'@'%';

SELECT * FROM patients;

SELECT users.username, users.role, patients.* FROM users JOIN patients ON users.ID = patients.userID WHERE users.ID = 2;

SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE TABLE_NAME = 'patients' AND PRIVILEGE_TYPE = 'UPDATE' AND GRANTEE = "'patient'@'%'";

SELECT CURRENT_USER();

SELECT 
    COLUMN_NAME AS column_name,
    CASE 
        WHEN PRIVILEGE_TYPE IS NOT NULL THEN TRUE
        ELSE FALSE
    END AS can_update
FROM 
    INFORMATION_SCHEMA.COLUMNS 
LEFT JOIN 
    INFORMATION_SCHEMA.COLUMN_PRIVILEGES 
ON 
    INFORMATION_SCHEMA.COLUMNS.TABLE_SCHEMA = INFORMATION_SCHEMA.COLUMN_PRIVILEGES.TABLE_SCHEMA 
    AND INFORMATION_SCHEMA.COLUMNS.TABLE_NAME = INFORMATION_SCHEMA.COLUMN_PRIVILEGES.TABLE_NAME 
    AND INFORMATION_SCHEMA.COLUMNS.COLUMN_NAME = INFORMATION_SCHEMA.COLUMN_PRIVILEGES.COLUMN_NAME 
    AND INFORMATION_SCHEMA.COLUMN_PRIVILEGES.GRANTEE = "'patient'@'%'"
    AND INFORMATION_SCHEMA.COLUMN_PRIVILEGES.PRIVILEGE_TYPE = 'UPDATE';

SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE TABLE_NAME = 'users' AND PRIVILEGE_TYPE = 'SELECT' AND GRANTEE = "'neutral'@'%'";
GRANT SELECT (role) ON users TO 'neutral'@'%';
SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS;
select * from users;
use easydoctor;
SELECT ID, username, role FROM users WHERE username = 'john123' AND SHA2(password, 256) = '123';
SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE PRIVILEGE_TYPE = 'SELECT' AND GRANTEE = "'neutral'@'%'";
SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE PRIVILEGE_TYPE = 'SELECT' AND GRANTEE = "'doctor'@'%'" AND TABLE_NAME = 'allergies';
GRANT SELECT (userID) on users TO 'doctor'@'%';
REVOKE SELECT ON vaccineRecords FROM 'doctor';
SELECT TABLE_NAME, COLUMN_NAME, IF(PRIVILEGE_TYPE = 'UPDATE', true, false) AS Updatable, IF(PRIVILEGE_TYPE = 'SELECT', true, false) AS Selectable, IF(PRIVILEGE_TYPE = "INSERT", true, false) AS Insertable, IF(PRIVILEGE_TYPE = "DELETE", true, false) AS Deletable FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE GRANTEE = "'doctor'@'%'";
use easydoctor;
select * from allergies;
GRANT SELECT (ID, userID, commonSource, severity, type, notes, allergen) ON allergies TO 'doctor';
GRANT UPDATE (userID, commonSource, severity, type, notes, allergen) ON allergies TO 'doctor';
SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE PRIVILEGE_TYPE = 'SELECT' AND GRANTEE = "'doctor'@'%'" AND TABLE_NAME = 'allergies';
SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE TABLE_NAME = 'allergies' AND PRIVILEGE_TYPE = 'SELECT' AND GRANTEE = "'doctor'@'%'";
GRANT SELECT (userID, firstName, lastName, sex, birthDate, email, phone, address, preferredDoctorID, bloodType, height, weight, race, ethnicity, insuranceProvider, insuranceID, emergencyContactName, emergencyContactPhone, motherFirstName, motherLastName, fatherFirstName, fatherLastName) ON patients TO 'doctor'@'%';
GRANT SELECT (userID, firstName, lastName, gender, birthDate, email, phone, address, managerID) ON employees TO 'doctor'@'%';
GRANT UPDATE (gender, email, phone, address) ON employees TO 'doctor'@'%';
GRANT SELECT (ID) ON users TO 'neutral'@'%';
GRANT SELECT (ID) ON patients TO 'neutral'@'%';
GRANT SELECT (ID) ON employees TO 'neutral'@'%';
GRANT UPDATE (fatherLastName, fatherFirstName, lastName, firstName, race, ethnicity, preferredDoctorID, motherLastName, motherFirstName, race) ON patients TO 'doctor';
GRANT UPDATE (fatherLastName, fatherFirstName, lastName, firstName, race, ethnicity, preferredDoctorID, motherLastName, motherFirstName, race) ON patients TO 'doctor';
SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE TABLE_NAME = 'employees' AND PRIVILEGE_TYPE = 'SELECT' AND GRANTEE = "'doctor'@'%'";
SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE TABLE_NAME = 'patients' AND PRIVILEGE_TYPE = 'UPDATE' AND GRANTEE = "'doctor'@'%'";
use easydoctor;
select * from patients;
select * from employees;
select * from users;
GRANT UPDATE (doctorID, type, date, location, notes) ON surgeries TO 'doctor';
select * from users;
select * from vaccines;
select * from patients;
SELECT DISTINCT bloodType FROM patients;

SELECT COLUMN_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'easydoctor' AND TABLE_NAME = 'patients' AND COLUMN_NAME = 'bloodType';
SELECT COLUMN_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'patients' AND COLUMN_NAME = 'bloodType';

select * from patients where ID = 2;
select * from surgeries where userID = 2;
select * from visits;
GRANT INSERT (ID, creationTime, creationType, date, userID, doctorID, completed, reason, description) ON visits TO 'doctor';
FLUSH PRIVILEGES;
SELECT TABLE_NAME, COLUMN_NAME, IF(PRIVILEGE_TYPE = 'UPDATE', true, false) AS Updatable, IF(PRIVILEGE_TYPE = 'SELECT', true, false) AS Selectable, IF(PRIVILEGE_TYPE = "INSERT", true, false) AS Insertable, IF(PRIVILEGE_TYPE = "DELETE", true, false) AS Deletable FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE GRANTEE = "'doctor'@'%'";
SELECT TABLE_NAME IS_GRANTABLE FROM INFORMATION_SCHEMA.TABLE_PRIVILEGES WHERE PRIVILEGE_TYPE = 'DELETE' AND GRANTEE = "'doctor'@'%'";
SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE PRIVILEGE_TYPE = 'UPDATE' AND TABLE_NAME = 'surgeries' AND GRANTEE = "'doctor'@'%'";
SELECT COLUMN_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'users' AND COLUMN_NAME = 'role';
GRANT DELETE ON allergies TO 'doctor';
GRANT DELETE ON surgeries TO 'doctor';
select * from surgeries;
SELECT TABLE_NAME, COLUMN_NAME, IF(PRIVILEGE_TYPE = 'UPDATE', true, false) AS can_update FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE GRANTEE = "'doctor'@'%'";
GRANT DELETE ON users to 'neutral'@'%';