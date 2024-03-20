CREATE TABLE allergies (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    patientID INT NOT NULL,
    allergen VARCHAR(100) NOT NULL,
    commonSource VARCHAR(100) NOT NULL,
    severity ENUM('MILD', 'MODERATE', 'SEVERE') NOT NULL,
    type ENUM('FOOD', 'DRUG', 'ENVIRONMENTAL', 'INSECT', 'ANIMAL', 'PLANT', 'OTHER') NOT NULL,
    notes TEXT,
    FOREIGN KEY (patientID) REFERENCES users(ID)
);
INSERT INTO allergies (patientID, allergen, commonSource, severity, type, notes)
VALUES (2, 'Peanuts', 'Peanut Butter', 'MILD', 'FOOD', 'Patient has nightmares about peanut butter.');

UPDATE allergies SET notes = 'Patient has nightmares about peanut butter.' WHERE patientID = 2;
UPDATE allergies SET severity = 'SEVERE' WHERE patientID = 2;

INSERT INTO allergies (patientID, allergen, commonSource, severity, type, notes)
VALUES (2, 'Pollen', 'Flowers', 'MODERATE', 'ENVIRONMENTAL', 'Patient sneezes a lot.');

INSERT INTO allergies (patientID, allergen, commonSource, severity, type, notes)
VALUES (2, 'Penicillin', 'Medicine', 'SEVERE', 'DRUG', 'Patient has anaphylactic shock.');

INSERT INTO allergies (patientID, allergen, commonSource, severity, type, notes)
VALUES (2, 'Cats', 'Cats', 'MILD', 'ANIMAL', 'Patient gets itchy.');

SELECT * FROM allergies;

