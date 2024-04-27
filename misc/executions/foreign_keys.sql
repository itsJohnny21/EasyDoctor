show create table visits;
alter table visits add FOREIGN KEY (patientID) REFERENCES users(ID) ON DELETE CASCADE;