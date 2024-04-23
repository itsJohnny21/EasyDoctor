DROP TABLE IF EXISTS activeVisits;

CREATE TABLE activeVisits (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    weight INT DEFAULT NULL,
    height INT DEFAULT NULL,
    systolicBloodPressure INT DEFAULT NULL,
    diastolicBloodPressure INT DEFAULT NULL,
    heartRate INT DEFAULT NULL,
    respiratoryRate INT DEFAULT NULL,
    bodyTemperature INT DEFAULT NULL,
    notes TEXT,
    currentPage INT DEFAULT 0,
    completed BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (ID) REFERENCES visits(ID) ON DELETE CASCADE
);
use easydoctor;
show create table activeVisits;


alter table activeVisits add column completed BOOLEAN DEFAULT FALSE;

-- Get active sessions
SELECT v.patientID, av.weight, av.height, av.systolicBloodPressure, av.diastolicBloodPressure, av.heartRate, av.bodyTemperature, av.notes FROM visits v JOIN activeVisits av ON v.ID = av.ID JOIN patients p ON v.patientID = p.ID WHERE av.ID = ?; -- Statement for Java
SELECT v.patientID, av.weight, av.height, av.systolicBloodPressure, av.diastolicBloodPressure, av.heartRate, av.bodyTemperature, av.notes FROM visits v JOIN activeVisits av ON v.ID = av.ID JOIN patients p ON v.patientID = p.ID WHERE av.ID = 33; -- Example

INSERT INTO activeVisits (ID) VALUES (33);

SELECT visits.ID, creationType, DATE(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'localdate', TIME(CONVERT_TZ(CONCAT(date, ' ', time), '+00:00', 'America/Phoenix')) AS 'localtime', patientID, doctorID, reason, description FROM visits JOIN activeVisits ON visits.ID = activeVisits.ID;

-- ADD DELETE ON CASCADE TO VISITS TABLE


select * from activeVisits;