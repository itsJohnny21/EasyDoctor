INSERT INTO users (username, passwordHash, role)
VALUES ('jsalaz59', SHA2('password', 256), 'PATIENT');
SET @userID = (SELECT ID FROM users WHERE username = 'jsalaz59');
INSERT INTO patients (userID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity, insuranceProvider, insuranceID, emergencyContactName, emergencyContactPhone, motherFirstName, motherLastName, fatherFirstName, fatherLastName)
VALUES (@userID, 'Johnny', 'Salazar',  'MALE', '1999-02-21', 'jsalaz59@asu.edu', '1234567890', '123 Test St', 'WHITE', 'HISPANIC', 'AHCCS', '1234567890', 'Anthony Salazar', '1234567890', 'Eloisa', 'Montoya', 'Roger', 'Salazar');
INSERT INTO visits (creationType, date, patientID, doctorID, reason, description)
VALUES ('ONLINE', '2025-04-02:09:12:49', @userID, 3, 'Checkup', 'Routine checkup');
DELETE FROM visits where patientID = @userID;
INSERT INTO visits (creationType, date, patientID, doctorID, reason, description)
VALUES ('IN-PERSON', '2022-01-30:09:12:49', @userID, 3, 'Checkup', 'Back pain');
select * from healthConditions;
INSERT INTO healthConditions (patientID, healthCondition, severity, type, notes)
VALUES (@userID, 'Back Pain', 'MILD', 'PHYSICAL', 'Hurts when sitting for long periods of time');
INSERT INTO healthConditions (patientID, healthCondition, severity, type, notes)
VALUES (@userID, 'Anxiety', 'MILD', 'MENTAL', 'Anxiety from school and work');
select * from vaccineRecords;
INSERT INTO vaccineRecords (patientID, vaccineGroup, date, provider, notes)
VALUES (@userID, 'COVID-19', '2021-12-01', 'CVS', 'Pfizer');
INSERT INTO allergies (patientID, allergen, commonSource, severity, type, notes)
VALUES (@userID, 'Cats', 'Cats', 'MILD', 'ANIMAL', 'Patient gets itchy.');
INSERT INTO surgeries (patientID, doctorID, type, date, location, notes)
VALUES (@userID, 3, 'Knee replacement', '2021-01-01', 'teeth', 'Patient is allergic to anesthesia');

use easydoctor;
SELECT * FROM visits WHERE patientID = @userID;

SELECT * FROM users;