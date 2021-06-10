/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50731
Source Host           : localhost:3306
Source Database       : all-in-one

Target Server Type    : MYSQL
Target Server Version : 50731
File Encoding         : 65001

Date: 2021-06-10 17:10:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `age` int(4) DEFAULT NULL,
  `addr` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('1', 'mmm', '5', 'guagnzhou');
