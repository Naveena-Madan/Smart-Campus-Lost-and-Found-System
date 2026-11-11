-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: lost_found_db
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `claims`
--

DROP TABLE IF EXISTS `claims`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `claims` (
  `id` int NOT NULL AUTO_INCREMENT,
  `report_id` int NOT NULL,
  `username` varchar(50) NOT NULL,
  `answer` varchar(255) NOT NULL,
  `status` varchar(20) DEFAULT 'Pending',
  `previous_report_status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `claims`
--

LOCK TABLES `claims` WRITE;
/*!40000 ALTER TABLE `claims` DISABLE KEYS */;
INSERT INTO `claims` VALUES (1,1,'student1','Blue color with a sticker','Approved','Verified'),(2,5,'student1','Blue color with a sticker','Pending',NULL),(3,5,'student1','scartches at the bottom of the bottel','Pending',NULL),(4,9,'student1','Brand : Boat , black color','Approved',NULL),(5,12,'student1','USB pendrive , pink pendrive','Approved',NULL),(6,16,'student25','hazel nut brown , licsence card , 500 rupees cash','Approved',NULL),(7,5,'student12','a scratch is there in the bottom of the bottle','Approved',NULL),(8,18,'Naveen','scratch at the bottom','Approved',NULL),(9,19,'Naveena','3- 20rs. note and 4 - 100rs. note and 2- 500rs. note is there inside the wallet','Approved','Pending'),(10,22,'Naveen','white scratch mark at the bottom','Pending',NULL),(11,22,'Naveen','small spiderman tag is attached to it','Pending',NULL),(12,29,'Naveena','spiderman wallpaper','Pending',NULL),(13,30,'Naveena','spiderman sticker','Pending',NULL),(14,32,'Naveena','Emp.No: 600697','Approved','Pending'),(15,36,'Naveena','My handwritting is very small and i have scribbled something on the last page of my notebook','Pending',NULL);
/*!40000 ALTER TABLE `claims` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `report_id` int DEFAULT NULL,
  `message` varchar(255) NOT NULL,
  `status` varchar(20) DEFAULT 'UNREAD',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,'teststudent',NULL,'Welcome to Smart Campus Lost & Found!','UNREAD','2026-10-03 15:59:53'),(2,'Naveen',NULL,'Your claim for item \'Black Dell Laptop\' (Report ID 18) has been approved. The item has been marked as Returned.','UNREAD','2026-10-03 18:19:07'),(3,'Naveen',22,'Possible match found for your lost item: boat airdopes. A new found-item report may match it.','UNREAD','2026-10-04 14:18:49'),(4,'student1',NULL,'Your claim for item \'Black Wallet\' (Report ID 1) has been rejected. Please contact the Lost & Found administrator for more information.','UNREAD','2026-10-06 16:47:34'),(5,'student1',NULL,'Your claim for item \'Black Wallet\' (Report ID 1) has been reopened for further ownership verification.','UNREAD','2026-10-06 17:01:23'),(6,'student1',NULL,'Your claim for item \'Black Wallet\' (Report ID 1) has been rejected. Please contact the Lost & Found administrator for more information.','UNREAD','2026-10-06 17:01:33'),(7,'Naveena',NULL,'Your claim for item \'I found a black wallet\' (Report ID 19) has been approved. The item has been marked as Returned.','UNREAD','2026-10-06 17:53:00'),(8,'Naveena',NULL,'Your approved claim for item \'I found a black wallet\' (Report ID 19) has been reopened for further ownership verification.','UNREAD','2026-10-06 17:58:35'),(9,'student1',NULL,'Your claim for item \'Black Wallet\' (Report ID 1) has been reopened for further ownership verification.','UNREAD','2026-10-06 18:24:08'),(10,'student1',NULL,'Your claim for item \'Black Wallet\' (Report ID 1) has been approved. The item has been marked as Returned.','UNREAD','2026-10-06 19:24:23'),(11,'student1',NULL,'Your claim for item \'Blue Water Bottle\' (Report ID 5) has been rejected. Please contact the Lost & Found administrator for more information.','UNREAD','2026-10-06 19:29:00'),(12,'student1',NULL,'Your claim for item \'Blue Water Bottle\' (Report ID 5) has been reopened for further ownership verification.','UNREAD','2026-10-06 19:29:10'),(13,'student1',NULL,'Your approved claim for item \'Black Wallet\' (Report ID 1) has been reopened for further ownership verification.','UNREAD','2026-10-06 19:44:54'),(14,'Naveen',26,'Possible match found for your lost item: Black Dell laptop. A new found-item report may match it.','UNREAD','2026-10-06 20:08:09'),(15,'student1',NULL,'Your claim for item \'Black Wallet\' (Report ID 1) has been approved. The item has been marked as Returned.','UNREAD','2026-10-08 14:20:24'),(16,'student1',NULL,'Your approved claim for item \'Black Wallet\' (Report ID 1) has been reopened for further ownership verification.','UNREAD','2026-10-08 14:20:34'),(17,'Naveena',NULL,'Your claim for item \'I found a black wallet\' (Report ID 19) has been approved. The item has been marked as Returned.','UNREAD','2026-10-08 16:23:02'),(18,'Naveena',29,'Possible match found for your lost item: iphone. A new found-item report may match it.','UNREAD','2026-10-08 16:35:02'),(19,'student1',NULL,'Your claim for item \'Black Wallet\' (Report ID 1) has been rejected. Please contact the Lost & Found administrator for more information.','UNREAD','2026-10-08 17:05:35'),(20,'student1',NULL,'Your claim for item \'Black Wallet\' (Report ID 1) has been reopened for further ownership verification.','UNREAD','2026-10-08 17:05:42'),(21,'student1',NULL,'Your approved claim for item \'Blue Water Bottle\' (Report ID 5) has been reopened for further ownership verification.','UNREAD','2026-10-08 17:28:14'),(22,'student1',NULL,'Your claim for item \'Black Wallet\' (Report ID 1) has been approved. The item has been marked as Returned.','UNREAD','2026-10-08 20:02:10'),(23,'Naveena',NULL,'Your approved claim for item \'I found a black wallet\' (Report ID 19) has been reopened for further ownership verification.','UNREAD','2026-10-08 20:02:21'),(24,'Naveena',NULL,'Your claim for item \'I found a black wallet\' (Report ID 19) has been approved. The item has been marked as Returned.','UNREAD','2026-10-08 20:07:18'),(25,'Naveena',NULL,'Your claim for item \'ID card\' (Report ID 32) has been approved. The item has been marked as Returned.','UNREAD','2026-10-12 04:52:28'),(26,'Naveena',36,'Possible match found for your lost item: Chemistry Record Note. A new found-item report may match it.','UNREAD','2026-10-19 16:40:31');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reports`
--

DROP TABLE IF EXISTS `reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reports` (
  `id` int NOT NULL AUTO_INCREMENT,
  `item_name` varchar(100) NOT NULL,
  `location` varchar(100) NOT NULL,
  `report_type` varchar(20) NOT NULL,
  `status` varchar(20) DEFAULT 'Pending',
  `username` varchar(50) DEFAULT NULL,
  `event_time` varchar(150) DEFAULT NULL,
  `description` text,
  `identifying_details` text,
  `current_location` varchar(255) DEFAULT NULL,
  `additional_details` text,
  `image_path` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reports`
--

LOCK TABLES `reports` WRITE;
/*!40000 ALTER TABLE `reports` DISABLE KEYS */;
INSERT INTO `reports` VALUES (1,'Black Wallet','CSE Block','Lost','Returned',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,'Black Wallet','CSE Block','Lost','Verified',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,'Blue Water Bottle','Library','Lost','Pending',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,'Blue Water Bottle','Library','Lost','Verified',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(5,'Blue Water Bottle','Library','Found','Pending',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6,'black pouch','AIML Block','Lost','Verified',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(7,'Black Water Bottle','CSE Block','Lost','Pending',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(8,'Black Water Bottle','CSE Block','Found','Pending',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(9,'Boat Airpods','IST 416','Found','Returned',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(10,'Airpods','IST 416','Lost','Verified',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(11,'pendrive','IST 308','Lost','Verified',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(12,'pendrive','IST 308','Found','Returned',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(13,'brown wallet','CSE block','Lost','Pending','student25',NULL,NULL,NULL,NULL,NULL,NULL),(14,'bown wallet','CSE block','Found','Rejected','student35',NULL,NULL,NULL,NULL,NULL,NULL),(15,'brown wallet','CSE block','Lost','Verified','student35',NULL,NULL,NULL,NULL,NULL,NULL),(16,'brown wallet','CSE block','Found','Returned','student35',NULL,NULL,NULL,NULL,NULL,NULL),(17,'Black Dell laptop','Library','Lost','Pending','Naveen',NULL,NULL,NULL,NULL,NULL,NULL),(18,'Black Dell Laptop','Library','Found','Returned','Naveena',NULL,NULL,NULL,NULL,NULL,NULL),(19,'I found a black wallet','Near the library','FOUND','Returned','Naveen','Yesterday evening','Black leather wallet with three card slots','There is a small Spider-Man sticker inside.','I handed it to the security guard near the main gate.','Not provided','C:\\Users\\navee\\OneDrive\\Desktop\\1_2e8ef8f2-373d-4718-bd2e-359c749624b9.webp'),(20,'Black Wallet','near the library','LOST','Pending','Naveena','yesterday morning','Black leather wallet with three card slots','small Spider-Man sticker inside.',NULL,'nothing',NULL),(21,'boat airdopes','CSE Block','LOST','Pending','Naveen','IST 303','Black boAt Airdopes 313','a small batman tag attached to it',NULL,'nothing',NULL),(22,'boat airdopes','CSE Block','FOUND','Pending','Naveena','yesterday evening','black boat airdopes 313','small batman tag is attached','in the lobby','i gave it to a staff in the lobby','C:\\Users\\navee\\OneDrive\\Desktop\\Mainimage_1284ecbe-924e-4232-ae44-5257444bbcdf.webp'),(23,'black purse','near tech block','LOST','Pending','','yesterday morning','black leather purse with a gold color zip','hellokitty tag will be attched to it',NULL,'nothing',NULL),(24,'Hand Fan','CSE Block IST 103','LOST','Pending','','yesterday morning','black color hand fan','cat sticker',NULL,'nothing',NULL),(25,'ipad','SRMIST 403','LOST','Pending','Naveena','yesterday','black color ipad','wallpaper of a CAR',NULL,'nothing',NULL),(26,'Laptop','library','FOUND','Pending','Naveena','yesterday evening','black color Hp laptop','batman laptop skin / cover','Lobby','i gave it to the staffs in the lobby','C:\\Users\\navee\\OneDrive\\Desktop\\Brothers-Innovation-Laptop-Skins-Comics-The_Batman_in_BW-Image-1_de0c860e-8584-42ca-b7bc-a7c71c8f2b28.webp'),(27,'Laptop','library','LOST','Pending','Naveen','yesterday morning','black color Hp laptop','Batman laptop skin/cover',NULL,'nothing',NULL),(28,'iphone','IST 303','LOST','Pending','Naveena','yesterday morning','Iphone 17 pro','spiderman case',NULL,'nothing',NULL),(29,'Iphone','IST 303','FOUND','Pending','Naveen','Yesterday evening','Iphone 17 pro','Spiderman case','Lobby','i handed the iphone to a staff in the lobby','C:\\Users\\navee\\OneDrive\\Desktop\\61GdPCOZTjL._AC_UF1000,1000_QL80_.jpg'),(30,'Flask bottle','Library','FOUND','Pending','Naveen','Yesterday 12.00pm','Milton flask bottle','silver color','In the Lobby','i handed it over to a staff in the lobby','C:\\Users\\navee\\OneDrive\\Desktop\\0c978c5b-cd9c-4311-81b7-9bc4d50887d01663072336286WaterBottle1.webp'),(31,'Flask bottle','Library','LOST','Pending','Naveena','yesterday 9am','Milton flask bottle','silver color',NULL,'Nothing','C:\\Users\\navee\\OneDrive\\Desktop\\0c978c5b-cd9c-4311-81b7-9bc4d50887d01663072336286WaterBottle1.webp'),(32,'ID card','IST 403','FOUND','Returned','Naveena','Yesterday evening','Dr.MONISHA M ; DESIG: Assistant Professor','Purple color id tag','In the lobbyb','i gave that id card to a staff member in the lobby','C:\\Users\\navee\\SmartCampusLostFound\\images\\report_photo_603b2ff5-3de5-4788-8410-113ece233900.png'),(33,'ID Card','IST 403','LOST','Pending','Naveena','Yesterday morning','Dr.MONISHA M ; DESIG: Assistant Professor','Purple Color Tag',NULL,'Nothing','C:\\Users\\navee\\SmartCampusLostFound\\images\\report_photo_7fe1efb1-7bfc-4e76-99b5-d85599b1f754.png'),(34,'Chemistry Record Note','Chemistry Lab','LOST','Pending','Naveena','Yesterday 11 am','Naveena M , reg.no: 7, CSE A','Spiderman label',NULL,'Nothing',NULL),(35,'Chemistry Record Note','Chemistry Lab','FOUND','Pending','Naveena','Yesterday 4 pm','Naveena M, Reg.no 7, CSE A','Spiderman Label','Lobby','Nothing','C:\\Users\\navee\\SmartCampusLostFound\\images\\report_photo_aa273408-4a62-4a88-b34b-430a3124484c.png'),(36,'Chemistry Record Note','Chemistry Lab','FOUND','Pending','Naveen','Yesterday 4 pm','Naveena M , Reg no. 7, CSE A','Spiderman Label','Lobby','Nothing','C:\\Users\\navee\\SmartCampusLostFound\\images\\report_photo_6ad02399-e273-401a-9dea-ca2e1a9ff5c6.png');
/*!40000 ALTER TABLE `reports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Naveen Madan','Naveen','n123','STUDENT'),(2,NULL,'Madan','m123','ADMIN'),(3,'Naveena Madan','Naveena','n123','STUDENT');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-11 22:41:11
