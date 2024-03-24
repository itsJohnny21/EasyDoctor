use easydoctor;
select * from users;
UPDATE users SET passwordHash = SHA2('test', 256) WHERE username = 'doctor1';