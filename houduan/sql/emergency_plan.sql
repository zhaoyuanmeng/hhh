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

 Date: 16/10/2024 17:07:14
*/


-- ----------------------------
-- Table structure for emergency_plan
-- ----------------------------
DROP TABLE IF EXISTS "public"."emergency_plan";
CREATE TABLE "public"."emergency_plan" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "basic_data_id" varchar(64) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(64) COLLATE "pg_catalog"."default",
  "data" jsonb,
  "delete_flag" int2
)
;
COMMENT ON COLUMN "public"."emergency_plan"."basic_data_id" IS '基础数据ID';
COMMENT ON COLUMN "public"."emergency_plan"."name" IS '预案名称';
COMMENT ON COLUMN "public"."emergency_plan"."type" IS '预案类型';
COMMENT ON COLUMN "public"."emergency_plan"."data" IS '预案数据';
COMMENT ON COLUMN "public"."emergency_plan"."delete_flag" IS '删除标志';
COMMENT ON TABLE "public"."emergency_plan" IS '应急预案';

-- ----------------------------
-- Primary Key structure for table emergency_plan
-- ----------------------------
ALTER TABLE "public"."emergency_plan" ADD CONSTRAINT "emergency_plan_pkey" PRIMARY KEY ("id");
