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

 Date: 31/12/2024 09:30:12
*/


-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role";
CREATE TABLE "public"."sys_role" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "role_name" varchar(255) COLLATE "pg_catalog"."default",
  "role_code" varchar(64) COLLATE "pg_catalog"."default",
  "resources" text COLLATE "pg_catalog"."default",
  "remark" text COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "delete_flag" int2
)
;
COMMENT ON COLUMN "public"."sys_role"."id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_role"."role_name" IS '角色名称';
COMMENT ON COLUMN "public"."sys_role"."role_code" IS '角色编码';
COMMENT ON COLUMN "public"."sys_role"."resources" IS '资源权限';
COMMENT ON COLUMN "public"."sys_role"."remark" IS '备注';
COMMENT ON COLUMN "public"."sys_role"."delete_flag" IS '删除标志，-1删除，0 未删除';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "public"."sys_role" VALUES ('1', '超级管理员', NULL, '工作驾驶舱,基础数据,任务管理,后台管理', NULL, NULL, '2024-09-04 13:55:31.443343', 0);

-- ----------------------------
-- Primary Key structure for table sys_role
-- ----------------------------
ALTER TABLE "public"."sys_role" ADD CONSTRAINT "role_pkey" PRIMARY KEY ("id");
