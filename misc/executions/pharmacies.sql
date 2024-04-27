DROP TABLE IF EXISTS pharmacies;

CREATE TABLE pharmacies (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    phone VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    UNIQUE(phone),
    UNIQUE(address)
);

use easydoctor;

INSERT INTO pharmacies (name, address, phone, email) VALUES
('CVS', '123 Main St', '1234567890', 'CVS1@outlook.com'),
('Walgreens', '456 Elm St', '0987654321', 'Walgreens1@outlook.com'),
('Rite Aid', '789 Oak St', '9090123456', 'RiteAid1@outlook.com'),
('Walmart', '101 Pine St', '6781234560', 'Walmart1@outlook.com'),
('Kroger', '202 Maple St', '3456789012', 'Kroger1@outlook.com'),
('Costco', '303 Birch St', '4567890123', 'Costco1@outlook.com'),
('Publix', '404 Cedar St', '5678901234', 'Publix1@outlook.com');
INSERT INTO pharmacies (name, address, phone, email) VALUES
('CVS', '321 Main St, Phoenix Arizona 85283', '1234527890', 'CVS1@outlook.com'),
('Walgreens', '456 Elm St, Phoenix Arizona 81232', '0988854321', 'Walgreens1@outlook.com'),
('Rite Aid', '789 Oak St, Phoenix Arizona 81232', '9098883456', 'RiteAid1@outlook.com'),
('Walmart', '101 Pine St, Phoenix Arizona 81232', '6788884560', 'Walmart1@outlook.com'),
('Kroger', '202 Maple St, Phoenix Arizona 81232', '3458889012', 'Kroger1@outlook.com'),
('Costco', '303 Birch St, Phoenix Arizona 81232', '4568880123', 'Costco1@outlook.com'),
('Publix', '404 Cedar St, Phoenix Arizona 81232', '5678881234', 'Publix1@outlook.com');

use easydoctor;
select * from pharmacies;

-- Search for a pharmacy by name
SELECT * FROM pharmacies WHERE name = ?; -- Statement for Java
SELECT * FROM pharmacies WHERE name LIKE '%?%'; -- Statement for Java
SELECT * FROM pharmacies WHERE name LIKE '%Wal%'; -- Example

-- Search for a pharmacy by address
SELECT * FROM pharmacies WHERE address = ?; -- Statement for Java
SELECT * FROM pharmacies WHERE address LIKE ?; -- Statement for Java

-- Search for a pharmacy by phone
SELECT * FROM pharmacies WHERE phone = ?; -- Statement for Java
SELECT * FROM pharmacies WHERE phone LIKE ?; -- Statement for Java
