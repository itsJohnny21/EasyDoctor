CREATE TABLE admin (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    userID INT NOT NULL,
    FOREIGN KEY (userID) REFERENCES users(ID)
);

INSERT INTO users (username, password, userType)
VALUES ('jonis6421', SHA2('easydoctor', 256), 'ADMIN');

INSERT INTO admin (userID)
VALUES (1);

DELETE FROM admin WHERE userID = 0;
DELETE FROM users WHERE ID = 0;

