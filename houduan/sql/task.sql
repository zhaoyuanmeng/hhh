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

 Date: 13/12/2024 10:02:37
*/


-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS "public"."task";
CREATE TABLE "public"."task" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "task_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "task_desc" varchar(255) COLLATE "pg_catalog"."default",
  "task_start_time" timestamp(6) NOT NULL,
  "task_end_time" timestamp(6) NOT NULL,
  "head" varchar(255) COLLATE "pg_catalog"."default",
  "task_level" varchar(64) COLLATE "pg_catalog"."default",
  "phone" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "create_id" varchar(64) COLLATE "pg_catalog"."default",
  "delete_flag" int2,
  "view_data" jsonb
)
;
COMMENT ON COLUMN "public"."task"."task_name" IS '任务名称';
COMMENT ON COLUMN "public"."task"."task_desc" IS '任务描述';
COMMENT ON COLUMN "public"."task"."task_start_time" IS '开始时间';
COMMENT ON COLUMN "public"."task"."task_end_time" IS '结束时间';
COMMENT ON COLUMN "public"."task"."head" IS '责任人';
COMMENT ON COLUMN "public"."task"."task_level" IS '任务等级';
COMMENT ON COLUMN "public"."task"."phone" IS '联系电话';
COMMENT ON TABLE "public"."task" IS '任务表';

-- ----------------------------
-- Primary Key structure for table task
-- ----------------------------
ALTER TABLE "public"."task" ADD CONSTRAINT "task_pkey" PRIMARY KEY ("id");
