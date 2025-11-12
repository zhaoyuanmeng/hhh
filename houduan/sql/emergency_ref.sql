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

 Date: 16/10/2024 17:07:25
*/


-- ----------------------------
-- Table structure for emergency_ref
-- ----------------------------
DROP TABLE IF EXISTS "public"."emergency_ref";
CREATE TABLE "public"."emergency_ref" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "emergency_plan_id" varchar(64) COLLATE "pg_catalog"."default",
  "scene_id" varchar(64) COLLATE "pg_catalog"."default",
  "delete_flag" int2
)
;
COMMENT ON COLUMN "public"."emergency_ref"."emergency_plan_id" IS '预案ID';
COMMENT ON COLUMN "public"."emergency_ref"."scene_id" IS '场景ID';
COMMENT ON COLUMN "public"."emergency_ref"."delete_flag" IS '删除标志';

-- ----------------------------
-- Primary Key structure for table emergency_ref
-- ----------------------------
ALTER TABLE "public"."emergency_ref" ADD CONSTRAINT "emergency_work_pkey" PRIMARY KEY ("id");
