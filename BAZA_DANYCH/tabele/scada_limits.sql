-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: scada
-- ------------------------------------------------------
-- Server version	5.7.17-log

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

--
-- Table structure for table `limits`
--

DROP TABLE IF EXISTS `limits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `limits` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(45) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `state_space_id` int(11) NOT NULL,
  `value` double NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_state_space_limit_idx` (`state_space_id`),
  CONSTRAINT `fk_state_space_limit` FOREIGN KEY (`state_space_id`) REFERENCES `state_space` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `limits`
--

LOCK TABLES `limits` WRITE;
/*!40000 ALTER TABLE `limits` DISABLE KEYS */;
INSERT INTO `limits` VALUES (1,'LEVEL_1_MIN','Poziom 1 min',2,3,-1),(2,'LEVEL_2_MIN','Poziom 2 min',3,2.9,-1),(3,'LEVEL_3_MIN','Poziom 3 min',4,1,-1),(4,'LEVEL_1_MIN_CRITICAL','Poziom 1 min kryt.',2,1,-2),(5,'LEVEL_2_MIN_CRITICAL','Poziom 2 min kryt.',3,0,-2),(6,'LEVEL_3_MIN_CRITICAL','Poziom 3 min kryt.',4,0,-2),(7,'LEVEL_1_MAX','Poziom 1 max',2,7,1),(8,'LEVEL_2_MAX','Poziom 2 max',3,10,1),(9,'LEVEL_3_MAX','Poziom 3 max',4,9,1),(10,'LEVEL_1_MAX_CRITICAL','Poziom 1 max kryt.',2,9,2),(11,'LEVEL_2_MAX_CRITICAL','Poziom 2 max kryt.',3,11,2),(12,'LEVEL_3_MAX_CRITICAL','Poziom 3 max kyt.',4,12,2);
/*!40000 ALTER TABLE `limits` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-19 17:19:08
