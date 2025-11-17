/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : PostgreSQL
 Source Server Version : 130015
 Source Host           : localhost:5432
 Source Catalog        : teqin
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 130015
 File Encoding         : 65001

 Date: 17/11/2025 08:48:41
*/


-- ----------------------------
-- Table structure for scene_group_rule
-- ----------------------------
DROP TABLE IF EXISTS "public"."scene_group_rule";
CREATE TABLE "public"."scene_group_rule" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "scene_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "plan_node" varchar(50) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '警力部署'::character varying,
  "group_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "start_distance" int4 NOT NULL,
  "end_distance" int4 NOT NULL,
  "delete_flag" int2 NOT NULL DEFAULT 0,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Primary Key structure for table scene_group_rule
-- ----------------------------
ALTER TABLE "public"."scene_group_rule" ADD CONSTRAINT "scene_group_rule_pkey" PRIMARY KEY ("id");
