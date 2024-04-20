DROP TABLE IF EXISTS visitsOLD;

CREATE TABLE visitsOLD (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creationType ENUM('IN_PERSON', 'ONLINE') NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    timezone VARCHAR(255) NOT NULL,
    userID INT NOT NULL,
    doctorID INT NOT NULL,
    completed BOOLEAN DEFAULT FALSE NOT NULL,
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETE', 'MISSED') DEFAULT 'PENDING' NOT NULL,
    reason TEXT,
    description TEXT,
    FOREIGN KEY (userID) REFERENCES users(ID),
    FOREIGN KEY (doctorID) REFERENCES users(ID),
    ALTER TABLE visitsOLD,
    UNIQUE (userID, date, timzezone)
);
RENAME TABLE visits TO visitsOLD;
alter table visitsOLD change column status status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED', 'MISSED', 'CANCELLED') DEFAULT 'PENDING' NOT NULL;

INSERT INTO visitsOLD (creationType, date, userID, doctorID, reason, description)
VALUES ('IN-PERSON', '2021-01-01 12:00:00', 2, 3, 'Checkup', 'Routine checkup');
INSERT INTO visitsOLD (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2021-01-02 12:00:00', 2, 3, 'Checkup', 'Routine checkup');
INSERT INTO visitsOLD (creationType, date, userID, doctorID, reason, description)
VALUES ('IN-PERSON', '2021-01-03 12:00:00', 2, 3, 'Checkup', 'Routine checkup');
INSERT INTO visitsOLD (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2021-01-04 12:00:00', 2, 3, 'Checkup', 'Routine checkup');
INSERT INTO visitsOLD (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', CURRENT_DATE(), 2, 3, 'Checkup', 'Ghonareah checkup');
INSERT INTO visitsOLD (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2024-03-10', 2, 3, 'Checkup', 'Chlamydia checkup');
INSERT INTO visitsOLD (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2024-04-01', 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visitsOLD (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2025-04-02:09:00:00', 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visitsOLD (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2025-04-02:09:12:49', 2, 3, 'Checkup', 'Mental health checkup');
VALUES ('ONLINE', '2025-04-02:09:00:00', 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visitsOLD (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2025-04-02:09:13:49', 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visitsOLD (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2025-12-03:09:14:53', 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visitsOLD (creationType, date, userID, doctorID, reason, description, time)
VALUES ('ONLINE', '2025-06-03:09:14:21', 2, 3, 'Checkup', 'Mental health checkup', '12:52:00');
INSERT INTO visitsOLD (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2025-04-03:09:14:54', 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visitsOLD (creationType, date, userID, doctorID, reason, description, time)
VALUES ('ONLINE', '2025-04-03:09:14:55', 2, 3, 'Checkup', 'Mental health checkup', '12:57:00');
INSERT INTO visitsOLD (creationType, userID, doctorID, reason, description, date, time)
VALUES ('ONLINE', 2, 3, 'Checkup', 'Mental health checkup', '1999-02-22', '12:56:00');
INSERT INTO visitsOLD (creationType, userID, doctorID, reason, description, date, time)
VALUES ('ONLINE', 2, 3, 'Checkup', 'Mental health checkup', '1999-02-23', '12:55:00');
INSERT INTO visitsOLD (creationType, userID, doctorID, reason, description, date, time)
VALUES ('ONLINE', 2, 3, 'Checkup', 'Mental health checkup', CURRENT_DATE, '02:55:00');

INSERT INTO visitsOLD (creationType, userID, doctorID, reason, description, date, time)
VALUES ('ONLINE', 182, 3, 'Checkup', 'Mental health checkup', CURRENT_DATE, '02:45:00');
INSERT INTO visitsOLD (creationType, userID, doctorID, reason, description, date, time)
VALUES ('ONLINE', 197, 3, 'Checkup', 'Mental health checkup', CURRENT_DATE, '02:45:00');
INSERT INTO visitsOLD (creationType, userID, doctorID, reason, description, date, time)
VALUES ('ONLINE', 206, 3, 'Checkup', 'Mental health checkup', CURRENT_DATE, '02:45:00');
INSERT INTO visitsOLD (creationType, userID, doctorID, reason, description, date, time)
VALUES ('ONLINE', 207, 3, 'Checkup', 'Mental health checkup', '2024-04-20', '12:59:00');

select * from patients;
UPDATE visitsOLD SET completed = TRUE WHERE userID =2;
SELECT * FROM visitsOLD;
use easydoctor;
SELECT * FROM visitsOLD;
SELECT * FROM visitsOLD WHERE userID = 2;

SELECT ID, doctorID, date, time, reason, completed, creationTime, creationType, description FROM visitsOLD WHERE userID = 2 ORDER BY date DESC;
SELECT ID, doctorID, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'date', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'time', reason, completed, creationTime, creationType, description, status FROM visitsOLD WHERE DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) = DATE(CONVERT_TZ(NOW(), '+00:00', 'America/Phoenix')) ORDER BY date DESC;
INSERT INTO visitsOLD (creationType, date, userID, doctorID, reason, description, time, timezone)
VALUES ('ONLINE', CURRENT_DATE, 182, 3, 'Checkup', 'Mental health checkup', CURRENT_TIME, 'America/Phoenix');
select DATE(CONVERT_TZ(NOW(), '+00:00', 'America/Phoenix'));