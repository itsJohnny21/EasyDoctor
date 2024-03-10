CREATE TABLE surgeries (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    patientID INT NOT NULL,
    doctorID INT NOT NULL,
    type VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    location VARCHAR(100) NOT NULL,
    notes TEXT,
    FOREIGN KEY (patientID) REFERENCES users(ID)
);

INSERT INTO surgeries (patientID, doctorID, type, date, location, notes)
VALUES (2, 1, 'Knee replacement', '2021-01-01', '123 Main St', 'Patient is allergic to anesthesia');

UPDATE surgeries SET notes = 'Patient is allergic to anesthesia. Patient died on the operating table.' WHERE patientID = 2;

INSERT INTO surgeries (patientID, doctorID, type, date, location, notes)
VALUES (2, 1, 'Brain surgery', '2021-01-01', '123 Main St', 'Brains are worth a lot of money on the black market.');

UPDATE surgeries SET notes = 'Performed brain surgery on the dead patient. Sold the brain on the black market.' WHERE patientID = 2;
