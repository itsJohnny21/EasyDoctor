DROP TABLE IF EXISTS vaccines;

CREATE TABLE vaccines (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    patientID INT NOT NULL,
    vaccineGroup VARCHAR(100) NOT NULL,
    dose1 DATE,
    dose2 DATE,
    dose3 DATE,
    dose4 DATE,
    dose5 DATE,
    dose6 DATE,
    provider1 VARCHAR(100),
    provider2 VARCHAR(100),
    provider3 VARCHAR(100),
    provider4 VARCHAR(100),
    provider5 VARCHAR(100),
    provider6 VARCHAR(100),
    notes TEXT,
    FOREIGN KEY (patientID) REFERENCES users(ID)
);

INSERT INTO vaccines (patientID, vaccineGroup, dose1, dose2, dose3, dose4, dose5, dose6, provider1, provider2, provider3, provider4, provider5, provider6, notes)
VALUES (2, "COVID-19", "2021-01-01", "2021-01-29", "2021-02-26", "2021-03-26", "2021-04-23", "2021-05-21", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (patientID, vaccineGroup, dose1, dose2, dose3, dose4, dose5, dose6, provider1, provider2, provider3, provider4, provider5, provider6, notes)
VALUES (2, "Influenza", "2021-01-01", "2021-01-29", "2021-02-26", "2021-03-26", "2021-04-23", "2021-05-21", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (patientID, vaccineGroup, dose1, dose2, dose3, dose4, dose5, dose6, provider1, provider2, provider3, provider4, provider5, provider6, notes)
VALUES (2, "Hepatitis A", "2021-01-01", "2021-01-29", "2021-02-26", "2021-03-26", "2021-04-23", "2021-05-21", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (patientID, vaccineGroup, dose1, dose2, dose3, dose4, dose5, dose6, provider1, provider2, provider3, provider4, provider5, provider6, notes)
VALUES (2, "Hepatitis B", "2021-01-01", "2021-01-29", "2021-02-26", "2021-03-26", "2021-04-23", "2021-05-21", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (patientID, vaccineGroup, dose1, dose2, dose3, dose4, dose5, dose6, provider1, provider2, provider3, provider4, provider5, provider6, notes)
VALUES (2, "Varicella", "2021-01-01", "2021-01-29", "2021-02-26", "2021-03-26", "2021-04-23", "2021-05-21", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (patientID, vaccineGroup, dose1, dose2, dose3, dose4, dose5, dose6, provider1, provider2, provider3, provider4, provider5, provider6, notes)
VALUES (2, "Polio", "2021-01-01", "2021-01-29", "2021-02-26", "2021-03-26", "2021-04-23", "2021-05-21", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (patientID, vaccineGroup, dose1, dose2, dose3, dose4, dose5, dose6, provider1, provider2, provider3, provider4, provider5, provider6, notes)
VALUES (2, "Pneumococcal", "2021-01-01", "2021-01-29", "2021-02-26", "2021-03-26", "2021-04-23", "2021-05-21", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (patientID, vaccineGroup, dose1, dose2, dose3, dose4, dose5, dose6, provider1, provider2, provider3, provider4, provider5, provider6, notes)
VALUES (2, "MMR", "2021-01-01", "2021-01-29", "2021-02-26", "2021-03-26", "2021-04-23", "2021-05-21", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (patientID, vaccineGroup, dose1, dose2, dose3, dose4, dose5, dose6, provider1, provider2, provider3, provider4, provider5, provider6, notes)
VALUES (2, "HPV", "2021-01-01", "2021-01-29", "2021-02-26", "2021-03-26", "2021-04-23", "2021-05-21", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (patientID, vaccineGroup, dose1, dose2, dose3, dose4, dose5, dose6, provider1, provider2, provider3, provider4, provider5, provider6, notes)
VALUES (2, "Shingles", "2021-01-01", "2021-01-29", "2021-02-26", "2021-03-26", "2021-04-23", "2021-05-21", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "Pfizer-BioNTech", "No adverse reactions");
use easydoctor;
RENAME TABLE vaccineRecord2 TO vaccines2;
SELECT * FROM vaccines2;
SELECT * FROM vaccines;

CREATE TABLE vaccines (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    patientID INT NOT NULL,
    vaccineGroup ENUM('COVID-19', 'Influenza', 'Hepatitis A', 'Hepatitis B', 'Varicella', 'Polio', 'Pneumococcal', 'MMR', 'HPV', 'Shingles') NOT NULL,
    date DATE,
    provider VARCHAR(100),
    notes TEXT,
    FOREIGN KEY (patientID) REFERENCES users(ID)
);
INSERT INTO vaccines (patientID, vaccineGroup, date, provider, notes)
VALUES (2, "COVID-19", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (patientID, vaccineGroup, date, provider, notes)
VALUES (2, "COVID-19", "2024-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (patientID, vaccineGroup, date, provider, notes)
VALUES (2, "Influenza", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (patientID, vaccineGroup, date, provider, notes)
VALUES (2, "Hepatitis A", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");

SELECT * FROM vaccines;
SELECT username, role, ID FROM users WHERE userID = 3;