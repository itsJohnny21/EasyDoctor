DROP TABLE IF EXISTS insuranceProviders;

CREATE TABLE insuranceProviders (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    UNIQUE(phone),
    UNIQUE(email)
);

use easydoctor;

INSERT INTO insuranceProviders (name, phone, email) VALUES
('Aetna', '1234437890', 'Aetna@outlook.com'),
('Blue Cross Blue Shield', '1232237890', 'BlueCrossBlueShield@outlook.com'),
('Cigna', '1234567890', 'Cigna@outlook.com'),
('UnitedHealthcare', '1234567490', 'UnitedHealthcare@outlook.com'),
('Humana', '1234562390', 'Humana@outlook.com'),
('Anthem', '1234367890', 'Anthem@outlook.com'),
('Kaiser Permanente', '1234067890', 'KaiserPermanente@outlook.com'),
('Molina Healthcare', '1200967890', 'MolinaHealthCare@outlook.com'),
('Centene', '1200867890', 'Centene@outlook.com'),
('WellCare', '1210567890', 'WellCare@outlook.com');

SELECT * FROM insuranceProviders;
use easydoctor;