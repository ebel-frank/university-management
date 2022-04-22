-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: schooldatabase
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `subject_id` int NOT NULL,
  `year` int NOT NULL,
  `semester` int NOT NULL,
  `module` int NOT NULL,
  `specialty` varchar(45) NOT NULL,
  PRIMARY KEY (`id`,`subject_id`),
  KEY `fk_courses_subject1_idx` (`subject_id`),
  CONSTRAINT `fk_courses_subject1` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (34,1,1,1,1,'TC'),(35,2,1,1,1,'TC'),(36,3,1,1,2,'TC'),(37,4,1,1,2,'TC'),(38,5,1,1,3,'TC'),(39,6,1,1,3,'TC'),(40,7,1,1,3,'TC'),(41,8,1,2,1,'TC'),(42,9,1,2,1,'TC'),(43,10,1,2,2,'TC'),(44,11,1,2,2,'TC'),(45,5,1,2,3,'TC'),(46,6,1,2,3,'TC'),(47,7,1,2,3,'TC'),(48,12,2,3,1,'SIC'),(49,13,2,3,1,'SIC'),(50,14,2,3,2,'SIC'),(51,15,2,3,2,'SIC'),(52,5,2,3,3,'SIC'),(53,6,2,3,3,'SIC'),(54,7,2,3,3,'SIC'),(55,16,2,4,1,'SIC'),(56,17,2,4,1,'SIC'),(57,18,2,4,2,'SIC'),(58,19,2,4,2,'SIC'),(59,5,2,4,3,'SIC'),(60,6,2,4,3,'SIC'),(61,7,2,4,3,'SIC'),(62,20,2,3,1,'GME'),(63,21,2,3,1,'GME'),(64,22,2,3,2,'GME'),(65,23,2,3,2,'GME'),(66,5,2,3,3,'GME'),(67,6,2,3,3,'GME'),(68,7,2,3,3,'GME'),(69,16,2,4,1,'GME'),(70,24,2,4,1,'GME'),(71,25,2,4,2,'GME'),(72,26,2,4,2,'GME'),(73,27,2,4,2,'GME'),(74,5,2,4,3,'GME'),(75,6,2,4,3,'GME'),(76,7,2,4,3,'GME'),(77,28,2,3,1,'GE'),(78,29,2,3,1,'GE'),(79,14,2,3,2,'GE'),(80,30,2,3,2,'GE'),(81,5,2,3,3,'GE'),(82,6,2,3,3,'GE'),(83,7,2,3,3,'GE'),(84,16,2,4,1,'GE'),(85,31,2,4,1,'GE'),(86,32,2,4,2,'GE'),(87,33,2,4,2,'GE'),(88,5,2,4,3,'GE'),(89,6,2,4,3,'GE'),(90,7,2,4,3,'GE');
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-22  0:43:52
