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

 Date: 31/12/2024 09:30:00
*/


-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user";
CREATE TABLE "public"."sys_user" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "real_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "user_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "password" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "role_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "phone" varchar(11) COLLATE "pg_catalog"."default" NOT NULL,
  "sex" int2 NOT NULL,
  "org_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int2 NOT NULL,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "delete_flag" int2 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_user"."id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user"."real_name" IS '真实姓名';
COMMENT ON COLUMN "public"."sys_user"."user_name" IS '用户名';
COMMENT ON COLUMN "public"."sys_user"."password" IS '密码';
COMMENT ON COLUMN "public"."sys_user"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_user"."phone" IS '电话';
COMMENT ON COLUMN "public"."sys_user"."sex" IS '性别，0男，1女';
COMMENT ON COLUMN "public"."sys_user"."org_id" IS '机构ID';
COMMENT ON COLUMN "public"."sys_user"."status" IS '状态，0正常，-1异常';
COMMENT ON COLUMN "public"."sys_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_user"."delete_flag" IS '删除标志，-1删除，0 未删除';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "public"."sys_user" VALUES ('1', '管理员', 'root', '123456', '1', '18112345678', 0, '1', 0, NULL, NULL, 0);

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user" ADD CONSTRAINT "user_pkey" PRIMARY KEY ("id");
