SET 29 = @userID;

-- All information together:
SELECT * 
FROM users 
LEFT JOIN patients ON users.ID = patients.ID 
LEFT JOIN allergies ON users.ID = allergies.ID 
LEFT JOIN surgeries ON users.ID = surgeries.ID 
LEFT JOIN visits ON users.ID = visits.ID 
LEFT JOIN vaccines ON users.ID = vaccines.ID 
LEFT JOIN healthConditions ON users.ID = healthConditions.ID 
LEFT JOIN prescriptions ON users.ID = prescriptions.ID;

-- All information by table:
select * from patients where patients.ID = @userID;
select * from allergies where allergies.ID = @userID;
select * from surgeries where surgeries.ID = @userID;
select * from visits where visits.ID = @userID;
select * from vaccines where vaccines.ID = @userID;
select * from healthConditions where healthConditions.ID = @userID;
select * from prescriptions where prescriptions.ID = @userID;
