DROP TABLE IF EXISTS visits;

CREATE TABLE visits (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creationType ENUM('IN_PERSON', 'ONLINE') NOT NULL,
    localdate DATE NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    patientID INT NOT NULL,
    nurseID INT DEFAULT NULL,
    doctorID INT NOT NULL,
    completed BOOLEAN DEFAULT FALSE NOT NULL,
    active BOOLEAN DEFAULT FALSE NOT NULL,
    cancelled BOOLEAN DEFAULT FALSE NOT NULL,
    reason TEXT,
    description TEXT,
    weight INT DEFAULT NULL,
    height INT DEFAULT NULL,
    systolicBloodPressure INT DEFAULT NULL,
    diastolicBloodPressure INT DEFAULT NULL,
    heartRate INT DEFAULT NULL,
    respiratoryRate INT DEFAULT NULL,
    bodyTemperature INT DEFAULT NULL,
    notes TEXT,
    currentPage INT DEFAULT 0,
    FOREIGN KEY (patientID) REFERENCES patients(ID),
    FOREIGN KEY (doctorID) REFERENCES employees(ID),
    FOREIGN KEY (nurseID) REFERENCES employees(ID),
    UNIQUE KEY (patientID, localdate),
    CONSTRAINT check_only_one_status CHECK (
        (completed = TRUE AND active = FALSE AND cancelled = FALSE) OR
        (completed = FALSE AND active = TRUE AND cancelled = FALSE) OR
        (completed = FALSE AND active = FALSE AND cancelled = TRUE) OR
        (completed = FALSE AND active = FALSE AND cancelled = FALSE)
    )
);

show create table visits;

-- Schedule a new visit
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', ?, DATE(CONVERT_TZ(CONCAT(?, ' ', ?), ?, '+00:00')), TIME(CONVERT_TZ(CONCAT(?, ' ', ?), ?, '+00:00')), ?, ?, ?, ?); -- Statement for Java
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', '2024-04-20', DATE(CONVERT_TZ('2024-04-20 01:30:00', 'America/Phoenix', '+00:00')), TIME(CONVERT_TZ('2024-04-20 01:30:00', 'America/Phoenix', '+00:00')), 182, 3, 'Checkup', 'Mental health checkup'); -- Visit for 2024-04-20 01:30 am in Arizona time with ID = 1
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', '2024-04-19', DATE(CONVERT_TZ('2024-04-19 22:30:00', 'America/Phoenix', '+00:00')), TIME(CONVERT_TZ('2024-04-19 22:30:00', 'America/Phoenix', '+00:00')), 182, 3, 'Checkup', 'Mental health checkup'); -- Visit for 2024-04-19 10:30 pm in Arizona time with ID = 3
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', '2024-04-19', DATE(CONVERT_TZ('2024-04-19 22:45:00', 'America/Phoenix', '+00:00')), TIME(CONVERT_TZ('2024-04-19 22:45:00', 'America/Phoenix', '+00:00')), 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', '2024-04-19', DATE(CONVERT_TZ('2024-04-19 23:45:00', 'America/Phoenix', '+00:00')), TIME(CONVERT_TZ('2024-04-19 23:45:00', 'America/Phoenix', '+00:00')), 207, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', '2024-04-21', DATE(CONVERT_TZ('2024-04-21 23:45:00', 'America/Phoenix', '+00:00')), TIME(CONVERT_TZ('2024-04-21 23:45:00', 'America/Phoenix', '+00:00')), 207, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', '2024-04-20', DATE(CONVERT_TZ('2024-04-20 23:45:00', 'America/Phoenix', '+00:00')), TIME(CONVERT_TZ('2024-04-20 23:45:00', 'America/Phoenix', '+00:00')), 207, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', '2024-04-20', DATE(CONVERT_TZ('2024-04-20 01:45:00', 'America/Phoenix', '+00:00')), TIME(CONVERT_TZ('2024-04-20 01:45:00', 'America/Phoenix', '+00:00')), 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', '2024-04-19', DATE(CONVERT_TZ('2024-04-19 22:30:00', 'America/Phoenix', '+00:00')), TIME(CONVERT_TZ('2024-04-19 22:30:00', 'America/Phoenix', '+00:00')), 2, 3, 'Checkup', 'Mental health checkup'); -- Duplicate key error for date and time
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', '2024-04-22', DATE(CONVERT_TZ('2024-04-22 22:30:00', 'America/Phoenix', '+00:00')), TIME(CONVERT_TZ('2024-04-22 22:30:00', 'America/Phoenix', '+00:00')), 226, 3, 'Checkup', 'Mental health checkup'); -- Duplicate key error for patientID and localdate

