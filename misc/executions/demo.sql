use easydoctor;
-- Patient: Patient1
-- Username: firstpatient123
-- Password: Patient123!

INSERT INTO users (username, password, role)
VALUES ('firstpatient123', SHA2('firstpatient123', 256), 'PATIENT');
SET @firstpatient123ID = (SELECT ID FROM users WHERE username = 'firstpatient123');
INSERT INTO patients (ID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity)
VALUES (@firstpatient123ID, 'FirstPatient', 'FirstPatient',  'FEMALE', '2000-01-01', 'firstpatient123@gmail.com', '4807383910', '123 Test St', 'WHITE', 'NON-HISPANIC');

-- Health Conditions for Patient1
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@firstpatient123ID, 'Diabetes', 'CHRONIC', 'PHYSICAL', 'Patient needs to reduce sugar intake and consume more vegetables');
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@firstpatient123ID, 'High Cholesterol', 'ACUTE', 'PHYSICAL', 'Patient needs to reduce fat intake');
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@firstpatient123ID, 'High Blood Pressure', 'ACUTE', 'PHYSICAL', 'Patient needs to reduce salt intake');
select * from healthConditions where userID = @firstpatient123ID;

-- Allergies for Patient1
INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (@firstpatient123ID, 'Peanuts', 'Peanut Butter', 'SEVERE', 'FOOD', 'Patient must avoid peanuts at all costs');
INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (@firstpatient123ID, 'Pollen', 'Flowers', 'MODERATE', 'ENVIRONMENTAL', 'Patient sneezes a lot');
select * from allergies where userID = @firstpatient123ID;

-- Vaccines for Patient1
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "COVID_19", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "COVID_19", "2024-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "COVID_19", "2022-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "COVID_19", "2023-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "Influenza", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "Hepatitis_A", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "Hepatitis_A", "2019-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "Hepatitis_A", "2012-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "Hepatitis_A", "2010-02-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "Hepatitis_A", "1990-01-01", "Pfizer-BioNTech", "No adverse reactions");
select * from vaccines where userID = @firstpatient123ID;

-- Patient: Patient2
-- Username: secondpatient123
-- Password: Patient2123!

INSERT INTO users (username, password, role)
VALUES ('secondpatient123', SHA2('secondpatient123', 256), 'PATIENT');
SET @secondpatient123ID = (SELECT ID FROM users WHERE username = 'secondpatient123');
INSERT INTO patients (ID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity)
VALUES (@secondpatient123ID, 'SecondPatient', 'SecondPatient',  'FEMALE', '2000-01-01', 'secondpatient123@gmail.com', '4807382910', '123 Test St', 'WHITE', 'NON-HISPANIC');

-- Health Conditions for Patient2
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@secondpatient123ID, 'Diabetes', 'CHRONIC', 'PHYSICAL', 'Patient needs to reduce sugar intake and consume more vegetables');
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@secondpatient123ID, 'High Cholesterol', 'ACUTE', 'PHYSICAL', 'Patient needs to reduce fat intake');
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@secondpatient123ID, 'High Blood Pressure', 'ACUTE', 'PHYSICAL', 'Patient needs to reduce salt intake');
select * from healthConditions where userID = @secondpatient123ID;

-- Allergies for Patient2
INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (@secondpatient123ID, 'Peanuts', 'Peanut Butter', 'SEVERE', 'FOOD', 'Patient must avoid peanuts at all costs');
INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (@secondpatient123ID, 'Pollen', 'Flowers', 'MODERATE', 'ENVIRONMENTAL', 'Patient sneezes a lot');
select * from allergies where userID = @secondpatient123ID;

-- Vaccines for Patient2
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@secondpatient123ID, "COVID_19", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@secondpatient123ID, "COVID_19", "2024-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@secondpatient123ID, "COVID_19", "2022-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@secondpatient123ID, "COVID_19", "2023-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@secondpatient123ID, "Influenza", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@secondpatient123ID, "Hepatitis_A", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@secondpatient123ID, "Hepatitis_A", "2019-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@secondpatient123ID, "Hepatitis_A", "2012-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@secondpatient123ID, "Hepatitis_A", "2010-02-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@secondpatient123ID, "Hepatitis_A", "1990-01-01", "Pfizer-BioNTech", "No adverse reactions");
select * from vaccines where userID = @secondpatient123ID;

