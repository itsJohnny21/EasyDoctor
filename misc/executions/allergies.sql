CREATE TABLE allergies (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    userID INT NOT NULL,
    allergen VARCHAR(100) NOT NULL,
    commonSource VARCHAR(100) NOT NULL,
    severity ENUM('MILD', 'MODERATE', 'SEVERE') NOT NULL,
    type ENUM('FOOD', 'DRUG', 'ENVIRONMENTAL', 'INSECT', 'ANIMAL', 'PLANT', 'OTHER') NOT NULL,
    notes TEXT,
    FOREIGN KEY (userID) REFERENCES users(ID) ON DELETE CASCADE,
    UNIQUE (userID, allergen)
);

show create table allergies;

alter table allergies drop foreign key allergies_ibfk_1;
alter table allergies add FOREIGN KEY (userID) REFERENCES users(ID) ON DELETE CASCADE;

INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (2, 'Peanuts', 'Peanut Butter', 'MILD', 'FOOD', 'Patient has nightmares about peanut butter.');

UPDATE allergies SET notes = 'Patient has nightmares about peanut butter.' WHERE userID = 2;
UPDATE allergies SET severity = 'SEVERE' WHERE userID = 2;

INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (2, 'Pollen', 'Flowers', 'MODERATE', 'ENVIRONMENTAL', 'Patient sneezes a lot.');

INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (2, 'Penicillin', 'Medicine', 'SEVERE', 'DRUG', 'Patient has anaphylactic shock.');

INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (31, 'Cats', 'Cats', 'MILD', 'ANIMAL', 'Patient gets itchy.');
INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (31, 'Dogs', 'Doggies', 'MILD', 'ANIMAL', 'Patient gets itchy.');

use easydoctor;
select * from allergies;
