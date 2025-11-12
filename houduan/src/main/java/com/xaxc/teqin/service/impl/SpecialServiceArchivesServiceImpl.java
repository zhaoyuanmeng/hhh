package com.xaxc.teqin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.common.util.Utils;
import com.xaxc.teqin.mapper.SpecialServiceArchivesMapper;
import com.xaxc.teqin.model.entity.PointInfo;
import com.xaxc.teqin.model.entity.SpecialServiceArchives;
import com.xaxc.teqin.model.entity.SpecialServiceArchivesForm;
import com.xaxc.teqin.service.IPointInfoService;
import com.xaxc.teqin.service.ISpecialServiceArchivesFormService;
import com.xaxc.teqin.service.ISpecialServiceArchivesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 档案类型信息表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-07-16
 */
@Service
public class SpecialServiceArchivesServiceImpl extends ServiceImpl<SpecialServiceArchivesMapper, SpecialServiceArchives> implements ISpecialServiceArchivesService {

    @Resource
    ISpecialServiceArchivesFormService serviceArchivesFormService;

    @Resource
    IPointInfoService pointInfoService;


    /**
     * 获取五个基础档案id
     */
    @Override
    public Map<String, Object> selectFileId() {
        List<Map<String, Object>> list = listMaps(new LambdaQueryWrapper<SpecialServiceArchives>()
                .eq(SpecialServiceArchives::getDelFlag, "0")
                .eq(SpecialServiceArchives::getParentId, "0"));
        Map<String, Object> fileMap = new HashMap<>();
        for (Map<String, Object> map : list) {
            if (map.get("archives_name").toString().equals("住地档案")) {
                fileMap.put(map.get("archives_name").toString(), map.get("id").toString());
            }
            if (map.get("archives_name").toString().equals("普通铁路档案")) {
                fileMap.put(map.get("archives_name").toString(), map.get("id").toString());
            }
            if (map.get("archives_name").toString().equals("高速铁路档案")) {
                fileMap.put(map.get("archives_name").toString(), map.get("id").toString());
            }
            if (map.get("archives_name").toString().equals("现场档案")) {
                fileMap.put(map.get("archives_name").toString(), map.get("id").toString());
            }
            if (map.get("archives_name").toString().equals("高速公路档案")) {
                fileMap.put(map.get("archives_name").toString(), map.get("id").toString());
            }
            if (map.get("archives_name").toString().equals("城市道路档案")) {
                fileMap.put(map.get("archives_name").toString(), map.get("id").toString());
            }
        }
        return fileMap;
    }

    @Override
    public Map<String, Object> getArchivesId(String fileId, String pointName) {
        return getMap(new LambdaQueryWrapper<SpecialServiceArchives>().eq(SpecialServiceArchives::getDelFlag, "0")
                .eq(SpecialServiceArchives::getParentId, fileId).eq(SpecialServiceArchives::getArchivesName, pointName));
    }

    @Override
    public boolean saveFormItemSort(List<SpecialServiceArchives> specialServiceArchivesList) {
        return updateBatchById(specialServiceArchivesList);
    }

    @Override
    public List<Map<String, Object>> getBasicInfoById(String parentId) {
        List<SpecialServiceArchives> specialServiceArchivesList = list(new LambdaQueryWrapper<SpecialServiceArchives>()
                .eq(SpecialServiceArchives::getDelFlag, "0")
                .eq(SpecialServiceArchives::getParentId, parentId)
                .orderByAsc(List.of(SpecialServiceArchives::getSort, SpecialServiceArchives::getCreateTime))
        );
        if (!CollectionUtils.isEmpty(specialServiceArchivesList)) {
            SpecialServiceArchives serviceArchives = specialServiceArchivesList.get(0);

            return serviceArchivesFormService.listMaps(new LambdaQueryWrapper<SpecialServiceArchivesForm>()
                    .eq(SpecialServiceArchivesForm::getArchivesId, serviceArchives.getId())
                    .eq(SpecialServiceArchivesForm::getDelFlag, "0")
                    .orderByAsc(List.of(SpecialServiceArchivesForm::getSort, SpecialServiceArchivesForm::getCreateTime)));
        } else {
            return null;
        }

    }


