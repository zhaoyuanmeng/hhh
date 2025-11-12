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

 Date: 18/02/2025 15:09:57
*/


-- ----------------------------
-- Table structure for commercial_lease_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."commercial_lease_info";
CREATE TABLE "public"."commercial_lease_info" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "code" varchar COLLATE "pg_catalog"."default" NOT NULL,
  "status" int4,
  "tenant" varchar COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6),
  "title" varchar COLLATE "pg_catalog"."default",
  "intention_flag" int4 DEFAULT 0
)
;
COMMENT ON TABLE "public"."commercial_lease_info" IS '办公楼租赁数据';

-- ----------------------------
-- Records of commercial_lease_info
-- ----------------------------
INSERT INTO "public"."commercial_lease_info" VALUES ('d68f02c0-dee7-43cf-b343-0e71e1092f5d', 'b521d32b-3e63-4aed-9cab-d13d1dd6e623', 255, '跨境电商', '2025-02-13 17:36:17.502408', '2025-02-18 11:17:34.043401', 'B003', 0);
