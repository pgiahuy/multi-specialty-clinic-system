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
  `status` enum('PENDING','CONFIRMED','IN_PROGRESS','COMPLETED','CANCELLED') DEFAULT 'PENDING',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `schedule_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_appointment_patient` (`patient_id`),
  KEY `appointment_ibfk_2_idx` (`schedule_id`),
  CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`),
  CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`schedule_id`) REFERENCES `schedules` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES (8,10,'PENDING','2026-05-07 16:26:56',2),(9,10,'PENDING','2026-05-07 16:30:02',2),(10,10,'PENDING','2026-05-07 19:40:38',2),(11,10,'PENDING','2026-05-07 19:40:54',2),(12,10,'PENDING','2026-05-07 19:41:09',2),(13,10,'PENDING','2026-05-07 19:41:10',2),(14,10,'PENDING','2026-05-07 19:41:42',3),(15,10,'PENDING','2026-05-07 19:41:43',3),(16,10,'COMPLETED','2026-05-07 19:41:54',1),(17,10,'PENDING','2026-05-07 19:41:56',1),(18,10,'PENDING','2026-05-07 19:41:56',1),(19,10,'PENDING','2026-05-07 19:42:00',3),(20,16,'COMPLETED','2026-05-07 19:49:52',3),(21,10,'PENDING','2026-05-07 19:49:53',3),(22,10,'PENDING','2026-05-07 19:49:54',3),(23,10,'PENDING','2026-05-07 19:50:00',1),(24,10,'PENDING','2026-05-07 19:50:01',1),(25,15,'COMPLETED','2026-05-07 19:50:02',1);
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
INSERT INTO `areas` VALUES (2,'Khu A',2),(3,'Khu B',1),(7,'Khu D',1),(8,'Khu D',2);
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  CONSTRAINT `doctor_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

LOCK TABLES `doctor` WRITE;
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
INSERT INTO `doctor` VALUES (1,'Nguyễn Văn Hùng','Trưởng khoa Tim mạch',NULL,'Nam'),(2,'Lê Thị Mai','Bác sĩ chuyên khoa',NULL,'Nữ'),(3,'Trần Quốc Bảo','Bác sĩ chuyên khoa',NULL,'Nam'),(4,'Hoàng Văn Thắng','Trưởng khoa Nội khoa',NULL,'Nam'),(7,'Trịnh Công Sơn','Trưởng khoa Ngoại khoa',NULL,'Nam'),(8,'Nguyễn Thu Trang','Bác sĩ chuyên khoa',NULL,'Nữ'),(9,'Đặng Văn Lâm','Bác sĩ chuyên khoa',NULL,'Nam'),(10,'Nguyễn Minh Hải','Trưởng khoa Nhi khoa',NULL,'Nam'),(11,'Trần Thị Thắm','Bác sĩ chuyên khoa',97,'Nữ'),(13,'Nguyễn Hoàng Nam','Trưởng khoa Da liễu',99,'Nam'),(16,'Lý Văn Phúc','Trưởng khoa Thần kinh',NULL,'Nam'),(18,'Trần Văn Hoàng','Bác sĩ chuyên khoa',104,'Nam'),(19,'Nguyễn Thị Minh Nguyệt','Trưởng khoa Sản',105,'Nữ'),(21,'Lê Hồng Hạnh','Bác sĩ chuyên khoa',107,'Nữ'),(22,'Lâm Vĩnh Hải','Trưởng khoa Răng Hàm Mặt',108,'Nam'),(23,'Trương Mỹ Nhân','Bác sĩ chuyên khoa',109,'Nữ'),(24,'Cao Thái Sơn','Bác sĩ chuyên khoa',110,'Nam'),(25,'Dương Triệu Vũ','Trưởng khoa Tai Mũi Họng',NULL,'Nam'),(26,'Lý Nhã Kỳ','Bác sĩ chuyên khoa',112,'Nữ'),(27,'Quách Ngọc Ngoan','Bác sĩ chuyên khoa',113,'Nam');
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventorylog`
--

DROP TABLE IF EXISTS `inventorylog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventorylog` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `medicine_id` bigint DEFAULT NULL,
  `batch_id` bigint DEFAULT NULL,
  `change_amount` int NOT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reference_id` int DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `medicine_id` (`medicine_id`),
  KEY `batch_id` (`batch_id`),
  CONSTRAINT `inventorylog_ibfk_1` FOREIGN KEY (`medicine_id`) REFERENCES `medicine` (`id`),
  CONSTRAINT `inventorylog_ibfk_2` FOREIGN KEY (`batch_id`) REFERENCES `medicinebatch` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventorylog`
--

LOCK TABLES `inventorylog` WRITE;
/*!40000 ALTER TABLE `inventorylog` DISABLE KEYS */;
/*!40000 ALTER TABLE `inventorylog` ENABLE KEYS */;
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
  `patient_id` bigint DEFAULT NULL,
  `test_id` int DEFAULT NULL,
  `result_value` varchar(50) DEFAULT NULL,
  `is_abnormal` tinyint(1) DEFAULT NULL,
  `pdf_url` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_res_appointment` (`appointment_id`),
  KEY `fk_res_patient` (`patient_id`),
  KEY `fk_res_test` (`test_id`),
  CONSTRAINT `fk_res_appointment` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_res_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_res_test` FOREIGN KEY (`test_id`) REFERENCES `lab_tests` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_results`
