CREATE TABLE tasks (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    userID INT NOT NULL,
    dueDate DATE,
    completed BOOLEAN DEFAULT FALSE NOT NULL,
    description TEXT NOT NULL,
    FOREIGN KEY (userID) REFERENCES users(ID)
);

INSERT INTO tasks (userID, dueDate, description)
VALUES (1, '2021-01-01', 'Complete the EasyDoctor project');

UPDATE tasks SET completed = TRUE WHERE userID = 1;