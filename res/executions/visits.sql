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
    FOREIGN KEY (doctorID) REFERENCES users(ID),
    ON DELE
);

INSERT INTO visits (creationType, date, patientID, doctorID, reason, description)
VALUES ('IN-PERSON', '2021-01-01 12:00:00', 2, 3, 'Checkup', 'Routine checkup');
INSERT INTO visits (creationType, date, patientID, doctorID, reason, description)
VALUES ('ONLINE', '2021-01-02 12:00:00', 2, 3, 'Checkup', 'Routine checkup');
INSERT INTO visits (creationType, date, patientID, doctorID, reason, description)
VALUES ('IN-PERSON', '2021-01-03 12:00:00', 2, 3, 'Checkup', 'Routine checkup');
INSERT INTO visits (creationType, date, patientID, doctorID, reason, description)
VALUES ('ONLINE', '2021-01-04 12:00:00', 2, 3, 'Checkup', 'Routine checkup');
INSERT INTO visits (creationType, date, patientID, doctorID, reason, description)
VALUES ('ONLINE', CURRENT_DATE(), 2, 3, 'Checkup', 'Ghonareah checkup');
INSERT INTO visits (creationType, date, patientID, doctorID, reason, description)
VALUES ('ONLINE', '2024-03-10', 2, 3, 'Checkup', 'Chlamydia checkup');
INSERT INTO visits (creationType, date, patientID, doctorID, reason, description)
VALUES ('ONLINE', '2024-04-01', 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, date, patientID, doctorID, reason, description)
VALUES ('ONLINE', '2025-04-02:09:00:00', 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, date, patientID, doctorID, reason, description)
VALUES ('ONLINE', '2025-04-02:09:12:49', 2, 3, 'Checkup', 'Mental health checkup');

UPDATE visits SET completed = TRUE WHERE patientID =2;
SELECT * FROM visits;
use easydoctor;
SELECT * FROM visits;
SELECT * FROM visits WHERE patientID = 2 AND date = '2021-01-03';