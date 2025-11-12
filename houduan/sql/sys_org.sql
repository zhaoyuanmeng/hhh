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

 Date: 31/12/2024 09:30:24
*/


-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_org";
CREATE TABLE "public"."sys_org" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "short_name" varchar(128) COLLATE "pg_catalog"."default",
  "full_name" varchar(255) COLLATE "pg_catalog"."default",
  "remark" text COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "delete_flag" int2
)
;
COMMENT ON COLUMN "public"."sys_org"."delete_flag" IS '删除标志，-1删除，0 未删除';

-- ----------------------------
-- Records of sys_org
-- ----------------------------
INSERT INTO "public"."sys_org" VALUES ('1', '雄创', '雄创', NULL, NULL, NULL, 0);

-- ----------------------------
-- Primary Key structure for table sys_org
-- ----------------------------
ALTER TABLE "public"."sys_org" ADD CONSTRAINT "org_pkey" PRIMARY KEY ("id");
