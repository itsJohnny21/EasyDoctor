CREATE TABLE logbook (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    userID INT NOT NULL,
    sourceIP VARCHAR(100),
    type ENUM('SIGN_IN', 'SIGN_OUT') NOT NULL,
    FOREIGN KEY (userID) REFERENCES users(ID) ON DELETE CASCADE
);


CREATE TABLE `logbook` (   `ID` int NOT NULL AUTO_INCREMENT,   `creationTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,   `userID` int NOT NULL,   `IP` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,   `type` enum('SIGN_IN','SIGN_OUT') NOT NULL,   PRIMARY KEY (`ID`),   KEY `userID` (`userID`),   CONSTRAINT `logbook_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`ID`) ) ENGINE=InnoDB AUTO_INCREMENT=2015 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
SHOW CREATE TABLE logbook;
INSERT INTO logbook (userID, sourceIP, type)
VALUES (3, '127.0.0.1', 'SIGN_OUT');

GRANT INSERT ON logbook TO 'patient';
GRANT INSERT ON logbook TO 'doctor';
GRANT INSERT ON logbook TO 'nurse';
GRANT INSERT ON logbook TO 'neutral';

DELETE FROM logbook;
SELECT * FROM logbook;
```