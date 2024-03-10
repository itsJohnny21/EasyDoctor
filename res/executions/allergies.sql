CREATE TABLE allergies (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    patientID INT NOT NULL,
    allergy VARCHAR(100) NOT NULL,
    severity ENUM('MILD', 'MODERATE', 'SEVERE') NOT NULL,
    notes TEXT,
    FOREIGN KEY (patientID) REFERENCES patients(ID)
);

INSERT INTO allergies (patientID, allergy, severity, notes)
VALUES (2, 'Peanuts', 'MODERATE', 'Patient is allergic to peanuts');

UPDATE allergies SET notes = 'Patient has nightmares about peanut butter.' WHERE patientID = 2;
UPDATE allergies SET severity = 'SEVERE' WHERE patientID = 2;

