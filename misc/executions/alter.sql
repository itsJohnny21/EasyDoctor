USE easydoctor;
 
 ALTER employees ADD COLUMN salary DECIMAL(10, 2);


 ALTER TABLE employees DROP COLUMN role;

 UPDATE users SET userType = "DOCTOR" WHERE ID = 3;

 ALTER TABLE users MODIFY COLUMN userType ENUM('ADMIN', 'DOCTOR', 'NURSE', 'PATIENT') NOT NULL;