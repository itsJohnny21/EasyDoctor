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
VALUES (2, 3, 'Knee replacement', '2021-01-01', '123 Main St', 'Patient is allergic to anesthesia');

UPDATE surgeries SET notes = 'Successfully removed brain tumor.' WHERE ID = 2;


UPDATE surgeries SET notes = 'Clean procedure, no issues.' WHERE ID = 1;
use easydoctor;
SELECT * FROM surgeries;
use easydoctor;
