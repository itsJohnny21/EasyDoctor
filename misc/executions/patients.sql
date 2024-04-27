DROP TABLE IF EXISTS patients;

CREATE TABLE patients (
    ID INT AUTO_INCREMENT PRIMARY KEY,
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
    insuranceProviderID INT DEFAULT NULL,
    insuranceID VARCHAR(100),
    emergencyContactName VARCHAR(255),
    emergencyContactPhone VARCHAR(255),
    motherFirstName VARCHAR(255),
    motherLastName VARCHAR(255),
    fatherFirstName VARCHAR(255),
    fatherLastName VARCHAR(255),
    pharmacyID INT DEFAULT NULL,
    UNIQUE(email),
    UNIQUE(phone),
    UNIQUE(insuranceProvider, insuranceID),
    FOREIGN KEY (ID) REFERENCES users(ID) ON DELETE, CASCADE,
    FOREIGN KEY (preferredDoctorID) REFERENCES users(ID) ON DELETE CASCADE,
    FOREIGN KEY (pharmacyID) REFERENCES pharmacies(ID),
    FOREIGN KEY (insuranceProviderID) REFERENCES insuranceProviders(ID)
);
select * from patients;
use easydoctor;
show create table patients;

INSERT INTO users (username, password, role)
VALUES ('test', 'test', 'PATIENT');

INSERT INTO patients (userID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity)
VALUES (2, 'test', 'test',  'OTHER', '2000-01-01', 'test@test.com', '1234567890', '123 Test St', 'WHITE', 'HISPANIC');

SELECT * FROM patients;


SELECT users.username, users.role, insuranceProviders.name AS 'insuranceProviderName', pharmacies.name AS 'pharmacyName', employees.firstName AS 'preferredDoctorFirstName', employees.lastName AS 'preferredDoctorLastName', patients.* FROM patients JOIN users ON users.ID = patients.ID LEFT JOIN insuranceProviders ON patients.insuranceProviderID = insuranceProviders.ID LEFT JOIN pharmacies ON patients.pharmacyID = pharmacies.ID LEFT JOIN employees ON patients.preferredDoctorID = employees.ID WHERE patients.ID = 2;
use easydoctor;
SELECT * FROM patients;
update patients set pharmacyID = 15 where ID = 2;
SELECT * FROM users;