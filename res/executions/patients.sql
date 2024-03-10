DROP TABLE IF EXISTS patients;

CREATE TABLE patients (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    userID INT NOT NULL,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    birthDate DATE NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    preferredDoctorID INT,
    bloodType ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'),
    height DECIMAL(5, 2),
    weight DECIMAL(5, 2),
    race ENUM('WHITE', 'BLACK', 'HISPANIC', 'ASIAN', 'NATIVE AMERICAN', 'PACIFIC ISLANDER', 'OTHER') NOT NULL,
    ethnicity ENUM('HISPANIC', 'NON-HISPANIC') NOT NULL,
    FOREIGN KEY (userID) REFERENCES users(ID),
    FOREIGN KEY (preferredDoctorID) REFERENCES users(ID)
);

INSERT INTO users (username, password, userType)
VALUES ('test', 'test', 'PATIENT');

INSERT INTO patients (userID, firstName, lastName, gender, birthDate, email, phone, address, race, ethnicity)
VALUES (2, 'test', 'test',  'OTHER', '2000-01-01', 'test@test.com', '1234567890', '123 Test St', 'WHITE', 'HISPANIC');

CREATE ROLE 'patients';
GRANT UPDATE (gender, email, phone, address, race, ethnicity) ON patients TO 'patients';
