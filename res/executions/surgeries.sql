CREATE TABLE surgeries (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    userID INT NOT NULL,
    doctorID INT NOT NULL,
    type VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    location VARCHAR(100) NOT NULL,
    notes TEXT,
    FOREIGN KEY (userID) REFERENCES users(ID),
    UNIQUE KEY (userID, doctorID, type, date)
);

ALTER TABLE surgeries ADD CONSTRAINT unique_surgery UNIQUE (userID, doctorID, type, date);
INSERT INTO surgeries (userID, doctorID, type, date, location, notes)
VALUES (2, 3, 'Knee replacement', '2021-01-01', '123 Main St', 'Knee replacement successful');
INSERT INTO surgeries (userID, doctorID, type, date, location, notes)
VALUES (2, 3, 'Elbow replacement', '2022-01-01', '123 Main St', 'Other knee was replaced');
INSERT INTO surgeries (userID, doctorID, type, date, location, notes)
VALUES (2, 3, 'Hip replacement', '2022-12-01', '123 Main St', 'Clean procedure');

use easydoctor;
SELECT * FROM surgeries;
