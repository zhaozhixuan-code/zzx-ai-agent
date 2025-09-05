/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80039
 Source Host           : localhost:3306
 Source Schema         : aiagent

 Target Server Type    : MySQL
 Target Server Version : 80039
 File Encoding         : 65001

 Date: 04/09/2025 20:02:48
*/

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `aiagent` CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- 选择使用该数据库
USE `aiagent`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chat_conversation
-- ----------------------------
DROP TABLE IF EXISTS `chat_conversation`;
CREATE TABLE `chat_conversation`
(
    `id`              bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `conversation_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话ID',
    `created_time`    timestamp                                                     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '会话创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for chat_message
-- ----------------------------
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message`
(
    `id`              bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `conversation_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话ID',
    `message_type`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '消息类型',
    `content`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NOT NULL COMMENT '消息内容',
    `properties`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NOT NULL COMMENT '元数据',
    `created_time`    timestamp                                                     NULL     DEFAULT CURRENT_TIMESTAMP,
    `is_delete`       tinyint(1)                                                    NOT NULL DEFAULT 0 COMMENT '是否删除 0-未删除 1-已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 144
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;