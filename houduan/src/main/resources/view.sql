DROP materialized VIEW IF EXISTS residential_building_info;
DROP materialized VIEW IF EXISTS house_info;
DROP materialized VIEW IF EXISTS building_person_num;
DROP materialized VIEW IF EXISTS residence_person;
DROP materialized VIEW IF EXISTS residence_info;
DROP materialized VIEW IF EXISTS t_dc_party_individual_new;
DROP materialized VIEW IF EXISTS t_dc_area_room_info_new;
DROP materialized VIEW IF EXISTS t_dc_area_building_info_new;
DROP materialized VIEW IF EXISTS  t_ci_rs_rel_new;
DROP materialized VIEW IF EXISTS  t_dc_ci_rs_new;




CREATE materialized VIEW "residence_info" AS  SELECT DISTINCT p.party_id AS id,
                                                              p.party_name AS name,
                                                              p.mobile_phone AS tel,
                                                              CASE
                                                                  WHEN p.gender = 'F' THEN '2'
                                                                  WHEN p.gender = 'M' THEN '1'
                                                                  ELSE p.gender
                                                                                                                    END AS sex,
                                                              p.resident_birthplace AS native_place,
                                                              CASE
                                                                  WHEN p.nationality IS NULL OR p.nationality::text = 'CHN'::text THEN '中国'::text
                                                                  ELSE ''::text
                                                                                                                    END AS nationality,
                                                              p.i_residence AS register_address,
                                                              p.employer_type AS post,
                                                              p.employer AS work_unit,
                                                              p.education_name,
                                                              p.nation_name,
                                                              p.work_status_name,
                                                              p.work_type_name,
                                                              p.identity_card,
                                                              p.room_id AS house_id,
                                                              p.building_id,
                                                              ''::text AS owner_flag,
                                                                                                        d.name AS focus_flag
                                              FROM ( SELECT i.room_id,
                                                            i.building_id,
                                                            l.party_id,
                                                            l.party_name,
                                                            l.nationality,
                                                            l.gender,
                                                            l.mobile_phone,
                                                            l.resident_birthplace,
                                                            l.i_residence,
                                                            l.employer,
                                                            l.employer_type,
                                                            l.education_name,
                                                            l.nation_name,
                                                            l.work_status_name,
                                                            l.work_type_name,
                                                            l.identity_card
                                                     FROM (select party_id,i_residence_addr from t_dc_ci_rs GROUP BY party_id,i_residence_addr) r,
                                                          (select * from (
                                                                             select *,row_number() over(partition by room_address order by update_date desc) rn
                                                                             from t_dc_area_room_info where status='001'
                                                                         ) t where t.rn <=1) i,
                                                          (SELECT
                                                               *
                                                           FROM
                                                               (
                                                                   SELECT
                                                                       party_id,
                                                                       party_name,
                                                                       nationality,
                                                                       gender,
                                                                       mobile_phone,
                                                                       resident_birthplace,
                                                                       i_residence,
                                                                       education_name,
                                                                       nation_name,
                                                                       employer,
                                                                       employer_type,
                                                                       work_status_name,
                                                                       work_type_name,
                                                                       identity_card,
                                                                       ROW_NUMBER ( ) OVER ( PARTITION BY party_id ORDER BY status_time DESC ) rn
                                                                   FROM
                                                                       t_dc_party_individual
                                                                   WHERE
                                                                           data_status = '001'
                                                               ) T
                                                           WHERE
                                                                   T.rn <= 1) l
                                                     WHERE r.i_residence_addr::text = i.room_address::text AND r.party_id::text = l.party_id::text) p
                                                       LEFT JOIN (		 SELECT rel.party_id,string_agg(d.name,',') as name from  (SELECT DISTINCT party_id,rel_code from  t_ci_rs_rel tt) rel
                                                                                                                                 ,( select DISTINCT name,code from t_res_tag_dict where name in ('信教人员','服刑人员','缠访闹访人员','“武疯子”（“酒疯子”）','2次以上行政拘留人员','涉羟人员','境外人口','台胞','涉稳重点人员','失信人员','危险品从业人员','吸毒人员','上访人员','邪教人员','刑满释放人员','重点人群')) d where rel.rel_code::text = d.code::text  GROUP BY rel.party_id) d
                                                                 on p.party_id::text = d.party_id::text;
ALTER TABLE "residence_info" OWNER TO "postgres";




CREATE materialized VIEW "building_person_num" AS SELECT T1.building_id,
                                                         SUM ( P.COUNT ) AS SUM
                                                  FROM
                                                      (
                                                      SELECT DISTINCT
                                                      TT.room_id,
                                                      TT.building_id
                                                      FROM
                                                      (
                                                      SELECT
                                                      *
                                                      FROM
                                                      ( SELECT *, ROW_NUMBER ( ) OVER ( PARTITION BY room_address ORDER BY update_date DESC ) rn FROM t_dc_area_room_info WHERE status = '001' ) T
                                                      WHERE
                                                      T.rn <= 1
                                                      ) TT
                                                      ) T1,
                                                      (SELECT residence_info.house_id,count(residence_info.house_id) AS count
                                                      FROM residence_info
                                                      GROUP BY residence_info.house_id) P
                                                  WHERE
                                                      ( ( T1.room_id ) :: TEXT = ( P.house_id ) :: TEXT )
                                                  GROUP BY
                                                      T1.building_id;
