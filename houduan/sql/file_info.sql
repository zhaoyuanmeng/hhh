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

 Date: 22/07/2024 08:41:37
*/


-- ----------------------------
-- Table structure for file_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."file_info";
CREATE TABLE "public"."file_info" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "group_id" varchar(64) COLLATE "pg_catalog"."default",
  "original_file_name" varchar(255) COLLATE "pg_catalog"."default",
  "new_file_name" varchar(255) COLLATE "pg_catalog"."default",
  "file_ext" varchar(255) COLLATE "pg_catalog"."default",
  "file_size" int8,
  "file_path" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "create_user_id" varchar(255) COLLATE "pg_catalog"."default",
  "file_type" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."file_info"."id" IS '文件ID';
COMMENT ON COLUMN "public"."file_info"."group_id" IS '文件组ID';
COMMENT ON COLUMN "public"."file_info"."original_file_name" IS '原始文件名';
COMMENT ON COLUMN "public"."file_info"."new_file_name" IS '新文件名';
COMMENT ON COLUMN "public"."file_info"."file_ext" IS '文件扩展名';
COMMENT ON COLUMN "public"."file_info"."file_size" IS '文件大小';
COMMENT ON COLUMN "public"."file_info"."file_path" IS '文件路径';
COMMENT ON COLUMN "public"."file_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."file_info"."create_user_id" IS '创建用户';
COMMENT ON COLUMN "public"."file_info"."file_type" IS '文件类型';
COMMENT ON TABLE "public"."file_info" IS '文件信息表';

-- ----------------------------
-- Primary Key structure for table file_info
-- ----------------------------
ALTER TABLE "public"."file_info" ADD CONSTRAINT "file_pkey" PRIMARY KEY ("id");