-- Retrieve all visits
SELECT ID, creationType, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localtime', patientID, doctorID, reason, description, 
CASE 
    WHEN completed = TRUE THEN 'COMPLETE'
    WHEN active = TRUE THEN 'ACTIVE'
    WHEN cancelled = TRUE THEN 'CANCELLED'
    WHEN CONCAT(date, ' ', time) < TIMESTAMPADD(MINUTE, -15, NOW()) THEN 'MISSED'
    WHEN CONCAT(date, ' ', time) BETWEEN TIMESTAMPADD(MINUTE, -15, NOW()) AND TIMESTAMPADD(MINUTE, 15, NOW()) THEN 'PENDING'
    ELSE 'UPCOMING'
END AS 'status'
FROM visits; -- Statement for Java

SELECT ID, creationType, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'localtime', patientID, doctorID, reason, description, 
CASE 
    WHEN completed = TRUE THEN 'COMPLETE'
    WHEN active = TRUE THEN 'ACTIVE'
    WHEN cancelled = TRUE THEN 'CANCELLED'
    WHEN CONCAT(date, ' ', time) < TIMESTAMPADD(MINUTE, -15, NOW()) THEN 'MISSED'
    WHEN CONCAT(date, ' ', time) BETWEEN TIMESTAMPADD(MINUTE, -15, NOW()) AND TIMESTAMPADD(MINUTE, 15, NOW()) THEN 'PENDING'
    ELSE 'UPCOMING'
END AS 'status'
FROM visits; -- Retrieve all visits for Phoneix time

-- Retrieve a visit
SELECT ID, creationType, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localtime', patientID, doctorID, reason, description,
CASE 
    WHEN completed = TRUE THEN 'COMPLETE'
    WHEN active = TRUE THEN 'ACTIVE'
    WHEN cancelled = TRUE THEN 'CANCELLED'
    WHEN CONCAT(date, ' ', time) < TIMESTAMPADD(MINUTE, -15, NOW()) THEN 'MISSED'
    WHEN CONCAT(date, ' ', time) BETWEEN TIMESTAMPADD(MINUTE, -15, NOW()) AND TIMESTAMPADD(MINUTE, 15, NOW()) THEN 'PENDING'
    ELSE 'UPCOMING'
END AS 'status'
FROM visits WHERE ID = ?; -- Statement for Java
SELECT ID, creationType, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'localtime', patientID, doctorID, reason, description,
CASE 
    WHEN completed = TRUE THEN 'COMPLETE'
    WHEN active = TRUE THEN 'ACTIVE'
    WHEN cancelled = TRUE THEN 'CANCELLED'
    WHEN CONCAT(date, ' ', time) < TIMESTAMPADD(MINUTE, -15, NOW()) THEN 'MISSED'
    WHEN CONCAT(date, ' ', time) BETWEEN TIMESTAMPADD(MINUTE, -15, NOW()) AND TIMESTAMPADD(MINUTE, 15, NOW()) THEN 'PENDING'
    ELSE 'UPCOMING'
END AS 'status'
FROM visits WHERE ID = 2; -- Visit with ID = 2

-- Retrieve todays visits
SELECT ID, creationType, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localtime', patientID, doctorID, reason, description,
CASE 
    WHEN completed = TRUE THEN 'COMPLETE'
    WHEN active = TRUE THEN 'ACTIVE'
    WHEN cancelled = TRUE THEN 'CANCELLED'
    WHEN CONCAT(date, ' ', time) < TIMESTAMPADD(MINUTE, -15, NOW()) THEN 'MISSED'
    WHEN CONCAT(date, ' ', time) BETWEEN TIMESTAMPADD(MINUTE, -15, NOW()) AND TIMESTAMPADD(MINUTE, 15, NOW()) THEN 'PENDING'
    ELSE 'UPCOMING'
END AS 'status'
FROM visits WHERE DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) = DATE(CONVERT_TZ(NOW(), '+00:00', ?)) ORDER BY date DESC; -- Statement for Java
SELECT ID, creationType, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'localtime', patientID, doctorID, reason, description,
CASE 
    WHEN completed = TRUE THEN 'COMPLETE'
    WHEN active = TRUE THEN 'ACTIVE'
    WHEN cancelled = TRUE THEN 'CANCELLED'
    WHEN CONCAT(date, ' ', time) < TIMESTAMPADD(MINUTE, -15, NOW()) THEN 'MISSED'
    WHEN CONCAT(date, ' ', time) BETWEEN TIMESTAMPADD(MINUTE, -15, NOW()) AND TIMESTAMPADD(MINUTE, 15, NOW()) THEN 'PENDING'
    ELSE 'UPCOMING'
END AS 'status'
FROM visits WHERE DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) = DATE(CONVERT_TZ(NOW(), '+00:00', 'America/Phoenix')) ORDER BY date DESC; -- Todays visits for Phoneix time

