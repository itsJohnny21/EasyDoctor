mysql -h easydoctor.c1wkcaa6ol0w.us-east-2.rds.amazonaws.com -P 3306 -u admin -p

-- MySQL dump 10.13  Distrib 5.7.24, for osx11.1 (x86_64)
--
-- Host: easydoctor.c1wkcaa6ol0w.us-east-2.rds.amazonaws.com    Database: easydoctor
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED='';

--
-- Table structure for table `allergies`
--

DROP TABLE IF EXISTS `allergies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `allergies` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `creationTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `userID` int NOT NULL,
  `allergen` varchar(100) NOT NULL,
  `commonSource` varchar(100) NOT NULL,
  `severity` enum('MILD','MODERATE','SEVERE') NOT NULL,
  `type` enum('FOOD','DRUG','ENVIRONMENTAL','INSECT','ANIMAL','PLANT','OTHER') NOT NULL,
  `notes` text,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `unique_user_allergen` (`userID`,`allergen`),
  CONSTRAINT `allergies_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `conversations`
--

DROP TABLE IF EXISTS `conversations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conversations` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `creationTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `readStatus` tinyint(1) NOT NULL DEFAULT '0',
  `message` text NOT NULL,
  `senderID` int NOT NULL,
  `receiverID` int NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `senderID` (`senderID`),
  KEY `receiverID` (`receiverID`),
  CONSTRAINT `conversations_ibfk_1` FOREIGN KEY (`senderID`) REFERENCES `users` (`ID`),
  CONSTRAINT `conversations_ibfk_2` FOREIGN KEY (`receiverID`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `drugs`
--

DROP TABLE IF EXISTS `drugs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drugs` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `manufacturer` varchar(100) DEFAULT NULL,
  `shelfLife` int DEFAULT NULL,
  `instructions` text,
  `description` text,
  `sideEffects` text,
  `storageInformation` text,
  `activeIngredients` text,
  `intakeTime` time NOT NULL,
  `intakeDay` enum('SUNDAY','MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY') NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employees` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `firstName` varchar(100) NOT NULL,
  `lastName` varchar(100) NOT NULL,
  `sex` enum('MALE','FEMALE','OTHER') NOT NULL,
  `birthDate` datetime NOT NULL,
  `hireDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `email` varchar(100) NOT NULL,
  `phone` varchar(100) NOT NULL,
  `address` varchar(100) NOT NULL,
  `managerID` int DEFAULT NULL,
  `race` enum('WHITE','BLACK','HISPANIC','ASIAN','NATIVE_AMERICAN','PACIFIC_ISLANDER','OTHER') NOT NULL,
  `ethnicity` enum('HISPANIC','NON_HISPANIC') NOT NULL,
  `middleName` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `email_unique` (`email`),
  KEY `employees_ibfk_2` (`managerID`),
  CONSTRAINT `employees_ibfk_1` FOREIGN KEY (`ID`) REFERENCES `users` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `employees_ibfk_2` FOREIGN KEY (`managerID`) REFERENCES `users` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=195 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `healthConditions`
--

DROP TABLE IF EXISTS `healthConditions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `healthConditions` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `creationTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `userID` int NOT NULL,
  `healthCondition` varchar(100) NOT NULL,
  `severity` enum('ACUTE','CHRONIC') NOT NULL,
  `type` enum('PHYSICAL','MENTAL') NOT NULL,
  `notes` text,
  PRIMARY KEY (`ID`),
  KEY `patientID` (`userID`),
  CONSTRAINT `healthConditions_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `logbook`
--

DROP TABLE IF EXISTS `logbook`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logbook` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `creationTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `userID` int NOT NULL,
  `IP` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `type` enum('SIGN_IN','SIGN_OUT') NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `logbook_ibfk_1` (`userID`),
  CONSTRAINT `logbook_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3602 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patients` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `firstName` varchar(100) NOT NULL,
  `lastName` varchar(100) NOT NULL,
  `sex` enum('MALE','FEMALE','OTHER') NOT NULL,
  `birthDate` date NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(100) NOT NULL,
  `address` varchar(100) NOT NULL,
  `preferredDoctorID` int DEFAULT NULL,
  `bloodType` enum('A+','A-','B+','B-','AB+','AB-','O+','O-','UNKNOWN') DEFAULT NULL,
  `height` decimal(5,2) DEFAULT NULL,
  `weight` decimal(5,2) DEFAULT NULL,
  `race` enum('WHITE','BLACK','HISPANIC','ASIAN','NATIVE_AMERICAN','PACIFIC_ISLANDER','OTHER') NOT NULL,
  `ethnicity` enum('HISPANIC','NON_HISPANIC') NOT NULL,
  `insuranceProvider` varchar(100) DEFAULT NULL,
  `insuranceID` varchar(100) DEFAULT NULL,
  `emergencyContactName` varchar(255) DEFAULT NULL,
  `emergencyContactPhone` varchar(255) DEFAULT NULL,
  `motherFirstName` varchar(255) DEFAULT NULL,
  `motherLastName` varchar(255) DEFAULT NULL,
  `fatherFirstName` varchar(255) DEFAULT NULL,
  `fatherLastName` varchar(255) DEFAULT NULL,
  `middleName` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `email_unique` (`email`),
  KEY `patients_ibfk_2` (`preferredDoctorID`),
  CONSTRAINT `patients_ibfk_1` FOREIGN KEY (`ID`) REFERENCES `users` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `patients_ibfk_2` FOREIGN KEY (`preferredDoctorID`) REFERENCES `users` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=199 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `prescriptions`
--

DROP TABLE IF EXISTS `prescriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prescriptions` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `creationTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `userID` int NOT NULL,
  `doctorID` int NOT NULL,
  `drugID` int NOT NULL,
  `intakeDay` enum('SUNDAY','MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY') NOT NULL,
  `intakeTime` time NOT NULL,
  `dosageQuantity` int NOT NULL,
  `dosageUnits` enum('ML','MG','G','IU','UNITS') NOT NULL,
  `description` text,
  `form` enum('TABLET','CAPSULE','LIQUID','INJECTION','CREAM','OINTMENT','INHALER','SUPPOSITORY','SOLUTION','SUSPENSION','SYRUP','SPRAY','LOZENGE','POWDER','GEL') DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `drugID` (`drugID`),
  KEY `doctorID` (`doctorID`),
  KEY `userID` (`userID`),
  CONSTRAINT `prescriptions_ibfk_1` FOREIGN KEY (`drugID`) REFERENCES `drugs` (`ID`),
  CONSTRAINT `prescriptions_ibfk_2` FOREIGN KEY (`doctorID`) REFERENCES `users` (`ID`),
  CONSTRAINT `prescriptions_ibfk_3` FOREIGN KEY (`userID`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `resetPasswordTokens`
--

DROP TABLE IF EXISTS `resetPasswordTokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resetPasswordTokens` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `creationTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `userID` int NOT NULL,
  `token` int DEFAULT NULL,
  `used` tinyint(1) DEFAULT '0',
  `sourceIP` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `userID` (`userID`),
  CONSTRAINT `resetPasswordTokens_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `resetPasswordTokens_chk_1` CHECK (((`token` > 100000) and (`TOKEN` <= 999999)))
) ENGINE=InnoDB AUTO_INCREMENT=208 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `surgeries`
--

DROP TABLE IF EXISTS `surgeries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `surgeries` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `creationTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `userID` int NOT NULL,
  `doctorID` int NOT NULL,
  `type` varchar(100) NOT NULL,
  `date` date NOT NULL,
  `location` varchar(100) NOT NULL,
  `notes` text,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `unique_surgery` (`userID`,`doctorID`,`type`,`date`),
  CONSTRAINT `surgeries_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `creationTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum('DOCTOR','NURSE','PATIENT') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `username_2` (`username`),
  UNIQUE KEY `username_3` (`username`),
  CONSTRAINT `users_chk_1` CHECK ((length(`username`) >= 5)),
  CONSTRAINT `users_chk_2` CHECK ((length(`username`) >= 5)),
  CONSTRAINT `users_chk_3` CHECK ((length(`password`) >= 8))
) ENGINE=InnoDB AUTO_INCREMENT=199 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vaccines`
--

DROP TABLE IF EXISTS `vaccines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vaccines` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `creationTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `userID` int NOT NULL,
  `vaccineGroup` enum('COVID-19','Influenza','Hepatitis A','Hepatitis B','Varicella','Polio','Pneumococcal','MMR','HPV','Shingles') NOT NULL,
  `date` date DEFAULT NULL,
  `provider` varchar(100) DEFAULT NULL,
  `notes` text,
  PRIMARY KEY (`ID`),
  KEY `vaccines_ibfk_1` (`userID`),
  CONSTRAINT `vaccines_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `visits`
--

DROP TABLE IF EXISTS `visits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visits` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `creationTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creationType` enum('IN-PERSON','ONLINE') NOT NULL,
  `userID` int NOT NULL,
  `doctorID` int NOT NULL,
  `completed` tinyint(1) NOT NULL DEFAULT '0',
  `reason` text,
  `description` text,
  `time` time NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `unique_user_date` (`userID`,`date`),
  KEY `doctorID` (`doctorID`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'easydoctor'
--
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-16 14:08:51
