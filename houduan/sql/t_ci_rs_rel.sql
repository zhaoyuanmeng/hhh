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

 Date: 21/02/2025 13:41:37
*/


-- ----------------------------
-- Table structure for t_ci_rs_rel
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_ci_rs_rel";
CREATE TABLE "public"."t_ci_rs_rel" (
  "id" varchar(255) COLLATE "pg_catalog"."default",
  "rel_id" varchar(255) COLLATE "pg_catalog"."default",
  "ci_rs_id" varchar(255) COLLATE "pg_catalog"."default",
  "grid_code" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" varchar(255) COLLATE "pg_catalog"."default",
  "rel_code" varchar(255) COLLATE "pg_catalog"."default",
  "status_time" varchar(255) COLLATE "pg_catalog"."default",
  "party_id" varchar(255) COLLATE "pg_catalog"."default",
  "is_to_es" varchar(255) COLLATE "pg_catalog"."default",
  "bdp_audit" varchar(255) COLLATE "pg_catalog"."default",
  "unionkey" varchar(255) COLLATE "pg_catalog"."default",
  "user_design_id" int4
)
;
COMMENT ON TABLE "public"."t_ci_rs_rel" IS '智慧社区_居民及特征关系对应表按天增量';

-- ----------------------------
-- Indexes structure for table t_ci_rs_rel
-- ----------------------------
CREATE INDEX "index_t_ci_rs_rel" ON "public"."t_ci_rs_rel" USING btree (
  "rel_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "party_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "index_t_ci_rs_rel_party_id" ON "public"."t_ci_rs_rel" USING btree (
  "party_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "index_t_ci_rs_rel_rel_code" ON "public"."t_ci_rs_rel" USING btree (
  "rel_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
