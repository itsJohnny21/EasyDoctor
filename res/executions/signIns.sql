CREATE TABLE signIns (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    sourceIP VARCHAR(100)
);

INSERT INTO signIns (username, password, sourceIP)
VALUES ('jonis6421', SHA2('easydoctor', 256), '127.0.0.1');

INSERT INTO signIns (username, password, sourceIP)
VALUES ('jonis6421', SHA2('password123', 256), '127.0.0.1');

INSERT INTO signIns (username, password, sourceIP)
VALUES ('jonis6421', SHA2('p455w0rd123', 256), '127.0.0.1');