    @Override
    public List<Map<String, Object>> getArchivesList(SpecialServiceArchives specialServiceArchives) {
        LambdaQueryWrapper<SpecialServiceArchives> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (org.springframework.util.StringUtils.hasText(specialServiceArchives.getParentId())) {
            lambdaQueryWrapper.eq(SpecialServiceArchives::getParentId, specialServiceArchives.getParentId());
        }

        if (org.springframework.util.StringUtils.hasText(specialServiceArchives.getId())) {
            lambdaQueryWrapper.eq(SpecialServiceArchives::getId, specialServiceArchives.getId());
        }
        lambdaQueryWrapper.eq(SpecialServiceArchives::getDelFlag, "0")
                .orderByAsc(List.of(SpecialServiceArchives::getSort, SpecialServiceArchives::getCreateTime));
        List<SpecialServiceArchives> list = list(lambdaQueryWrapper);
        List zddwList = new ArrayList();///重点点位输出列表
        //获取高速公路的名称
        if (ObjectUtil.isNotEmpty(list)) {
            //循环档案类型
            for (SpecialServiceArchives fileMap : list) {
                //重点点位map
                Map<String, Object> pointMap = new HashMap<>();
                int gsglsl = 0;//高速公路数量
                int zddw = 0;//重点点位数量
                double lcqc = 0;//里程全长或占地面积
                List infolist = new ArrayList();//档案-道路详情
                //查询基本情况表
                List<PointInfo> lists = pointInfoService.queryListByParentId(fileMap.getId());
                System.out.println("查询基本情况表" + lists.size());
                if (ObjectUtil.isNotEmpty(lists)) {
                    gsglsl = lists.size();///高速公路数量
                    for (PointInfo map : lists) {
                        Map<String, Object> mapinfo = new HashMap<>();
                        //获取公路名称和全长历程
                        JSONObject lll = map.getData();
                        if (ObjectUtil.isNotEmpty(lll.get("TotalLengthOfMileage"))) {
                            lcqc += Double.parseDouble(Utils.transterJson(lll.get("TotalLengthOfMileage")));
                        }
                        if (ObjectUtil.isNotEmpty(lll.get("zhandimianji"))) {
                            String zhandimianji = lll.get("zhandimianji").toString();
                            String[] ms = zhandimianji.split("㎡");
                            double mianji = Double.parseDouble(ms[0]);
                            lcqc += mianji;
                        }
                        //获取重点点位数量
                        if (ObjectUtil.isNotEmpty(lll.get("title"))) {
                            Map mapsCount = new HashMap();
                            mapsCount.put("jcxxId", map.getId());
                            //查询基本情况表关联的子档案信息
                            List<PointInfo> listzd = pointInfoService.queryListByJcxxId(map.getId());
                            zddw += listzd.size();///重点点位数量（子档案数量）
                            mapinfo.put("id", map.getId());
                            JSONObject dataMap = map.getData();
                            if (ObjectUtil.isNotEmpty(dataMap.get("TotalLengthOfMileage"))) {
                                mapinfo.put("changdu", dataMap.get("TotalLengthOfMileage"));
                            } else if (ObjectUtil.isNotEmpty(dataMap.get("zhandimianji"))) {
                                String zhandimianji = dataMap.get("zhandimianji").toString();
                                String[] ms = zhandimianji.split("㎡");
                                mapinfo.put("zhandimianji", ms[0]);
                            }
                            if (ObjectUtils.isNotEmpty(dataMap.get("qidian"))) {
                                mapinfo.put("qidian", dataMap.get("qidian"));
                            }
                            if (ObjectUtils.isNotEmpty(dataMap.get("zhidian"))) {
                                mapinfo.put("zhidian", dataMap.get("zhidian"));
                            }
                            if (ObjectUtils.isNotEmpty(dataMap.get("beizhu"))) {
                                mapinfo.put("beizhu", dataMap.get("beizhu"));
                            }
                            if (ObjectUtils.isNotEmpty(dataMap.get("guanxiapaichusuo"))) {
                                mapinfo.put("guanxiapaichusuo", dataMap.get("guanxiapaichusuo"));
                            }
                            if (ObjectUtils.isNotEmpty(dataMap.get("danweixingzhi"))) {
                                mapinfo.put("danweixingzhi", dataMap.get("danweixingzhi"));
                            }
                            if (ObjectUtils.isNotEmpty(dataMap.get("lishuguanxi"))) {
                                mapinfo.put("lishuguanxi", dataMap.get("lishuguanxi"));
                            }
                            if (ObjectUtils.isNotEmpty(dataMap.get("faren"))) {
                                mapinfo.put("faren", dataMap.get("faren"));
                            }
                            if (ObjectUtils.isNotEmpty(dataMap.get("lianxidianhua"))) {
                                mapinfo.put("lianxidianhua", dataMap.get("lianxidianhua"));
                            }
                            if (ObjectUtils.isNotEmpty(dataMap.get("zongrenshu"))) {
                                mapinfo.put("zongrenshu", dataMap.get("zongrenshu"));
                            }
                            if (ObjectUtils.isNotEmpty(dataMap.get("dizhi"))) {
                                mapinfo.put("dizhi", dataMap.get("dizhi"));
                            }
                            if (ObjectUtils.isNotEmpty(dataMap.get("jingdu"))) {
                                mapinfo.put("jingdu", Utils.transterJson(dataMap.get("jingdu")));
                            }
                            if (ObjectUtils.isNotEmpty(dataMap.get("weidu"))) {
                                mapinfo.put("weidu",  Utils.transterJson(dataMap.get("weidu")));
                            }
                            mapinfo.put("title", lll.get("title").toString());
                            //重点点位数量
                            mapinfo.put("zddwgs", listzd.size());
                            infolist.add(mapinfo);
                        }

                    }
                }
                pointMap.put("id", fileMap.getId());
                pointMap.put("name", fileMap.getArchivesName());
                pointMap.put("gsglsl", gsglsl);
                pointMap.put("zddw", zddw);
                pointMap.put("lcqc", lcqc);
                pointMap.put("list", infolist);
                zddwList.add(pointMap);
            }
        }
        return zddwList;
    }

