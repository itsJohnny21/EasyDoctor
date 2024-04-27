DROP TABLE IF EXISTS prescriptions;

CREATE TABLE prescriptions (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    userID INT NOT NULL,
    doctorID INT NOT NULL,
    drugID INT NOT NULL,
    intakeDay ENUM('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY') NOT NULL,
    intakeTime TIME NOT NULL,
    dosageQuantity INT NOT NULL,
    dosageUnits ENUM('PILLS', 'ML', 'MG', 'G', 'IU', 'UNITS') NOT NULL,
    description TEXT,
    FOREIGN KEY (drugID) REFERENCES drugs(ID),
    FOREIGN KEY (doctorID) REFERENCES employees(ID),
    FOREIGN KEY (userID) REFERENCES patients(ID),
    UNIQUE(userID, drugID, intakeDay, intakeTime)
);

show create table prescriptions;
INSERT INTO prescriptions (userID, doctorID, drugID, intakeDay, intakeTime, dosageQuantity, dosageUnits, description)
VALUES (2, 3, 1, 'MONDAY', '08:00:00', 1, 'PILLS', 'Take one pill every Monday at 8:00 AM');

INSERT INTO prescriptions (userID, doctorID, drugID, intakeDay, intakeTime, dosageQuantity, dosageUnits, description)
VALUES (2, 3, (SELECT ID FROM drugs WHERE name = 'Albuterol'), 'MONDAY', '08:00:00', 200, 'ML', 'Take one pill every Monday at 8:00 AM');

INSERT INTO prescriptions (userID, doctorID, drugID, intakeDay, intakeTime, dosageQuantity, dosageUnits, description)
VALUES (2, 3, 1, 'MONDAY', '15:00:00', 1, 'PILLS', 'Take one pill every Wednesday at 03:00 PM');

INSERT INTO prescriptions (userID, doctorID, drugID, intakeDay, intakeTime, dosageQuantity, dosageUnits, description)
VALUES (2, 3, (SELECT ID FROM drugs WHERE name = 'Atorvastatin'), 'MONDAY', '15:00:00', 1, 'G', 'Take one pill every Wednesday at 03:00 PM');

use easydoctor;
select * from drugs;
select * from prescriptions;

SELECT * FROM prescriptions WHERE drugID IN (SELECT ID FROM drugs WHERE name LIKE '%08%') OR dosageQuantity LIKE '%08%' OR dosageUnits LIKE '%08%' OR intakeDay LIKE '%08%' OR intakeTime LIKE '%08%' OR description LIKE '%08%';