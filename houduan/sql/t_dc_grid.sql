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

 Date: 21/02/2025 13:40:51
*/


-- ----------------------------
-- Table structure for t_dc_grid
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_dc_grid";
CREATE TABLE "public"."t_dc_grid" (
  "grid_id" varchar(255) COLLATE "pg_catalog"."default",
  "info_org_id" varchar(255) COLLATE "pg_catalog"."default",
  "info_org_code" varchar(255) COLLATE "pg_catalog"."default",
  "parent_grid_id" varchar(255) COLLATE "pg_catalog"."default",
  "grid_name" varchar(255) COLLATE "pg_catalog"."default",
  "grid_code" varchar(255) COLLATE "pg_catalog"."default",
  "grid_level" varchar(255) COLLATE "pg_catalog"."default",
  "grid_area" varchar(255) COLLATE "pg_catalog"."default",
  "grid_type" varchar(255) COLLATE "pg_catalog"."default",
  "duty_telnum" varchar(255) COLLATE "pg_catalog"."default",
  "grid_photo" varchar(255) COLLATE "pg_catalog"."default",
  "model_id" varchar(255) COLLATE "pg_catalog"."default",
  "user_id" varchar(255) COLLATE "pg_catalog"."default",
  "result_table_name" varchar(255) COLLATE "pg_catalog"."default",
  "map_zoom" varchar(255) COLLATE "pg_catalog"."default",
  "longitude" varchar(255) COLLATE "pg_catalog"."default",
  "latitude" varchar(255) COLLATE "pg_catalog"."default",
  "status" varchar(255) COLLATE "pg_catalog"."default",
  "create_user" varchar(255) COLLATE "pg_catalog"."default",
  "create_date" varchar(255) COLLATE "pg_catalog"."default",
  "update_user" varchar(255) COLLATE "pg_catalog"."default",
  "update_date" varchar(255) COLLATE "pg_catalog"."default",
  "map_type" varchar(255) COLLATE "pg_catalog"."default",
  "grid_status" varchar(255) COLLATE "pg_catalog"."default",
  "grid_model" varchar(255) COLLATE "pg_catalog"."default",
  "ic_org_id" varchar(255) COLLATE "pg_catalog"."default",
  "order_id" varchar(255) COLLATE "pg_catalog"."default",
  "grid_manager_name" varchar(255) COLLATE "pg_catalog"."default",
  "grid_desc" varchar(255) COLLATE "pg_catalog"."default",
  "grid_path" varchar(255) COLLATE "pg_catalog"."default",
  "community_type" varchar(255) COLLATE "pg_catalog"."default",
  "grid_type_2" varchar(255) COLLATE "pg_catalog"."default",
  "grid_initials" varchar(255) COLLATE "pg_catalog"."default",
  "grid_property" varchar(255) COLLATE "pg_catalog"."default",
  "new_departmentno" varchar(255) COLLATE "pg_catalog"."default",
  "wys_id" varchar(255) COLLATE "pg_catalog"."default",
  "region_type" varchar(255) COLLATE "pg_catalog"."default",
  "province_id" varchar(255) COLLATE "pg_catalog"."default",
  "province_code" varchar(255) COLLATE "pg_catalog"."default",
  "is_perfect" varchar(255) COLLATE "pg_catalog"."default",
  "reg_num" varchar(255) COLLATE "pg_catalog"."default",
  "declare_floating_population" varchar(255) COLLATE "pg_catalog"."default",
  "declare_house_amount" varchar(255) COLLATE "pg_catalog"."default",
  "provincial_unified_coding" varchar(255) COLLATE "pg_catalog"."default",
  "bdp_audit" varchar COLLATE "pg_catalog"."default",
  "unionkey" varchar COLLATE "pg_catalog"."default",
  "user_design_id" int8
)
;
COMMENT ON TABLE "public"."t_dc_grid" IS '网格';