-- Add visit for current time (testing purposes only)
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', DATE(CONVERT_TZ(NOW(), '+00:00', 'America/Phoenix')), DATE(NOW()), TIME(NOW()), 2, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', DATE(CONVERT_TZ(NOW(), '+00:00', 'America/Phoenix')), DATE(NOW()), TIME(NOW()), 224, 3, 'Checkup', 'Mental health checkup');
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', DATE(CONVERT_TZ(NOW(), '+00:00', 'America/Phoenix')), DATE(NOW()), TIME(NOW()), 207, 3, 'Checkup', 'Mental health checkup');
delete from visits where localdate = '2024-04-22';
select * from patients;

-- Get upcoming visit for a patient
SELECT ID, creationType, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localtime', patientID, doctorID, reason, description FROM visits WHERE patientID = ? AND CONCAT(date, ' ', time) >= NOW() AND cancelled = FALSE ORDER BY CONCAT(date, ' ', time) ASC LIMIT 1; -- Statement for Java
SELECT ID, creationType, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'localtime', patientID, doctorID, reason, description FROM visits WHERE patientID = 207 AND CONCAT(date, ' ', time) >= NOW() AND cancelled = FALSE AND completed = FALSE AND active = FALSE ORDER BY CONCAT(date, ' ', time) ASC LIMIT 1; -- Upcoming visits for patient with ID = 207

-- Get a patient's visits
SELECT ID, creationType, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localtime', patientID, doctorID, reason, description,
CASE 
    WHEN completed = TRUE THEN 'COMPLETE'
    WHEN active = TRUE THEN 'ACTIVE'
    WHEN cancelled = TRUE THEN 'CANCELLED'
    WHEN CONCAT(date, ' ', time) < TIMESTAMPADD(MINUTE, -15, NOW()) THEN 'MISSED'
    WHEN CONCAT(date, ' ', time) BETWEEN TIMESTAMPADD(MINUTE, -15, NOW()) AND TIMESTAMPADD(MINUTE, 15, NOW()) THEN 'PENDING'
    ELSE 'UPCOMING'
END AS 'status'
FROM visits WHERE patientID = ? ORDER BY date DESC, time DESC; -- Statement for Java
SELECT ID, creationType, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'localtime', patientID, doctorID, reason, description,
CASE 
    WHEN completed = TRUE THEN 'COMPLETE'
    WHEN active = TRUE THEN 'ACTIVE'
    WHEN cancelled = TRUE THEN 'CANCELLED'
    WHEN CONCAT(date, ' ', time) < TIMESTAMPADD(MINUTE, -15, NOW()) THEN 'MISSED'
    WHEN CONCAT(date, ' ', time) BETWEEN TIMESTAMPADD(MINUTE, -15, NOW()) AND TIMESTAMPADD(MINUTE, 15, NOW()) THEN 'PENDING'
    ELSE 'UPCOMING'
END AS 'status'
FROM visits WHERE patientID = 2 ORDER BY date DESC, time DESC; -- Visits for patient with ID = 2

-- Cancel a visit
UPDATE visits SET cancelled = TRUE, active = FALSE WHERE ID = ?;
UPDATE visits SET cancelled = TRUE, active = FALSE WHERE ID = 20; -- Cancel visit with ID = 207
UPDATE visits SET cancelled = FALSE WHERE ID = 20; -- Uncancel visit with ID = 207

-- Complete a visit
UPDATE visits SET completed = TRUE, active = FALSE WHERE ID = ? AND active = TRUE;
UPDATE visits SET completed = TRUE, active = FALSE WHERE ID = 20; -- Complete visit with ID = 207
UPDATE visits SET completed = FALSE, active = TRUE WHERE ID = 20; -- Uncomplete visit with ID = 207

-- Start a visit
UPDATE visits SET active = TRUE WHERE ID = ? AND CONCAT(date, ' ', time) BETWEEN TIMESTAMPADD(MINUTE, -15, NOW()) AND TIMESTAMPADD(MINUTE, 15, NOW());
UPDATE visits SET active = TRUE WHERE ID = 20;
UPDATE visits SET active = FALSE WHERE ID = 20; -- Unstart visit with ID = 207

-- Get visits in progress
SELECT ID, creationType, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', ?)) AS 'localtime', patientID, doctorID, reason, description FROM visits WHERE active = TRUE; -- Statement for Java
SELECT ID, creationType, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'localtime', patientID, doctorID, reason, description FROM visits WHERE active = TRUE; -- Visits in progress for Phoneix time

-- Select a visit raw
SELECT * FROM visits WHERE ID = 2;

use easydoctor;
select * from users;
select * from patients;
select * from employees;
select * from visits;