-- Patient: Patient3
-- Username: thirdpatient123
-- Password: ThirdPatient123!

INSERT INTO users (username, password, role)
VALUES ('thirdpatient123', SHA2('thirdpatient123', 256), 'PATIENT');
SET @thirdpatient123ID = (SELECT ID FROM users WHERE username = 'thirdpatient123');
INSERT INTO patients (ID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity)
VALUES (@thirdpatient123ID, 'ThirdPatient', 'ThirdPatient',  'FEMALE', '2000-01-01', 'thirdpatient123@gmail.com', '4807382911', '123 Test St', 'WHITE', 'NON-HISPANIC');

-- Health Conditions for Patient3
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@thirdpatient123ID, 'Diabetes', 'CHRONIC', 'PHYSICAL', 'Patient needs to reduce sugar intake and consume more vegetables');
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@thirdpatient123ID, 'High Cholesterol', 'ACUTE', 'PHYSICAL', 'Patient needs to reduce fat intake');
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@thirdpatient123ID, 'High Blood Pressure', 'ACUTE', 'PHYSICAL', 'Patient needs to reduce salt intake');
select * from healthConditions where userID = @thirdpatient123ID;

-- Allergies for Patient3
INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (@thirdpatient123ID, 'Peanuts', 'Peanut Butter', 'SEVERE', 'FOOD', 'Patient must avoid peanuts at all costs');
INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (@thirdpatient123ID, 'Pollen', 'Flowers', 'MODERATE', 'ENVIRONMENTAL', 'Patient sneezes a lot');
select * from allergies where userID = @thirdpatient123ID;

-- Vaccines for Patient3
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@thirdpatient123ID, "COVID_19", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@thirdpatient123ID, "COVID_19", "2024-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@thirdpatient123ID, "COVID_19", "2022-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@thirdpatient123ID, "COVID_19", "2023-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@thirdpatient123ID, "Influenza", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@thirdpatient123ID, "Hepatitis_A", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@thirdpatient123ID, "Hepatitis_A", "2019-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@thirdpatient123ID, "Hepatitis_A", "2012-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@thirdpatient123ID, "Hepatitis_A", "2010-02-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@thirdpatient123ID, "Hepatitis_A", "1990-01-01", "Pfizer-BioNTech", "No adverse reactions");
select * from vaccines where userID = @thirdpatient123ID;

-- Patient: Patient4
-- Username: fourthpatient123
-- Password: fourthPatient123!

INSERT INTO users (username, password, role)
VALUES ('fourthpatient123', SHA2('fourthpatient123', 256), 'PATIENT');
SET @fourthpatient123ID = (SELECT ID FROM users WHERE username = 'fourthpatient123');
INSERT INTO patients (ID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity)
VALUES (@fourthpatient123ID, 'FourthPatient', 'FourthPatient',  'FEMALE', '2000-01-01', 'fourthpatient123@gmail.com', '4807332911', '123 Test St', 'WHITE', 'NON-HISPANIC');

-- Health Conditions for Patient4
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@fourthpatient123ID, 'Diabetes', 'CHRONIC', 'PHYSICAL', 'Patient needs to reduce sugar intake and consume more vegetables');
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@fourthpatient123ID, 'High Cholesterol', 'ACUTE', 'PHYSICAL', 'Patient needs to reduce fat intake');
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@fourthpatient123ID, 'High Blood Pressure', 'ACUTE', 'PHYSICAL', 'Patient needs to reduce salt intake');
select * from healthConditions where userID = @fourthpatient123ID;

-- Allergies for Patient4
INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (@fourthpatient123ID, 'Peanuts', 'Peanut Butter', 'SEVERE', 'FOOD', 'Patient must avoid peanuts at all costs');
INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (@fourthpatient123ID, 'Pollen', 'Flowers', 'MODERATE', 'ENVIRONMENTAL', 'Patient sneezes a lot');
select * from allergies where userID = @fourthpatient123ID;

-- Vaccines for Patient4
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@fourthpatient123ID, "COVID_19", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@fourthpatient123ID, "COVID_19", "2024-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@fourthpatient123ID, "COVID_19", "2022-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@fourthpatient123ID, "COVID_19", "2023-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@fourthpatient123ID, "Influenza", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@fourthpatient123ID, "Hepatitis_A", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@fourthpatient123ID, "Hepatitis_A", "2019-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@fourthpatient123ID, "Hepatitis_A", "2012-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@fourthpatient123ID, "Hepatitis_A", "2010-02-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@fourthpatient123ID, "Hepatitis_A", "1990-01-01", "Pfizer-BioNTech", "No adverse reactions");
select * from vaccines where userID = @fourthpatient123ID;

