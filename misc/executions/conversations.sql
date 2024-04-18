CREATE TABLE conversations (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    readStatus BOOLEAN DEFAULT FALSE NOT NULL,
    message TEXT NOT NULL,
    senderID INT NOT NULL,
    receiverID INT NOT NULL,
    FOREIGN KEY (senderID) REFERENCES users(ID) ON DELETE CASCADE,
    FOREIGN KEY (receiverID) REFERENCES users(ID) ON DELETE CASCADE
);

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('The dont feel too good doctor. I am having headaches and nausea :(', 2, 3);

SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('I am sorry to hear that. I will prescribe you some drugs.', 3, 2);

SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('Thank you doctor. I appreciate your help.', 2, 3);

SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;
INSERT INTO conversations (message, senderID, receiverID)
VALUES ('You are welcome. I am always here to help.', 3, 2);

SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('How often should I take the drugs?', 2, 3);

SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('You should take advil twice a day to relieve your headaches.', 3, 2);

SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 2, 3);
INSERT INTO conversations (message, senderID, receiverID)
VALUES ("Quantum mechanics is a fundamental theory in physics that describes the behavior of nature at and below the scale of atoms.[2]: 1.1  It is the foundation of all quantum physics, which includes quantum chemistry, quantum field theory, quantum technology, and quantum information science.

Quantum mechanics can describe many systems that classical physics cannot. Classical physics can describe many aspects of nature at an ordinary (macroscopic and (optical) microscopic) scale, but is not sufficient for describing them at very small submicroscopic (atomic and subatomic) scales. Most theories in classical physics can be derived from quantum mechanics as an approximation valid at large (macroscopic/microscopic) scale.[3]

Quantum systems have bound states that are quantized to discrete values of energy, momentum, angular momentum, and other quantities, in contrast to classical systems where these quantities can be measured continuously. Measurements of quantum systems show characteristics of both particles and waves (wave–particle duality), and there are limits to how accurately the value of a physical quantity can be predicted prior to its measurement, given a complete set of initial conditions (the uncertainty principle).

Quantum mechanics arose gradually from theories to explain observations that could not be reconciled with classical physics, such as Max Planck's solution in 1900 to the black-body radiation problem, and the correspondence between energy and frequency in Albert Einstein's 1905 paper, which explained the photoelectric effect. These early attempts to understand microscopic phenomena, now known as the 'old quantum theory', led to the full development of quantum mechanics in the mid-1920s by Niels Bohr, Erwin Schrödinger, Werner Heisenberg, Max Born, Paul Dirac and others. The modern theory is formulated in various specially developed mathematical formalisms. In one of them, a mathematical entity called the wave function provides information, in the form of probability amplitudes, about what measurements of a particle's energy, momentum, and other physical properties may yield.", 2, 3);


select * from users;
select * from conversations;
use easydoctor;
SELECT creationTime, message, readStatus, senderID, receiverID FROM conversations WHERE (receiverID = 2 AND senderID = 3) OR (senderID = 2 AND receiverID = 3) ORDER BY creationTime ASC;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('Hello doctor, I am not feeling too well :(', 182, 3);
SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('I am sorry to hear that. What seems to be the problem?', 3, 182);
SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('I am having lots of stomach aches. I need some medication please.', 182, 3);
SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;

INSERT INTO conversations (message, senderID, receiverID)
VALUES ('I will prescribe you some antacids. Take them twice a day.', 3, 182);
SET @rowID = LAST_INSERT_ID();
UPDATE conversations SET readStatus = TRUE WHERE ID = @rowID;

SELECT creationTime, message, ID, readStatus, senderID, receiverID FROM conversations WHERE (receiverID = 182 AND senderID = 3) OR (senderID = 182 AND receiverID = 3) ORDER BY creationTime ASC;
UPDATE conversations SET readStatus = TRUE WHERE receiverID = 2 AND senderID = 3;
SELECT * FROM conversations;
SELECT creationTime, message, readStatus, senderID, receiverID FROM conversations WHERE receiverID = 3 OR senderID = 3 ORDER BY creationTime DESC;

sender = doctor1, receiver = patient1
sender = patient2, receiver = patient1