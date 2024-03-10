CREATE TABLE drugs (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    manufacturer VARCHAR(100),
    shelfLife INT,
    instructions TEXT,
    description TEXT,
    sideEffects TEXT,
    storageInformation TEXT,
    activeIngredients TEXT
)

INSERT INTO drugs (name, manufacturer, shelfLife, instructions, description, sideEffects, storageInformation, activeIngredients) VALUES
('Aspirin', 'Bayer', 24, 'Take 1-2 tablets every 4 to 6 hours.', 'Used to reduce fever and relieve mild to moderate pain.', 'Nausea, vomiting, stomach pain.', 'Store at room temperature away from moisture and heat.', 'Aspirin'),
('Amoxicillin', 'GlaxoSmithKline', 36, 'Take every 8 hours with water.', 'Antibiotic used to treat a wide variety of bacterial infections.', 'Nausea, diarrhea, skin rash.', 'Store in a cool, dry place, away from direct heat and light.', 'Amoxicillin'),
('Lisinopril', 'Pfizer', 48, 'Take once daily with or without food.', 'Treats high blood pressure and heart failure.', 'Dizziness, headache, persistent cough.', 'Store at room temperature away from moisture and light.', 'Lisinopril'),
('Metformin', 'Merck', 60, 'Take with meals 1-2 times a day.', 'Used to improve blood sugar control in adults with type 2 diabetes.', 'Nausea, stomach upset, diarrhea.', 'Store at room temperature away from moisture and heat.', 'Metformin'),
('Atorvastatin', 'Pfizer', 24, 'Take once a day with or without food.', 'Used to lower cholesterol and triglycerides in the blood.', 'Muscle pain, weakness, headache.', 'Store at room temperature, away from excess heat and moisture.', 'Atorvastatin'),
('Albuterol', 'Merck', 24, 'Inhale 2 puffs as needed for wheezing.', 'Treats bronchospasm in patients with reversible obstructive airway disease.', 'Nervousness, shaking, headache, mouth/throat dryness or irritation.', 'Store at room temperature, away from direct sunlight and heat.', 'Albuterol'),
('Simvastatin', 'Merck', 36, 'Take once a day in the evening.', 'Used to lower levels of cholesterol and triglycerides in the blood.', 'Headache, muscle pain, abdominal pain.', 'Store at room temperature, protect from moisture, heat, and light.', 'Simvastatin');
