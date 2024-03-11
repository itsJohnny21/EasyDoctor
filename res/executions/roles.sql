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