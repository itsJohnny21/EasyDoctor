DROP TABLE IF EXISTS patients;

CREATE TABLE patients (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    userID INT NOT NULL,
    firstName VARCHAR(100) NOT NULL,
    middleName VARCHAR(100) DEFAULT NULL,
    lastName VARCHAR(100) NOT NULL,
    sex ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    birthDate DATE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(100) NOT NULL UNIQUE,
    address VARCHAR(100) NOT NULL,
    preferredDoctorID INT,
    bloodType bloodType ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-', 'UNKNOWN')
    height DECIMAL(5, 2),
    weight DECIMAL(5, 2),
    race ENUM('WHITE', 'BLACK', 'HISPANIC', 'ASIAN', 'NATIVE_AMERICAN', 'PACIFIC_ISLANDER', 'OTHER') NOT NULL,
    ethnicity ENUM('HISPANIC', 'NON_HISPANIC') NOT NULL,
    insuranceProvider VARCHAR(100),
    insuranceID VARCHAR(100),
    emergencyContactName VARCHAR(255),
    emergencyContactPhone VARCHAR(255),
    motherFirstName VARCHAR(255),
    motherLastName VARCHAR(255),
    fatherFirstName VARCHAR(255),
    fatherLastName VARCHAR(255),
    FOREIGN KEY (userID) REFERENCES users(ID) ON CASCADE DELETE,
    FOREIGN KEY (preferredDoctorID) REFERENCES users(ID) ON CASCADE DELETE
);

show create table patients;

INSERT INTO users (username, password, role)
VALUES ('test', 'test', 'PATIENT');

INSERT INTO patients (userID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity)
VALUES (2, 'test', 'test',  'OTHER', '2000-01-01', 'test@test.com', '1234567890', '123 Test St', 'WHITE', 'HISPANIC');

CREATE ROLE 'patients';
GRANT UPDATE (sex, email, phone, address, race, ethnicity) ON patients TO 'patients';
SELECT * FROM patients;


SELECT patients.firstName, patients.lastName, patients.sex, patients.birthDate, patients.phone, patients.email, patients.address, users.username, patients.race, patients.ethnicity, patients.emergencyContactName, patients.emergencyContactPhone, patients.motherFirstName, patients.motherLastName, patients.fatherFirstName, patients.fatherLastName FROM patients JOIN users ON users.ID = patients.userID WHERE userID = 2;
use easydoctor;
SELECT * FROM patients;
SELECT * FROM users;
delete from users where ID = 106;