-- Doctor: Johnny Salazar
-- Username: itsJohnny21
-- Password: itsJohnny21!

INSERT INTO users (username, password, role)
VALUES ('itsJohnny21', SHA2('itsJohnny21!', 256), 'DOCTOR');
SET @itsJohnny21ID = (SELECT ID FROM users WHERE username = 'itsJohnny21');
INSERT INTO employees (ID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity)
VALUES (@itsJohnny21ID, 'Jonathan', 'Salazar', 'MALE', '1999-02-21', 'itsJohnny21@gmail.com', '1234564321', '123 Test St', 'WHITE', 'NON-HISPANIC');

-- Set variables for IDs
SET @itsJohnny21ID = (SELECT ID FROM users WHERE username = 'itsJohnny21');
SET @firstpatient123ID = (SELECT ID FROM users WHERE username = 'firstpatient123');
SET @secondpatient123ID = (SELECT ID FROM users WHERE username = 'secondpatient123');
SET @thirdpatient123ID = (SELECT ID FROM users WHERE username = 'thirdpatient123');
SET @fourthpatient123ID = (SELECT ID FROM users WHERE username = 'fourthpatient123');

-- Schedule visits for later
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', '2024-04-21', DATE(CONVERT_TZ('2024-04-21 21:30:00', 'America/Phoenix', '+00:00')), TIME(CONVERT_TZ('2024-04-21 21:30:00', 'America/Phoenix', '+00:00')), @fourthpatient123ID, @itsJohnny21ID, 'Checkup', 'Mental health checkup'); -- Patient4 schedules a visit with Jonathan

-- Schedule visits for now
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', DATE(CONVERT_TZ(NOW(), '+00:00', 'America/Phoenix')), DATE(NOW()), TIME(NOW()), @firstpatient123ID, @itsJohnny21ID, 'Checkup', 'Mental health checkup'); -- Patient1 schedules a visit with Jonathan
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', DATE(CONVERT_TZ(NOW(), '+00:00', 'America/Phoenix')), DATE(NOW()), TIME(NOW()), @secondpatient123ID, @itsJohnny21ID, 'Checkup', 'Mental health checkup'); -- Patient2 schedules a visit with Jonathan
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('IN_PERSON', DATE(CONVERT_TZ(NOW(), '+00:00', 'America/Phoenix')), DATE(NOW()), TIME(NOW()), @thirdpatient123ID, @itsJohnny21ID, 'Checkup', 'Mental health checkup'); -- Patient3 schedules a visit with Jonathan

-- Set preferred doctor
UPDATE patients SET preferredDoctorID = @itsJohnny21ID WHERE ID = @firstpatient123ID;
UPDATE patients SET preferredDoctorID = @itsJohnny21ID WHERE ID = @secondpatient123ID;
UPDATE patients SET preferredDoctorID = @itsJohnny21ID WHERE ID = @thirdpatient123ID;
UPDATE patients SET preferredDoctorID = @itsJohnny21ID WHERE ID = @fourthpatient123ID;

-- Select active visits
select * from activeVisits where creationTime > '2024-04-21 00:00:00';

-- Select all visits

-- Select all users
select * from users where creationTime > '2024-04-21 00:00:00';
-- Select all patients
select * from users JOIN patients on users.ID = patients.ID where creationTime > '2024-04-21 00:00:00';
-- Select all employees
select * from users JOIN employees on users.ID = employees.ID where creationTime > '2024-04-21 00:00:00';

-- Delete users
delete from users where ID = @barbara123ID;
delete from users where ID = @firstpatient123ID;
delete from users where ID = @secondpatient123ID;
delete from users where ID = @thirdpatient123ID;
delete from users where ID = @fourthpatient123ID;
delete from users where ID = @itsJohnny21ID;

use easydoctor;
-- Demo start:
-- 1. Sign up as a patient:
-- Patient: Patient1
-- Username: firstpatient123
-- Password: Patient2123!

