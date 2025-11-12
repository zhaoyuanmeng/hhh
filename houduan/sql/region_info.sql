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

 Date: 13/12/2024 10:01:43
*/


-- ----------------------------
-- Table structure for region_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."region_info";
CREATE TABLE "public"."region_info" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(64) COLLATE "pg_catalog"."default",
  "x" varchar(64) COLLATE "pg_catalog"."default",
  "y" varchar(64) COLLATE "pg_catalog"."default",
  "z" varchar(64) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."region_info"."name" IS '区域名称';
COMMENT ON COLUMN "public"."region_info"."type" IS '社区、商服';
COMMENT ON TABLE "public"."region_info" IS '区域信息';

-- ----------------------------
-- Records of region_info
-- ----------------------------
INSERT INTO "public"."region_info" VALUES ('593d7b96-143a-4add-bba1-3cc0654dcdd1', '商服中心', '商服', NULL, NULL, NULL);
INSERT INTO "public"."region_info" VALUES ('8bfd3dea-7087-4197-b782-a988033d4811', '中关村产业', '商服', NULL, NULL, NULL);
INSERT INTO "public"."region_info" VALUES ('2649c4d4-6497-4e47-824d-9dee48adb2d3', '阳光园', '社区', '492423.5525', '4324991.68', '3.7644140625');
INSERT INTO "public"."region_info" VALUES ('d7fdbcab-4cc1-445f-8cd9-9eca732d7fb8', '繁星园', '社区', '492376.89', '4325138.88', '17.0422265625');
INSERT INTO "public"."region_info" VALUES ('edf47702-9bd9-4e0c-b966-0dc05d1ba319', '明月园', '社区', '492190.36125', '4325352.32', ' 8.6119921875');
INSERT INTO "public"."region_info" VALUES ('165b84d9-a2e6-48f1-83ce-6de9bf8c9002', '明珠园', '社区', '491979.77', '4325997.76', '5.69486328125');
INSERT INTO "public"."region_info" VALUES ('37a0a518-3f25-4990-970b-6f6ace9ba0a8', '明月小区', NULL, '1', '2', '3');

-- ----------------------------
-- Primary Key structure for table region_info
-- ----------------------------
ALTER TABLE "public"."region_info" ADD CONSTRAINT "region_info_pkey" PRIMARY KEY ("id");
