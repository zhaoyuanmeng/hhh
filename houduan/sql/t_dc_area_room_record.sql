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

 Date: 21/02/2025 13:40:25
*/


-- ----------------------------
-- Table structure for t_dc_area_room_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_dc_area_room_record";
CREATE TABLE "public"."t_dc_area_room_record" (
  "recode_id" varchar(255) COLLATE "pg_catalog"."default",
  "building_id" varchar(255) COLLATE "pg_catalog"."default",
  "unit" varchar(255) COLLATE "pg_catalog"."default",
  "up_floor" varchar(255) COLLATE "pg_catalog"."default",
  "up_number" varchar(255) COLLATE "pg_catalog"."default",
  "is_down" varchar(255) COLLATE "pg_catalog"."default",
  "down_floor" varchar(255) COLLATE "pg_catalog"."default",
  "down_number" varchar(255) COLLATE "pg_catalog"."default",
  "down_type" varchar(255) COLLATE "pg_catalog"."default",
  "creator" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" varchar(255) COLLATE "pg_catalog"."default",
  "updator" varchar(255) COLLATE "pg_catalog"."default",
  "update_time" varchar(255) COLLATE "pg_catalog"."default",
  "is_valid" varchar(255) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "up_start_number" varchar(255) COLLATE "pg_catalog"."default",
  "down_start_number" varchar(255) COLLATE "pg_catalog"."default",
  "bdp_audit" varchar COLLATE "pg_catalog"."default",
  "unionkey" varchar COLLATE "pg_catalog"."default",
  "user_design_id" int8
)
;
COMMENT ON TABLE "public"."t_dc_area_room_record" IS '智慧社区_楼栋批量新增房屋子表';
