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

 Date: 31/12/2024 09:32:55
*/


-- ----------------------------
-- Table structure for house_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."house_info";
CREATE TABLE "public"."house_info" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "building_id" varchar(64) COLLATE "pg_catalog"."default",
  "building_cell" varchar(255) COLLATE "pg_catalog"."default",
  "owner_user_name" varchar(255) COLLATE "pg_catalog"."default",
  "owner_user_tel" varchar(64) COLLATE "pg_catalog"."default",
  "room_type" varchar(255) COLLATE "pg_catalog"."default",
  "room_area" varchar(64) COLLATE "pg_catalog"."default",
  "per_number" int4,
  "house_number" varchar(64),
  "floor_number" int4,
  "checked_flag" int2
)
;
COMMENT ON COLUMN "public"."house_info"."building_id" IS '楼栋ID';
COMMENT ON COLUMN "public"."house_info"."building_cell" IS '楼栋单元';
COMMENT ON COLUMN "public"."house_info"."owner_user_name" IS '户主';
COMMENT ON COLUMN "public"."house_info"."owner_user_tel" IS '户主联系方式';
COMMENT ON COLUMN "public"."house_info"."room_type" IS '房屋类型';
COMMENT ON COLUMN "public"."house_info"."room_area" IS '房屋面积';
COMMENT ON COLUMN "public"."house_info"."per_number" IS '常住人数';
COMMENT ON COLUMN "public"."house_info"."house_number" IS '门牌号';
COMMENT ON COLUMN "public"."house_info"."floor_number" IS '楼层';
COMMENT ON COLUMN "public"."house_info"."checked_flag" IS '是否排查';
COMMENT ON TABLE "public"."house_info" IS '居民楼房子数据';

-- ----------------------------
-- Primary Key structure for table house_info
-- ----------------------------
ALTER TABLE "public"."house_info" ADD CONSTRAINT "house_info_pkey" PRIMARY KEY ("id");
