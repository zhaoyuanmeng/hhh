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

 Date: 18/02/2025 15:09:40
*/


-- ----------------------------
-- Table structure for commercial_building_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."commercial_building_info";
CREATE TABLE "public"."commercial_building_info" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "region_id" varchar(64) COLLATE "pg_catalog"."default",
  "building_name" varchar(255) COLLATE "pg_catalog"."default",
  "x" varchar(64) COLLATE "pg_catalog"."default",
  "y" varchar(64) COLLATE "pg_catalog"."default",
  "z" varchar(64) COLLATE "pg_catalog"."default",
  "floor_number" int2,
  "pull_out_flag" int2
)
;
COMMENT ON COLUMN "public"."commercial_building_info"."region_id" IS '区域ID';
COMMENT ON COLUMN "public"."commercial_building_info"."floor_number" IS '楼层数';
COMMENT ON COLUMN "public"."commercial_building_info"."pull_out_flag" IS '抽出标志';

-- ----------------------------
-- Records of commercial_building_info
-- ----------------------------
INSERT INTO "public"."commercial_building_info" VALUES ('64fb7fc7-be2e-4993-88a2-a1a108f063de', '8bfd3dea-7087-4197-b782-a988033d4811', 'B1#', '494389.1325', '4324146.88', '50', 10, 0);

-- ----------------------------
-- Primary Key structure for table commercial_building_info
-- ----------------------------
ALTER TABLE "public"."commercial_building_info" ADD CONSTRAINT "cfommercial_building_info_pkey" PRIMARY KEY ("id");
