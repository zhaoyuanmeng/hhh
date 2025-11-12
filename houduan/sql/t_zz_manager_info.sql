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

 Date: 21/02/2025 13:41:21
*/


-- ----------------------------
-- Table structure for t_zz_manager_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_zz_manager_info";
CREATE TABLE "public"."t_zz_manager_info" (
  "m_id" varchar(255) COLLATE "pg_catalog"."default",
  "module_id" varchar(255) COLLATE "pg_catalog"."default",
  "module_type" varchar(255) COLLATE "pg_catalog"."default",
  "rs_type" varchar(255) COLLATE "pg_catalog"."default",
  "rs_name" varchar(255) COLLATE "pg_catalog"."default",
  "rs_card_type" varchar(255) COLLATE "pg_catalog"."default",
  "rs_card_code" varchar(255) COLLATE "pg_catalog"."default",
  "rs_phone" varchar(255) COLLATE "pg_catalog"."default",
  "party_id" varchar(255) COLLATE "pg_catalog"."default",
  "creator" varchar(255) COLLATE "pg_catalog"."default",
  "created" varchar(255) COLLATE "pg_catalog"."default",
  "updater" varchar(255) COLLATE "pg_catalog"."default",
  "updated" varchar(255) COLLATE "pg_catalog"."default",
  "status" varchar(255) COLLATE "pg_catalog"."default",
  "relation" varchar(255) COLLATE "pg_catalog"."default",
  "jt_politics" varchar(255) COLLATE "pg_catalog"."default",
  "bdp_audit" varchar COLLATE "pg_catalog"."default",
  "unionkey" varchar COLLATE "pg_catalog"."default",
  "user_design_id" int8
)
;
COMMENT ON TABLE "public"."t_zz_manager_info" IS '智慧社区_负责人信息表';
