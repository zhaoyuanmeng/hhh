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

 Date: 18/02/2025 15:09:49
*/


-- ----------------------------
-- Table structure for commercial_enterprise_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."commercial_enterprise_info";
CREATE TABLE "public"."commercial_enterprise_info" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "industry" varchar(255) COLLATE "pg_catalog"."default",
  "product_type" varchar(255) COLLATE "pg_catalog"."default",
  "head" varchar(255) COLLATE "pg_catalog"."default",
  "phone" varchar(64) COLLATE "pg_catalog"."default",
  "settled_flag" int2,
  "signing_time" timestamp(6),
  "checked_flag" int2,
  "key_flag" int2,
  "area" varchar(64) COLLATE "pg_catalog"."default",
  "type" int2
)
;
COMMENT ON COLUMN "public"."commercial_enterprise_info"."type" IS '1企业 2商业';
COMMENT ON TABLE "public"."commercial_enterprise_info" IS '办公楼企业数据';

-- ----------------------------
-- Records of commercial_enterprise_info
-- ----------------------------
INSERT INTO "public"."commercial_enterprise_info" VALUES ('92a1e84b-a579-4f5e-82c5-a4d1d0a4f4a6', '寻环造物社', NULL, NULL, NULL, NULL, 1, NULL, 0, 0, NULL, 2);

-- ----------------------------
-- Primary Key structure for table commercial_enterprise_info
-- ----------------------------
ALTER TABLE "public"."commercial_enterprise_info" ADD CONSTRAINT "enterprise_info_pkey" PRIMARY KEY ("id");
