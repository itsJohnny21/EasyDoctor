DROP TABLE IF EXISTS immunizations;

CREATE TABLE immunizations (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    patientID INT NOT NULL,
    vaccineGroup VARCHAR(100) NOT NULL,
    doseNumber INT NOT NULL,
    date DATE NOT NULL,
    notes TEXT,
    FOREIGN KEY (patientID) REFERENCES users(ID)
);

INSERT INTO immunizations (patientID, vaccineGroup, doseNumber, date, notes)
VALUES (2, 'COVID-19', 1, '2021-01-01', 'First dose of the Pfizer vaccine');

UPDATE immunizations SET notes = 'First dose of the Pfizer vaccine. Dropped dead 10 min later.' WHERE patientID = 2;

INSERT INTO immunizations (patientID, vaccineGroup, doseNumber, date, notes)
VALUES (2, 'COVID-19', 2, '2021-01-01', 'Second dose of the Pfizer vaccine');