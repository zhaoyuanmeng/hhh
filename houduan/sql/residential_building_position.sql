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

 Date: 25/02/2025 09:18:56
*/


-- ----------------------------
-- Table structure for residential_building_position
-- ----------------------------
DROP TABLE IF EXISTS "public"."residential_building_position";
CREATE TABLE "public"."residential_building_position" (
  "building_address" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "x" varchar(255) COLLATE "pg_catalog"."default",
  "y" varchar(255) COLLATE "pg_catalog"."default",
  "z" varchar(255) COLLATE "pg_catalog"."default",
  "building_name" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of residential_building_position
-- ----------------------------
INSERT INTO "public"."residential_building_position" VALUES ('河北省容城县富家路26号滨河园4号楼', NULL, NULL, NULL, '滨河园4号楼');
INSERT INTO "public"."residential_building_position" VALUES ('河北省容城县富家路26号滨河园5号楼', NULL, NULL, NULL, '滨河园5号楼');

-- ----------------------------
-- Primary Key structure for table residential_building_position
-- ----------------------------
ALTER TABLE "public"."residential_building_position" ADD CONSTRAINT "residential_building_position_pkey" PRIMARY KEY ("building_address");
