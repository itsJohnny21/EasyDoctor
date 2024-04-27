CREATE TABLE healthConditions (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    patientID INT NOT NULL,
    healthCondition VARCHAR(100) NOT NULL,
    severity ENUM('ACUTE', 'CHRONIC') NOT NULL,
    type ENUM('PHYSICAL', 'MENTAL') NOT NULL,
    notes TEXT,
    FOREIGN KEY (patientID) REFERENCES users(ID) ON DELETE CASCADE,
);

alter table healthConditions drop foreign key healthConditions_ibfk_1;
alter table healthConditions add FOREIGN KEY (userID) REFERENCES users(ID) ON DELETE CASCADE;

INSERT INTO healthConditions (patientID, healthCondition, severity, type, notes)
VALUES (2, 'Diabetes', 'CHRONIC', 'PHYSICAL', 'Type 2 diabetes cus he be eatin too much sugar');

INSERT INTO healthConditions (patientID, healthCondition, severity, type, notes)
VALUES (2, 'Obesity', 'CHRONIC', 'PHYSICAL', 'Patient loves to eat junk food');

INSERT INTO healthConditions (patientID, healthCondition, severity, type, notes)
VALUES (2, 'Depression', 'CHRONIC', 'MENTAL', 'Patient is hella stressed out');

UPDATE healthConditions SET notes = 'Type 2 diabetes' WHERE patientID = 2;
use easydoctor;
SELECT * FROM healthConditions;