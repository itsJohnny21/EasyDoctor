CREATE TABLE healthConditions (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    patientID INT NOT NULL,
    healthCondition VARCHAR(100) NOT NULL,
    severity ENUM('ACUTE', 'CHRONIC') NOT NULL,
    type ENUM('PHYSICAL', 'MENTAL') NOT NULL,
    notes TEXT,
    FOREIGN KEY (patientID) REFERENCES users(ID)
);

INSERT INTO healthConditions (patientID, healthCondition, severity, type, notes)
VALUES (2, 'Diabetes', 'CHRONIC', 'PHYSICAL', 'Type 2 diabetes cus he be eatin too much sugar');

UPDATE healthConditions SET notes = 'Type 2 diabetes' WHERE patientID = 2;