--

LOCK TABLES `lab_results` WRITE;
/*!40000 ALTER TABLE `lab_results` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_tests`
--

LOCK TABLES `lab_tests` WRITE;
/*!40000 ALTER TABLE `lab_tests` DISABLE KEYS */;
INSERT INTO `lab_tests` VALUES (1,'Tổng phân tích tế bào máu (CBC)','G/L','4.0 - 10.0',NULL),(2,'Định lượng Hemoglobin','g/dL','12.0 - 16.5',NULL),(3,'Tốc độ lắng máu (ESR)','mm/h','0 - 20',NULL),(4,'Đường huyết lúc đói (Glucose)','mmol/L','3.9 - 6.4',NULL),(5,'Định lượng Creatinine (Chức năng thận)','umol/L','62 - 120',NULL),(6,'Định lượng Ure','mmol/L','2.5 - 7.5',NULL),(7,'Men gan AST (SGOT)','U/L','0 - 40',NULL),(8,'Men gan ALT (SGPT)','U/L','0 - 41',NULL),(9,'Định lượng Albumin','g/L','35 - 50',NULL),(10,'Cholesterol toàn phần','mmol/L','3.9 - 5.2',NULL),(11,'Triglyceride','mmol/L','0.46 - 1.88',NULL),(12,'HDL-Cholesterol (Mỡ tốt)','mmol/L','> 0.9',NULL),(13,'LDL-Cholesterol (Mỡ xấu)','mmol/L','< 3.4',NULL),(14,'Định lượng TSH','uIU/mL','0.27 - 4.2',NULL),(15,'Định lượng Free T4 (FT4)','pmol/L','12 - 22',NULL),(16,'Định lượng HbA1c','%','4.0 - 6.0',NULL),(17,'Tổng phân tích nước tiểu (10 thông số)','N/A','Âm tính',NULL),(18,'Định lượng Axit Uric (Tầm soát Gout)','umol/L','208 - 428',NULL),(19,'Định lượng Calci toàn phần','mmol/L','2.15 - 2.55',NULL),(20,'Xét nghiệm Viêm gan B (HBsAg)','S/CO','< 1.0 (Âm tính)',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medical_record`
--

LOCK TABLES `medical_record` WRITE;
/*!40000 ALTER TABLE `medical_record` DISABLE KEYS */;
INSERT INTO `medical_record` VALUES (1,8,'Viêm đại tràng','Hạn chế ăn cay nóng','2026-05-08 16:05:33');
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicine`
--

LOCK TABLES `medicine` WRITE;
/*!40000 ALTER TABLE `medicine` DISABLE KEYS */;
/*!40000 ALTER TABLE `medicine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicinebatch`
--

DROP TABLE IF EXISTS `medicinebatch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicinebatch` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `medicine_id` bigint DEFAULT NULL,
  `batch_code` varchar(50) NOT NULL,
  `expiry_date` date NOT NULL,
  `quantity` int NOT NULL,
  `import_date` date DEFAULT (curdate()),
  PRIMARY KEY (`id`),
  KEY `medicine_id` (`medicine_id`),
  CONSTRAINT `medicinebatch_ibfk_1` FOREIGN KEY (`medicine_id`) REFERENCES `medicine` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicinebatch`
--

LOCK TABLES `medicinebatch` WRITE;
/*!40000 ALTER TABLE `medicinebatch` DISABLE KEYS */;
/*!40000 ALTER TABLE `medicinebatch` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,114,'Đơn thuốc mới','Bác sĩ vừa kê đơn thuốc mới cho bạn. Vui lòng kiểm tra!',1,'2026-05-09 15:29:32','/api/secure/prescriptions/1');
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
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `patient_ibfk_1` (`user_id`),
  CONSTRAINT `patient_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (10,'012345678001','Nguyễn Văn A','1990-05-15','Nam','123 Nguyễn Kiệm, Gò Vấp, TP.HCM','0901234567',114),(15,'089205019243','Gia Huy','2005-05-11','Nữ','HCM','0887951236',114),(16,'089205019243','Gia Huy','2005-05-11','Nam','HCM','0887951236',114);
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
  `patient_id` bigint NOT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `status` enum('PENDING','SUCCESS','FAILURE') DEFAULT 'PENDING',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `method` enum('CASH','MOMO','VNPAY') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_payments_patient` (`patient_id`),
  CONSTRAINT `fk_payments_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,10,200000.00,'SUCCESS','2026-04-23 17:00:00',NULL);
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
  `appointment_id` bigint DEFAULT NULL,
  `lab_test_id` int DEFAULT NULL,
  `prescription_id` bigint DEFAULT NULL,
  `item_type` enum('APPOINTMENT','LAB_TEST','PRESCRIPTION') DEFAULT NULL,
  `amount` decimal(10,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('PENDING','SUCCESS','FAILURE') DEFAULT 'PENDING',
  `method` enum('CASH','MOMO','VNPAY') DEFAULT NULL,
  `paid_at` timestamp NULL DEFAULT NULL,
  `trans_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_payment` (`payment_id`),
  KEY `fk_item_appointment` (`appointment_id`),
  KEY `fk_item_prescription` (`prescription_id`),
  KEY `fk_payment_items_lab_test` (`lab_test_id`),
  CONSTRAINT `fk_item_appointment` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_item_prescription` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_payment` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_payment_items_lab_test` FOREIGN KEY (`lab_test_id`) REFERENCES `lab_tests` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_items`
--

LOCK TABLES `payment_items` WRITE;
/*!40000 ALTER TABLE `payment_items` DISABLE KEYS */;
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `medical_record_id` (`medical_record_id`),
  CONSTRAINT `prescription_ibfk_1` FOREIGN KEY (`medical_record_id`) REFERENCES `medical_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription`
--

LOCK TABLES `prescription` WRITE;
/*!40000 ALTER TABLE `prescription` DISABLE KEYS */;
INSERT INTO `prescription` VALUES (1,1,'2026-05-09 15:29:32',NULL);
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
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `prescription_id` (`prescription_id`),
  KEY `prescription_item_ibfk_2_idx` (`medicine_id`),
  CONSTRAINT `prescription_item_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`id`),
  CONSTRAINT `prescription_item_ibfk_2` FOREIGN KEY (`medicine_id`) REFERENCES `medicine` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription_item`
--

LOCK TABLES `prescription_item` WRITE;
/*!40000 ALTER TABLE `prescription_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `prescription_item` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES (1,'A101',NULL),(2,'A102',NULL),(3,'A103',NULL),(4,'A104',NULL),(5,'A105',NULL),(6,'A201',2),(7,'A202',2),(8,'A203',2),(9,'A204',2),(10,'A205',2),(11,'B101',3),(12,'B102',3),(13,'B103',3),(14,'B104',3),(15,'B105',3),(16,'B201',NULL),(17,'B202',NULL),(18,'B203',NULL),(19,'B204',NULL),(20,'B205',NULL),(21,'C101',NULL),(22,'C102',NULL),(23,'C103',NULL),(24,'C104',NULL),(25,'C105',NULL),(26,'C201',NULL),(27,'C202',NULL),(28,'C203',NULL),(29,'C204',NULL),(30,'C205',NULL),(31,'D101',7),(32,'D102',7),(33,'D103',7),(34,'D104',7),(35,'D105',7),(36,'D201',8),(37,'D202',8),(38,'D203',8),(39,'D204',8),(40,'D205',8);
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedules`
--

LOCK TABLES `schedules` WRITE;
/*!40000 ALTER TABLE `schedules` DISABLE KEYS */;
INSERT INTO `schedules` VALUES (1,'2026-09-27',6,6,1,7,6,1),(2,'2026-09-27',6,6,1,1,6,1),(3,'2026-09-27',6,6,1,2,6,NULL),(4,'2026-09-27',6,0,1,3,6,NULL),(5,'2026-09-27',6,0,1,9,6,NULL),(6,'2026-09-27',6,0,1,4,6,NULL),(7,'2026-09-27',6,0,1,5,6,NULL),(8,'2026-09-27',6,0,1,6,6,NULL),(9,'2026-09-27',6,0,1,7,6,NULL),(10,'2026-09-27',6,0,1,8,6,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shifts`
--

LOCK TABLES `shifts` WRITE;
/*!40000 ALTER TABLE `shifts` DISABLE KEYS */;
INSERT INTO `shifts` VALUES (1,'07:30:00','08:30:00','MORNING',10,2),(2,'08:30:00','09:30:00','MORNING',10,2),(3,'09:30:00','10:30:00','MORNING',10,2),(4,'10:30:00','11:30:00','MORNING',10,2),(5,'11:30:00','12:30:00','MORNING',10,2),(6,'13:00:00','14:00:00','AFTERNOON',10,2),(7,'14:00:00','15:00:00','AFTERNOON',10,2),(8,'15:00:00','16:00:00','AFTERNOON',10,2),(9,'16:00:00','17:00:00','AFTERNOON',10,2),(10,'17:00:00','18:00:00','AFTERNOON',10,2);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `social_account`
--

LOCK TABLES `social_account` WRITE;
/*!40000 ALTER TABLE `social_account` DISABLE KEYS */;
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_hod_UNIQUE` (`id_hod`),
  KEY `fk_specialty_hod` (`id_hod`),
  CONSTRAINT `fk_specialty_hod` FOREIGN KEY (`id_hod`) REFERENCES `doctor` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specialty`
--

LOCK TABLES `specialty` WRITE;
/*!40000 ALTER TABLE `specialty` DISABLE KEYS */;
INSERT INTO `specialty` VALUES (1,'Tim mạch',300000.00,1),(2,'Nội khoa',150000.00,4),(3,'Ngoại khoa',250000.00,7),(4,'Nhi khoa',200000.00,10),(5,'Da liễu',200000.00,13),(6,'Thần kinh',300000.00,16),(7,'Sản phụ khoa',250000.00,19),(8,'Răng Hàm Mặt',150000.00,22),(9,'Tai Mũi Họng',150000.00,25);
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
INSERT INTO `specialty_doctor` VALUES (1,1),(1,2);
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (25,'admin@gmail.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','admin','ROLE_ADMIN','2026-01-10 08:50:00','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp','clinic/avatar/wb9pa7iiwb1nuvhnoh5b',NULL),(97,'doctor11@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_tham_k4','ROLE_DOCTOR','2026-04-19 14:20:01','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,NULL),(99,'doctor13@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_nam_k5','ROLE_DOCTOR','2026-04-19 14:20:01','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,NULL),(104,'doctor18@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_hoang_k6','ROLE_DOCTOR','2026-04-19 14:20:01','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,NULL),(105,'doctor19@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_nguyet_k7','ROLE_DOCTOR','2026-04-19 14:20:01','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,NULL),(107,'doctor21@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_hanh_k7','ROLE_DOCTOR','2026-04-19 14:20:01',NULL,NULL,NULL),(108,'doctor22@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_hai_k8','ROLE_DOCTOR','2026-04-19 14:20:01','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,NULL),(109,'doctor23@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_nhan_k8','ROLE_DOCTOR','2026-04-19 14:20:01',NULL,NULL,NULL),(110,'doctor24@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_son_k8','ROLE_DOCTOR','2026-04-19 14:20:01',NULL,NULL,NULL),(112,'doctor26@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_ky_k9','ROLE_DOCTOR','2026-04-19 14:20:01','https://res.cloudinary.com/dyojjvkxz/image/upload/v1778723190/clinic/avatar/pe1o4c8dihmawxrtz6nx.webp',NULL,NULL),(113,'doctor27@clinic.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','dr_ngoan_k9','ROLE_DOCTOR','2026-04-19 14:20:01',NULL,NULL,NULL),(114,'nguyenvana@gmail.com','$2a$12$v3ZvboJ17qle0mQkqhrYN.x/5X2/7.NyOqHVjJRhbrE6kC16ykwqO','vana_patient','ROLE_PATIENT','2026-04-19 14:27:19',NULL,NULL,'fwZwNMro0V65tq-_DjS7x0:APA91bFRzsBXBF1WMaAurFctEtMcWaXTbyF9Xi2xmazqVAD2H6VQessIteqUgUaATNWtdauYh2giJb5Hq2CWuEmTaxHzJMYL8idtDki-oL2BnA94FOE3LUo');
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

-- Dump completed on 2026-05-25 17:29:09
