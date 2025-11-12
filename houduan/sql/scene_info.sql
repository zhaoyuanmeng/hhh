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

 Date: 13/12/2024 14:21:58
*/


-- ----------------------------
-- Table structure for scene_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."scene_info";
CREATE TABLE "public"."scene_info" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "task_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "scene_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "type" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "begin_time" timestamp(6),
  "end_time" timestamp(6),
  "head" varchar(64) COLLATE "pg_catalog"."default",
  "phone" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "delete_flag" int2,
  "scene_frame" jsonb,
  "key_frames" jsonb,
  "draw_data" jsonb,
  "floors" jsonb,
  "time" int2,
  "scene_desc" varchar(255) COLLATE "pg_catalog"."default",
  "basic_data_id" varchar(64) COLLATE "pg_catalog"."default",
  "scene_road_desc" varchar(1024) COLLATE "pg_catalog"."default",
  "scene_road_length" float8,
  "scene_road_time" float8,
  "third_time" int2,
  "third_key_frames" jsonb,
  "view_data" jsonb,
  "scheme_flag" int2,
  "level" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."scene_info"."task_id" IS '关联任务ID';
COMMENT ON COLUMN "public"."scene_info"."scene_name" IS '名称';
COMMENT ON COLUMN "public"."scene_info"."type" IS '类型 1路线 2 现场 3 住地  方案（4高速 5高铁）';
COMMENT ON COLUMN "public"."scene_info"."begin_time" IS '开始时间';
COMMENT ON COLUMN "public"."scene_info"."end_time" IS '结束时间';
COMMENT ON COLUMN "public"."scene_info"."head" IS '负责人';
COMMENT ON COLUMN "public"."scene_info"."phone" IS '联系电话';
COMMENT ON COLUMN "public"."scene_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."scene_info"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."scene_info"."delete_flag" IS '删除标记 0未删除 1已删除';
COMMENT ON COLUMN "public"."scene_info"."scene_frame" IS '场景数据';
COMMENT ON COLUMN "public"."scene_info"."key_frames" IS '漫游数据';
COMMENT ON COLUMN "public"."scene_info"."draw_data" IS '标绘数据';
COMMENT ON COLUMN "public"."scene_info"."floors" IS '楼层数据';
COMMENT ON COLUMN "public"."scene_info"."time" IS '漫游时间';
COMMENT ON COLUMN "public"."scene_info"."scheme_flag" IS '方案标记 1是 其他否';
COMMENT ON COLUMN "public"."scene_info"."level" IS '方案等级';
COMMENT ON TABLE "public"."scene_info" IS '场景表';

-- ----------------------------
-- Primary Key structure for table scene_info
-- ----------------------------
ALTER TABLE "public"."scene_info" ADD CONSTRAINT "scene_copy1_pkey" PRIMARY KEY ("id");
