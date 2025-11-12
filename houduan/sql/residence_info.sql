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

 Date: 31/12/2024 09:33:38
*/


-- ----------------------------
-- Table structure for residence_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."residence_info";
CREATE TABLE "public"."residence_info" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "tel" varchar(64) COLLATE "pg_catalog"."default",
  "sex" varchar(64) COLLATE "pg_catalog"."default",
  "native_place" varchar(255) COLLATE "pg_catalog"."default",
  "nationality" varchar(255) COLLATE "pg_catalog"."default",
  "register_address" varchar(255) COLLATE "pg_catalog"."default",
  "post" varchar(255) COLLATE "pg_catalog"."default",
  "work_unit" varchar(255) COLLATE "pg_catalog"."default",
  "house_id" varchar(64) COLLATE "pg_catalog"."default",
  "owner_flag" varchar(64) COLLATE "pg_catalog"."default",
  "focus_flag" varchar(64) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."residence_info"."name" IS '姓名';
COMMENT ON COLUMN "public"."residence_info"."tel" IS '联系电话';
COMMENT ON COLUMN "public"."residence_info"."sex" IS '性别';
COMMENT ON COLUMN "public"."residence_info"."native_place" IS '籍贯';
COMMENT ON COLUMN "public"."residence_info"."nationality" IS '国籍';
COMMENT ON COLUMN "public"."residence_info"."register_address" IS '户籍地';
COMMENT ON COLUMN "public"."residence_info"."post" IS '职务';
COMMENT ON COLUMN "public"."residence_info"."work_unit" IS '工作单位';
COMMENT ON COLUMN "public"."residence_info"."house_id" IS '房屋ID';
COMMENT ON COLUMN "public"."residence_info"."owner_flag" IS '户主标志';
COMMENT ON COLUMN "public"."residence_info"."focus_flag" IS '重点关注标志';
COMMENT ON TABLE "public"."residence_info" IS '居民楼居住数据';

-- ----------------------------
-- Primary Key structure for table residence_info
-- ----------------------------
ALTER TABLE "public"."residence_info" ADD CONSTRAINT "residence_info_pkey" PRIMARY KEY ("id");
