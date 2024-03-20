CREATE TABLE logbook (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    userID INT NOT NULL,
    sourceIP VARCHAR(100),
    type ENUM('SIGN_IN', 'SIGN_OUT') NOT NULL,
    FOREIGN KEY (userID) REFERENCES users(ID)
);

INSERT INTO logbook (userID, sourceIP, type)
VALUES (3, '127.0.0.1', 'SIGN_OUT');

GRANT INSERT ON logbook TO 'patient';
GRANT INSERT ON logbook TO 'doctor';
GRANT INSERT ON logbook TO 'nurse';
GRANT INSERT ON logbook TO 'neutral';

DELETE FROM logbook;
SELECT * FROM logbook;
```