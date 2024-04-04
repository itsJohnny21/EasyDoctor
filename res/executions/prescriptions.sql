DROP TABLE IF EXISTS prescriptions;

CREATE TABLE prescriptions (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    patientID INT NOT NULL,
    doctorID INT NOT NULL,
    drugID INT NOT NULL,
    intakeDay ENUM('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY') NOT NULL,
    intakeTime TIME NOT NULL,
    dosageQuantity INT NOT NULL,
    dosageUnits ENUM('PILLS', 'ML', 'MG', 'G', 'IU', 'UNITS') NOT NULL,
    description TEXT,
    FOREIGN KEY (drugID) REFERENCES drugs(ID),
    FOREIGN KEY (doctorID) REFERENCES users(ID),
    FOREIGN KEY (patientID) REFERENCES users(ID)
);

INSERT INTO prescriptions (patientID, doctorID, drugID, intakeDay, intakeTime, dosageQuantity, dosageUnits, description)
VALUES (2, 3, 1, 'MONDAY', '08:00:00', 1, 'PILLS', 'Take one pill every Monday at 8:00 AM');