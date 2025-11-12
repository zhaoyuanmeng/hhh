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

 Date: 24/02/2025 08:29:03
*/


-- ----------------------------
-- Table structure for cover_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."cover_info";
CREATE TABLE "public"."cover_info" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "device_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "model_type" char(2) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '03'::bpchar,
  "client_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "addr" varchar(255) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "lng" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "lat" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "is_alarm" int2,
  "active_at" timestamp(6),
  "last_upload_at" timestamp(6),
  "status" varchar(50) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'inactive'::character varying
)
;

-- ----------------------------
-- Records of cover_info
-- ----------------------------
INSERT INTO "public"."cover_info" VALUES ('62791', '雄昝-S042-W2-N4-LD62返厂', '03', '864383062508770', '河北省保定市雄县朱各庄镇042省道', '116.09185411', '39.03507243', 1, '2024-08-13 15:12:22', '2025-02-20 01:58:07', 'online');

-- ----------------------------
-- Primary Key structure for table cover_info
-- ----------------------------
ALTER TABLE "public"."cover_info" ADD CONSTRAINT "cover_info_pkey" PRIMARY KEY ("id");
