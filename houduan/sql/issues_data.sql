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

 Date: 14/01/2025 10:18:15
*/


-- ----------------------------
-- Table structure for issues_data
-- ----------------------------
DROP TABLE IF EXISTS "public"."issues_data";
CREATE TABLE "public"."issues_data" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "parent_id" varchar(64) COLLATE "pg_catalog"."default",
  "content" text COLLATE "pg_catalog"."default",
  "delete_flag" varchar(8) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6)
)
;

-- ----------------------------
-- Primary Key structure for table issues_data
-- ----------------------------
ALTER TABLE "public"."issues_data" ADD CONSTRAINT "issues_data_pkey" PRIMARY KEY ("id");