    @Override
    public List<Map<String, Object>> getInfoByArchivesId(String archivesId) {
        return listMaps(new LambdaQueryWrapper<SpecialServiceArchives>()
                .eq(SpecialServiceArchives::getDelFlag, "0")
                .eq(SpecialServiceArchives::getParentId, archivesId));
    }

    @Override
    public Map<String, Object> getPointInfo(String archivesId, int size, int page) {
        Map<String, Object> dataMap = new HashMap<>();
        //查出来是fileid和filename
        List<SpecialServiceArchivesForm> list = serviceArchivesFormService.getKV(archivesId);
        dataMap.put("file_info", list);
        //组装 file_data
        List<PointInfo> maps = pointInfoService.selectPointList(archivesId);
        List<PointInfo> dataList = new ArrayList<>();
        for (int i = 0; i < maps.size(); i++) {
            if (i >= page * size && i < (page + 1) * size) {
                dataList.add(maps.get(i));
            }
        }
        int total = maps.size();
        int totalPage;
        if (total % size == 0) {
            totalPage = total / size;
        } else {
            totalPage = total / size + 1;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("totalPage", totalPage);
        data.put("page", page);
        data.put("size", size);
        data.put("content", dataList);
        dataMap.put("field_data", data);

        return dataMap;
    }

    @Override
    public List<Map<String, Object>> selectPointInfo(String id, String jcxxId) {
        // 获取对应档案的点位信息
        List<Map<String, Object>> list = listMaps(new LambdaQueryWrapper<SpecialServiceArchives>()
                .select(List.of(SpecialServiceArchives::getId, SpecialServiceArchives::getArchivesName,SpecialServiceArchives::getSort))
                .eq(SpecialServiceArchives::getParentId, id)
                .eq(SpecialServiceArchives::getDelFlag, "0")
                .groupBy(SpecialServiceArchives::getId)
                .orderByAsc(SpecialServiceArchives::getSort));
        // 新建返回数据的集合
        List<Map<String, Object>> lists = new ArrayList<>();
        // 遍历获取的点位信息
        for (Map<String, Object> map : list) {
            System.out.println(map+"--------------------");
            // 获取重点点位id
            String pointId = map.get("id").toString();
            // 根据重点点位id查询mongo数据库点位的数量
            long count = pointInfoService.selectPointCount(jcxxId, id, pointId);
            // 获取返回的mongo数据
            //List<Map> mappedResults = count.getMappedResults();
            // 获取返回数据的个数
            String archivesName = map.get("archives_name").toString();
            // 新建map 组装数据
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", pointId);
            hashMap.put("archives_name", archivesName);
            hashMap.put("count", count);
            // 放入返回数据的list
            lists.add(hashMap);
        }
        return lists;
    }

    @Override
    public ResponseResult selectPointList(String id, String jcxxId) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(jcxxId)) {
            List<Map<String, Object>> pointInfo = listMaps(new QueryWrapper<SpecialServiceArchives>()
                    .select("DISTINCT id")
                    .lambda()
                    .ne(SpecialServiceArchives::getParentId, "0")
                    .eq(SpecialServiceArchives::getDelFlag, "0")
                    .ne(SpecialServiceArchives::getSort, "1"));
            return ResponseResult.success(pointInfo);
        }
        // 获取到 重点点位的数据
        List<Map<String, Object>> lists = pointInfoService.selectPointList(id, jcxxId);
        lists.forEach(ls -> {
            if (ls.containsKey("data") && ls.get("data") != null) {
                LinkedHashMap data = (LinkedHashMap) ls.get("data");
                if (data.get("video") == null) {
                    data.put("video", new ArrayList());
                }
                if (data.get("photo") == null) {
                    data.put("photo", new ArrayList());
                }
            }
        });
        // 获取重点点位的动态表单
        List<SpecialServiceArchivesForm> list = serviceArchivesFormService.selectPointInfo(id);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("point", lists);

