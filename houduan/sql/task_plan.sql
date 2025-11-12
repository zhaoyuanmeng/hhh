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

 Date: 13/12/2024 10:02:57
*/


-- ----------------------------
-- Table structure for task_plan
-- ----------------------------
DROP TABLE IF EXISTS "public"."task_plan";
CREATE TABLE "public"."task_plan" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "plan_node" varchar(64) COLLATE "pg_catalog"."default",
  "data" jsonb,
  "task_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."task_plan"."plan_node" IS '规划节点，指导思想，基本情况，组织领导、工作要求等';
COMMENT ON COLUMN "public"."task_plan"."data" IS '规划节点数据';
COMMENT ON COLUMN "public"."task_plan"."task_id" IS '任务ID';
COMMENT ON TABLE "public"."task_plan" IS '任务规划';

-- ----------------------------
-- Primary Key structure for table task_plan
-- ----------------------------
ALTER TABLE "public"."task_plan" ADD CONSTRAINT "task_ext_pkey" PRIMARY KEY ("id");
