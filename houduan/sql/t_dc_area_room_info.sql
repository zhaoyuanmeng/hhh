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

 Date: 21/02/2025 13:40:16
*/


-- ----------------------------
-- Table structure for t_dc_area_room_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_dc_area_room_info";
CREATE TABLE "public"."t_dc_area_room_info" (
  "room_id" varchar(255) COLLATE "pg_catalog"."default",
  "org_id" varchar(255) COLLATE "pg_catalog"."default",
  "building_id" varchar(255) COLLATE "pg_catalog"."default",
  "room_number" varchar(255) COLLATE "pg_catalog"."default",
  "room_address" varchar(255) COLLATE "pg_catalog"."default",
  "room_source" varchar(255) COLLATE "pg_catalog"."default",
  "structure_type" varchar(255) COLLATE "pg_catalog"."default",
  "room_type" varchar(255) COLLATE "pg_catalog"."default",
  "design_type" varchar(255) COLLATE "pg_catalog"."default",
  "construction_area" varchar(255) COLLATE "pg_catalog"."default",
  "used_area" varchar(255) COLLATE "pg_catalog"."default",
  "share_area" varchar(255) COLLATE "pg_catalog"."default",
  "floor" varchar(255) COLLATE "pg_catalog"."default",
  "url_room_design_pic_2d" varchar(255) COLLATE "pg_catalog"."default",
  "url_room_design_pic_3d" varchar(255) COLLATE "pg_catalog"."default",
  "url_floor_design_pic_2d" varchar(255) COLLATE "pg_catalog"."default",
  "load_time" varchar(255) COLLATE "pg_catalog"."default",
  "update_date" varchar(255) COLLATE "pg_catalog"."default",
  "status" varchar(255) COLLATE "pg_catalog"."default",
  "status_time" varchar(255) COLLATE "pg_catalog"."default",
  "room_name" varchar(255) COLLATE "pg_catalog"."default",
  "house_structure" varchar(255) COLLATE "pg_catalog"."default",
  "unit_code" varchar(255) COLLATE "pg_catalog"."default",
  "unit_nature" varchar(255) COLLATE "pg_catalog"."default",
  "unit_uses" varchar(255) COLLATE "pg_catalog"."default",
  "owner" varchar(255) COLLATE "pg_catalog"."default",
  "owner_phone" varchar(255) COLLATE "pg_catalog"."default",
  "create_user" varchar(255) COLLATE "pg_catalog"."default",
  "create_date" varchar(255) COLLATE "pg_catalog"."default",
  "update_user" varchar(255) COLLATE "pg_catalog"."default",
  "owner_rs_id" varchar(255) COLLATE "pg_catalog"."default",
  "agent_rs_id" varchar(255) COLLATE "pg_catalog"."default",
  "house_room_count" varchar(255) COLLATE "pg_catalog"."default",
  "house_owner_certificate" varchar(255) COLLATE "pg_catalog"."default",
  "is_rent" varchar(255) COLLATE "pg_catalog"."default",
  "address_type" varchar(255) COLLATE "pg_catalog"."default",
  "is_refresh_owner" varchar(255) COLLATE "pg_catalog"."default",
  "is_refresh_agent" varchar(255) COLLATE "pg_catalog"."default",
  "floor_ml" varchar(255) COLLATE "pg_catalog"."default",
  "car_no" varchar(255) COLLATE "pg_catalog"."default",
  "car_ku" varchar(255) COLLATE "pg_catalog"."default",
  "rentcode" varchar(255) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "is_bind" varchar(255) COLLATE "pg_catalog"."default",
  "water_meter_id" varchar(255) COLLATE "pg_catalog"."default",
  "guid" varchar(255) COLLATE "pg_catalog"."default",
  "qyrq" varchar(255) COLLATE "pg_catalog"."default",
  "tyrq" varchar(255) COLLATE "pg_catalog"."default",
  "room_status" varchar(255) COLLATE "pg_catalog"."default",
  "room_kind" varchar(255) COLLATE "pg_catalog"."default",
  "province_id" varchar(255) COLLATE "pg_catalog"."default",
  "address_id" varchar(255) COLLATE "pg_catalog"."default",
  "use_nature" varchar(255) COLLATE "pg_catalog"."default",
  "info_org_code" varchar(255) COLLATE "pg_catalog"."default",
  "unittype" varchar(255) COLLATE "pg_catalog"."default",
  "is_synchronization" varchar(255) COLLATE "pg_catalog"."default",
  "correction_status" varchar(255) COLLATE "pg_catalog"."default",
  "unit_number" varchar(255) COLLATE "pg_catalog"."default",
  "is_empty_account" varchar(255) COLLATE "pg_catalog"."default",
  "house_owner_certificate_date" varchar(255) COLLATE "pg_catalog"."default",
  "transverse" varchar(255) COLLATE "pg_catalog"."default",
  "portrait" varchar(255) COLLATE "pg_catalog"."default",
  "building_uses" varchar(255) COLLATE "pg_catalog"."default",
  "risk_type" varchar(255) COLLATE "pg_catalog"."default",
  "room_check_status" varchar(255) COLLATE "pg_catalog"."default",
  "repair_types" varchar(255) COLLATE "pg_catalog"."default",
  "room_check_time" varchar(255) COLLATE "pg_catalog"."default",
  "real_rs_num" varchar(255) COLLATE "pg_catalog"."default",
  "room_handle_status" varchar(255) COLLATE "pg_catalog"."default",
  "room_is_exist" varchar(255) COLLATE "pg_catalog"."default",
  "message_accuracy" varchar(255) COLLATE "pg_catalog"."default",
  "room_remark" varchar(255) COLLATE "pg_catalog"."default",
  "is_odb_code" varchar(255) COLLATE "pg_catalog"."default",
  "rent_status" varchar(255) COLLATE "pg_catalog"."default",
  "is_visit" varchar(255) COLLATE "pg_catalog"."default",
  "family_info" varchar(255) COLLATE "pg_catalog"."default",
  "check_status" varchar(255) COLLATE "pg_catalog"."default",
  "is_sign_security_letter" varchar(255) COLLATE "pg_catalog"."default",
  "is_facilities_perfect" varchar(255) COLLATE "pg_catalog"."default",
  "is_keep_pets" varchar(255) COLLATE "pg_catalog"."default",
  "pets_number" varchar(255) COLLATE "pg_catalog"."default",
  "pets_type" varchar(255) COLLATE "pg_catalog"."default",
  "is_large_pet" varchar(255) COLLATE "pg_catalog"."default",
  "remove_house" varchar(255) COLLATE "pg_catalog"."default",
  "is_live_to_commercial" varchar(255) COLLATE "pg_catalog"."default",
  "is_group_rent" varchar(255) COLLATE "pg_catalog"."default",
  "is_homestay" varchar(255) COLLATE "pg_catalog"."default",
  "is_zero_job" varchar(255) COLLATE "pg_catalog"."default",
  "house_income_source" varchar(255) COLLATE "pg_catalog"."default",
  "estate_id" varchar(255) COLLATE "pg_catalog"."default",
  "sync_time" varchar(255) COLLATE "pg_catalog"."default",
  "bdp_audit" varchar COLLATE "pg_catalog"."default",
  "unionkey" varchar COLLATE "pg_catalog"."default",
  "user_design_id" int8
)
;
COMMENT ON TABLE "public"."t_dc_area_room_info" IS '智慧社区_保障房管理';

-- ----------------------------
-- Indexes structure for table t_dc_area_room_info
-- ----------------------------
CREATE INDEX "index_t_dc_area_room_info" ON "public"."t_dc_area_room_info" USING btree (
  "room_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "room_address" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "index_t_dc_area_room_info_ids" ON "public"."t_dc_area_room_info" USING btree (
  "room_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "building_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
