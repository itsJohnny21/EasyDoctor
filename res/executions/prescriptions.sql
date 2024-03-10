DROP TABLE IF EXISTS prescriptions;

CREATE TABLE prescriptions (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    patientID INT NOT NULL,
    doctorID INT NOT NULL,
    drugID INT NOT NULL,
    dosage INT NOT NULL,
    dosageUnits ENUM('PILLS', 'ML', 'MG', 'G', 'IU', 'UNITS') NOT NULL,
    notes TEXT,
    intakeDay ENUM('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY') NOT NULL;
    intakeTime TIME NOT NULL;
    FOREIGN KEY (drugID) REFERENCES drugs(ID),
    FOREIGN KEY (doctorID) REFERENCES users(ID),
    FOREIGN KEY (patientID) REFERENCES users(ID)
);

INSERT INTO prescriptions (patientID, doctorID, drugID, dosage, notes, intakeDay, intakeTime)
VALUES (2, 3, 1, '1 pill every 6 hours', 'Take with food', 'MONDAY', '12:00:00');