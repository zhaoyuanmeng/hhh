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

 Date: 21/01/2025 12:07:08
*/


-- ----------------------------
-- Table structure for point_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."point_info";
CREATE TABLE "public"."point_info" (
  "id" varchar(64) COLLATE "pg_catalog"."default" PRIMARY KEY NOT NULL,
  "child_type_id" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "data" jsonb,
  "jcxx_id" varchar(64) COLLATE "pg_catalog"."default",
  "parent_type_id" varchar(64) COLLATE "pg_catalog"."default",
  "geom" geometry(GEOMETRY),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."point_info"."id" IS 'id';
COMMENT ON COLUMN "public"."point_info"."child_type_id" IS '分类ID';
COMMENT ON COLUMN "public"."point_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."point_info"."data" IS '数据';
COMMENT ON COLUMN "public"."point_info"."jcxx_id" IS '基础信息ID';
COMMENT ON COLUMN "public"."point_info"."parent_type_id" IS '父类ID';
COMMENT ON COLUMN "public"."point_info"."geom" IS '空间位置';
COMMENT ON COLUMN "public"."point_info"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."point_info" IS '点位信息表';

-- ----------------------------
-- Records of point_info
-- ----------------------------
INSERT INTO "public"."point_info" VALUES ('a036c3dbc6574fc39789ed7c2771ee86', '72cf70077c0148d4b89448072989ebda', '2022-08-29 17:33:57', '{"photo": null, "title": "大广高速", "video": null, "weidu": "", "jingdu": "", "qidian": "1397", "geoType": {}, "zhidian": "1435", "TotalLengthOfMileage": 28.65}', NULL, '8374ec30c3724f179074c9186bd716a9', NULL, NULL);

