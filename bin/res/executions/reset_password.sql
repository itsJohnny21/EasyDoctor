use easydoctor;
select * from users;
UPDATE users SET password = SHA2('meatCuh21!', 256) WHERE username = 'itsJohnnyDoctor21';