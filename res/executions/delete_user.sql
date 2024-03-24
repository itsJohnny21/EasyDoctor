select * from users;
SET @userID = (SELECT userID from users where userID = 17);
delete from healthConditions where userID = @userID;
delete from surgeries where userID = @userID;
delete from allergies where userID = @userID;
delete from visits where userID = @userID;
delete from prescriptions where userID = @userID;
delete from vaccineRecords where userID = @userID;
delete from logbook where userID = @userID;
delete from conversations where receiverID = @userID or senderID = @userID;
delete from patients where userID = @userID;
delete from users where ID = @userID;

select * from users;
select * from employees;
