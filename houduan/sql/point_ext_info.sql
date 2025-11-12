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

 Date: 21/01/2025 12:07:22
*/


-- ----------------------------
-- Table structure for point_ext_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."point_ext_info";
CREATE TABLE "public"."point_ext_info" (
  "id" varchar(64) COLLATE "pg_catalog"."default" PRIMARY KEY NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(255) COLLATE "pg_catalog"."default",
  "geom" geometry(GEOMETRY)
)
;
COMMENT ON COLUMN "public"."point_ext_info"."name" IS '名称';
COMMENT ON COLUMN "public"."point_ext_info"."type" IS '类型';
COMMENT ON COLUMN "public"."point_ext_info"."geom" IS '空间位置';

-- ----------------------------
-- Records of point_ext_info


