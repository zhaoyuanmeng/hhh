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

 Date: 13/12/2024 10:03:10
*/


-- ----------------------------
-- Table structure for draw_data
-- ----------------------------
DROP TABLE IF EXISTS "public"."draw_data";
CREATE TABLE "public"."draw_data" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(1024) COLLATE "pg_catalog"."default",
  "geom" geometry(GEOMETRY),
  "data" jsonb,
  "type" varchar(64) COLLATE "pg_catalog"."default",
  "scene_id" varchar(64) COLLATE "pg_catalog"."default",
  "delete_flag" int2,
  "task_id" varchar(64) COLLATE "pg_catalog"."default",
  "plan_node" varchar(255) COLLATE "pg_catalog"."default",
  "police_type" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."draw_data"."id" IS '数据ID';
COMMENT ON COLUMN "public"."draw_data"."name" IS '标绘数据名称';
COMMENT ON COLUMN "public"."draw_data"."geom" IS '空间位置';
COMMENT ON COLUMN "public"."draw_data"."data" IS '数据内容';
COMMENT ON COLUMN "public"."draw_data"."type" IS 'car、lines、police、uav、basic';
COMMENT ON COLUMN "public"."draw_data"."scene_id" IS '场景ID';
COMMENT ON COLUMN "public"."draw_data"."delete_flag" IS '删除标记';
COMMENT ON COLUMN "public"."draw_data"."task_id" IS '任务ID';
COMMENT ON COLUMN "public"."draw_data"."plan_node" IS '规划节点';
COMMENT ON COLUMN "public"."draw_data"."police_type" IS '警力标绘大类，执行警力、应急处突警力';
COMMENT ON TABLE "public"."draw_data" IS '标绘数据';

-- ----------------------------
-- Indexes structure for table draw_data
-- ----------------------------
CREATE INDEX "draw_data_geom_idx" ON "public"."draw_data" USING gist (
  "geom" "public"."gist_geometry_ops_2d"
);

-- ----------------------------
-- Primary Key structure for table draw_data
-- ----------------------------
ALTER TABLE "public"."draw_data" ADD CONSTRAINT "draw_data_pkey" PRIMARY KEY ("id");
