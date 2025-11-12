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

 Date: 21/02/2025 13:40:43
*/


-- ----------------------------
-- Table structure for t_dc_ci_rs
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_dc_ci_rs";
CREATE TABLE "public"."t_dc_ci_rs" (
  "ci_rs_id" varchar(255) COLLATE "pg_catalog"."default",
  "party_id" varchar(255) COLLATE "pg_catalog"."default",
  "i_identity_card" varchar(255) COLLATE "pg_catalog"."default",
  "i_name" varchar(255) COLLATE "pg_catalog"."default",
  "registry_new_property" varchar(255) COLLATE "pg_catalog"."default",
  "house_reside" varchar(255) COLLATE "pg_catalog"."default",
  "registry_old" varchar(255) COLLATE "pg_catalog"."default",
  "registry_old_property" varchar(255) COLLATE "pg_catalog"."default",
  "house_source" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(255) COLLATE "pg_catalog"."default",
  "committee" varchar(255) COLLATE "pg_catalog"."default",
  "registry_new_date" varchar(255) COLLATE "pg_catalog"."default",
  "registry_move_in" varchar(255) COLLATE "pg_catalog"."default",
  "registry_move_in_date" varchar(255) COLLATE "pg_catalog"."default",
  "guardian_name" varchar(255) COLLATE "pg_catalog"."default",
  "guardian_id_card" varchar(255) COLLATE "pg_catalog"."default",
  "identity" varchar(255) COLLATE "pg_catalog"."default",
  "identity_type" varchar(255) COLLATE "pg_catalog"."default",
  "subdistrictid" varchar(255) COLLATE "pg_catalog"."default",
  "subdistrictname" varchar(255) COLLATE "pg_catalog"."default",
  "retired_date" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" varchar(255) COLLATE "pg_catalog"."default",
  "status" varchar(255) COLLATE "pg_catalog"."default",
  "status_time" varchar(255) COLLATE "pg_catalog"."default",
  "operate_user" varchar(255) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "is_leave_reside" varchar(255) COLLATE "pg_catalog"."default",
  "area_code" varchar(255) COLLATE "pg_catalog"."default",
  "used_name" varchar(255) COLLATE "pg_catalog"."default",
  "the_aged_insurance" varchar(255) COLLATE "pg_catalog"."default",
  "medical_insurance" varchar(255) COLLATE "pg_catalog"."default",
  "other_insurance" varchar(255) COLLATE "pg_catalog"."default",
  "cq_rs_type" varchar(255) COLLATE "pg_catalog"."default",
  "org_code" varchar(255) COLLATE "pg_catalog"."default",
  "data_source" varchar(255) COLLATE "pg_catalog"."default",
  "i_residence_addr" varchar(255) COLLATE "pg_catalog"."default",
  "residence_addr_no" varchar(255) COLLATE "pg_catalog"."default",
  "residence_addr_house_no" varchar(255) COLLATE "pg_catalog"."default",
  "check_id" varchar(255) COLLATE "pg_catalog"."default",
  "i_hold_unit" varchar(255) COLLATE "pg_catalog"."default",
  "cometime" varchar(255) COLLATE "pg_catalog"."default",
  "estate_code" varchar(255) COLLATE "pg_catalog"."default",
  "outtime" varchar(255) COLLATE "pg_catalog"."default",
  "is_to_es" varchar(255) COLLATE "pg_catalog"."default",
  "address_source" varchar(255) COLLATE "pg_catalog"."default",
  "is_bind" varchar(255) COLLATE "pg_catalog"."default",
  "address_id" varchar(255) COLLATE "pg_catalog"."default",
  "live_type" varchar(255) COLLATE "pg_catalog"."default",
  "housepersonid" varchar(255) COLLATE "pg_catalog"."default",
  "is_to_province" varchar(255) COLLATE "pg_catalog"."default",
  "province_id" varchar(255) COLLATE "pg_catalog"."default",
  "address_attribute" varchar(255) COLLATE "pg_catalog"."default",
  "wys_residentid" varchar(255) COLLATE "pg_catalog"."default",
  "is_empty_hang" varchar(255) COLLATE "pg_catalog"."default",
  "flow_reason" varchar(255) COLLATE "pg_catalog"."default",
  "residence_address_type" varchar(255) COLLATE "pg_catalog"."default",
  "longitude" varchar(255) COLLATE "pg_catalog"."default",
  "latitude" varchar(255) COLLATE "pg_catalog"."default",
  "residence_period" varchar(255) COLLATE "pg_catalog"."default",
  "local_situation" varchar(255) COLLATE "pg_catalog"."default",
  "is_location" varchar(255) COLLATE "pg_catalog"."default",
  "systemid" varchar(255) COLLATE "pg_catalog"."default",
  "org_name" varchar(255) COLLATE "pg_catalog"."default",
  "org_id" varchar(255) COLLATE "pg_catalog"."default",
  "house_relation" varchar(255) COLLATE "pg_catalog"."default",
  "sync_time" varchar(255) COLLATE "pg_catalog"."default",
  "bdp_audit" varchar COLLATE "pg_catalog"."default",
  "unionkey" varchar COLLATE "pg_catalog"."default",
  "user_design_id" int8
)
;
COMMENT ON TABLE "public"."t_dc_ci_rs" IS '智慧社区_居民信息表';

-- ----------------------------
-- Indexes structure for table t_dc_ci_rs
-- ----------------------------
CREATE INDEX "index_t_dc_ci_rs_i_residence_addr" ON "public"."t_dc_ci_rs" USING btree (
  "i_residence_addr" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "index_t_dc_ci_rs_party_id" ON "public"."t_dc_ci_rs" USING btree (
  "party_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
