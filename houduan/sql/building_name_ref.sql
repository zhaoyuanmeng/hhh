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

 Date: 28/02/2025 17:20:30
*/


-- ----------------------------B
-- Table structure for building_name_ref
-- ----------------------------
DROP TABLE IF EXISTS "public"."building_name_ref";
CREATE TABLE "public"."building_name_ref" (
  "layer_tag_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "building_name" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."building_name_ref"."layer_tag_name" IS '图层标签名';
COMMENT ON COLUMN "public"."building_name_ref"."building_name" IS '数据源楼栋名';

-- ----------------------------
-- Records of building_name_ref
-- ----------------------------
INSERT INTO "public"."building_name_ref" VALUES ('璟学庭1区1号楼', '璟学庭一区1号楼');

-- ----------------------------
-- Primary Key structure for table building_name_ref
-- ----------------------------
ALTER TABLE "public"."building_name_ref" ADD CONSTRAINT "residential_building_position_copy1_pkey" PRIMARY KEY ("layer_tag_name");
