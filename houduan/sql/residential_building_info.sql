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

 Date: 31/12/2024 09:33:47
*/


-- ----------------------------
-- Table structure for residential_building_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."residential_building_info";
CREATE TABLE "public"."residential_building_info" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "building_name" varchar(255) COLLATE "pg_catalog"."default",
  "community_name" varchar(255) COLLATE "pg_catalog"."default",
  "estate_name" varchar(255) COLLATE "pg_catalog"."default",
  "building_floor" int2,
  "unit_number" int2,
  "building_address" varchar(1024) COLLATE "pg_catalog"."default",
  "household_number" int2,
  "x" varchar(64) COLLATE "pg_catalog"."default",
  "y" varchar(64) COLLATE "pg_catalog"."default",
  "z" varchar(64) COLLATE "pg_catalog"."default",
  "household_rs_number" int4
)
;
COMMENT ON COLUMN "public"."residential_building_info"."building_name" IS '楼栋名称';
COMMENT ON COLUMN "public"."residential_building_info"."community_name" IS '社区';
COMMENT ON COLUMN "public"."residential_building_info"."estate_name" IS '小区';
COMMENT ON COLUMN "public"."residential_building_info"."building_floor" IS '层数';
COMMENT ON COLUMN "public"."residential_building_info"."unit_number" IS '单元数';
COMMENT ON COLUMN "public"."residential_building_info"."building_address" IS '地址';
COMMENT ON COLUMN "public"."residential_building_info"."household_number" IS '户数';
COMMENT ON COLUMN "public"."residential_building_info"."household_rs_number" IS '楼栋人数';
COMMENT ON TABLE "public"."residential_building_info" IS '居民楼数据';

-- ----------------------------
-- Primary Key structure for table residential_building_info
-- ----------------------------
ALTER TABLE "public"."residential_building_info" ADD CONSTRAINT "residential_building_pkey" PRIMARY KEY ("id");
