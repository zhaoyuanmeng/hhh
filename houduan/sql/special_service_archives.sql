
DROP TABLE IF EXISTS "public"."special_service_archives";
CREATE TABLE "public"."special_service_archives" (
  "id" varchar(255) COLLATE "pg_catalog"."default" PRIMARY KEY NOT NULL,
  "parent_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 0,
  "archives_name" varchar(255) COLLATE "pg_catalog"."default",
  "archives_img" varchar(255) COLLATE "pg_catalog"."default",
  "sort" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" varchar(255) COLLATE "pg_catalog"."default",
  "del_flag" char(1) COLLATE "pg_catalog"."default",
  "kind" char(10) COLLATE "pg_catalog"."default",
  "special_type" int2
)
;
COMMENT ON COLUMN "public"."special_service_archives"."id" IS '主键';
COMMENT ON COLUMN "public"."special_service_archives"."parent_id" IS '父ID';
COMMENT ON COLUMN "public"."special_service_archives"."archives_name" IS '类型名称';
COMMENT ON COLUMN "public"."special_service_archives"."archives_img" IS '图标';
COMMENT ON COLUMN "public"."special_service_archives"."sort" IS '排序';
COMMENT ON COLUMN "public"."special_service_archives"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."special_service_archives"."del_flag" IS '删除标识(0 未删，2删除)';
COMMENT ON COLUMN "public"."special_service_archives"."kind" IS '点位类型（1.车站、2.桥梁、3.涵洞、4.隧道、5.基站、6.制高点、7.沿线监控设施、8.四电所 、9.疏散梯、10.复杂村镇、11.危爆物品厂库、12.高速服务区、13.无人机反制点、14.复杂路段、15.结合部协议、16.铁路平安道口、17.高铁下穿道路、18.高铁低路基路段、19.高速出入口、20.现场情况、21.住地情况）';
COMMENT ON COLUMN "public"."special_service_archives"."special_type" IS '特殊点位的类型值，目前用于住地和现场档案基本信息里面的（停车场，主要建筑等）';
COMMENT ON TABLE "public"."special_service_archives" IS '档案类型信息表';

-- ----------------------------
-- Records of special_service_archives
-- ----------------------------
INSERT INTO "public"."special_service_archives" VALUES ('00374c44bb444f9da3f95ae63915ebca', '0', '住地档案', NULL, '5', '2022-06-09 14:46:38', '0', NULL, NULL);


