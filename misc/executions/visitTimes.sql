DROP TABLE IF EXISTS visitTimes;

CREATE TABLE visitTimes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    dayOfWeek ENUM('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') NOT NULL,
    startTime TIME NOT NULL,
    endTime TIME NOT NULL,
    UNIQUE(dayOfWeek, startTime, endTime)
)


INSERT INTO visitTimes (dayOfWeek, startTime, endTime) VALUES
('MONDAY', '09:00:00', '17:00:00'),
('TUESDAY', '09:00:00', '17:00:00'),
('WEDNESDAY', '09:00:00', '17:00:00'),
('THURSDAY', '09:00:00', '17:00:00'),
('FRIDAY', '09:00:00', '17:00:00'),
('SATURDAY', '09:00:00', '17:00:00'),
('SUNDAY', '09:00:00', '17:00:00');