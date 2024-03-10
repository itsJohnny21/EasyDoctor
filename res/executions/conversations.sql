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

UPDATE conversations SET readStatus = TRUE WHERE ID = 1;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('I am a doctor, not a drug dealer. Please refrain from using such language.', 3, 2);

UPDATE conversations SET readStatus = TRUE WHERE ID = 2;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('I am sorry, I will not do it again.', 2, 3);


UPDATE conversations SET readStatus = TRUE WHERE ID = 3;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('Thank you. I appreciate your cooperation.', 3, 2);

UPDATE conversations SET readStatus = TRUE WHERE ID = 4;
