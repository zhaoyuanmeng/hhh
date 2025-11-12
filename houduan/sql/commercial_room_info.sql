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

 Date: 18/02/2025 15:10:06
*/


-- ----------------------------
-- Table structure for commercial_room_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."commercial_room_info";
CREATE TABLE "public"."commercial_room_info" (
  "id" varchar(64) COLLATE "pg_catalog"."default",
  "room_code" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "room_title" varchar(255) COLLATE "pg_catalog"."default",
  "floor_code" varchar(255) COLLATE "pg_catalog"."default",
  "floor_title" varchar(255) COLLATE "pg_catalog"."default",
  "building_code" varchar(255) COLLATE "pg_catalog"."default",
  "building_title" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
  "x" varchar(64) COLLATE "pg_catalog"."default" DEFAULT 0,
  "y" varchar(64) COLLATE "pg_catalog"."default" DEFAULT 0,
  "z" varchar(64) COLLATE "pg_catalog"."default" DEFAULT 0,
  "area_structure" varchar(64) COLLATE "pg_catalog"."default",
  "area_usable" varchar(64) COLLATE "pg_catalog"."default",
  "merchant_type" int4,
  "link_id" varchar(64) COLLATE "pg_catalog"."default",
  "height" varchar(64) COLLATE "pg_catalog"."default",
  "load" varchar(64) COLLATE "pg_catalog"."default",
  "water" varchar(64) COLLATE "pg_catalog"."default",
  "waste_oil" varchar(64) COLLATE "pg_catalog"."default",
  "waste_industrial" varchar(64) COLLATE "pg_catalog"."default",
  "waste_sewage" varchar(64) COLLATE "pg_catalog"."default",
  "strong_electricity" varchar(64) COLLATE "pg_catalog"."default",
  "size_lampblack" varchar(64) COLLATE "pg_catalog"."default",
  "fuel_gas" varchar(64) COLLATE "pg_catalog"."default",
  "building_id" varchar(64) COLLATE "pg_catalog"."default",
  "floor_number" int2
)
;
COMMENT ON COLUMN "public"."commercial_room_info"."merchant_type" IS '0办公1商业';
COMMENT ON TABLE "public"."commercial_room_info" IS '办公楼房间数据';

-- ----------------------------
-- Records of commercial_room_info
-- ----------------------------
INSERT INTO "public"."commercial_room_info" VALUES ('6', 'ZL003C0409', '3号楼C座409', 'ZL003C04', '3号楼C座4层', 'ZL003C', '3号楼C座', '2024-07-08 15:33:26.419835', '491802.851875', '4324475.84', '12.797032', '197.78', '119.47', 0, '0b50e484-d1f3-43f3-8524-ee699087c5bc', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2a5ab4d7-5fce-4a24-9eac-ddca5a91b276', 4);

-- ----------------------------
-- Primary Key structure for table commercial_room_info
-- ----------------------------
ALTER TABLE "public"."commercial_room_info" ADD CONSTRAINT "commercial_room_info_copy1_pkey" PRIMARY KEY ("room_code");
