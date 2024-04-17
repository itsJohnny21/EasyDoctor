CREATE TABLE conversations (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    readStatus BOOLEAN DEFAULT FALSE NOT NULL,
    message TEXT NOT NULL,
    senderID INT NOT NULL,
    receiverID INT NOT NULL,
    FOREIGN KEY (senderID) REFERENCES users(ID),
    FOREIGN KEY (receiverID) REFERENCES users(ID)
);

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('The drugs you gave me made me high asf haha', 2, 3);

SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('I am a doctor, not a drug dealer. Please refrain from using such language.', 3, 2);

SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('I am sorry, I will not do it again.', 2, 3);

SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;
INSERT INTO conversations (message, senderID, receiverID)
VALUES ('Thank you. I appreciate your cooperation.', 3, 2);

SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('I dont feel too good. I need more drugs please.', 2, 3);

SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('I am sorry, but I cannot provide you with more drugs.', 3, 2);

SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 2, 3);


select * from users;
select * from conversations;
use easydoctor;

SELECT creationTime, message, readStatus, senderID, receiverID FROM conversations WHERE (receiverID = 2 AND senderID = 3) OR (senderID = 2 AND receiverID = 3) ORDER BY creationTime ASC;