INSERT INTO users (username, password, role)
VALUES ('firstpatient123', SHA2('firstpatient123', 256), 'PATIENT');
SET @firstpatient123ID = (SELECT ID FROM users WHERE username = 'firstpatient123');
INSERT INTO patients (ID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity)
VALUES (@firstpatient123ID, 'FirstPatient', 'FirstPatient',  'FEMALE', '2000-01-01', 'firstpatient123@gmail.com', '4807383910', '123 Test St', 'WHITE', 'NON-HISPANIC');

-- Health Conditions for Patient1
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@firstpatient123ID, 'Diabetes', 'CHRONIC', 'PHYSICAL', 'Patient needs to reduce sugar intake and consume more vegetables');
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@firstpatient123ID, 'High Cholesterol', 'ACUTE', 'PHYSICAL', 'Patient needs to reduce fat intake');
INSERT INTO healthConditions (userID, healthCondition, severity, type, notes)
VALUES (@firstpatient123ID, 'High Blood Pressure', 'ACUTE', 'PHYSICAL', 'Patient needs to reduce salt intake');

-- Allergies for Patient1
INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (@firstpatient123ID, 'Peanuts', 'Peanut Butter', 'SEVERE', 'FOOD', 'Patient must avoid peanuts at all costs');
INSERT INTO allergies (userID, allergen, commonSource, severity, type, notes)
VALUES (@firstpatient123ID, 'Pollen', 'Flowers', 'MODERATE', 'ENVIRONMENTAL', 'Patient sneezes a lot');

-- Vaccines for Patient1
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "COVID_19", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "COVID_19", "2024-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "COVID_19", "2022-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "COVID_19", "2023-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "Influenza", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "Hepatitis_A", "2021-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "Hepatitis_A", "2019-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "Hepatitis_A", "2012-01-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "Hepatitis_A", "2010-02-01", "Pfizer-BioNTech", "No adverse reactions");
INSERT INTO vaccines (userID, vaccineGroup, date, provider, notes)
VALUES (@firstpatient123ID, "Hepatitis_A", "1990-01-01", "Pfizer-BioNTech", "No adverse reactions");

select * from allergies where userID = @firstpatient123ID;
select * from vaccines where userID = @firstpatient123ID;
select * from healthConditions where userID = @firstpatient123ID;

-- 2. Sign up as a doctor:
-- Doctor: Johnny Salazar
-- Username: itsJohnny21
-- Password: itsJohnny21!

INSERT INTO users (username, password, role)
VALUES ('itsJohnny21', SHA2('itsJohnny21!', 256), 'DOCTOR');
SET @itsJohnny21ID = (SELECT ID FROM users WHERE username = 'itsJohnny21');
INSERT INTO employees (ID, firstName, lastName, sex, birthDate, email, phone, address, race, ethnicity)
VALUES (@itsJohnny21ID, 'Jonathan', 'Salazar', 'MALE', '1999-02-21', 'itsJohnny21@gmail.com', '1234564321', '123 Test St', 'WHITE', 'NON-HISPANIC');

-- 3. Set preferred doctor
UPDATE patients SET preferredDoctorID = @itsJohnny21ID WHERE ID = @firstpatient123ID;

-- 4. Schedule a visit with the doctor:
INSERT INTO visits (creationType, localdate, date, time, patientID, doctorID, reason, description)
VALUES('ONLINE', DATE(CONVERT_TZ(NOW(), '+00:00', 'America/Phoenix')), DATE(NOW()), TIME(NOW()), @firstpatient123ID, @itsJohnny21ID, 'Checkup', 'Mental health checkup');

-- 5. Load two separate windows and log in as the patient and the doctor
-- 6. Doctor: Start the patient's visit
-- 7. Patient: Visit status should say: "IN_PROGRESS"
-- 8. Doctor: Finish the patient's visit
-- 9. Patient: Visit status should say: "COMPLETED"
-- 10. Doctor: Send a message to the patient
-- 11. Patient: Read the message and reply
-- 12. Doctor: Read the patient's reply
-- 13. BOTH: Sign out
-- 14. Patient: Reset password
-- 15. Patient: Sign in with new password
-- Demo end

update users set password = SHA2('Patient123!', 256) where username = 'firstpatient123';
SELECT * from employees;

-- Delete
delete from users where ID = @itsJohnny21ID;
delete from users where ID = @firstpatient123ID;
delete from visits where localdate = '2024-04-21';
delete from activeVisits;