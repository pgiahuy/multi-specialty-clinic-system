-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: clinicdb
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `patient_id` bigint DEFAULT NULL,
  `status` enum('UN_PAID','PENDING','CONFIRMED','IN_PROGRESS','COMPLETED','CANCELLED') DEFAULT 'PENDING',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `schedule_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_appointment_patient` (`patient_id`),
  KEY `appointment_ibfk_2_idx` (`schedule_id`),
  CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`),
  CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`schedule_id`) REFERENCES `schedules` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES (1,23,'PENDING','2026-05-29 00:00:00',2),(41,24,'IN_PROGRESS','2026-05-30 04:40:38',11);
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `areas`
--

DROP TABLE IF EXISTS `areas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `areas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `area_name` varchar(100) NOT NULL,
  `location_floor` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `areas`
--

LOCK TABLES `areas` WRITE;
/*!40000 ALTER TABLE `areas` DISABLE KEYS */;
INSERT INTO `areas` VALUES (7,'Khu D',1);
/*!40000 ALTER TABLE `areas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_message`
--

DROP TABLE IF EXISTS `chat_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `conversation_id` bigint NOT NULL,
  `sender_id` bigint NOT NULL,
  `sender_type` varchar(50) NOT NULL,
  `message_type` varchar(20) DEFAULT 'TEXT',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `sender_id` (`sender_id`),
  KEY `idx_chat_message_conversation` (`conversation_id`),
  CONSTRAINT `chat_message_ibfk_1` FOREIGN KEY (`conversation_id`) REFERENCES `conversation` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chat_message_ibfk_2` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_message`
--

