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
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `variable_state_id` int(11) NOT NULL,
  `state_space_id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL,
  `value` double NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_system_state_idx` (`variable_state_id`),
  KEY `fk_state_space_hist_idx` (`state_space_id`),
  CONSTRAINT `fk_state_space_hist` FOREIGN KEY (`state_space_id`) REFERENCES `state_space` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_variable_state` FOREIGN KEY (`variable_state_id`) REFERENCES `variable_state` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history`
--

LOCK TABLES `history` WRITE;
/*!40000 ALTER TABLE `history` DISABLE KEYS */;
INSERT INTO `history` VALUES (1,2,2,1,1.55,'2017-10-27 21:20:00'),(2,2,2,2,2,'2017-10-20 15:00:00'),(3,1,2,1,25,'2017-10-28 13:00:00'),(4,1,2,7,25,'2017-10-28 13:00:00'),(5,1,2,8,25,'2017-10-28 13:00:00'),(6,1,4,9,120,'2017-10-28 13:43:00'),(7,1,4,10,121,'2017-10-28 14:43:00'),(8,1,4,11,120,'2017-10-28 13:43:00'),(9,1,4,12,120,'2017-10-28 13:43:00'),(10,2,4,2,57,'2017-10-28 15:43:00'),(11,5,1,2,25,'2017-10-28 16:43:00'),(12,5,1,3,25,'2017-10-28 16:43:00'),(13,5,1,4,25,'2017-10-28 16:43:00'),(14,5,1,5,25,'2017-10-28 16:43:00'),(15,5,1,6,25,'2017-10-28 16:43:00'),(16,5,1,7,25,'2017-10-28 16:43:00'),(17,4,5,1,21,'2017-10-29 11:05:00');
/*!40000 ALTER TABLE `history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-10-29 20:37:28
