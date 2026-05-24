-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: agro_erp
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `account_ledger`
--

DROP TABLE IF EXISTS `account_ledger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_ledger` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `credit_amount` decimal(14,2) NOT NULL,
  `debit_amount` decimal(14,2) NOT NULL,
  `narration` varchar(255) DEFAULT NULL,
  `reference_id` bigint DEFAULT NULL,
  `reference_type` varchar(255) DEFAULT NULL,
  `running_balance` decimal(14,2) NOT NULL,
  `transaction_date` date NOT NULL,
  `account_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_account_ledger_date` (`transaction_date`,`account_id`),
  KEY `FKfqswg3nihr9i15biebabvra83` (`account_id`),
  CONSTRAINT `FKfqswg3nihr9i15biebabvra83` FOREIGN KEY (`account_id`) REFERENCES `chart_of_accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_ledger`
--

LOCK TABLES `account_ledger` WRITE;
/*!40000 ALTER TABLE `account_ledger` DISABLE KEYS */;
INSERT INTO `account_ledger` VALUES (1,_binary '','2026-05-19 03:50:32.735662','admin',_binary '\0','2026-05-19 03:50:32.735662','admin',0.00,1351.35,'Accounts receivable for INV-20260519-1002',1,'INVOICE',1351.35,'2026-05-19',2),(2,_binary '','2026-05-19 03:50:32.739663','admin',_binary '\0','2026-05-19 03:50:32.739663','admin',1351.35,0.00,'Sales revenue for INV-20260519-1002',1,'INVOICE',-1351.35,'2026-05-19',3),(3,_binary '','2026-05-19 06:32:20.147799','admin',_binary '\0','2026-05-19 06:32:20.147799','admin',0.00,67567.50,'Accounts receivable for INV-20260519-1003',2,'INVOICE',68918.85,'2026-05-19',2),(4,_binary '','2026-05-19 06:32:20.155575','admin',_binary '\0','2026-05-19 06:32:20.155575','admin',67567.50,0.00,'Sales revenue for INV-20260519-1003',2,'INVOICE',-68918.85,'2026-05-19',3),(5,_binary '','2026-05-19 13:48:08.027951','admin',_binary '\0','2026-05-19 13:48:08.027951','admin',0.00,539.55,'Accounts receivable for INV-20260519-1004',3,'INVOICE',69458.40,'2026-05-19',2),(6,_binary '','2026-05-19 13:48:08.032544','admin',_binary '\0','2026-05-19 13:48:08.032544','admin',539.55,0.00,'Sales revenue for INV-20260519-1004',3,'INVOICE',-69458.40,'2026-05-19',3),(7,_binary '','2026-05-19 15:50:46.729357','admin',_binary '\0','2026-05-19 15:50:46.729357','admin',0.00,83790.00,'Accounts receivable for INV-20260519-1005',4,'INVOICE',153248.40,'2026-05-19',2),(8,_binary '','2026-05-19 15:50:46.733357','admin',_binary '\0','2026-05-19 15:50:46.733357','admin',83790.00,0.00,'Sales revenue for INV-20260519-1005',4,'INVOICE',-153248.40,'2026-05-19',3),(9,_binary '','2026-05-19 16:27:10.933600','admin',_binary '\0','2026-05-19 16:27:10.933600','admin',0.00,1351.35,'Cash received MR-20260519-1001',1,'PAYMENT',1351.35,'2026-05-19',1),(10,_binary '','2026-05-19 16:27:10.937600','admin',_binary '\0','2026-05-19 16:27:10.937600','admin',1351.35,0.00,'Receivable settled MR-20260519-1001',1,'PAYMENT',151897.05,'2026-05-19',2),(11,_binary '','2026-05-19 17:11:51.380460','admin',_binary '\0','2026-05-19 17:11:51.380460','admin',0.00,1351.35,'Accounts receivable for INV-20260519-1006',5,'INVOICE',153248.40,'2026-05-19',2),(12,_binary '','2026-05-19 17:11:51.386460','admin',_binary '\0','2026-05-19 17:11:51.386460','admin',1351.35,0.00,'Sales revenue for INV-20260519-1006',5,'INVOICE',-154599.75,'2026-05-19',3),(13,_binary '','2026-05-19 17:16:42.559165','admin',_binary '\0','2026-05-19 17:16:42.559165','admin',0.00,1351.35,'Cash received MR-20260519-1002',2,'PAYMENT',2702.70,'2026-05-19',1),(14,_binary '','2026-05-19 17:16:42.562354','admin',_binary '\0','2026-05-19 17:16:42.562354','admin',1351.35,0.00,'Receivable settled MR-20260519-1002',2,'PAYMENT',151897.05,'2026-05-19',2);
/*!40000 ALTER TABLE `account_ledger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accounting_entries`
--

DROP TABLE IF EXISTS `accounting_entries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounting_entries` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `amount` decimal(16,2) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `entry_type` enum('CREDIT','DEBIT') NOT NULL,
  `material_document_number` varchar(50) NOT NULL,
  `posting_date` date NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_accounting_entry_product_date` (`product_id`,`posting_date`),
  KEY `idx_accounting_entry_document` (`material_document_number`),
  CONSTRAINT `FKdtlwrfqqf0wk7o4ro79lhm4a8` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounting_entries`
--

LOCK TABLES `accounting_entries` WRITE;
/*!40000 ALTER TABLE `accounting_entries` DISABLE KEYS */;
INSERT INTO `accounting_entries` VALUES (1,_binary '','2026-05-19 10:58:26.172025','admin',_binary '\0','2026-05-19 10:58:26.172025','admin',3410.00,'Inventory Asset debit for price increase','DEBIT','PRC-20260519-1001','2026-05-19',9),(2,_binary '','2026-05-19 10:58:26.174024','admin',_binary '\0','2026-05-19 10:58:26.174024','admin',3410.00,'Price Variance credit for price increase','CREDIT','PRC-20260519-1001','2026-05-19',9),(3,_binary '','2026-05-19 10:58:36.151017','admin',_binary '\0','2026-05-19 10:58:36.151017','admin',3410.00,'Inventory Asset credit for price decrease','CREDIT','PRC-20260519-1002','2026-05-19',9),(4,_binary '','2026-05-19 10:58:36.152017','admin',_binary '\0','2026-05-19 10:58:36.152017','admin',3410.00,'Price Variance debit for price decrease','DEBIT','PRC-20260519-1002','2026-05-19',9),(5,_binary '','2026-05-19 13:44:38.636979','admin',_binary '\0','2026-05-19 13:44:38.636979','admin',53300.00,'Inventory Asset debit for price increase','DEBIT','PRC-20260519-1001','2026-05-19',9),(6,_binary '','2026-05-19 13:44:38.638982','admin',_binary '\0','2026-05-19 13:44:38.638982','admin',53300.00,'Price Variance credit for price increase','CREDIT','PRC-20260519-1001','2026-05-19',9);
/*!40000 ALTER TABLE `accounting_entries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_logs`
--

DROP TABLE IF EXISTS `audit_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `action_name` varchar(255) DEFAULT NULL,
  `details` varchar(1000) DEFAULT NULL,
  `module_name` varchar(255) DEFAULT NULL,
  `record_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_logs`
--

LOCK TABLES `audit_logs` WRITE;
/*!40000 ALTER TABLE `audit_logs` DISABLE KEYS */;
INSERT INTO `audit_logs` VALUES (1,_binary '','2026-05-19 03:50:32.684539','admin',_binary '\0','2026-05-19 03:50:32.684539','admin','CREATE','ORD-20260519-1001','Order',1),(2,_binary '','2026-05-19 03:50:32.704209','admin',_binary '\0','2026-05-19 03:50:32.704209','admin','APPROVE','ORD-20260519-1001','Order',1),(3,_binary '','2026-05-19 03:50:32.740933','admin',_binary '\0','2026-05-19 03:50:32.740933','admin','GENERATE','INV-20260519-1002','Invoice',1),(4,_binary '','2026-05-19 06:25:30.448233','admin',_binary '\0','2026-05-19 06:25:30.448233','admin','CREATE','ORD-20260519-1002','Order',3),(5,_binary '','2026-05-19 06:31:39.712472','admin',_binary '\0','2026-05-19 06:31:39.712472','admin','CREATE','ORD-20260519-1003','Order',4),(6,_binary '','2026-05-19 06:31:59.507572','admin',_binary '\0','2026-05-19 06:31:59.507572','admin','APPROVE','ORD-20260519-1003','Order',4),(7,_binary '','2026-05-19 06:32:20.156121','admin',_binary '\0','2026-05-19 06:32:20.156121','admin','GENERATE','INV-20260519-1003','Invoice',2),(8,_binary '','2026-05-19 06:33:43.213167','admin',_binary '\0','2026-05-19 06:33:43.213167','admin','CREATE','DLV-20260519-1001','Delivery',1),(9,_binary '','2026-05-19 11:19:29.134058','admin',_binary '\0','2026-05-19 11:19:29.134058','admin','CREATE','ORD-20260519-1004','Order',5),(10,_binary '','2026-05-19 11:20:04.720527','admin',_binary '\0','2026-05-19 11:20:04.720527','admin','APPROVE','ORD-20260519-1004','Order',5),(11,_binary '','2026-05-19 11:22:31.762191','admin',_binary '\0','2026-05-19 11:22:31.762191','admin','CREATE','RTL-004','Retailer',4),(12,_binary '','2026-05-19 11:22:58.212773','admin',_binary '\0','2026-05-19 11:22:58.212773','admin','CREATE','ORD-20260519-1005','Order',6),(13,_binary '','2026-05-19 13:46:56.730627','admin',_binary '\0','2026-05-19 13:46:56.730627','admin','CREATE','ORD-20260519-1006','Order',7),(14,_binary '','2026-05-19 13:47:13.194633','admin',_binary '\0','2026-05-19 13:47:13.194633','admin','CREATE','ORD-20260519-1007','Order',8),(15,_binary '','2026-05-19 13:47:14.094180','admin',_binary '\0','2026-05-19 13:47:14.094180','admin','CREATE','ORD-20260519-1008','Order',9),(16,_binary '','2026-05-19 13:47:14.924363','admin',_binary '\0','2026-05-19 13:47:14.924363','admin','CREATE','ORD-20260519-1009','Order',10),(17,_binary '','2026-05-19 13:47:32.386644','admin',_binary '\0','2026-05-19 13:47:32.386644','admin','APPROVE','ORD-20260519-1009','Order',10),(18,_binary '','2026-05-19 13:48:08.033544','admin',_binary '\0','2026-05-19 13:48:08.033544','admin','GENERATE','INV-20260519-1004','Invoice',3),(19,_binary '','2026-05-19 13:49:14.629009','admin',_binary '\0','2026-05-19 13:49:14.629009','admin','CREATE','DLV-20260519-1002','Delivery',2),(20,_binary '','2026-05-19 15:50:46.735358','admin',_binary '\0','2026-05-19 15:50:46.735358','admin','GENERATE','INV-20260519-1005','Invoice',4),(21,_binary '','2026-05-19 16:27:10.940737','admin',_binary '\0','2026-05-19 16:27:10.940737','admin','RECEIVE','MR-20260519-1001','Payment',1),(22,_binary '','2026-05-19 16:33:52.986039','admin',_binary '\0','2026-05-19 16:33:52.986039','admin','UPDATE','PRD-BONEMEAL-001','Product',9),(23,_binary '','2026-05-19 17:10:34.152859','admin',_binary '\0','2026-05-19 17:10:34.152859','admin','CREATE','ORD-20260519-1010','Order',11),(24,_binary '','2026-05-19 17:11:26.427952','admin',_binary '\0','2026-05-19 17:11:26.427952','admin','APPROVE','ORD-20260519-1010','Order',11),(25,_binary '','2026-05-19 17:11:51.387595','admin',_binary '\0','2026-05-19 17:11:51.387595','admin','GENERATE','INV-20260519-1006','Invoice',5),(26,_binary '','2026-05-19 17:14:26.849794','admin',_binary '\0','2026-05-19 17:14:26.849794','admin','CREATE','DLV-20260519-1003','Delivery',3),(27,_binary '','2026-05-19 17:16:42.563629','admin',_binary '\0','2026-05-19 17:16:42.563629','admin','RECEIVE','MR-20260519-1002','Payment',2);
/*!40000 ALTER TABLE `audit_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `brands`
--

DROP TABLE IF EXISTS `brands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brands` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `name` varchar(80) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKoce3937d2f4mpfqrycbr0l93m` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brands`
--

LOCK TABLES `brands` WRITE;
/*!40000 ALTER TABLE `brands` DISABLE KEYS */;
INSERT INTO `brands` VALUES (1,_binary '','2026-05-18 03:25:43.227785','system',_binary '\0','2026-05-18 03:25:43.227785','system','Agro Prime'),(2,_binary '','2026-05-18 15:55:37.389376','system',_binary '\0','2026-05-18 15:55:37.389376','system','Bangla Agro'),(3,_binary '','2026-05-18 15:55:37.393375','system',_binary '\0','2026-05-18 15:55:37.393375','system','Green Care');
/*!40000 ALTER TABLE `brands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `name` varchar(80) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKt8o6pivur7nn124jehx7cygw5` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,_binary '','2026-05-18 03:25:43.221785','system',_binary '\0','2026-05-18 03:25:43.221785','system','Fertilizer'),(2,_binary '','2026-05-18 15:55:37.342375','system',_binary '\0','2026-05-18 15:55:37.342375','system','Crop Protection'),(3,_binary '','2026-05-18 15:55:37.347375','system',_binary '\0','2026-05-18 15:55:37.347375','system','Organic & Bio Product');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chart_of_accounts`
--

DROP TABLE IF EXISTS `chart_of_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chart_of_accounts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `account_code` varchar(40) NOT NULL,
  `account_name` varchar(160) NOT NULL,
  `account_type` enum('ASSET','EQUITY','EXPENSE','INCOME','LIABILITY') NOT NULL,
  `system_account` bit(1) NOT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK62a4crs7s8ypnqi9u76qtc0du` (`account_code`),
  KEY `FKt1046gd7mgo0v7rdnh6aa3per` (`parent_id`),
  CONSTRAINT `FKt1046gd7mgo0v7rdnh6aa3per` FOREIGN KEY (`parent_id`) REFERENCES `chart_of_accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chart_of_accounts`
--

LOCK TABLES `chart_of_accounts` WRITE;
/*!40000 ALTER TABLE `chart_of_accounts` DISABLE KEYS */;
INSERT INTO `chart_of_accounts` VALUES (1,_binary '','2026-05-18 15:55:37.538679','system',_binary '\0','2026-05-18 15:55:37.538679','system','1000','Cash and Bank','ASSET',_binary '',NULL),(2,_binary '','2026-05-18 15:55:37.542680','system',_binary '\0','2026-05-18 15:55:37.542680','system','1100','Accounts Receivable','ASSET',_binary '',NULL),(3,_binary '','2026-05-18 15:55:37.545679','system',_binary '\0','2026-05-18 15:55:37.545679','system','4000','Sales Revenue','INCOME',_binary '',NULL),(4,_binary '','2026-05-18 15:55:37.548679','system',_binary '\0','2026-05-18 15:55:37.548679','system','5000','Discount Allowed','EXPENSE',_binary '',NULL),(5,_binary '','2026-05-18 15:55:37.550679','system',_binary '\0','2026-05-18 15:55:37.550679','system','2100','VAT Payable','LIABILITY',_binary '',NULL);
/*!40000 ALTER TABLE `chart_of_accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `companies`
--

DROP TABLE IF EXISTS `companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `companies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `address` varchar(500) DEFAULT NULL,
  `company_code` varchar(40) NOT NULL,
  `company_name` varchar(160) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile_number` varchar(255) DEFAULT NULL,
  `tax_registration_no` varchar(255) DEFAULT NULL,
  `trade_license_no` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6yrvsg7p3v2e969nfmvbo9440` (`company_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companies`
--

LOCK TABLES `companies` WRITE;
/*!40000 ALTER TABLE `companies` DISABLE KEYS */;
INSERT INTO `companies` VALUES (1,_binary '','2026-05-18 15:55:37.273377','system',_binary '\0','2026-05-18 15:55:37.273377','system','Head Office','AGRO-001','Agro Business Ltd.','info@agroerp.local','01700000000',NULL,NULL);
/*!40000 ALTER TABLE `companies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deliveries`
--

DROP TABLE IF EXISTS `deliveries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `deliveries` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `delivery_address` varchar(255) DEFAULT NULL,
  `delivery_date` date NOT NULL,
  `delivery_number` varchar(40) NOT NULL,
  `delivery_person` varchar(255) DEFAULT NULL,
  `status` enum('CANCELLED','DELIVERED','IN_TRANSIT','PARTIALLY_DELIVERED','PENDING','READY_FOR_DELIVERY') NOT NULL,
  `vehicle_details` varchar(255) DEFAULT NULL,
  `invoice_id` bigint NOT NULL,
  `retailer_id` bigint NOT NULL,
  `delivery_contact_no` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `route_name` varchar(255) DEFAULT NULL,
  `transport_cost` decimal(14,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK667lysacjsrsb1r6ppswku1rx` (`delivery_number`),
  KEY `FKfu6ys5ephat6ixk6l1huap9td` (`invoice_id`),
  KEY `FKj4j32ct274dgjqriryiyjmne3` (`retailer_id`),
  CONSTRAINT `FKfu6ys5ephat6ixk6l1huap9td` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`),
  CONSTRAINT `FKj4j32ct274dgjqriryiyjmne3` FOREIGN KEY (`retailer_id`) REFERENCES `retailers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deliveries`
--

LOCK TABLES `deliveries` WRITE;
/*!40000 ALTER TABLE `deliveries` DISABLE KEYS */;
INSERT INTO `deliveries` VALUES (1,_binary '','2026-05-19 06:33:43.201107','admin',_binary '\0','2026-05-19 06:33:43.201107','admin','Rahman Agro Traders','2026-05-19','DLV-20260519-1001','test','DELIVERED','test',2,1,'test','','test',100.00),(2,_binary '','2026-05-19 13:49:14.616009','admin',_binary '\0','2026-05-19 13:49:14.616009','admin','Rahman Agro Traders','2026-05-19','DLV-20260519-1002','test','DELIVERED','dac',3,1,'0144','challan','rangpur',100.00),(3,_binary '','2026-05-19 17:14:26.829729','admin',_binary '\0','2026-05-19 17:14:26.829729','admin','Rahman Agro Traders','2026-05-19','DLV-20260519-1003','','DELIVERED','',5,1,'','','',0.00);
/*!40000 ALTER TABLE `deliveries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery_items`
--

DROP TABLE IF EXISTS `delivery_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `delivered_quantity` decimal(14,3) NOT NULL,
  `pending_quantity` decimal(14,3) NOT NULL,
  `delivery_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtrb7ybqtdhmfsqfw5csgg5muy` (`delivery_id`),
  KEY `FKqvm3lh0lexb88fc5k46pfc62v` (`product_id`),
  CONSTRAINT `FKqvm3lh0lexb88fc5k46pfc62v` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKtrb7ybqtdhmfsqfw5csgg5muy` FOREIGN KEY (`delivery_id`) REFERENCES `deliveries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery_items`
--

LOCK TABLES `delivery_items` WRITE;
/*!40000 ALTER TABLE `delivery_items` DISABLE KEYS */;
INSERT INTO `delivery_items` VALUES (1,_binary '','2026-05-19 06:33:43.204163','admin',_binary '\0','2026-05-19 06:33:43.204163','admin',50.000,0.000,1,1),(2,_binary '','2026-05-19 13:49:14.620008','admin',_binary '\0','2026-05-19 13:49:14.620008','admin',1.000,0.000,2,9),(3,_binary '','2026-05-19 17:14:26.834729','admin',_binary '\0','2026-05-19 17:14:26.834729','admin',1.000,0.000,3,1);
/*!40000 ALTER TABLE `delivery_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_items`
--

DROP TABLE IF EXISTS `invoice_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `line_total` decimal(14,2) NOT NULL,
  `quantity` decimal(14,3) NOT NULL,
  `unit_price` decimal(14,2) NOT NULL,
  `invoice_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK46ae0lhu1oqs7cv91fn6y9n7w` (`invoice_id`),
  KEY `FKs3tu9gmkgshq8oeq5n0rinxeu` (`product_id`),
  CONSTRAINT `FK46ae0lhu1oqs7cv91fn6y9n7w` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`),
  CONSTRAINT `FKs3tu9gmkgshq8oeq5n0rinxeu` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_items`
--

LOCK TABLES `invoice_items` WRITE;
/*!40000 ALTER TABLE `invoice_items` DISABLE KEYS */;
INSERT INTO `invoice_items` VALUES (1,_binary '','2026-05-19 03:50:32.722617','admin',_binary '\0','2026-05-19 03:50:32.722617','admin',1351.35,1.000,1300.00,1,1),(2,_binary '','2026-05-19 06:32:20.128238','admin',_binary '\0','2026-05-19 06:32:20.128238','admin',67567.50,50.000,1300.00,2,1),(3,_binary '','2026-05-19 13:48:08.013130','admin',_binary '\0','2026-05-19 13:48:08.013130','admin',539.55,1.000,545.00,3,9),(4,_binary '','2026-05-19 15:50:46.722118','admin',_binary '\0','2026-05-19 15:50:46.722118','admin',83790.00,42.000,2000.00,4,2),(5,_binary '','2026-05-19 17:11:51.363479','admin',_binary '\0','2026-05-19 17:11:51.363479','admin',1351.35,1.000,1300.00,5,1);
/*!40000 ALTER TABLE `invoice_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `due_amount` decimal(14,2) NOT NULL,
  `invoice_date` date NOT NULL,
  `invoice_number` varchar(40) NOT NULL,
  `net_amount` decimal(14,2) NOT NULL,
  `paid_amount` decimal(14,2) NOT NULL,
  `status` enum('CANCELLED','PAID','PARTIALLY_PAID','PENDING') NOT NULL,
  `order_id` bigint NOT NULL,
  `retailer_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKl1x55mfsay7co0r3m9ynvipd5` (`invoice_number`),
  UNIQUE KEY `UKe718q5klx5pempy28p2nx88a6` (`order_id`),
  KEY `FKe2dnlo17ts16c2o6nnh31j65k` (`retailer_id`),
  CONSTRAINT `FK4ko3y00tkkk2ya3p6wnefjj2f` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKe2dnlo17ts16c2o6nnh31j65k` FOREIGN KEY (`retailer_id`) REFERENCES `retailers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
INSERT INTO `invoices` VALUES (1,_binary '','2026-05-19 03:50:32.720615','admin',_binary '\0','2026-05-19 16:27:10.941983','admin',0.00,'2026-05-19','INV-20260519-1002',1351.35,1351.35,'PAID',1,1),(2,_binary '','2026-05-19 06:32:20.123235','admin',_binary '\0','2026-05-19 06:32:20.123235','admin',67567.50,'2026-05-19','INV-20260519-1003',67567.50,0.00,'PENDING',4,1),(3,_binary '','2026-05-19 13:48:08.009130','admin',_binary '\0','2026-05-19 13:48:08.009130','admin',539.55,'2026-05-19','INV-20260519-1004',539.55,0.00,'PENDING',10,1),(4,_binary '','2026-05-19 15:50:46.721111','admin',_binary '\0','2026-05-19 15:50:46.721111','admin',83790.00,'2026-05-19','INV-20260519-1005',83790.00,0.00,'PENDING',5,2),(5,_binary '','2026-05-19 17:11:51.359053','admin',_binary '\0','2026-05-19 17:16:42.571135','admin',0.00,'2026-05-19','INV-20260519-1006',1351.35,1351.35,'PAID',11,1);
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `material_price_history`
--

DROP TABLE IF EXISTS `material_price_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `material_price_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `current_discount_percent` decimal(8,2) NOT NULL,
  `current_purchase_price` decimal(14,2) NOT NULL,
  `current_retailer_price` decimal(14,2) NOT NULL,
  `current_sales_price` decimal(14,2) NOT NULL,
  `current_vat_percent` decimal(8,2) NOT NULL,
  `posting_date` date NOT NULL,
  `previous_discount_percent` decimal(8,2) NOT NULL,
  `previous_purchase_price` decimal(14,2) NOT NULL,
  `previous_retailer_price` decimal(14,2) NOT NULL,
  `previous_sales_price` decimal(14,2) NOT NULL,
  `previous_vat_percent` decimal(8,2) NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_price_history_product_date` (`product_id`,`posting_date`),
  CONSTRAINT `FK1hfs63up2xc7ejdrr7ww9etgw` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `material_price_history`
--

LOCK TABLES `material_price_history` WRITE;
/*!40000 ALTER TABLE `material_price_history` DISABLE KEYS */;
INSERT INTO `material_price_history` VALUES (1,_binary '','2026-05-19 04:47:19.320143','admin',_binary '\0','2026-05-19 04:47:19.320143','admin',1.00,240.00,341.00,361.00,0.00,'2026-05-19',1.00,240.00,340.00,360.00,0.00,'Price history verification',9),(2,_binary '','2026-05-19 04:47:30.653894','admin',_binary '\0','2026-05-19 04:47:30.653894','admin',1.00,240.00,340.00,360.00,0.00,'2026-05-19',1.00,240.00,341.00,361.00,0.00,'Restore verification price',9),(3,_binary '','2026-05-19 10:39:09.343730','admin',_binary '\0','2026-05-19 10:39:09.343730','admin',5.00,1400.00,2000.00,1700.00,5.00,'2026-05-19',1.00,1400.00,1650.00,1700.00,5.00,'update price ',2);
/*!40000 ALTER TABLE `material_price_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `discount_amount` decimal(14,2) NOT NULL,
  `line_total` decimal(14,2) NOT NULL,
  `quantity` decimal(14,3) NOT NULL,
  `unit_price` decimal(14,2) NOT NULL,
  `vat_amount` decimal(14,2) NOT NULL,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  KEY `FKocimc7dtr037rh4ls4l95nlfi` (`product_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,_binary '','2026-05-19 03:50:32.681539','admin',_binary '\0','2026-05-19 03:50:32.681539','admin',13.00,1351.35,1.000,1300.00,64.35,1,1),(2,_binary '','2026-05-19 06:25:30.445233','admin',_binary '\0','2026-05-19 06:25:30.445233','admin',3.40,336.60,1.000,340.00,0.00,3,9),(3,_binary '','2026-05-19 06:31:39.711474','admin',_binary '\0','2026-05-19 06:31:39.711474','admin',650.00,67567.50,50.000,1300.00,3217.50,4,1),(4,_binary '','2026-05-19 11:19:29.129937','admin',_binary '\0','2026-05-19 11:19:29.129937','admin',4200.00,83790.00,42.000,2000.00,3990.00,5,2),(5,_binary '','2026-05-19 11:22:58.211856','admin',_binary '\0','2026-05-19 11:22:58.211856','admin',100.00,1995.00,1.000,2000.00,95.00,6,2),(6,_binary '','2026-05-19 13:46:56.729629','admin',_binary '\0','2026-05-19 13:46:56.729629','admin',5.45,539.55,1.000,545.00,0.00,7,9),(7,_binary '','2026-05-19 13:47:13.193634','admin',_binary '\0','2026-05-19 13:47:13.193634','admin',5.45,539.55,1.000,545.00,0.00,8,9),(8,_binary '','2026-05-19 13:47:14.092181','admin',_binary '\0','2026-05-19 13:47:14.092181','admin',5.45,539.55,1.000,545.00,0.00,9,9),(9,_binary '','2026-05-19 13:47:14.923362','admin',_binary '\0','2026-05-19 13:47:14.923362','admin',5.45,539.55,1.000,545.00,0.00,10,9),(10,_binary '','2026-05-19 17:10:34.149857','admin',_binary '\0','2026-05-19 17:10:34.149857','admin',13.00,1351.35,1.000,1300.00,64.35,11,1);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `discount_amount` decimal(14,2) NOT NULL,
  `gross_amount` decimal(14,2) NOT NULL,
  `net_amount` decimal(14,2) NOT NULL,
  `order_date` date NOT NULL,
  `order_number` varchar(40) NOT NULL,
  `status` enum('APPROVED','CANCELLED','DELIVERED','DRAFT','INVOICED','PENDING','RETURNED') NOT NULL,
  `vat_amount` decimal(14,2) NOT NULL,
  `retailer_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnthkiu7pgmnqnu86i2jyoe2v7` (`order_number`),
  KEY `FK30usn9hlqlv9vbj83ocmuwd5q` (`retailer_id`),
  CONSTRAINT `FK30usn9hlqlv9vbj83ocmuwd5q` FOREIGN KEY (`retailer_id`) REFERENCES `retailers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,_binary '','2026-05-19 03:50:32.670540','admin',_binary '\0','2026-05-19 03:50:32.741931','admin',13.00,1300.00,1351.35,'2026-05-19','ORD-20260519-1001','INVOICED',64.35,1),(3,_binary '','2026-05-19 06:25:30.431337','admin',_binary '\0','2026-05-19 06:25:30.431337','admin',3.40,340.00,336.60,'2026-05-19','ORD-20260519-1002','PENDING',0.00,1),(4,_binary '','2026-05-19 06:31:39.709472','admin',_binary '\0','2026-05-19 06:33:43.215311','admin',650.00,65000.00,67567.50,'2026-05-19','ORD-20260519-1003','DELIVERED',3217.50,1),(5,_binary '','2026-05-19 11:19:29.117937','admin',_binary '\0','2026-05-19 15:50:46.736357','admin',4200.00,84000.00,83790.00,'2026-05-19','ORD-20260519-1004','INVOICED',3990.00,2),(6,_binary '','2026-05-19 11:22:58.207856','admin',_binary '\0','2026-05-19 11:22:58.207856','admin',100.00,2000.00,1995.00,'2026-05-19','ORD-20260519-1005','PENDING',95.00,4),(7,_binary '','2026-05-19 13:46:56.728008','admin',_binary '\0','2026-05-19 13:46:56.728008','admin',5.45,545.00,539.55,'2026-05-19','ORD-20260519-1006','PENDING',0.00,1),(8,_binary '','2026-05-19 13:47:13.191632','admin',_binary '\0','2026-05-19 13:47:13.191632','admin',5.45,545.00,539.55,'2026-05-19','ORD-20260519-1007','PENDING',0.00,1),(9,_binary '','2026-05-19 13:47:14.091180','admin',_binary '\0','2026-05-19 13:47:14.091180','admin',5.45,545.00,539.55,'2026-05-19','ORD-20260519-1008','PENDING',0.00,1),(10,_binary '','2026-05-19 13:47:14.922362','admin',_binary '\0','2026-05-19 13:49:14.631097','admin',5.45,545.00,539.55,'2026-05-19','ORD-20260519-1009','DELIVERED',0.00,1),(11,_binary '','2026-05-19 17:10:34.141847','admin',_binary '\0','2026-05-19 17:14:26.853800','admin',13.00,1300.00,1351.35,'2026-05-19','ORD-20260519-1010','DELIVERED',64.35,1);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `amount` decimal(14,2) NOT NULL,
  `approved` bit(1) NOT NULL,
  `payment_date` date NOT NULL,
  `payment_method` enum('ADJUSTMENT','BANK','CASH','CHEQUE','MOBILE_BANKING','ONLINE_PAYMENT') NOT NULL,
  `receipt_number` varchar(40) NOT NULL,
  `reference_number` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `invoice_id` bigint DEFAULT NULL,
  `retailer_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKu6rnuxne864s4rh7qgeql1vx` (`receipt_number`),
  KEY `FKrbqec6be74wab8iifh8g3i50i` (`invoice_id`),
  KEY `FKnq0vt5wqiyn7ishvv956qqued` (`retailer_id`),
  CONSTRAINT `FKnq0vt5wqiyn7ishvv956qqued` FOREIGN KEY (`retailer_id`) REFERENCES `retailers` (`id`),
  CONSTRAINT `FKrbqec6be74wab8iifh8g3i50i` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,_binary '','2026-05-19 16:27:10.910411','admin',_binary '\0','2026-05-19 16:27:10.910411','admin',1351.35,_binary '','2026-05-19','BANK','MR-20260519-1001','bank','paod',1,1),(2,_binary '','2026-05-19 17:16:42.526968','admin',_binary '\0','2026-05-19 17:16:42.526968','admin',1351.35,_binary '','2026-05-19','MOBILE_BANKING','MR-20260519-1002','bkash','01774254988',5,1);
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `price_change_history`
--

DROP TABLE IF EXISTS `price_change_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `price_change_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `change_date_time` datetime(6) NOT NULL,
  `change_reason` varchar(500) DEFAULT NULL,
  `changed_by` varchar(100) DEFAULT NULL,
  `condition_summary` varchar(1000) DEFAULT NULL,
  `inventory_value_after` decimal(16,2) NOT NULL,
  `inventory_value_before` decimal(16,2) NOT NULL,
  `inventory_value_change` decimal(16,2) NOT NULL,
  `material_document_number` varchar(50) DEFAULT NULL,
  `new_price` decimal(14,2) NOT NULL,
  `old_price` decimal(14,2) NOT NULL,
  `requested_price` decimal(14,2) NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_price_change_product_time` (`product_id`,`change_date_time`),
  CONSTRAINT `FKe8ti1txhgfa8r8qpwnf6fs6m1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `price_change_history`
--

LOCK TABLES `price_change_history` WRITE;
/*!40000 ALTER TABLE `price_change_history` DISABLE KEYS */;
INSERT INTO `price_change_history` VALUES (1,_binary '','2026-05-19 10:58:26.160025','admin',_binary '\0','2026-05-19 10:58:26.160025','admin','2026-05-19 10:58:26.152024','?????? ?????? ?????','codex','[SURCHARGE:2:test surcharge, DISCOUNT:1:test discount]',108810.00,105400.00,3410.00,'PRC-20260519-1001',351.00,340.00,350.00,9),(2,_binary '','2026-05-19 10:58:36.150019','admin',_binary '\0','2026-05-19 10:58:36.150019','admin','2026-05-19 10:58:36.149017','Restore verification price','codex','',105400.00,108810.00,-3410.00,'PRC-20260519-1002',340.00,351.00,340.00,9),(3,_binary '','2026-05-19 10:58:36.172209','admin',_binary '\0','2026-05-19 10:58:36.172209','admin','2026-05-19 10:58:36.172209','Bulk no-op verification','codex','',105400.00,105400.00,0.00,'PRC-20260519-1003',340.00,340.00,340.00,9),(4,_binary '','2026-05-19 13:44:38.631979','admin',_binary '\0','2026-05-19 13:44:38.631979','admin','2026-05-19 13:44:38.628980','Material price posting','system','[SURCHARGE:50:Surcharge condition, DISCOUNT:5:Discount condition]',141700.00,88400.00,53300.00,'PRC-20260519-1001',545.00,340.00,500.00,9);
/*!40000 ALTER TABLE `price_change_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `batch_number` varchar(255) DEFAULT NULL,
  `discount_percent` decimal(8,2) NOT NULL,
  `expiry_date` date DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `pack_size` varchar(255) DEFAULT NULL,
  `product_code` varchar(50) NOT NULL,
  `product_name` varchar(180) NOT NULL,
  `purchase_price` decimal(14,2) NOT NULL,
  `reorder_level` decimal(14,2) NOT NULL,
  `retailer_price` decimal(14,2) NOT NULL,
  `sales_price` decimal(14,2) NOT NULL,
  `vat_percent` decimal(8,2) NOT NULL,
  `brand_id` bigint DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `unit_id` bigint DEFAULT NULL,
  `material_type` enum('FINISHED_PRODUCTS','RAW_MATERIALS','SEMIFINISHED_PRODUCTS','TRADING_PRODUCT') NOT NULL,
  `sub_category_id` bigint DEFAULT NULL,
  `current_inventory_value` decimal(16,2) NOT NULL DEFAULT '0.00',
  `current_price` decimal(14,2) NOT NULL DEFAULT '0.00',
  `total_stock_quantity` decimal(14,3) NOT NULL DEFAULT '0.000',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK922x4t23nx64422orei4meb2y` (`product_code`),
  KEY `FKa3a4mpsfdf4d2y6r8ra3sc8mv` (`brand_id`),
  KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
  KEY `FKeex0i50vfsa5imebrfdiyhmp9` (`unit_id`),
  KEY `FKno5p9kcr384tg56cbk8l9l6h2` (`sub_category_id`),
  CONSTRAINT `FKa3a4mpsfdf4d2y6r8ra3sc8mv` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`),
  CONSTRAINT `FKeex0i50vfsa5imebrfdiyhmp9` FOREIGN KEY (`unit_id`) REFERENCES `units` (`id`),
  CONSTRAINT `FKno5p9kcr384tg56cbk8l9l6h2` FOREIGN KEY (`sub_category_id`) REFERENCES `sub_categories` (`id`),
  CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,_binary '','2026-05-18 15:55:37.444375','system',_binary '\0','2026-05-18 15:55:37.444375','system','UREA-2026-A',1.00,NULL,NULL,'50 KG Bag','PRD-UREA-001','Urea Fertilizer - Nitrogen based fertilizer for fast leaf and stem growth.',1100.00,50.00,1300.00,1350.00,5.00,1,1,6,'TRADING_PRODUCT',1,0.00,0.00,0.000),(2,_binary '','2026-05-18 15:55:37.454376','system',_binary '\0','2026-05-19 10:39:09.363040','admin','TSP-2026-A',5.00,NULL,NULL,'50 KG Bag','PRD-TSP-001','TSP Triple Super Phosphate - Phosphorus fertilizer for stronger root development.',1400.00,50.00,2000.00,1700.00,5.00,2,1,6,'TRADING_PRODUCT',1,0.00,0.00,0.000),(3,_binary '','2026-05-18 15:55:37.461471','system',_binary '\0','2026-05-18 15:55:37.461471','system','DAP-2026-A',1.00,NULL,NULL,'50 KG Bag','PRD-DAP-001','DAP Di-Ammonium Phosphate - Combined nitrogen and phosphorus fertilizer for balanced crop nutrition.',1550.00,50.00,1850.00,1900.00,5.00,2,1,6,'TRADING_PRODUCT',1,0.00,0.00,0.000),(4,_binary '','2026-05-18 15:55:37.467468','system',_binary '\0','2026-05-18 15:55:37.467468','system','MOP-2026-A',1.00,NULL,NULL,'50 KG Bag','PRD-MOP-001','MOP Muriate of Potash - Potassium fertilizer for better yield and crop disease resistance.',1250.00,50.00,1500.00,1550.00,5.00,1,1,6,'TRADING_PRODUCT',1,0.00,0.00,0.000),(5,_binary '','2026-05-18 15:55:37.473469','system',_binary '\0','2026-05-18 15:55:37.473469','system','GYP-2026-A',0.00,NULL,NULL,'25 KG Bag','PRD-GYP-001','Gypsum Fertilizer - Sulfur and calcium source for soil nutrition correction.',450.00,50.00,620.00,650.00,5.00,3,1,6,'TRADING_PRODUCT',1,0.00,0.00,0.000),(6,_binary '','2026-05-18 15:55:37.479468','system',_binary '\0','2026-05-18 15:55:37.479468','system','ZNB-2026-A',0.00,NULL,NULL,'1 KG Packet','PRD-ZNB-001','Zinc & Boron Micronutrient - Micronutrient support for rice panicle formation and fruit cracking control.',180.00,50.00,245.00,260.00,5.00,3,1,5,'TRADING_PRODUCT',2,0.00,0.00,0.000),(7,_binary '','2026-05-18 15:55:37.484470','system',_binary '\0','2026-05-18 15:55:37.484470','system','VRM-2026-A',2.00,NULL,NULL,'20 KG Bag','PRD-VERMI-001','Vermicompost - Organic compost made from cow dung and earthworm processing for soil fertility.',180.00,50.00,260.00,280.00,0.00,3,3,6,'FINISHED_PRODUCTS',3,0.00,0.00,0.000),(8,_binary '','2026-05-18 15:55:37.490001','system',_binary '\0','2026-05-18 15:55:37.490001','system','TRC-2026-A',2.00,NULL,NULL,'25 KG Bag','PRD-TRICO-001','Trico-Compost - Bio compost with fungal disease suppression support.',260.00,50.00,365.00,390.00,0.00,3,3,6,'FINISHED_PRODUCTS',3,0.00,0.00,0.000),(9,_binary '','2026-05-18 15:55:37.495129','system',_binary '\0','2026-05-19 16:33:53.000732','admin','BML-2026-A',1.00,NULL,'','5 KG Pack','PRD-BONEMEAL-001','Bone Meal - Natural phosphorus source for nursery and rooftop gardening.',240.00,50.00,545.00,360.00,0.00,2,3,1,'RAW_MATERIALS',3,141700.00,545.00,260.000),(10,_binary '','2026-05-18 15:55:37.499127','system',_binary '\0','2026-05-18 15:55:37.499127','system','IMD-2026-A',0.00,NULL,NULL,'100 ML Bottle','PRD-IMIDA-001','Imidacloprid - Insecticide for jassid, thrips, and whitefly control.',130.00,50.00,195.00,210.00,5.00,1,2,8,'TRADING_PRODUCT',4,0.00,0.00,0.000),(11,_binary '','2026-05-18 15:55:37.504127','system',_binary '\0','2026-05-18 15:55:37.504127','system','CYP-2026-A',0.00,NULL,NULL,'250 ML Bottle','PRD-CYPER-001','Cypermethrin - Insecticide used for caterpillar and stem borer control.',190.00,50.00,290.00,310.00,5.00,1,2,8,'TRADING_PRODUCT',4,0.00,0.00,0.000),(12,_binary '','2026-05-18 15:55:37.510285','system',_binary '\0','2026-05-18 15:55:37.510285','system','CRB-2026-A',0.00,NULL,NULL,'1 KG Pack','PRD-CARBO-001','Carbofuran Granules - Granular insecticide for soil insects and rice stem borer management.',210.00,50.00,310.00,330.00,5.00,2,2,1,'TRADING_PRODUCT',4,0.00,0.00,0.000),(13,_binary '','2026-05-18 15:55:37.515273','system',_binary '\0','2026-05-18 15:55:37.515273','system','MNZ-2026-A',0.00,NULL,NULL,'500 Gram Packet','PRD-MANCO-001','Mancozeb Fungicide - Fungicide for blight, dieback, and plant rot disease control.',260.00,50.00,390.00,410.00,5.00,3,2,5,'TRADING_PRODUCT',5,0.00,0.00,0.000),(14,_binary '','2026-05-18 15:55:37.521274','system',_binary '\0','2026-05-18 15:55:37.521274','system','CBD-2026-A',0.00,NULL,NULL,'100 Gram Packet','PRD-CARBEN-001','Carbendazim Fungicide - Systemic fungicide for crop disease protection.',120.00,50.00,195.00,210.00,5.00,1,2,5,'TRADING_PRODUCT',5,0.00,0.00,0.000),(15,_binary '','2026-05-18 15:55:37.527274','system',_binary '\0','2026-05-18 15:55:37.527274','system','GLY-2026-A',0.00,NULL,NULL,'1 Liter Bottle','PRD-GLYPHO-001','Glyphosate Herbicide - Herbicide for clearing harmful weeds and grasses from fields.',420.00,50.00,620.00,650.00,5.00,2,2,3,'TRADING_PRODUCT',6,0.00,0.00,0.000),(16,_binary '','2026-05-18 15:55:37.531679','system',_binary '\0','2026-05-18 15:55:37.531679','system','NEEM-2026-A',2.00,NULL,NULL,'500 ML Bottle','PRD-NEEM-001','Neem Oil Bio-Pesticide - Environment-friendly bio pesticide for safer crop protection.',220.00,50.00,330.00,350.00,0.00,3,3,8,'FINISHED_PRODUCTS',7,0.00,0.00,0.000);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `retailer_ledger`
--

DROP TABLE IF EXISTS `retailer_ledger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `retailer_ledger` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `credit_amount` decimal(14,2) NOT NULL,
  `debit_amount` decimal(14,2) NOT NULL,
  `ledger_type` enum('DISCOUNT_ADJUSTMENT','INVOICE','OPENING','PAYMENT','RETURN_ADJUSTMENT') NOT NULL,
  `narration` varchar(255) DEFAULT NULL,
  `reference_id` bigint DEFAULT NULL,
  `reference_type` varchar(255) DEFAULT NULL,
  `running_balance` decimal(14,2) NOT NULL,
  `transaction_date` date NOT NULL,
  `retailer_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_ledger_retailer_date` (`retailer_id`,`transaction_date`),
  CONSTRAINT `FK2e5yiesaffjxdgy9dejxjnqyt` FOREIGN KEY (`retailer_id`) REFERENCES `retailers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `retailer_ledger`
--

LOCK TABLES `retailer_ledger` WRITE;
/*!40000 ALTER TABLE `retailer_ledger` DISABLE KEYS */;
INSERT INTO `retailer_ledger` VALUES (1,_binary '','2026-05-19 03:50:32.726615','admin',_binary '\0','2026-05-19 03:50:32.726615','admin',0.00,1351.35,'INVOICE','Invoice INV-20260519-1002',1,'INVOICE',26351.35,'2026-05-19',1),(2,_binary '','2026-05-19 06:32:20.134234','admin',_binary '\0','2026-05-19 06:32:20.134234','admin',0.00,67567.50,'INVOICE','Invoice INV-20260519-1003',2,'INVOICE',93918.85,'2026-05-19',1),(3,_binary '','2026-05-19 13:48:08.019950','admin',_binary '\0','2026-05-19 13:48:08.019950','admin',0.00,539.55,'INVOICE','Invoice INV-20260519-1004',3,'INVOICE',94458.40,'2026-05-19',1),(4,_binary '','2026-05-19 15:50:46.725121','admin',_binary '\0','2026-05-19 15:50:46.725121','admin',0.00,83790.00,'INVOICE','Invoice INV-20260519-1005',4,'INVOICE',95790.00,'2026-05-19',2),(5,_binary '','2026-05-19 16:27:10.923249','admin',_binary '\0','2026-05-19 16:27:10.923249','admin',1351.35,0.00,'PAYMENT','Money receipt MR-20260519-1001',1,'PAYMENT',93107.05,'2026-05-19',1),(6,_binary '','2026-05-19 17:11:51.369988','admin',_binary '\0','2026-05-19 17:11:51.369988','admin',0.00,1351.35,'INVOICE','Invoice INV-20260519-1006',5,'INVOICE',94458.40,'2026-05-19',1),(7,_binary '','2026-05-19 17:16:42.530506','admin',_binary '\0','2026-05-19 17:16:42.530506','admin',1351.35,0.00,'PAYMENT','Money receipt MR-20260519-1002',2,'PAYMENT',93107.05,'2026-05-19',1);
/*!40000 ALTER TABLE `retailer_ledger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `retailers`
--

DROP TABLE IF EXISTS `retailers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `retailers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `address` varchar(500) DEFAULT NULL,
  `credit_limit` decimal(14,2) NOT NULL,
  `current_due_balance` decimal(14,2) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `market_name` varchar(255) DEFAULT NULL,
  `mobile_number` varchar(30) NOT NULL,
  `opening_balance` decimal(14,2) NOT NULL,
  `owner_name` varchar(255) DEFAULT NULL,
  `retailer_code` varchar(40) NOT NULL,
  `retailer_name` varchar(160) NOT NULL,
  `territory_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKebj4sye6p8gb3uav3egs9oinr` (`retailer_code`),
  KEY `FK63g888svrdoxvt6lll9ndv0om` (`territory_id`),
  CONSTRAINT `FK63g888svrdoxvt6lll9ndv0om` FOREIGN KEY (`territory_id`) REFERENCES `territories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `retailers`
--

LOCK TABLES `retailers` WRITE;
/*!40000 ALTER TABLE `retailers` DISABLE KEYS */;
INSERT INTO `retailers` VALUES (1,_binary '','2026-05-18 15:55:37.429376','system',_binary '\0','2026-05-19 17:16:42.571135','admin','Kawran Bazar, Dhaka',500000.00,93107.05,'rahman.agro@example.com','Kawran Bazar','01711000001',25000.00,'Md. Rahman','RTL-001','Rahman Agro Traders',1),(2,_binary '','2026-05-18 15:55:37.433376','system',_binary '\0','2026-05-19 15:50:46.736357','admin','Savar, Dhaka',350000.00,95790.00,'greenfield@example.com','Savar Bazar','01711000002',12000.00,'Sadia Islam','RTL-002','Green Field Krishi Ghar',1),(3,_binary '','2026-05-18 15:55:37.437377','system',_binary '\0','2026-05-18 15:55:37.437377','system','Monirampur, Jashore',420000.00,18000.00,'molla.store@example.com','Monirampur Market','01711000003',18000.00,'Abdul Molla','RTL-003','Molla Fertilizer Store',2),(4,_binary '','2026-05-19 11:22:31.761202','admin',_binary '\0','2026-05-19 11:22:31.761202','admin','rangpur',1000.00,0.00,NULL,'','01774254988',0.00,'aktar','RTL-004','apple agro',NULL);
/*!40000 ALTER TABLE `retailers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(60) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,_binary '','2026-05-18 03:25:43.032179','system',_binary '\0','2026-05-18 03:25:43.032179','system','SUPER ADMIN','ROLE_SUPER_ADMIN'),(2,_binary '','2026-05-18 03:25:43.066179','system',_binary '\0','2026-05-18 03:25:43.066179','system','ADMIN','ROLE_ADMIN'),(3,_binary '','2026-05-18 03:25:43.071181','system',_binary '\0','2026-05-18 03:25:43.071181','system','SALES MANAGER','ROLE_SALES_MANAGER'),(4,_binary '','2026-05-18 03:25:43.076176','system',_binary '\0','2026-05-18 03:25:43.076176','system','SALES OFFICER','ROLE_SALES_OFFICER'),(5,_binary '','2026-05-18 03:25:43.079176','system',_binary '\0','2026-05-18 03:25:43.079176','system','ACCOUNTS USER','ROLE_ACCOUNTS_USER'),(6,_binary '','2026-05-18 03:25:43.084181','system',_binary '\0','2026-05-18 03:25:43.084181','system','STORE USER','ROLE_STORE_USER'),(7,_binary '','2026-05-18 03:25:43.089181','system',_binary '\0','2026-05-18 03:25:43.089181','system','DELIVERY USER','ROLE_DELIVERY_USER'),(8,_binary '','2026-05-18 03:25:43.093180','system',_binary '\0','2026-05-18 03:25:43.093180','system','RETAILER','ROLE_RETAILER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_transactions`
--

DROP TABLE IF EXISTS `stock_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_transactions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `batch_number` varchar(255) DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `quantity` decimal(14,3) NOT NULL,
  `reference_id` bigint DEFAULT NULL,
  `reference_type` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `transaction_type` enum('ADJUSTMENT','DAMAGE','ISSUE','OPENING','RECEIVE','RETURN_IN','SALES_OUT') NOT NULL,
  `product_id` bigint NOT NULL,
  `warehouse_id` bigint NOT NULL,
  `movement_type_code` varchar(10) DEFAULT NULL,
  `movement_type_name` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_stock_product_warehouse` (`product_id`,`warehouse_id`),
  KEY `idx_stock_reference` (`reference_type`,`reference_id`),
  KEY `FKcr6bbr62ww46o63wgnlw2e8mm` (`warehouse_id`),
  CONSTRAINT `FK9qbjlda0gjdsmqn7bkhii6bb0` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKcr6bbr62ww46o63wgnlw2e8mm` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_transactions`
--

LOCK TABLES `stock_transactions` WRITE;
/*!40000 ALTER TABLE `stock_transactions` DISABLE KEYS */;
INSERT INTO `stock_transactions` VALUES (1,_binary '','2026-05-18 15:55:37.449375','system',_binary '\0','2026-05-18 15:55:37.449375','system','UREA-2026-A',NULL,500.000,1,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',1,1,NULL,NULL),(2,_binary '','2026-05-18 15:55:37.458375','system',_binary '\0','2026-05-18 15:55:37.458375','system','TSP-2026-A',NULL,420.000,2,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',2,1,NULL,NULL),(3,_binary '','2026-05-18 15:55:37.464470','system',_binary '\0','2026-05-18 15:55:37.464470','system','DAP-2026-A',NULL,380.000,3,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',3,1,NULL,NULL),(4,_binary '','2026-05-18 15:55:37.470469','system',_binary '\0','2026-05-18 15:55:37.470469','system','MOP-2026-A',NULL,410.000,4,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',4,1,NULL,NULL),(5,_binary '','2026-05-18 15:55:37.475470','system',_binary '\0','2026-05-18 15:55:37.475470','system','GYP-2026-A',NULL,300.000,5,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',5,1,NULL,NULL),(6,_binary '','2026-05-18 15:55:37.481469','system',_binary '\0','2026-05-18 15:55:37.481469','system','ZNB-2026-A',NULL,900.000,6,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',6,1,NULL,NULL),(7,_binary '','2026-05-18 15:55:37.486469','system',_binary '\0','2026-05-18 15:55:37.486469','system','VRM-2026-A',NULL,260.000,7,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',7,1,NULL,NULL),(8,_binary '','2026-05-18 15:55:37.492102','system',_binary '\0','2026-05-18 15:55:37.492102','system','TRC-2026-A',NULL,220.000,8,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',8,1,NULL,NULL),(9,_binary '','2026-05-18 15:55:37.497126','system',_binary '\0','2026-05-18 15:55:37.497126','system','BML-2026-A',NULL,180.000,9,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',9,1,NULL,NULL),(10,_binary '','2026-05-18 15:55:37.501127','system',_binary '\0','2026-05-18 15:55:37.501127','system','IMD-2026-A',NULL,800.000,10,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',10,1,NULL,NULL),(11,_binary '','2026-05-18 15:55:37.507127','system',_binary '\0','2026-05-18 15:55:37.507127','system','CYP-2026-A',NULL,650.000,11,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',11,1,NULL,NULL),(12,_binary '','2026-05-18 15:55:37.512270','system',_binary '\0','2026-05-18 15:55:37.512270','system','CRB-2026-A',NULL,500.000,12,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',12,1,NULL,NULL),(13,_binary '','2026-05-18 15:55:37.518273','system',_binary '\0','2026-05-18 15:55:37.518273','system','MNZ-2026-A',NULL,480.000,13,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',13,1,NULL,NULL),(14,_binary '','2026-05-18 15:55:37.523273','system',_binary '\0','2026-05-18 15:55:37.523273','system','CBD-2026-A',NULL,720.000,14,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',14,1,NULL,NULL),(15,_binary '','2026-05-18 15:55:37.528676','system',_binary '\0','2026-05-18 15:55:37.528676','system','GLY-2026-A',NULL,280.000,15,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',15,1,NULL,NULL),(16,_binary '','2026-05-18 15:55:37.534679','system',_binary '\0','2026-05-18 15:55:37.534679','system','NEEM-2026-A',NULL,360.000,16,'DEMO_SEED','Opening stock for demo sales and delivery flow','OPENING',16,1,NULL,NULL),(17,_binary '','2026-05-19 04:07:13.067531','admin',_binary '\0','2026-05-19 04:07:13.067531','admin','CODEX-TEST',NULL,2.000,NULL,'MANUAL','Material stock verification','RECEIVE',9,1,NULL,NULL),(18,_binary '','2026-05-19 04:07:27.847979','admin',_binary '\0','2026-05-19 04:07:27.847979','admin','CODEX-TEST',NULL,2.000,NULL,'MANUAL','Reverse material stock verification','ISSUE',9,1,NULL,NULL),(19,_binary '','2026-05-19 04:34:06.320495','admin',_binary '\0','2026-05-19 04:34:06.320495','admin','1','2026-05-31',20.000,NULL,'MANUAL','new production','ISSUE',10,1,NULL,NULL),(20,_binary '','2026-05-19 04:35:19.532609','admin',_binary '\0','2026-05-19 04:35:19.532609','admin','1','2026-05-31',50.000,NULL,'MANUAL','new 2 production','RECEIVE',9,1,NULL,NULL),(21,_binary '','2026-05-19 04:47:19.265472','admin',_binary '\0','2026-05-19 04:47:19.265472','admin','CODEX-MVT',NULL,1.000,NULL,'MANUAL','Movement history verification','RECEIVE',9,1,'101','Goods Receipt'),(22,_binary '','2026-05-19 04:47:30.639816','admin',_binary '\0','2026-05-19 04:47:30.639816','admin','CODEX-MVT',NULL,1.000,NULL,'MANUAL','Reverse movement history verification','ISSUE',9,1,'201','Goods Issue'),(23,_binary '','2026-05-19 06:19:54.029246','admin',_binary '\0','2026-05-19 06:19:54.029246','admin','1','2026-05-31',80.000,NULL,'MANUAL','test','RECEIVE',9,1,'101','Goods Receipt'),(24,_binary '','2026-05-19 06:33:43.211162','admin',_binary '\0','2026-05-19 06:33:43.211162','admin',NULL,NULL,50.000,1,'DELIVERY','Auto stock out','SALES_OUT',1,1,'601','Goods Issue for Delivery'),(25,_binary '','2026-05-19 13:36:02.016773','admin',_binary '\0','2026-05-19 13:36:02.016773','admin','',NULL,50.000,NULL,'MANUAL','','ISSUE',9,1,'201','Goods Issue'),(26,_binary '','2026-05-19 13:49:14.627010','admin',_binary '\0','2026-05-19 13:49:14.627010','admin',NULL,NULL,1.000,2,'DELIVERY','Auto stock out','SALES_OUT',9,1,'601','Goods Issue for Delivery'),(27,_binary '','2026-05-19 17:14:26.845609','admin',_binary '\0','2026-05-19 17:14:26.845609','admin',NULL,NULL,1.000,3,'DELIVERY','Auto stock out','SALES_OUT',1,1,'601','Goods Issue for Delivery');
/*!40000 ALTER TABLE `stock_transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sub_categories`
--

DROP TABLE IF EXISTS `sub_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sub_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `category_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjwy7imy3rf6r99x48ydq45otw` (`category_id`),
  CONSTRAINT `FKjwy7imy3rf6r99x48ydq45otw` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sub_categories`
--

LOCK TABLES `sub_categories` WRITE;
/*!40000 ALTER TABLE `sub_categories` DISABLE KEYS */;
INSERT INTO `sub_categories` VALUES (1,_binary '','2026-05-18 15:55:37.354375','system',_binary '\0','2026-05-18 15:55:37.354375','system','Chemical Fertilizer',1),(2,_binary '','2026-05-18 15:55:37.361375','system',_binary '\0','2026-05-18 15:55:37.361375','system','Micronutrient',1),(3,_binary '','2026-05-18 15:55:37.365376','system',_binary '\0','2026-05-18 15:55:37.365376','system','Organic Fertilizer',3),(4,_binary '','2026-05-18 15:55:37.369375','system',_binary '\0','2026-05-18 15:55:37.369375','system','Insecticide',2),(5,_binary '','2026-05-18 15:55:37.373376','system',_binary '\0','2026-05-18 15:55:37.373376','system','Fungicide',2),(6,_binary '','2026-05-18 15:55:37.377375','system',_binary '\0','2026-05-18 15:55:37.377375','system','Herbicide',2),(7,_binary '','2026-05-18 15:55:37.381375','system',_binary '\0','2026-05-18 15:55:37.381375','system','Bio Pesticide',3);
/*!40000 ALTER TABLE `sub_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `territories`
--

DROP TABLE IF EXISTS `territories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `territories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `code` varchar(40) NOT NULL,
  `market_name` varchar(255) DEFAULT NULL,
  `name` varchar(120) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKgvujn8y7gi3sy2e0hspbla38` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `territories`
--

LOCK TABLES `territories` WRITE;
/*!40000 ALTER TABLE `territories` DISABLE KEYS */;
INSERT INTO `territories` VALUES (1,_binary '','2026-05-18 15:55:37.417376','system',_binary '\0','2026-05-18 15:55:37.417376','system','DHK-N','Kawran Bazar','Dhaka North'),(2,_binary '','2026-05-18 15:55:37.423376','system',_binary '\0','2026-05-18 15:55:37.423376','system','JSR-S','Monirampur Market','Jashore South');
/*!40000 ALTER TABLE `territories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `units`
--

DROP TABLE IF EXISTS `units`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `units` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKetw07nfppovq9p7ov8hcb38wy` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `units`
--

LOCK TABLES `units` WRITE;
/*!40000 ALTER TABLE `units` DISABLE KEYS */;
INSERT INTO `units` VALUES (1,_binary '','2026-05-18 03:25:43.170180','system',_binary '\0','2026-05-18 03:25:43.170180','system','KG'),(2,_binary '','2026-05-18 03:25:43.175176','system',_binary '\0','2026-05-18 03:25:43.175176','system','Gram'),(3,_binary '','2026-05-18 03:25:43.179178','system',_binary '\0','2026-05-18 03:25:43.179178','system','Liter'),(4,_binary '','2026-05-18 03:25:43.182824','system',_binary '\0','2026-05-18 03:25:43.182824','system','ML'),(5,_binary '','2026-05-18 03:25:43.187826','system',_binary '\0','2026-05-18 03:25:43.187826','system','Packet'),(6,_binary '','2026-05-18 03:25:43.191823','system',_binary '\0','2026-05-18 03:25:43.191823','system','Bag'),(7,_binary '','2026-05-18 03:25:43.197290','system',_binary '\0','2026-05-18 03:25:43.197290','system','Sack'),(8,_binary '','2026-05-18 03:25:43.201290','system',_binary '\0','2026-05-18 03:25:43.201290','system','Bottle'),(9,_binary '','2026-05-18 03:25:43.206290','system',_binary '\0','2026-05-18 03:25:43.206290','system','Box'),(10,_binary '','2026-05-18 03:25:43.211290','system',_binary '\0','2026-05-18 03:25:43.211290','system','PCS');
/*!40000 ALTER TABLE `units` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,1);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `email` varchar(120) NOT NULL,
  `full_name` varchar(120) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(80) NOT NULL,
  `company_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  KEY `FKin8gn4o1hpiwe6qe4ey7ykwq7` (`company_id`),
  CONSTRAINT `FKin8gn4o1hpiwe6qe4ey7ykwq7` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,_binary '','2026-05-18 03:25:43.151180','system',_binary '\0','2026-05-19 15:52:11.781183','admin','admin@agroerp.local','System Administrator','$2a$10$HeEwYVs.wiq9DJ8p/niOg.os3SSk3hFBtpVlwA/QPevaSSc17Zx/O','admin',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehouses`
--

DROP TABLE IF EXISTS `warehouses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `code` varchar(40) NOT NULL,
  `name` varchar(120) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6herdbg4x5wp6gkor8epv73oc` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehouses`
--

LOCK TABLES `warehouses` WRITE;
/*!40000 ALTER TABLE `warehouses` DISABLE KEYS */;
INSERT INTO `warehouses` VALUES (1,_binary '','2026-05-18 03:25:43.234785','system',_binary '\0','2026-05-18 03:25:43.234785','system','Head office warehouse','MAIN','Main Warehouse');
/*!40000 ALTER TABLE `warehouses` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-24 16:32:53