LOCK TABLES `chat_message` WRITE;
/*!40000 ALTER TABLE `chat_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conversation`
--

DROP TABLE IF EXISTS `conversation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conversation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `patient_id` bigint NOT NULL,
  `receiver_id` bigint DEFAULT NULL,
  `conversation_type` varchar(50) NOT NULL,
  `appointment_id` bigint DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_video_appointment` (`appointment_id`),
  KEY `patient_id` (`patient_id`),
  KEY `receiver_id` (`receiver_id`),
  CONSTRAINT `conversation_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `conversation_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`) ON DELETE SET NULL,
  CONSTRAINT `conversation_ibfk_3` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conversation`
--

LOCK TABLES `conversation` WRITE;
/*!40000 ALTER TABLE `conversation` DISABLE KEYS */;
/*!40000 ALTER TABLE `conversation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor`
--

DROP TABLE IF EXISTS `doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) DEFAULT NULL,
  `description` text,
  `user_id` bigint DEFAULT NULL,
  `gender` enum('Nam','Nữ') DEFAULT NULL,
  `is_active` tinyint DEFAULT '1',
  `rating` float DEFAULT NULL,
  `cccd` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  UNIQUE KEY `cccd_UNIQUE` (`cccd`),
  CONSTRAINT `doctor_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

LOCK TABLES `doctor` WRITE;
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
INSERT INTO `doctor` VALUES (1,'Nguyễn Văn Hùng','Trưởng khoa Tim mạch',NULL,'Nam',1,NULL,NULL),(2,'Lê Thị Mai','',NULL,'Nữ',1,NULL,NULL),(3,'Trần Quốc Bảo','',NULL,'Nam',1,NULL,NULL),(4,'Hoàng Văn Thắng','',NULL,'Nam',1,NULL,NULL),(7,'Trịnh Công Sơn','',NULL,'Nam',1,NULL,NULL),(8,'Nguyễn Thu Trang','',NULL,'Nữ',1,NULL,NULL),(9,'Đặng Văn Lâm','',NULL,'Nam',1,NULL,NULL),(10,'Nguyễn Minh Hải','',NULL,'Nam',1,NULL,NULL),(11,'Trần Thị Thắm','',NULL,'Nữ',1,NULL,'12345678'),(13,'Nguyễn Hoàng Nam','',99,'Nam',1,NULL,NULL),(16,'Lý Văn Phúc','Trưởng khoa Thần kinh',NULL,'Nam',1,NULL,'123456789'),(18,'Trần Văn Hoàng','Bác sĩ chuyên khoa',104,'Nam',1,NULL,'234'),(19,'Nguyễn Thị Minh Nguyệt','',NULL,'Nữ',1,NULL,NULL),(21,'Lê Hồng Hạnh','',NULL,'Nữ',1,NULL,NULL),(22,'Lâm Vĩnh Hải','',NULL,'Nam',1,NULL,NULL),(23,'Trương Mỹ Nhân','',109,'Nữ',1,NULL,NULL),(24,'Cao Thái Sơn','Bác sĩ chuyên khoa',110,'Nam',1,NULL,'265'),(25,'Dương Triệu Vũ','',NULL,'Nam',1,NULL,NULL),(26,'Lý Nhã Kỳ','',112,'Nữ',1,NULL,NULL),(27,'Quách Ngọc Ngoan','',113,'Nam',1,NULL,NULL),(28,'Bảo','',NULL,'Nam',1,NULL,NULL),(29,'Trần Thị Thắm','',NULL,'Nữ',1,NULL,NULL),(30,'Trần Thị','',NULL,'Nam',NULL,NULL,NULL),(31,'Trần Thị Thắmm','Bác sĩ chuyên khoa II',NULL,'Nam',NULL,NULL,NULL);
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_log`
--

DROP TABLE IF EXISTS `inventory_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `medicine_id` bigint DEFAULT NULL,
  `batch_id` bigint DEFAULT NULL,
  `change_amount` int NOT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reference_id` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `medicine_id` (`medicine_id`),
  KEY `batch_id` (`batch_id`),
  CONSTRAINT `inventory_log_ibfk_1` FOREIGN KEY (`medicine_id`) REFERENCES `medicine` (`id`),
  CONSTRAINT `inventory_log_ibfk_2` FOREIGN KEY (`batch_id`) REFERENCES `medicine_batch` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_log`
--

LOCK TABLES `inventory_log` WRITE;
/*!40000 ALTER TABLE `inventory_log` DISABLE KEYS */;
INSERT INTO `inventory_log` VALUES (1,49,46,-1,'Kê đơn',11,'2026-05-30 18:13:05','dr_hoang_k6'),(2,47,44,-1,'Kê đơn',11,'2026-05-30 18:13:05','dr_hoang_k6');
/*!40000 ALTER TABLE `inventory_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lab_results`
--

DROP TABLE IF EXISTS `lab_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lab_results` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `appointment_id` bigint DEFAULT NULL,
  `test_id` int DEFAULT NULL,
  `result_value` varchar(50) DEFAULT NULL,
  `is_abnormal` tinyint(1) DEFAULT NULL,
  `pdf_url` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_res_appointment` (`appointment_id`),
  KEY `fk_res_test` (`test_id`),
  CONSTRAINT `fk_res_appointment` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_res_test` FOREIGN KEY (`test_id`) REFERENCES `lab_tests` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_results`
--

LOCK TABLES `lab_results` WRITE;
/*!40000 ALTER TABLE `lab_results` DISABLE KEYS */;
INSERT INTO `lab_results` VALUES (13,41,2,NULL,NULL,NULL,'2026-05-29 23:31:12');
/*!40000 ALTER TABLE `lab_results` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lab_tests`
--

DROP TABLE IF EXISTS `lab_tests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lab_tests` (
  `id` int NOT NULL AUTO_INCREMENT,
  `test_name` varchar(255) DEFAULT NULL,
  `unit` varchar(50) DEFAULT NULL,
  `normal_range` varchar(100) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_tests`
--

LOCK TABLES `lab_tests` WRITE;
/*!40000 ALTER TABLE `lab_tests` DISABLE KEYS */;
INSERT INTO `lab_tests` VALUES (2,'Định lượng Hemoglobin','g/dL','12.0 - 16.5',10000.00),(3,'Tốc độ lắng máu (ESR)','mm/h','0 - 20',NULL),(4,'Đường huyết lúc đói (Glucose)','mmol/L','3.9 - 6.4',NULL),(5,'Định lượng Creatinine (Chức năng thận)','umol/L','62 - 120',NULL),(6,'Định lượng Ure','mmol/L','2.5 - 7.5',NULL),(7,'Men gan AST (SGOT)','U/L','0 - 40',NULL),(8,'Men gan ALT (SGPT)','U/L','0 - 41',NULL),(9,'Định lượng Albumin','g/L','35 - 50',NULL),(10,'Cholesterol toàn phần','mmol/L','3.9 - 5.2',NULL),(11,'Triglyceride','mmol/L','0.46 - 1.88',NULL),(12,'HDL-Cholesterol (Mỡ tốt)','mmol/L','> 0.9',NULL),(13,'LDL-Cholesterol (Mỡ xấu)','mmol/L','< 3.4',NULL),(14,'Định lượng TSH','uIU/mL','0.27 - 4.2',NULL),(15,'Định lượng Free T4 (FT4)','pmol/L','12 - 22',NULL),(16,'Định lượng HbA1c','%','4.0 - 6.0',NULL),(17,'Tổng phân tích nước tiểu (10 thông số)','N/A','Âm tính',NULL),(18,'Định lượng Axit Uric (Tầm soát Gout)','umol/L','208 - 428',NULL),(19,'Định lượng Calci toàn phần','mmol/L','2.15 - 2.55',NULL),(20,'Xét nghiệm Viêm gan B (HBsAg)','S/CO','< 1.0 (Âm tính)',NULL),(21,'Tổng phân tích tế bào máu hrh','Viên','4.0 - 10.0',NULL);
/*!40000 ALTER TABLE `lab_tests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medical_record`
--

DROP TABLE IF EXISTS `medical_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medical_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `appointment_id` bigint DEFAULT NULL,
  `diagnosis` text,
  `note` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `appointment_id` (`appointment_id`),
  CONSTRAINT `medical_record_ibfk_1` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medical_record`
--

LOCK TABLES `medical_record` WRITE;
/*!40000 ALTER TABLE `medical_record` DISABLE KEYS */;
INSERT INTO `medical_record` VALUES (2,41,'Khùng he',NULL,'2026-05-30 06:08:28');
/*!40000 ALTER TABLE `medical_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicine`
--

DROP TABLE IF EXISTS `medicine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicine` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `price` decimal(18,2) NOT NULL,
  `secure_url` varchar(500) DEFAULT NULL,
  `public_id` varchar(255) DEFAULT NULL,
  `min_stock_alert` int DEFAULT '20',
  `unit` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicine`
--

LOCK TABLES `medicine` WRITE;
/*!40000 ALTER TABLE `medicine` DISABLE KEYS */;
INSERT INTO `medicine` VALUES (1,'G18911','Thuốc',2000.00,NULL,NULL,12,'Bộ'),(3,'G18917','Entases',23.00,'https://res.cloudinary.com/dyojjvkxz/image/upload/v1779709824/clinic/medicine/fzyhacwtjywwzkwvmsnh.jpg','clinic/medicine/fzyhacwtjywwzkwvmsnh',1,'Viên'),(4,'G1212','Panadon',32322.00,'https://res.cloudinary.com/dyojjvkxz/image/upload/v1779710890/clinic/medicine/agcmy5rffzwxmevgikew.png','clinic/medicine/agcmy5rffzwxmevgikew',12,'Viên'),(5,'G1893','252',11.00,NULL,NULL,11,'1'),(6,'MED006','Paracetamol 500mg',1500.00,NULL,NULL,100,'Viên'),(7,'MED007','Efferalgan Codeine',4500.00,NULL,NULL,50,'Viên'),(8,'MED008','Amoxicillin 500mg',2200.00,NULL,NULL,120,'Viên'),(9,'MED009','Cefuroxim 500mg',8500.00,NULL,NULL,60,'Viên'),(10,'MED010','Azithromycin 500mg',15000.00,NULL,NULL,30,'Viên'),(11,'MED011','Ibuprofen 400mg',1800.00,NULL,NULL,80,'Viên'),(12,'MED012','Meloxicam 15mg',3500.00,NULL,NULL,40,'Viên'),(13,'MED013','Celecoxib 200mg',6000.00,NULL,NULL,50,'Viên'),(14,'MED014','Voltaren Emulgel 20g',75000.00,NULL,NULL,15,'Tuýp'),(15,'MED015','Omeprazol 20mg',1200.00,NULL,NULL,150,'Viên'),(16,'MED016','Esomeprazol 40mg',12000.00,NULL,NULL,90,'Viên'),(17,'MED017','Phosphalugel (Chữ P)',4800.00,NULL,NULL,200,'Gói'),(18,'MED018','Gaviscon Dual Action',6500.00,NULL,NULL,150,'Gói'),(19,'MED019','Motilium-M',2500.00,NULL,NULL,100,'Viên'),(20,'MED020','Berberin 100mg',500.00,NULL,NULL,300,'Viên'),(21,'MED021','Smecta 3g',5200.00,NULL,NULL,100,'Gói'),(22,'MED022','Loperamid 2mg',800.00,NULL,NULL,60,'Viên'),(23,'MED023','Duphalac 15ml',7000.00,NULL,NULL,50,'Gói'),(24,'MED024','Amlodipin 5mg',1100.00,NULL,NULL,200,'Viên'),(25,'MED025','Losartan 50mg',3800.00,NULL,NULL,100,'Viên'),(26,'MED026','Concor 2.5mg',4200.00,NULL,NULL,90,'Viên'),(27,'MED027','Crestor 10mg',16500.00,NULL,NULL,60,'Viên'),(28,'MED028','Lipitor 20mg',22000.00,NULL,NULL,60,'Viên'),(29,'MED029','Metformin 850mg',2500.00,NULL,NULL,150,'Viên'),(30,'MED030','Diamicron MR 60mg',5500.00,NULL,NULL,100,'Viên'),(31,'MED031','Singulair 10mg',14000.00,NULL,NULL,40,'Viên'),(32,'MED032','Ventolin Evohaler',95000.00,NULL,NULL,10,'Chai'),(33,'MED033','Seretide Inhaler',280000.00,NULL,NULL,5,'Chai'),(34,'MED034','Telfast HD 180mg',8500.00,NULL,NULL,50,'Viên'),(35,'MED035','Loratadin 10mg',1200.00,NULL,NULL,100,'Viên'),(36,'MED036','Cetinzin 10mg',1500.00,NULL,NULL,100,'Viên'),(37,'MED037','Siro Prospan 100ml',78000.00,NULL,NULL,20,'Chai'),(38,'MED038','Acemuc 200mg',2800.00,NULL,NULL,150,'Gói'),(39,'MED039','Eugica xanh',1000.00,NULL,NULL,200,'Viên'),(40,'MED040','Boganic',2400.00,NULL,NULL,120,'Viên'),(41,'MED041','Ginkgo Biloba 80mg',3000.00,NULL,NULL,150,'Viên'),(42,'MED042','Neurobion',3500.00,NULL,NULL,100,'Viên'),(43,'MED043','Enervon-C',2200.00,NULL,NULL,100,'Viên'),(44,'MED044','Calcium Corbiere 10ml',8500.00,NULL,NULL,60,'Ống'),(45,'MED045','Vitamin C 500mg',1000.00,NULL,NULL,200,'Viên'),(46,'MED046','Zinc-Kid 20mg',3200.00,NULL,NULL,80,'Gói'),(47,'MED047','Otrivin 0.1% nhỏ mũi',52000.00,NULL,NULL,20,'Chai'),(48,'MED048','Nước muối sinh lý 0.9%',5000.00,NULL,NULL,50,'Chai'),(49,'MED049','Tobradex 5ml nhỏ mắt',55000.00,NULL,NULL,15,'Chai'),(50,'MED050','Systane Ultra 10ml',75000.00,NULL,NULL,15,'Chai'),(51,'MED051','Diazepam 5mg',4000.00,NULL,NULL,30,'Viên'),(52,'MED052','Rotunda 30mg',1500.00,NULL,NULL,120,'Viên'),(53,'MED053','Hapacol 250mg trẻ em',2500.00,NULL,NULL,100,'Gói'),(54,'MED054','Salbutamol 2mg',800.00,NULL,NULL,100,'Viên'),(55,'MED055','Prednisolon 5mg',1200.00,NULL,NULL,150,'Viên');
/*!40000 ALTER TABLE `medicine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicine_batch`
--

DROP TABLE IF EXISTS `medicine_batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicine_batch` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `medicine_id` bigint DEFAULT NULL,
  `batch_code` varchar(50) NOT NULL,
  `expiry_date` date NOT NULL,
  `quantity` int NOT NULL,
  `import_date` date DEFAULT (curdate()),
  PRIMARY KEY (`id`),
  KEY `medicine_id` (`medicine_id`),
  CONSTRAINT `medicine_batch_ibfk_1` FOREIGN KEY (`medicine_id`) REFERENCES `medicine` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicine_batch`
--

LOCK TABLES `medicine_batch` WRITE;
/*!40000 ALTER TABLE `medicine_batch` DISABLE KEYS */;
INSERT INTO `medicine_batch` VALUES (1,4,'1111','2026-05-29',11,'2026-05-01'),(2,5,'AUG2601','2027-06-15',500,'2026-05-10'),(3,6,'PAR2602','2026-07-20',12,'2026-04-15'),(4,7,'EFF2601','2028-01-10',200,'2026-05-01'),(5,8,'AMO2603','2027-09-05',450,'2026-05-12'),(6,9,'CEF2601','2026-06-15',8,'2026-03-20'),(7,10,'AZI2602','2027-12-25',150,'2026-05-18'),(8,11,'IBU2601','2027-03-14',300,'2026-04-01'),(9,12,'MEL2604','2028-02-18',180,'2026-05-20'),(10,13,'CEL2601','2026-08-30',250,'2026-05-05'),(11,14,'VOL2602','2027-05-22',40,'2026-04-10'),(12,15,'OME2601','2027-11-11',1000,'2026-05-01'),(13,16,'ESO2603','2026-06-01',95,'2026-03-15'),(14,17,'PHO2601','2028-04-05',600,'2026-05-15'),(15,18,'GAV2602','2027-10-20',400,'2026-05-16'),(16,19,'MOT2601','2027-01-15',350,'2026-04-22'),(17,20,'BER2605','2028-06-30',2000,'2026-05-25'),(18,21,'SME2601','2027-08-14',500,'2026-05-02'),(19,22,'LOP2602','2026-09-12',150,'2026-04-11'),(20,23,'DUP2601','2027-04-19',120,'2026-03-30'),(21,24,'AML2603','2028-03-01',800,'2026-05-10'),(22,25,'LOS2601','2027-07-25',400,'2026-05-08'),(23,26,'CON2602','2027-11-30',300,'2026-05-12'),(24,27,'CRE2601','2028-02-14',250,'2026-05-14'),(25,28,'LIP2604','2027-10-05',180,'2026-04-18'),(26,29,'MET2601','2027-05-20',600,'2026-05-01'),(27,30,'DIA2602','2027-12-15',350,'2026-05-03'),(28,31,'SIN2601','2028-01-22',140,'2026-05-19'),(29,32,'VEN2603','2026-11-11',30,'2026-04-05'),(30,33,'SER2601','2027-03-08',25,'2026-03-25'),(31,34,'TEL2602','2027-09-18',220,'2026-05-11'),(32,35,'LOR2601','2026-12-25',500,'2026-05-02'),(33,36,'CET2604','2027-06-14',450,'2026-05-15'),(34,37,'PRO2601','2026-10-30',50,'2026-04-20'),(35,38,'ACE2602','2027-08-05',700,'2026-05-07'),(36,39,'EUG2601','2028-05-12',1200,'2026-05-22'),(37,40,'BOG2603','2027-04-25',600,'2026-04-30'),(38,41,'GIN2601','2027-11-01',400,'2026-05-02'),(39,42,'NEU2602','2027-07-19',350,'2026-05-10'),(40,43,'ENE2601','2026-12-10',500,'2026-04-12'),(41,44,'CAL2605','2027-03-15',180,'2026-05-01'),(42,45,'VIT2602','2028-02-28',1500,'2026-05-24'),(43,46,'ZIN2601','2027-09-09',240,'2026-05-11'),(44,47,'OTR2603','2026-08-15',4,'2026-03-01'),(45,48,'NAT2601','2028-06-01',300,'2026-05-26'),(46,49,'TOB2602','2027-01-20',79,'2026-04-15'),(47,50,'SYS2601','2027-05-14',60,'2026-05-05'),(48,51,'DIA2604','2026-10-01',100,'2026-04-02'),(49,52,'ROT2601','2027-12-30',400,'2026-05-18'),(50,53,'HAP2602','2027-08-22',350,'2026-05-12'),(51,54,'SAL2601','2026-07-05',200,'2026-03-10'),(52,55,'PRE2603','2027-11-15',500,'2026-05-19'),(53,4,'1112','2027-11-30',200,'2026-05-28'),(54,5,'AUG2602','2028-01-10',300,'2026-05-25'),(55,6,'PAR2603','2028-06-15',1000,'2026-05-29'),(56,7,'EFF2602','2028-05-20',150,'2026-05-20'),(57,8,'AMO2604','2027-12-01',500,'2026-05-24'),(58,9,'CEF2602','2027-03-10',150,'2026-05-15'),(59,9,'CEF2603','2027-08-22',200,'2026-05-28'),(60,10,'AZI2603','2028-04-18',100,'2026-05-26'),(61,11,'IBU2602','2027-09-05',400,'2026-05-22'),(62,12,'MEL2605','2028-03-14',220,'2026-05-27'),(63,13,'CEL2602','2027-10-10',300,'2026-05-20'),(64,14,'VOL2603','2027-11-15',60,'2026-05-25'),(65,15,'OME2602','2028-05-01',1200,'2026-05-29'),(66,16,'ESO2604','2027-12-25',400,'2026-05-28'),(67,17,'PHO2602','2028-09-10',800,'2026-05-27'),(68,18,'GAV2603','2028-02-14',500,'2026-05-26'),(69,19,'MOT2602','2027-08-19',300,'2026-05-21'),(70,20,'BER2606','2028-12-20',1500,'2026-05-29'),(71,21,'SME2602','2028-01-05',450,'2026-05-24'),(72,22,'LOP2603','2027-05-12',200,'2026-05-18'),(73,23,'DUP2602','2027-10-30',160,'2026-05-19'),(74,24,'AML2604','2028-06-01',700,'2026-05-28'),(75,25,'LOS2602','2027-12-15',350,'2026-05-22'),(76,26,'CON2603','2028-03-20',400,'2026-05-25'),(77,27,'CRE2602','2028-07-14',300,'2026-05-26'),(78,28,'LIP2605','2028-01-10',250,'2026-05-24'),(79,29,'MET2602','2028-02-28',500,'2026-05-28'),(80,30,'DIA2603','2028-04-05',400,'2026-05-21'),(81,31,'SIN2602','2028-06-30',180,'2026-05-25'),(82,32,'VEN2604','2027-08-15',50,'2026-05-14'),(83,33,'SER2602','2027-10-22',40,'2026-05-18'),(84,34,'TEL2603','2028-03-12',300,'2026-05-26'),(85,35,'LOR2602','2027-11-20',600,'2026-05-27'),(86,36,'CET2605','2028-01-15',500,'2026-05-23'),(87,37,'PRO2602','2027-09-30',80,'2026-05-20'),(88,38,'ACE2603','2028-05-14',900,'2026-05-29'),(89,39,'EUG2602','2028-09-01',1500,'2026-05-28'),(90,40,'BOG2604','2028-03-15',700,'2026-05-22'),(91,41,'GIN2602','2028-04-10',300,'2026-05-12'),(92,41,'GIN2603','2028-10-05',500,'2026-05-29'),(93,42,'NEU2603','2028-02-20',400,'2026-05-25'),(94,43,'ENE2602','2027-10-15',600,'2026-05-24'),(95,44,'CAL2606','2028-01-22',250,'2026-05-26'),(96,45,'VIT2603','2028-08-30',2000,'2026-05-29'),(97,46,'ZIN2602','2028-03-11',300,'2026-05-20'),(98,47,'OTR2604','2027-09-15',100,'2026-05-28'),(99,48,'NAT2602','2028-11-15',500,'2026-05-29'),(100,49,'TOB2603','2028-04-25',120,'2026-05-27'),(101,50,'SYS2602','2028-06-10',90,'2026-05-24'),(102,51,'DIA2605','2027-11-20',150,'2026-05-19'),(103,52,'ROT2602','2028-05-15',500,'2026-05-28'),(104,53,'HAP2603','2028-02-28',450,'2026-05-22'),(105,54,'SAL2602','2027-09-12',300,'2026-05-25'),(106,55,'PRE2604','2028-04-01',600,'2026-05-26');
/*!40000 ALTER TABLE `medicine_batch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `title` text,
  `content` text,
  `is_read` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `path` text,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (6,114,'Đơn thuốc mới','Bác sĩ vừa kê đơn thuốc mới cho bạn. Vui lòng kiểm tra!',1,'2026-05-25 19:55:27','/api/secure/prescriptions/6'),(7,114,'Đơn thuốc mới','Bác sĩ vừa kê đơn thuốc mới cho bạn. Vui lòng kiểm tra!',1,'2026-05-25 20:04:18','/api/secure/prescriptions/7'),(8,114,'Đơn thuốc mới','Bác sĩ vừa kê đơn thuốc mới cho bạn. Vui lòng kiểm tra!',1,'2026-05-25 20:07:04','/api/secure/prescriptions/8'),(9,114,'Đơn thuốc mới','Bác sĩ vừa kê đơn thuốc mới cho bạn. Vui lòng kiểm tra!',1,'2026-05-30 10:39:07','/api/secure/prescriptions/9'),(10,114,'Đơn thuốc mới','Bác sĩ vừa kê đơn thuốc mới cho bạn. Vui lòng kiểm tra!',1,'2026-05-30 10:40:01','/api/secure/prescriptions/10'),(11,114,'Đơn thuốc mới','Bác sĩ vừa kê đơn thuốc mới cho bạn. Vui lòng kiểm tra!',1,'2026-05-30 18:13:05','/api/secure/prescriptions/11');
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cccd` char(12) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `dob` date NOT NULL,
  `gender` varchar(10) NOT NULL,
  `address` varchar(255) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `relationship` enum('SELF','PARENT','CHILD','SPOUSE','SIBLING','OTHER','GRANDPARENT') DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `is_active` tinyint DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `patient_ibfk_1` (`user_id`),
  CONSTRAINT `patient_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (10,'012345678001','Nguyễn Văn A','1990-05-15','Nam','123 Nguyễn Kiệm, Gò Vấp, TP.HCM','0901234567','SELF',NULL,1),(15,'089205019243','Gia Huy','2005-05-11','Nữ','HCM','0887951236','SELF',NULL,0),(16,'089205019243','Gia Huy','2005-05-11','Nam','HCM','0887951236','SELF',NULL,0),(20,'089205019243','Phan Gia Huy','2005-09-27','Nữ','Thpt Võ Thành Trinh','0772889159','SELF',NULL,1),(23,'083205012801','Nguyễn Dương Quốc Bảo','2005-04-20','Nam','vl','0848482273',NULL,135,NULL),(24,'048681550561','Phan Gia Huy','2005-10-12','Nam','Nhà bè','0775998746',NULL,114,NULL);
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `total_amount` decimal(10,2) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('PENDING','SUCCESS','FAILURE') NOT NULL DEFAULT 'PENDING',
  `method` enum('CASH','MOMO','VNPAY') DEFAULT NULL,
  `appointment_id` bigint NOT NULL,
  `paid_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_payment_appointment_idx` (`appointment_id`),
  CONSTRAINT `fk_appointment_payment` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,200000.00,'2026-05-29 00:00:00','PENDING',NULL,1,NULL),(5,300000.00,'2026-05-30 04:40:38','PENDING','MOMO',41,'2026-05-30 04:40:38'),(16,10000.00,'2026-05-30 06:25:17','PENDING',NULL,41,NULL),(17,10000.00,'2026-05-30 06:29:40','PENDING',NULL,41,NULL),(18,10000.00,'2026-05-30 06:31:12','PENDING',NULL,41,NULL);
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_items`
--

DROP TABLE IF EXISTS `payment_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `payment_id` bigint NOT NULL,
  `item_type` enum('APPOINTMENT','LAB_TEST','PRESCRIPTION') NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `reference_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_payment` (`payment_id`),
  CONSTRAINT `fk_payment` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_items`
--

LOCK TABLES `payment_items` WRITE;
/*!40000 ALTER TABLE `payment_items` DISABLE KEYS */;
INSERT INTO `payment_items` VALUES (1,1,'APPOINTMENT',200000.00,1),(5,5,'APPOINTMENT',300000.00,41),(13,16,'LAB_TEST',10000.00,2),(14,17,'LAB_TEST',10000.00,2),(15,18,'LAB_TEST',10000.00,2);
/*!40000 ALTER TABLE `payment_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescription`
--

DROP TABLE IF EXISTS `prescription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescription` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `medical_record_id` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` enum('DRAFT','PUBLIC','CANCELLED') DEFAULT 'DRAFT',
  `public_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `medical_record_id` (`medical_record_id`),
  CONSTRAINT `prescription_ibfk_1` FOREIGN KEY (`medical_record_id`) REFERENCES `medical_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription`
--

LOCK TABLES `prescription` WRITE;
/*!40000 ALTER TABLE `prescription` DISABLE KEYS */;
INSERT INTO `prescription` VALUES (11,2,'2026-05-30 18:13:05','PUBLIC','2026-05-30 18:13:05');
/*!40000 ALTER TABLE `prescription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescription_item`
--

DROP TABLE IF EXISTS `prescription_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescription_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `prescription_id` bigint DEFAULT NULL,
  `medicine_id` bigint DEFAULT NULL,
  `quantity` int NOT NULL,
  `days_to_use` int DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `prescription_id` (`prescription_id`),
  KEY `prescription_item_ibfk_2_idx` (`medicine_id`),
  CONSTRAINT `prescription_item_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`id`),
  CONSTRAINT `prescription_item_ibfk_2` FOREIGN KEY (`medicine_id`) REFERENCES `medicine` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription_item`
--

LOCK TABLES `prescription_item` WRITE;
/*!40000 ALTER TABLE `prescription_item` DISABLE KEYS */;
INSERT INTO `prescription_item` VALUES (28,11,49,1,7,''),(29,11,47,1,7,'');
/*!40000 ALTER TABLE `prescription_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refresh_token`
--

DROP TABLE IF EXISTS `refresh_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refresh_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(512) NOT NULL,
  `expiry_date` datetime NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `revoked` tinyint DEFAULT '1',
  `user_id` bigint NOT NULL,
  `device_id` varchar(100) DEFAULT NULL,
  `device_info` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rf_ibfk_1_idx` (`user_id`),
  CONSTRAINT `rf_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=209 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_token`
--

LOCK TABLES `refresh_token` WRITE;
/*!40000 ALTER TABLE `refresh_token` DISABLE KEYS */;
INSERT INTO `refresh_token` VALUES (147,'454665a8-817e-4257-878b-358e3a1b8509','2026-06-04 12:13:56','2026-05-28 12:13:56',1,97,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(148,'ca4d9ea6-3726-4de0-a0d3-272d99cfc256','2026-06-04 11:07:14','2026-05-28 11:07:14',0,97,'575ccaae-e256-413c-9c8d-ab93e3041848','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(149,'b7f4134f-3b64-4e86-8d0c-bd688d40f599','2026-06-04 11:06:42','2026-05-28 11:06:42',0,131,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(150,'17115ace-90ad-4534-9529-ec4238a14bc1','2026-06-04 12:14:08','2026-05-28 12:14:08',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(151,'63ec973a-28b7-4e53-9bf7-fc14e84235f0','2026-06-05 14:54:46','2026-05-29 14:54:46',1,135,'b53af933-bc2b-4daf-aa74-9237f70947dd','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(152,'1462daf6-9053-4821-85d7-34f29feae935','2026-06-05 15:01:07','2026-05-29 15:01:07',0,135,NULL,NULL),(153,'de8404a7-e53c-4bfe-a120-47c3affad26a','2026-06-05 14:54:46','2026-05-29 15:07:21',1,135,'b53af933-bc2b-4daf-aa74-9237f70947dd','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(154,'7ead1efd-5966-491f-b1f5-f889bbc5a799','2026-06-05 15:07:42','2026-05-29 15:07:42',1,135,'b53af933-bc2b-4daf-aa74-9237f70947dd','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(155,'1e80a633-9a36-4da3-9049-a0ffacdb87ff','2026-06-05 15:07:42','2026-05-29 15:18:56',0,135,'b53af933-bc2b-4daf-aa74-9237f70947dd','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(156,'927839dc-75cc-4f52-97b8-bdf09769a1a9','2026-06-06 02:10:43','2026-05-30 02:10:43',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(157,'adaa359e-9fd5-4b7b-bc42-8b8ad63e9e37','2026-06-06 02:11:50','2026-05-30 02:11:50',1,104,'7c3235be-c10e-4fc3-992b-21b42d87c5c4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(158,'aa2cabb4-503f-41e3-b8aa-c2b9782da23c','2026-06-06 02:15:54','2026-05-30 02:15:54',1,136,'7c3235be-c10e-4fc3-992b-21b42d87c5c4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(159,'cc41132f-3b18-49b2-8ed5-103f6612e9f9','2026-06-06 02:10:43','2026-05-30 02:20:58',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(160,'42666342-06c1-4469-9bbf-b89efe260e27','2026-06-06 02:10:43','2026-05-30 02:34:47',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(161,'0484ec21-6805-43b4-b9d0-f4bdb2fe3b9b','2026-06-06 02:39:05','2026-05-30 02:39:05',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(162,'9281fb1f-05be-4d46-b737-372a42f02837','2026-06-06 02:39:05','2026-05-30 02:50:10',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(163,'007e1202-6fd1-4e27-840e-d5037d6a4b5d','2026-06-06 02:53:12','2026-05-30 02:53:12',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(164,'a6933882-5236-4bb8-bb68-94589343eeed','2026-06-06 02:59:36','2026-05-30 02:59:36',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(165,'de15dfbd-1707-4d0b-b35b-c2f1611bd718','2026-06-06 02:59:36','2026-05-30 03:11:54',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(166,'3d2627c4-f1c3-4817-9b3d-7b696ebbbe94','2026-06-06 02:59:36','2026-05-30 03:22:54',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(167,'e25917e6-7f9a-40b3-bc0d-fd8d76ae258f','2026-06-06 02:59:36','2026-05-30 03:33:12',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(168,'66374117-7dad-4b89-acf2-d0486143f6d2','2026-06-06 02:59:36','2026-05-30 03:44:04',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(169,'e542e360-1a94-4e43-a7aa-487a55c3ab35','2026-06-06 02:59:36','2026-05-30 03:54:09',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(170,'6a62afe1-d1b3-4d1c-8705-d4ea21699213','2026-06-06 03:55:29','2026-05-30 03:55:29',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(171,'405a78cc-d217-4443-9990-291127e3a4c0','2026-06-06 02:59:36','2026-05-30 04:06:15',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(172,'3e40de91-5950-44c1-883d-dde810b64003','2026-06-06 02:59:36','2026-05-30 04:18:48',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(173,'fb51fe78-c678-419f-ae84-88c3e6585d2f','2026-06-06 02:59:36','2026-05-30 04:30:37',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(174,'343140d0-8964-46d5-bb08-dd07190a6be3','2026-06-06 02:59:36','2026-05-30 04:40:38',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(175,'e7fa1e4d-b267-4741-9695-7028eca874e0','2026-06-06 02:59:36','2026-05-30 04:51:47',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(176,'10abdc37-381e-48a1-81ae-e84e718692f8','2026-06-06 03:55:29','2026-05-30 04:53:48',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(177,'7e5f2b72-80bf-4408-82e2-e5fb95dc3a9a','2026-06-06 02:59:36','2026-05-30 05:04:42',1,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(178,'852106e7-15b6-4463-9e39-5e2baf3eb44a','2026-06-06 03:55:29','2026-05-30 05:04:49',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(179,'62ee9bd1-35a4-46b1-817b-14767f4a4b67','2026-06-06 05:05:07','2026-05-30 05:05:07',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(180,'5fb7350a-acbd-4983-82bc-fdb538f7d2ec','2026-06-06 05:05:07','2026-05-30 05:16:32',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(181,'d1e84730-a10b-435a-925e-ebda272dffb6','2026-06-06 05:05:07','2026-05-30 05:27:10',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(182,'0ca4b25c-a338-4392-8612-5f0a38a5d280','2026-06-06 05:05:07','2026-05-30 05:37:13',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(183,'78822bf8-a963-4be3-b6fd-b2e143897ea2','2026-06-06 05:05:07','2026-05-30 05:50:32',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(184,'4800532a-9efd-41fd-8e9a-7f959275f917','2026-06-06 05:05:07','2026-05-30 06:00:34',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(185,'1b7d0295-726f-4402-a732-60505346c314','2026-06-06 05:05:07','2026-05-30 06:12:52',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(186,'893bd8fb-f2dc-4a76-beef-69a9fefd208f','2026-06-06 05:05:07','2026-05-30 06:25:17',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(187,'c55f70a8-d765-4775-b949-706018107342','2026-06-06 02:59:36','2026-05-30 06:27:00',0,114,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(188,'be949245-b5a8-416b-96e3-3aa22e3593b1','2026-06-06 05:05:07','2026-05-30 06:35:16',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(189,'a7f657e2-8f65-49f2-b2e1-5d686854aa1c','2026-06-06 05:05:07','2026-05-30 06:45:23',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(190,'44f70279-5959-4de3-bc82-55b3d487a798','2026-06-06 05:05:07','2026-05-30 07:13:22',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(191,'183a846d-db70-4087-a112-5f98a08ec5b1','2026-06-06 05:05:07','2026-05-30 07:45:14',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(192,'37dbe37a-353b-484f-8836-7c5a2c3fca37','2026-06-06 05:05:07','2026-05-30 08:39:11',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(193,'0c431cbe-63a3-4682-9e9f-4930622dd4a7','2026-06-06 05:05:07','2026-05-30 08:52:28',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(194,'2ccc6f6c-883c-49af-8eb6-57bc1d7815de','2026-06-06 05:05:07','2026-05-30 09:03:36',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(195,'e7b5416a-f633-4355-9b0f-7b946ebc4164','2026-06-06 05:05:07','2026-05-30 09:13:51',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(196,'38eb5591-8037-4c3c-9eca-27e593b733c7','2026-06-06 05:05:07','2026-05-30 09:28:17',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(197,'9057ac2f-323d-43fa-baff-12ccb13b5025','2026-06-06 05:05:07','2026-05-30 09:55:58',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(198,'f91e7906-ab32-4c09-ad5d-f036f416f4f3','2026-06-06 05:05:07','2026-05-30 10:07:07',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(199,'698c6aac-ce45-4242-aef3-26a7603ea3ee','2026-06-06 05:05:07','2026-05-30 10:17:19',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(200,'eb92cb2d-7bf1-4f47-bb5f-9502392ab9b0','2026-06-06 05:05:07','2026-05-30 10:28:04',1,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(201,'6046c091-c9fc-4e39-a8ca-f707dd685385','2026-06-06 05:05:07','2026-05-30 10:38:06',0,104,'a5accf82-c44f-42e1-82a1-8bb02764c31e','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(202,'792c7462-b809-413c-9ab6-65288541e2af','2026-06-06 16:23:39','2026-05-30 16:23:39',1,104,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(203,'19f0051c-ac09-4fad-8f2f-9d36d2d5045d','2026-06-06 16:23:39','2026-05-30 16:33:41',1,104,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(204,'4c549edb-5456-46bb-8b87-d4ee2b222292','2026-06-06 16:23:39','2026-05-30 17:11:58',1,104,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(205,'202f3bf7-ba18-4f41-9db9-0f71ded89341','2026-06-06 16:23:39','2026-05-30 17:26:31',1,104,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(206,'04e259bb-b2b8-42a0-a57e-a3b6924fc384','2026-06-06 16:23:39','2026-05-30 17:36:55',1,104,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(207,'f2dc19de-9876-4970-8931-77edeada7b03','2026-06-06 16:23:39','2026-05-30 18:04:52',0,104,'82c85628-bef7-4535-853d-682d681ab2a4','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36'),(208,'9f905eb1-ae40-4262-a34a-bde3769c7d50','2026-06-06 18:12:27','2026-05-30 18:12:27',0,114,'4280c17b-55c2-4eaf-846d-7c842fabbd7f','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36');
/*!40000 ALTER TABLE `refresh_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `room_number` varchar(20) DEFAULT NULL,
  `area_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_area_clinic` (`area_id`),
  CONSTRAINT `fk_area_clinic` FOREIGN KEY (`area_id`) REFERENCES `areas` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES (3,'A103',NULL),(4,'A104',NULL),(5,'A105',NULL),(6,'A201',7),(7,'A202',NULL),(8,'A203',NULL),(9,'A204',NULL),(10,'A205',NULL),(11,'B101',NULL),(12,'B102',NULL),(13,'B103',NULL),(14,'B104',NULL),(15,'B105',NULL),(16,'B201',NULL),(17,'B202',NULL),(18,'B203',NULL),(19,'B204',NULL),(20,'B205',NULL),(21,'C101',NULL),(22,'C102',NULL),(23,'C103',NULL),(24,'C104',NULL),(25,'C105',NULL),(26,'C201',NULL),(27,'C202',NULL),(28,'C203',NULL),(29,'C204',NULL),(30,'C205',NULL),(31,'D101',7),(32,'D102',7),(33,'D103',7),(34,'D104',7),(35,'D105',7),(36,'D201',NULL),(37,'D202',NULL),(38,'D203',NULL),(39,'D204',NULL),(40,'D205',NULL),(41,'A101',7);
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedules`
--

DROP TABLE IF EXISTS `schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedules` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `max_patients` int DEFAULT NULL,
  `current_patients` int DEFAULT NULL,
  `doctor_id` bigint DEFAULT NULL,
  `shift_id` bigint DEFAULT NULL,
  `room_id` bigint DEFAULT NULL,
  `specialty_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_doctor_schedule` (`doctor_id`),
  KEY `fk_shift_schedule` (`shift_id`),
  KEY `fk_room_schedule` (`room_id`),
  KEY `fk_specialty_schedule_idx` (`specialty_id`),
  CONSTRAINT `fk_doctor_schedule` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_room_schedule` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_shift_schedule` FOREIGN KEY (`shift_id`) REFERENCES `shifts` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_specialty_schedule` FOREIGN KEY (`specialty_id`) REFERENCES `specialty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedules`
--

LOCK TABLES `schedules` WRITE;
/*!40000 ALTER TABLE `schedules` DISABLE KEYS */;
INSERT INTO `schedules` VALUES (2,'2026-09-27',6,1,13,1,6,1),(3,'2026-09-27',6,1,4,2,6,1),(4,'2026-09-27',6,1,1,3,6,1),(5,'2026-09-27',6,1,1,9,6,1),(6,'2026-09-27',6,1,1,4,6,1),(7,'2026-09-27',6,1,1,5,6,1),(8,'2026-09-27',6,2,1,6,6,1),(9,'2026-09-27',6,1,1,7,6,1),(10,'2026-09-27',6,1,1,8,6,1),(11,'2026-06-01',5,1,18,10,8,1);
/*!40000 ALTER TABLE `schedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shifts`
--

DROP TABLE IF EXISTS `shifts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shifts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `session` enum('MORNING','AFTERNOON') DEFAULT NULL,
  `max_patients` int DEFAULT NULL,
  `min_patients` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shifts`
--

LOCK TABLES `shifts` WRITE;
/*!40000 ALTER TABLE `shifts` DISABLE KEYS */;
INSERT INTO `shifts` VALUES (1,'16:58:00','11:59:00','MORNING',10,2),(2,'08:30:00','09:30:00','MORNING',10,2),(3,'09:30:00','10:30:00','MORNING',10,2),(4,'10:30:00','11:30:00','MORNING',10,2),(5,'11:30:00','12:30:00','MORNING',10,2),(6,'13:00:00','14:00:00','AFTERNOON',10,2),(7,'14:00:00','15:00:00','AFTERNOON',10,2),(8,'15:00:00','16:00:00','AFTERNOON',10,2),(9,'16:00:00','17:00:00','AFTERNOON',10,2),(10,'17:00:00','18:00:00','AFTERNOON',10,2),(11,'13:00:00','13:30:00','AFTERNOON',10,2),(12,'13:00:00','13:30:00','MORNING',12,1);
/*!40000 ALTER TABLE `shifts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `social_account`
--

DROP TABLE IF EXISTS `social_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `social_account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `provider` enum('GOOGLE','FACEBOOK') DEFAULT NULL,
  `provider_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_socialacc` (`user_id`),
  CONSTRAINT `fk_user_socialacc` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `social_account`
--

LOCK TABLES `social_account` WRITE;
/*!40000 ALTER TABLE `social_account` DISABLE KEYS */;
INSERT INTO `social_account` VALUES (1,131,'GOOGLE','106810516165711821867'),(2,132,'GOOGLE','113026225977569138629');
/*!40000 ALTER TABLE `social_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specialty`
--

DROP TABLE IF EXISTS `specialty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `specialty` (
  `id` bigint NOT NULL,
  `name` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `id_hod` bigint DEFAULT NULL,
  `is_active` tinyint DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_hod_UNIQUE` (`id_hod`),
  KEY `fk_specialty_hod` (`id_hod`),
  CONSTRAINT `fk_specialty_hod` FOREIGN KEY (`id_hod`) REFERENCES `doctor` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specialty`
--

LOCK TABLES `specialty` WRITE;
/*!40000 ALTER TABLE `specialty` DISABLE KEYS */;
INSERT INTO `specialty` VALUES (1,'Tim mạch',300000.00,1,1),(2,'Nội khoa',150000.00,4,1),(3,'Ngoại khoa',250000.00,7,1),(4,'Nhi khoa',200000.00,10,1),(5,'Da liễu',200000.00,24,1),(6,'Thần kinh',300000.00,16,1),(7,'Sản phụ khoa',250000.00,19,1),(8,'Răng Hàm Mặt',150000.00,22,1),(9,'Tai Mũi Họng',150000.00,25,1);
/*!40000 ALTER TABLE `specialty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specialty_doctor`
--

DROP TABLE IF EXISTS `specialty_doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `specialty_doctor` (
  `doctor_id` bigint NOT NULL,
  `specialty_id` bigint NOT NULL,
  PRIMARY KEY (`doctor_id`,`specialty_id`),
  KEY `fk_specialty_idx` (`specialty_id`),
  CONSTRAINT `fk_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`),
  CONSTRAINT `fk_specialty` FOREIGN KEY (`specialty_id`) REFERENCES `specialty` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specialty_doctor`
--

LOCK TABLES `specialty_doctor` WRITE;
/*!40000 ALTER TABLE `specialty_doctor` DISABLE KEYS */;
INSERT INTO `specialty_doctor` VALUES (13,1),(18,1),(18,2),(18,7);
/*!40000 ALTER TABLE `specialty_doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(100) NOT NULL,
  `role` enum('ROLE_ADMIN','ROLE_DOCTOR','ROLE_PATIENT') DEFAULT 'ROLE_PATIENT',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `secure_url` varchar(255) DEFAULT NULL,
  `public_id` varchar(255) DEFAULT NULL,
  `fcm_token` text,
  `is_active` tinyint DEFAULT '1',
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (25,'admin@gmail.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','admin','ROLE_ADMIN','2026-01-10 08:50:00','https://res.cloudinary.com/dyojjvkxz/image/upload/v1779864972/clinic/avatar/hpuyxvpr90n1ksnreafv.png','clinic/avatar/hpuyxvpr90n1ksnreafv',NULL,1,NULL),(97,'doctor11@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_tham_k4','ROLE_DOCTOR','2026-04-19 14:20:01','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,'fwZwNMro0V65tq-_DjS7x0:APA91bFRzsBXBF1WMaAurFctEtMcWaXTbyF9Xi2xmazqVAD2H6VQessIteqUgUaATNWtdauYh2giJb5Hq2CWuEmTaxHzJMYL8idtDki-oL2BnA94FOE3LUo',0,NULL),(99,'doctor13@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_nam_k5','ROLE_DOCTOR','2026-04-19 14:20:01','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,'eE430dVQXCHhp7zA_Dq_qq:APA91bFi9tN5aVkwaFTVWrD4yMCGTeksHXz49V3E-tHU2idWLrY1CXmqG9Dw_G4-dHtiABK3pHGW9465dqk4XLNeE4DdupWFJ818fxCoyhv8A-LFpmh02-Q',1,NULL),(104,'doctor18@clinic.com','$2a$10$IGf43XgOnglTXpBhceQiqOD40..ZNTFICG4OcbCD/IYmJzjL2SmZO','dr_hoang_k6','ROLE_DOCTOR','2026-04-19 14:20:01','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,'fwZwNMro0V65tq-_DjS7x0:APA91bFRzsBXBF1WMaAurFctEtMcWaXTbyF9Xi2xmazqVAD2H6VQessIteqUgUaATNWtdauYh2giJb5Hq2CWuEmTaxHzJMYL8idtDki-oL2BnA94FOE3LUo',1,NULL),(105,'doctor19@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_nguyet_k7','ROLE_DOCTOR','2026-04-19 14:20:01','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,NULL,0,NULL),(107,'doctor21@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_hanh_k7','ROLE_DOCTOR','2026-04-19 14:20:01',NULL,NULL,NULL,0,NULL),(108,'doctor22@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_hai_k8','ROLE_DOCTOR','2026-04-19 14:20:01','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,NULL,0,NULL),(109,'doctor23@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_nhan_k8','ROLE_DOCTOR','2026-04-19 14:20:01',NULL,NULL,NULL,1,NULL),(110,'doctor24@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_son_k8','ROLE_DOCTOR','2026-04-19 14:20:01',NULL,NULL,NULL,1,NULL),(112,'doctor26@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_ky_k9','ROLE_DOCTOR','2026-04-19 14:20:01','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,NULL,1,NULL),(113,'doctor27@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_ngoan_k9','ROLE_DOCTOR','2026-04-19 14:20:01',NULL,NULL,NULL,1,NULL),(114,'nguyenvana@gmail.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','vana_patient','ROLE_PATIENT','2026-04-19 14:27:19','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,'fwZwNMro0V65tq-_DjS7x0:APA91bFRzsBXBF1WMaAurFctEtMcWaXTbyF9Xi2xmazqVAD2H6VQessIteqUgUaATNWtdauYh2giJb5Hq2CWuEmTaxHzJMYL8idtDki-oL2BnA94FOE3LUo',0,NULL),(130,'92005@gmail.com','$2a$10$J71C166fmbG9txvSe1d/C.Vs5tpXwMMld97DR1Q82cz9velJjGigW','vana_patient22','ROLE_PATIENT','2026-05-26 10:44:44','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,NULL,NULL,NULL),(131,'huyphan2792005@gmail.com','$2a$10$kAx45nXhZAK7SJCQ8UXAWuORvhMtWlPNdewwbQKAbR1XaAAGejYlu','huyphan2792005@gmail.com','ROLE_PATIENT','2026-05-26 12:46:09','https://lh3.googleusercontent.com/a/ACg8ocL0rmykL8Kwh3FR3V-6h-kusKOxdDOTi_pimgpN6hEUgBXo4qHU=s96-c',NULL,NULL,NULL,'Huy Phan'),(132,'2351050061huy@ou.edu.vn','$2a$10$KV.M0r7vLy28WlcPisw4fO1y.QkHO6xAjY7h/0cipmP5gqoIs0wS2','2351050061huy@ou.edu.vn','ROLE_PATIENT','2026-05-26 12:57:37','https://lh3.googleusercontent.com/a/ACg8ocI2JfdGm5bO8ZfbqaE-3Ht2N_4u95HcGbBKlvntkMxOrCPnG3U=s96-c',NULL,NULL,NULL,'HUY PHAN GIA'),(133,'ph9@gmail.com','$2a$10$GnqHybdPoeyJTnzz52kPMeyXNm5MWGDBdTnHpXJb6tp5rpJN8Izp.','giahuy11','ROLE_PATIENT','2026-05-27 06:32:24','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,NULL,1,NULL),(134,'huypha@gmail.com','$2a$10$z5NIk/eb79segbLkfuA3ke012I9ZL/25mFVFPKlVLh0sTH0zpEkAu','huy','ROLE_PATIENT','2026-05-27 07:05:40','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,NULL,1,'Phan Gia Huy'),(135,'baon6172@gmail.com','$2a$10$900xZipY9K0RGNMXLmXIoOatSWhpWAVYrdN7o1UAVMWEKpomFOM6q','ndqbao','ROLE_PATIENT','2026-05-29 03:20:26','https://res.cloudinary.com/dyojjvkxz/image/upload/v1780024771/clinic/avatar/i9bww8r2cozlyoahkddg.webp','clinic/avatar/i9bww8r2cozlyoahkddg','eE430dVQXCHhp7zA_Dq_qq:APA91bFi9tN5aVkwaFTVWrD4yMCGTeksHXz49V3E-tHU2idWLrY1CXmqG9Dw_G4-dHtiABK3pHGW9465dqk4XLNeE4DdupWFJ818fxCoyhv8A-LFpmh02-Q',NULL,NULL),(136,'drhung@clinic.com','$2a$10$BahM4XTpY8gArceDDhsTyOTSsIpIxCXSFBxaTTGdVY.MFxynoTpqa','dr_hung','ROLE_PATIENT','2026-05-29 03:26:47','https://res.cloudinary.com/dyojjvkxz/image/upload/v1780025151/clinic/avatar/j78dnef7gh54njig30mn.jpg','clinic/avatar/j78dnef7gh54njig30mn',NULL,NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-31  1:25:17
