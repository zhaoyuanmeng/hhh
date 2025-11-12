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

 Date: 21/02/2025 13:42:30
*/


-- ----------------------------
-- Table structure for t_res_tag_dict
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_res_tag_dict";
CREATE TABLE "public"."t_res_tag_dict" (
  "id" varchar(255) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "code" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(255) COLLATE "pg_catalog"."default",
  "isdefault" varchar(255) COLLATE "pg_catalog"."default",
  "bdp_audit" varchar COLLATE "pg_catalog"."default",
  "unionkey" varchar COLLATE "pg_catalog"."default",
  "user_design_id" int8
)
;
COMMENT ON TABLE "public"."t_res_tag_dict" IS '智慧社区_标签表';

-- ----------------------------
-- Indexes structure for table t_res_tag_dict
-- ----------------------------
CREATE INDEX "index_t_res_tag_dict" ON "public"."t_res_tag_dict" USING btree (
  "name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "t_res_tag_dict_code" ON "public"."t_res_tag_dict" USING btree (
  "code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "t_res_tag_dict_name" ON "public"."t_res_tag_dict" USING btree (
  "name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
