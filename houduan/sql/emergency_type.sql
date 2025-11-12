/*
 Navicat Premium Data Transfer

 Source Server         : pgsql
 Source Server Type    : PostgreSQL
 Source Server Version : 130015
 Source Host           : localhost:5432
 Source Catalog        : teqin
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 130015
 File Encoding         : 65001

 Date: 16/10/2024 17:07:34
*/


-- ----------------------------
-- Table structure for emergency_type
-- ----------------------------
DROP TABLE IF EXISTS "public"."emergency_type";
CREATE TABLE "public"."emergency_type" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Primary Key structure for table emergency_type
-- ----------------------------
ALTER TABLE "public"."emergency_type" ADD CONSTRAINT "emergency_type_copy1_pkey" PRIMARY KEY ("id");
