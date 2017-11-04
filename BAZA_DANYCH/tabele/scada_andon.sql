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
-- Table structure for table `andon`
--

DROP TABLE IF EXISTS `andon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `andon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `limit_id` int(11) NOT NULL,
  `state_space_id` int(11) NOT NULL,
  `value` double NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_limit_idx` (`limit_id`),
  KEY `fk_state_space_idx` (`state_space_id`),
  CONSTRAINT `fk_limit` FOREIGN KEY (`limit_id`) REFERENCES `limits` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_state_space` FOREIGN KEY (`state_space_id`) REFERENCES `state_space` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `andon`
--

LOCK TABLES `andon` WRITE;
/*!40000 ALTER TABLE `andon` DISABLE KEYS */;
INSERT INTO `andon` VALUES (1,1,2,25,'2017-10-28 12:00:00'),(2,8,3,100,'2017-10-28 12:43:00'),(3,1,2,25,'2017-10-28 12:00:00'),(4,8,3,100,'2017-10-28 12:43:00'),(5,8,3,100,'2017-10-28 12:43:00'),(6,1,2,25,'2017-10-28 13:00:00'),(7,1,2,25,'2017-10-28 13:00:00'),(8,1,2,25,'2017-10-28 13:00:00'),(9,9,4,120,'2017-10-28 13:43:00'),(10,9,4,120,'2017-10-28 13:43:00'),(11,9,4,120,'2017-10-28 13:43:00'),(12,9,4,120,'2017-10-28 13:43:00');
/*!40000 ALTER TABLE `andon` ENABLE KEYS */;
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