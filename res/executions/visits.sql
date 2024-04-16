DROP TABLE IF EXISTS visits;

CREATE TABLE visits (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creationType ENUM('IN-PERSON', 'ONLINE') NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    userID INT NOT NULL,
    doctorID INT NOT NULL,
    completed BOOLEAN DEFAULT FALSE NOT NULL,
    reason TEXT,
    description TEXT,
    FOREIGN KEY (userID) REFERENCES users(ID),
    FOREIGN KEY (doctorID) REFERENCES users(ID),
    ALTER TABLE visits,
    UNIQUE (userID, date)
);

INSERT INTO visits (creationType, date, userID, doctorID, reason, description)
VALUES ('IN-PERSON', '2021-01-01 12:00:00', 2, 3, 'Checkup', 'Routine checkup');
INSERT INTO visits (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2021-01-02 12:00:00', 2, 3, 'Checkup', 'Routine checkup');
INSERT INTO visits (creationType, date, userID, doctorID, reason, description)
VALUES ('IN-PERSON', '2021-01-03 12:00:00', 2, 3, 'Checkup', 'Routine checkup');
INSERT INTO visits (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2021-01-04 12:00:00', 2, 3, 'Checkup', 'Routine checkup');
INSERT INTO visits (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', CURRENT_DATE(), 2, 3, 'Checkup', 'Ghonareah checkup');
INSERT INTO visits (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2024-03-10', 2, 3, 'Checkup', 'Chlamydia checkup');
INSERT INTO visits (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2024-04-01', 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2025-04-02:09:00:00', 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2025-04-02:09:12:49', 2, 3, 'Checkup', 'Mental health checkup');
VALUES ('ONLINE', '2025-04-02:09:00:00', 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2025-04-02:09:13:49', 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2025-04-03:09:14:53', 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2025-04-03:09:14:21', 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2025-04-03:09:14:54', 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, date, userID, doctorID, reason, description, date2, time)
VALUES ('ONLINE', '2025-04-03:09:14:55', 2, 3, 'Checkup', 'Mental health checkup', '1999-02-21', '12:57:00');
INSERT INTO visits (creationType, userID, doctorID, reason, description, date, time)
VALUES ('ONLINE', 2, 3, 'Checkup', 'Mental health checkup', '1999-02-22', '12:56:00');
INSERT INTO visits (creationType, userID, doctorID, reason, description, date, time)
VALUES ('ONLINE', 2, 3, 'Checkup', 'Mental health checkup', '1999-02-23', '12:55:00');

UPDATE visits SET completed = TRUE WHERE userID =2;
SELECT * FROM visits;
use easydoctor;
SELECT * FROM visits;
SELECT * FROM visits WHERE userID = 2 AND date = '2021-01-03';