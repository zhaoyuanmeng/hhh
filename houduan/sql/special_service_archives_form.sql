
DROP TABLE IF EXISTS "public"."special_service_archives_form";
CREATE TABLE "public"."special_service_archives_form" (
  "id" varchar(255) COLLATE "pg_catalog"."default" PRIMARY KEY NOT NULL,
  "archives_parent_id" varchar(255) COLLATE "pg_catalog"."default",
  "archives_id" varchar(255) COLLATE "pg_catalog"."default",
  "field_id" varchar(255) COLLATE "pg_catalog"."default",
  "field_name" varchar(255) COLLATE "pg_catalog"."default",
  "mandatory" char(1) COLLATE "pg_catalog"."default",
  "field_type" varchar(255) COLLATE "pg_catalog"."default",
  "default_value" varchar(255) COLLATE "pg_catalog"."default",
  "sort" varchar(16) COLLATE "pg_catalog"."default",
  "create_time" varchar(100) COLLATE "pg_catalog"."default",
  "update_time" varchar(100) COLLATE "pg_catalog"."default",
  "remark" text COLLATE "pg_catalog"."default",
  "del_flag" char(1) COLLATE "pg_catalog"."default",
  "pid" char(1) COLLATE "pg_catalog"."default",
  "column_index" int2
)
;
COMMENT ON COLUMN "public"."special_service_archives_form"."id" IS '主键ID';
COMMENT ON COLUMN "public"."special_service_archives_form"."archives_parent_id" IS '档案类型父ID';
COMMENT ON COLUMN "public"."special_service_archives_form"."archives_id" IS '档案类型ID';
COMMENT ON COLUMN "public"."special_service_archives_form"."field_id" IS '字段ID';
COMMENT ON COLUMN "public"."special_service_archives_form"."field_name" IS '表单项名称';
COMMENT ON COLUMN "public"."special_service_archives_form"."mandatory" IS '是否必填(0 必填，1 非必填，2不显示)';
COMMENT ON COLUMN "public"."special_service_archives_form"."field_type" IS '表单项类型';
COMMENT ON COLUMN "public"."special_service_archives_form"."default_value" IS '默认值';
COMMENT ON COLUMN "public"."special_service_archives_form"."sort" IS '排序';
COMMENT ON COLUMN "public"."special_service_archives_form"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."special_service_archives_form"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."special_service_archives_form"."remark" IS '备注字段';
COMMENT ON COLUMN "public"."special_service_archives_form"."del_flag" IS '删除标识(0 未删，2删除)';
COMMENT ON COLUMN "public"."special_service_archives_form"."pid" IS '''1：基本信息、2：内设机构、3：主要建筑、4：重要部位、5：四邻情况、6：停车场''';
COMMENT ON COLUMN "public"."special_service_archives_form"."column_index" IS '列下标，当field为多行时使用';
COMMENT ON TABLE "public"."special_service_archives_form" IS '档案自定义表单项信息表';

-- ----------------------------
-- Records of special_service_archives_form
-- ----------------------------
INSERT INTO "public"."special_service_archives_form" VALUES ('002b687565fd4229a12a72c2402c9e68', '00374c44bb444f9da3f95ae63915ebca', 'ed00b5199dbc4721b9eb1eb6f8218482', 'xingbie', '性别', '1', 'input', NULL, '3', '2022-06-09 14:59:52', '2022-06-09 14:59:52', NULL, '0', NULL, NULL);
