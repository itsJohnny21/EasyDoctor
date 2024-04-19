INSERT INTO users (username, password, role)
VALUES ('barb123', SHA2('123', 256), 'PATIENT');
SET @userID = LAST_INSERT_ID();
INSERT INTO patients (ID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity)
VALUES (@userID, 'Barbara', 'Williams',  'FEMALE', '2000-01-01', 'barb123@gmail.com', '1234567890', '123 Test St', 'WHITE', 'NON-HISPANIC');
INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (@userID, 'Peanuts', 'Peanut Butter', 'MILD', 'FOOD', 'Patient has nightmares about peanut butter.');
INSERT INTO surgeries (userID, doctorID, type, date, location, notes)
VALUES (@userID, 3, 'Knee replacement', '2021-01-01', '123 Main St', 'Patient is allergic to anesthesia');
INSERT INTO visits (creationType, date, userID, doctorID, reason, description)
VALUES ('ONLINE', '2024-03-23 09:00:00', @userID, 3, 'Checkup', 'Routine checkup');
INSERT INTO vaccineRecords (userID, vaccineGroup, date, provider, notes)
VALUES (@userID, "MMR", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@userID, 'Depression', 'CHRONIC', 'MENTAL', 'Patient is very stressed from work and school.');
INSERT INTO prescriptions (userID, doctorID, drugID, intakeDay, intakeTime, dosageQuantity, dosageUnits, form, description)
VALUES (@userID, 3, 1, 'MONDAY', '08:00:00', 200, 'MG', 'CAPUSLE', 'Take one pill every Monday at 8:00 AM');

INSERT INTO users (username, password, role)
VALUES ('convertUTCtoLocalTest', SHA2('convertUTCtoLocalTest', 256), 'PATIENT');
SET @userID = LAST_INSERT_ID();
INSERT INTO patients (ID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity)
VALUES (@userID, 'convertUTCtoLocalTest', 'convertUTCtoLocalTest',  'FEMALE', '2000-01-01', 'convertUTCtoLocalTest@test.com', '1234567890', '123 Test St', 'WHITE', 'NON-HISPANIC');


use easydoctor;
select * from users;
select * from patients;


INSERT INTO users (ID, username, password, role)
VALUES (68, 'john1234', '12345', 'PATIENT');

INSERT INTO patients (ID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity)
VALUES (68, 'John', 'Doe', '2021-01-01', 'idk', '123', '123', 'MALE', 'WHITE', 'HISPANIC');
INSERT INTO patients (ID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity)
VALUES (69, 'John', 'Doe', '2021-01-01', 'idk', '123', '123', 'MALE', 'WHITE', 'HISPANIC');
delete from users where username = 'user1';
delete from users where ID = 81;
select * from users;
select * from patients;