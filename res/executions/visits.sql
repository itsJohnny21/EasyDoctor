DROP TABLE IF EXISTS visits;

CREATE TABLE visits (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creationType ENUM('IN-PERSON', 'ONLINE') NOT NULL,
    date DATETIME NOT NULL,
    patientID INT NOT NULL,
    doctorID INT NOT NULL,
    completed BOOLEAN DEFAULT FALSE NOT NULL,
    reason TEXT,
    description TEXT,
    FOREIGN KEY (patientID) REFERENCES users(ID),
    FOREIGN KEY (doctorID) REFERENCES users(ID)
);

INSERT INTO visits (creationType, date, patientID, doctorID, reason, description)
VALUES ('IN-PERSON', '2021-01-01 12:00:00', 2, 3, 'Checkup', 'Routine checkup');

UPDATE visits SET completed = TRUE WHERE patientID =2;

DELETE FROM visits WHERE patientID = 2;