        List<SpecialServiceArchivesForm> infolist = new ArrayList<>();
        for (SpecialServiceArchivesForm info : list) {
            SpecialServiceArchivesForm vo = new SpecialServiceArchivesForm();
            if ("家庭主要成员".equals(info.getFieldName())) {
                List<Map<String, Object>> listmap = new ArrayList<>();
                Map<String, String> fm = new HashMap<>();
                fm.put("xingming", "姓名");
                fm.put("guanxi", "关系");
                fm.put("nianling", "年龄");
                fm.put("zhengzhimianmao", "政治面貌");
                fm.put("gongzuodanwei", "工作单位");
                Set<String> keySet = fm.keySet();
                int a = 0;
                for (String key : keySet) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("field_name", fm.get(key));
                    map.put("field_id", key);
                    map.put("mandatory", "1");
                    map.put("sort", a);
                    map.put("field_type", "input");
                    listmap.add(map);
                    a++;
                }
                vo.setListFild(listmap);
            }
            vo.setArchivesId(info.getArchivesId());
            vo.setArchivesParentId(info.getArchivesParentId());
            vo.setCreateTime(info.getCreateTime());
            vo.setDefaultValue(info.getDefaultValue());
            vo.setDelFlag(info.getDelFlag());
            vo.setFieldId(info.getFieldId());
            vo.setId(info.getId());
            vo.setRemark(info.getRemark());
            vo.setUpdateTime(info.getUpdateTime());
            vo.setFieldName(info.getFieldName());
            vo.setMandatory(info.getMandatory());
            vo.setSort(info.getSort());
            vo.setFieldType(info.getFieldType());
            infolist.add(vo);
        }
        hashMap.put("info", infolist);
        return ResponseResult.success(hashMap);
    }
}
