drop table resetPasswordTokens;

CREATE TABLE resetPasswordTokens (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    userID int NOT NULL,
    token INT CONSTRAINT CHECK (token > 100000 AND TOKEN <= 999999),
    used BOOLEAN DEFAULT FALSE,
    sourceIP VARCHAR(100),
    FOREIGN KEY (userID) REFERENCES users(ID) ON DELETE CASCADE
);

SELECT userID, creationTime, used FROM resetPasswordTokens WHERE token = 946930 ORDER BY creationTime DESC LIMIT 1;
use easydoctor;
select * from users;
select * from resetPasswordTokens;

SELECT userID, UNIX_TIMESTAMP(creationTime) * 1000 AS 'creationTime', used, UNIX_TIMESTAMP() * 1000 AS 'nowTime' FROM resetPasswordTokens WHERE token = 322131 ORDER BY creationTime DESC LIMIT 1;