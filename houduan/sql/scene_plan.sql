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

 Date: 11/09/2024 13:53:51
*/


-- ----------------------------
-- Table structure for scene_plan
-- ----------------------------
DROP TABLE IF EXISTS "scene_plan";
CREATE TABLE "scene_plan" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "plan_node" varchar(64) COLLATE "pg_catalog"."default",
  "data" jsonb,
  "scene_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "scene_plan"."plan_node" IS '规划节点，指导思想，基本情况，组织领导、工作要求等';
COMMENT ON COLUMN "scene_plan"."data" IS '规划节点数据';
COMMENT ON COLUMN "scene_plan"."scene_id" IS '场景ID';
COMMENT ON TABLE "scene_plan" IS '任务规划';

-- ----------------------------
-- Primary Key structure for table scene_plan
-- ----------------------------
ALTER TABLE "scene_plan" ADD CONSTRAINT "task_plan_pkey" PRIMARY KEY ("id");
