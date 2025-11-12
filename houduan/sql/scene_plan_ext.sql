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

 Date: 23/09/2024 08:47:15
*/


-- ----------------------------
-- Table structure for scene_plan_ext
-- ----------------------------
DROP TABLE IF EXISTS "scene_plan_ext";
CREATE TABLE "scene_plan_ext" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "plan_node" varchar(64) COLLATE "pg_catalog"."default",
  "data" jsonb,
  "scene_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "scene_plan_ext"."plan_node" IS '规划节点，警力部署、应急力量';
COMMENT ON COLUMN "scene_plan_ext"."data" IS '节点数据';
COMMENT ON COLUMN "scene_plan_ext"."scene_id" IS '场景ID';
COMMENT ON TABLE "scene_plan_ext" IS '任务规划扩展信息';

-- ----------------------------
-- Primary Key structure for table scene_plan_ext
-- ----------------------------
ALTER TABLE "scene_plan_ext" ADD CONSTRAINT "scene_plan_ext_pkey" PRIMARY KEY ("id");
