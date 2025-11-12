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

 Date: 09/01/2025 10:17:27
*/


-- ----------------------------
-- Table structure for import_records
-- ----------------------------
DROP TABLE IF EXISTS "public"."import_records";
CREATE TABLE "public"."import_records" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "type" varchar(255) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6),
  "update_name" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of import_records
-- ----------------------------
INSERT INTO "public"."import_records" VALUES ('1', '基础工作数据', '2025-01-01 00:00:00', '管理员');
INSERT INTO "public"."import_records" VALUES ('2', '商业企业数据', '2025-01-01 00:00:00', '管理员');
INSERT INTO "public"."import_records" VALUES ('3', '社区数据', '2025-01-01 00:00:00', '管理员');

-- ----------------------------
-- Primary Key structure for table import_records
-- ----------------------------
ALTER TABLE "public"."import_records" ADD CONSTRAINT "import_records_pkey" PRIMARY KEY ("id");
