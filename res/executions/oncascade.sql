
ALTER TABLE vaccines DROP FOREIGN KEY vaccines_ibfk_1;
ALTER TABLE vaccines
ADD CONSTRAINT vaccines_ibfk_1 FOREIGN KEY (userID) REFERENCES users(ID) ON DELETE CASCADE;