ALTER TABLE "building_person_num" OWNER TO "postgres";

CREATE materialized VIEW "house_info" AS   SELECT DISTINCT r.room_id AS id,
                                                           r.building_id,
                                                           CASE
                                                               WHEN ((r.unit_number)::text = 'null'::text) THEN '0'::character varying
            ELSE r.unit_number
END AS building_cell,
    r.unit_code AS house_number,
        CASE
            WHEN ((r.is_rent)::text = '0'::text) THEN '自用'::text
            WHEN ((r.is_rent)::text = '1'::text) THEN '出租'::text
            WHEN ((r.is_rent)::text = '2'::text) THEN '空置'::text
            ELSE ''::text
END AS room_type,
    r.floor AS floor_number,
    m.rs_name AS owner_user_name,
    m.rs_phone AS owner_user_tel,
    m.rs_card_code AS owner_user_card_code,
    ''::text AS room_area,
    1 AS checked_flag,
    p.count AS per_number,
    rr.count AS focus_number
   FROM ((((select * from (
 select *,row_number() over(partition by room_address order by update_date desc) rn
 from t_dc_area_room_info where status='001'
) t where t.rn <=1) r
     LEFT JOIN (SELECT residence_info.house_id, count(residence_info.house_id) AS count
                                               FROM residence_info
                                               GROUP BY residence_info.house_id) p ON (((r.room_id)::text = (p.house_id)::text)))
     LEFT JOIN t_zz_manager_info m ON (((r.room_id)::text = (m.module_id)::text)))
     LEFT JOIN ( SELECT residence_info.house_id,
            count(*) AS count
           FROM residence_info
          WHERE (residence_info.focus_flag <> ''::text)
          GROUP BY residence_info.house_id) rr ON (((r.room_id)::text = (rr.house_id)::text)));
ALTER TABLE "house_info" OWNER TO "postgres";

CREATE materialized VIEW "residential_building_info" AS  SELECT b.building_id AS id,
                                                                b.building_name,
                                                                b.building_floor,
                                                                CASE
                                                                    WHEN ((b.unit_number)::text = 'null'::text) THEN '0'::character varying
            ELSE b.unit_number
END AS unit_number,
    b.building_address,
    g.parent_grid_name AS community_name,
    g.grid_name AS estate_name,
    ( SELECT sum(((t_dc_area_room_record.up_floor)::integer * (t_dc_area_room_record.up_number)::integer)) AS sum
           FROM t_dc_area_room_record
          WHERE (((t_dc_area_room_record.building_id)::text = (b.building_id)::text) AND ((t_dc_area_room_record.is_valid)::text = '1'::text))) AS household_number,
    p.x,
    p.y,
    p.z,
    b.longitude,
    b.latitude,
    ( SELECT building_person_num.sum
           FROM building_person_num
          WHERE ((building_person_num.building_id)::text = (b.building_id)::text)) AS household_rs_number,
    ( SELECT count(*) AS count
           FROM residence_info
          WHERE (((residence_info.building_id)::text = (b.building_id)::text) AND (residence_info.focus_flag <> ''::text))) AS focus_number
   FROM (((select * from (
 select *,row_number() over(partition by building_address order by update_date desc) rn
 from t_dc_area_building_info where status='001'
) t where t.rn <=1) b
     LEFT JOIN ( SELECT DISTINCT a.grid_id,
            a.grid_name,
            a.grid_path,
                CASE
                    WHEN ((b_1.grid_name IS NULL) AND ("position"((a.grid_path)::text, '容东片区'::text) > 0)) THEN (replace("substring"((a.grid_path)::text, ("position"((a.grid_path)::text, '容东片区'::text) + 4)), (a.grid_name)::text, ''::text))::character varying
                    WHEN ((b_1.grid_name IS NULL) AND ("position"((a.grid_path)::text, '容西管委会'::text) > 0)) THEN (replace("substring"((a.grid_path)::text, ("position"((a.grid_path)::text, '容西管委会'::text) + 5)), (a.grid_name)::text, ''::text))::character varying
                    ELSE b_1.grid_name
                END AS parent_grid_name
           FROM (t_dc_grid a
             LEFT JOIN t_dc_grid b_1 ON ((((a.grid_level)::text = '6'::text) AND ((a.parent_grid_id)::text = (b_1.grid_id)::text))))) g ON (((b.grid_id)::text = (g.grid_id)::text)))
     LEFT JOIN residential_building_position p ON (((b.building_address)::text = (p.building_address)::text)));
ALTER TABLE "residential_building_info" OWNER TO "postgres";
