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

 Date: 06/01/2025 11:18:12
*/


-- ----------------------------
-- Table structure for task_data
-- ----------------------------
DROP TABLE IF EXISTS "public"."task_data";
CREATE TABLE "public"."task_data" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "year" varchar(64) COLLATE "pg_catalog"."default",
  "one_plus" int4,
  "one" int4,
  "two" int4,
  "three" int4,
  "security" int4,
  "other" int4
)
;

-- ----------------------------
-- Primary Key structure for table task_data
-- ----------------------------
ALTER TABLE "public"."task_data" ADD CONSTRAINT "task_data_pkey" PRIMARY KEY ("id");
