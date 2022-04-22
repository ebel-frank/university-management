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
-- Table structure for table `professor`
--

DROP TABLE IF EXISTS `professor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `professor` (
  `id` int NOT NULL AUTO_INCREMENT,
  `credentials_id` int NOT NULL,
  `firstname` varchar(45) NOT NULL,
  `lastname` varchar(45) NOT NULL,
  `examCoeff` int NOT NULL DEFAULT '50',
  `tpCoeff` int NOT NULL DEFAULT '20',
  `ccCoeff` int NOT NULL DEFAULT '30',
  `subject_id` int NOT NULL,
  PRIMARY KEY (`id`,`credentials_id`,`subject_id`),
  UNIQUE KEY `subject_id_UNIQUE` (`subject_id`),
  KEY `fk_professor_credentials1_idx` (`credentials_id`),
  KEY `fk_professor_subject1_idx` (`subject_id`),
  CONSTRAINT `fk_professor_credentials1` FOREIGN KEY (`credentials_id`) REFERENCES `credentials` (`id`),
  CONSTRAINT `fk_professor_subject1` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `professor`
--

LOCK TABLES `professor` WRITE;
/*!40000 ALTER TABLE `professor` DISABLE KEYS */;
INSERT INTO `professor` VALUES (5,26,'Emeka','Ebeledike',60,25,15,1),(6,27,'Victoria','Emmanuel',50,20,30,20),(7,29,'Davidson','Luther',50,20,30,2),(13,38,'Robert','Stone',50,20,30,3),(14,39,'Bill','Gates',50,20,30,8),(15,40,'Adam','Smith',50,20,30,16),(16,41,'Will','Smith',50,20,30,5),(17,42,'Wisdom','Smart',50,20,30,7),(18,43,'Jude','Xavier',50,20,30,9),(19,44,'Mirable','White',50,20,30,4),(20,45,'George','Brown',50,20,30,17),(21,47,'Elnesto','DelaCruise',50,20,30,6),(22,48,'Bruno','Mars',50,20,30,19),(23,49,'Dominic','Wood',50,20,30,21),(24,50,'Clark','Kent',50,20,30,23),(25,51,'Dominion','Bench',50,20,30,26),(28,54,'Mark','Haywire',50,20,30,27),(29,55,'Ceya','Bright',50,20,30,13),(31,58,'Nathaniel','Benson',50,20,30,10),(32,59,'Priyakah','Choprah',50,20,30,11),(33,60,'Kahmlah','Vithal',50,20,30,12),(34,61,'Micheal','Jackson',50,20,30,14),(35,62,'Rose','Cream',50,20,30,15),(36,63,'Miguel','Santiago',50,20,30,18),(38,65,'James','Larry',50,20,30,22),(39,66,'Amanda','Adichie',50,20,30,24),(40,67,'Simeon','Obute',50,20,30,25),(42,69,'Lawrence','Nevo',50,20,30,28),(43,70,'Joshua','Ohize',50,20,30,29),(44,71,'Fredrick','Snow',50,20,30,30),(45,72,'Martin','Cardbury',50,20,30,31),(46,73,'Chinaza','Terna',50,20,30,32),(47,74,'Monseiur','Bonath',50,20,30,33);
/*!40000 ALTER TABLE `professor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-22  0:43:54
