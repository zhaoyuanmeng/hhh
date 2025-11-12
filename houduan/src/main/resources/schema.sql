alter table file_info add column if not exists business_id varchar(64);
alter table task_archives_data add column if not exists parent_id varchar(64);
alter table draw_data add column if not exists sort int2;
alter table scene_info add column if not exists origin_scheme_id varchar(64);