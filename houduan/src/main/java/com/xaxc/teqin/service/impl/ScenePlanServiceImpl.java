package com.xaxc.teqin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xaxc.teqin.mapper.PointInfoMapper;
import com.xaxc.teqin.mapper.SceneInfoMapper;
import com.xaxc.teqin.mapper.ScenePlanMapper;
import com.xaxc.teqin.model.entity.*;
import com.xaxc.teqin.service.IDrawDataService;
import com.xaxc.teqin.service.IScenePlanExtService;
import com.xaxc.teqin.service.IScenePlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 任务规划 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-09-10
 */
@Slf4j
@Service
public class ScenePlanServiceImpl extends ServiceImpl<ScenePlanMapper, ScenePlan> implements IScenePlanService {

    @Resource
    IDrawDataService drawDataService;

    @Resource
    IScenePlanExtService scenePlanExtService;

    @Resource
    SceneInfoMapper sceneInfoMapper;

    @Resource
    PointInfoMapper pointInfoMapper;

    @Override
    public boolean saveScenePlan(ScenePlan plan) {
        return saveOrUpdate(plan);
    }

    @Override
    public List<ScenePlan> getScenePlanNode(String sceneId, String planNode) {
        if ("警力部署".equals(planNode)) {
            //查询警力部署节点下是否有数据，如果有取节点数据并关联统计警力数据
            //如果没有则根据警力数据构建节点数据

//            List<ScenePlan> scenePlanList = new ArrayList<>();
//
//            ScenePlan scenePlan = getOne(new LambdaQueryWrapper<ScenePlan>().eq(ScenePlan::getSceneId, sceneId).eq(ScenePlan::getPlanNode, planNode)
//                    , false);
//
//            if (scenePlan == null) {
//                scenePlan = new ScenePlan();
//                scenePlan.setSceneId(sceneId);
//                scenePlan.setPlanNode(planNode);
//            }
            // AI
            List<ScenePlan> scenePlanList = list(new LambdaQueryWrapper<ScenePlan>()
                    .eq(ScenePlan::getSceneId, sceneId)
                    .eq(ScenePlan::getPlanNode, planNode));

            ScenePlan scenePlan;
            if (scenePlanList != null && !scenePlanList.isEmpty()) {
                scenePlan = scenePlanList.get(0);
            } else {
                scenePlan = new ScenePlan();
                scenePlan.setSceneId(sceneId);
                scenePlan.setPlanNode(planNode);
            }

            // end

            List<ScenePlanExt> scenePlanExtList = new ArrayList<>();

            //查询防线数据
            List<Map<String, Object>> drawFangxian = drawDataService.getLineOfScenePlanNode(sceneId, planNode);
            if (CollectionUtils.isEmpty(drawFangxian)) {
                //无标绘直接查节点数据
                return baseMapper.getScenePlanOfNode(sceneId, planNode);
            }
            //扩展数据（如防线下警力部署
            for (int i = 0; i < drawFangxian.size(); i++) {
                //按防线划分
                Object fangxian = drawFangxian.get(i).get("fangxian");
                ScenePlanExt scenePlanExt = scenePlanExtService.getScenePlanNodeExtOfLine(sceneId, planNode, fangxian == null ? "" : fangxian.toString());
                if (scenePlanExt == null) {
                    scenePlanExt = new ScenePlanExt();
                    scenePlanExt.setPlanNode(planNode);
                    scenePlanExt.setSceneId(sceneId);
                }
                //查询防线下的标绘数据统计
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("fangxian", fangxian);
                //按哨位统计
                List<Map<String, Object>> postStatistics = drawDataService.statisticalByPostOfScenePlanNode(sceneId, planNode, fangxian == null ? "" : fangxian.toString());
                if (!CollectionUtils.isEmpty(postStatistics)) {
                    jsonObject.put("postStatistics", postStatistics.stream().filter(post -> post.containsKey("post") && post.containsKey("num")).collect(Collectors.toList()));
                } else {
                    jsonObject.put("postStatistics", new ArrayList<>());
                }
                //防线下统计数据 XXX
                jsonObject.put("policeTypeOfLine", drawDataService.statisticalPoliceType(sceneId, planNode, fangxian == null ? "" : fangxian.toString(), null));
                //防线下分组
                List<Map<String, String>> groups = drawDataService.getGroupOfLine(sceneId, planNode, fangxian == null ? "" : fangxian.toString());
                List<JSONObject> groupDatas = new ArrayList<>();
                //组下面的警力部署
                for (Map<String, String> group : groups) {
                    JSONObject groupData = new JSONObject();
                    groupData.put("policeNum", drawDataService.getPoliceNumOfGroup(sceneId, planNode, fangxian == null ? "" : fangxian.toString(), group.get("group")));
                    groupData.put("policeTypeOfGroup", drawDataService.statisticalByPostOfGroup(sceneId, planNode, fangxian == null ? "" : fangxian.toString(), group.get("group")));
                    groupData.put("group", group.get("group"));
                    groupData.put("groupDesc", group.get("groupDesc") != null ? group.get("groupDesc") : group.get("groupdesc"));
                    List<Map<String, Object>> groupPoliceData = drawDataService.getDefenseLinePoliceDataOfScenePlanNode(sceneId, planNode, fangxian == null ? "" : fangxian.toString(), group.get("group"));
                    groupData.put("jinglibushu", groupPoliceData);
                    groupDatas.add(groupData);
                }
                jsonObject.put("groupData", groupDatas);
                scenePlanExt.setPoliceData(jsonObject);
                List<Map<String, Object>> list = drawDataService.getDefenseLinePoliceDataOfScenePlanNode(sceneId, planNode, fangxian == null ? "" : fangxian.toString(), null);
                scenePlanExt.setPlacePoliceData(policeDataOfPosition(list));
                //统计防线下的警力大类
                Map map = drawDataService.statisticalOfPoliceType(sceneId, planNode, fangxian == null ? null : fangxian.toString());
                if (!CollectionUtils.isEmpty(map)) {
                    scenePlanExt.setStatisticalData(new JSONObject(map));
                }
                scenePlanExtList.add(scenePlanExt);
            }
            scenePlan.setExtDataList(scenePlanExtList);
            //按场景维度统计警力数据
            Map policeTypeData = drawDataService.statisticalByPoliceTypeOfScenePlanNode(sceneId, "警力部署", null);
            scenePlan.setPoliceTypeStatistics(policeTypeData);
            //按哨位统计
            List<Map<String, Object>> postData = drawDataService.statisticalByPostOfScenePlanNode(sceneId, "警力部署");
            if (!CollectionUtils.isEmpty(postData)) {
                scenePlan.setPostStatistics(postData.stream().filter(post -> post.containsKey("post") && post.containsKey("num")).collect(Collectors.toList()));
            } else {
                scenePlan.setPostStatistics(new ArrayList<>());
            }
            //按位置统计
            List<Map<String, Object>> list = drawDataService.getPoliceDetailOfScenePlanNode(sceneId, planNode);
            scenePlan.setPlaceStatistics(policeData(list));
            //scenePlan.setPlaceStatistics(policeDataOfPosition(list));
            scenePlanList.add(scenePlan);
            return scenePlanList;
        } else if ("应急力量".equals(planNode)) {
            int num = drawDataService.getEmergencyPolice(sceneId);
            ScenePlan scenePlan = getOne(new LambdaQueryWrapper<ScenePlan>().eq(ScenePlan::getSceneId, sceneId).eq(ScenePlan::getPlanNode, planNode)
                    , false);

            if (scenePlan == null) {
                scenePlan = new ScenePlan();
                scenePlan.setSceneId(sceneId);
                scenePlan.setPlanNode(planNode);
                scenePlan.setData(new JSONObject());
            }
            scenePlan.getData().put("emergency", num);
            //按位置统计应急力量
            scenePlan.getData().put("emergencyOfPosition", drawDataService.getEmergencyPoliceOfPosition(sceneId));
            return Arrays.asList(scenePlan);

        } else if ("应急避险点".equals(planNode)) {
            ScenePlan scenePlan = getOne(new LambdaQueryWrapper<ScenePlan>().eq(ScenePlan::getSceneId, sceneId).eq(ScenePlan::getPlanNode, planNode)
                    , false);

            if (scenePlan == null) {
                scenePlan = new ScenePlan();
                scenePlan.setSceneId(sceneId);
                scenePlan.setPlanNode(planNode);
                scenePlan.setData(new JSONObject());
                List<DrawData> points = drawDataService.list(new LambdaQueryWrapper<DrawData>().eq(DrawData::getDeleteFlag, 0)
                        .eq(DrawData::getSceneId, sceneId).eq(DrawData::getPlanNode, planNode));
                if (!CollectionUtils.isEmpty(points)) {
                    scenePlan.getData().put("pointNames", points.stream().map(DrawData::getName).collect(Collectors.toList()));
                }
                List<DrawData> lines = drawDataService.list(new LambdaQueryWrapper<DrawData>().eq(DrawData::getDeleteFlag, 0)
                        .eq(DrawData::getSceneId, sceneId).apply("data->>'customStyle' = '4'"));
                if (!CollectionUtils.isEmpty(lines)) {
                    scenePlan.getData().put("lineNames", lines.stream().map(DrawData::getName).collect(Collectors.toList()));
                }
            }

            return Arrays.asList(scenePlan);

        } else if ("应急医院".equals(planNode)) {
            ScenePlan scenePlan = getOne(new LambdaQueryWrapper<ScenePlan>().eq(ScenePlan::getSceneId, sceneId).eq(ScenePlan::getPlanNode, planNode)
                    , false);

            if (scenePlan == null) {
                scenePlan = new ScenePlan();
                scenePlan.setSceneId(sceneId);
                scenePlan.setPlanNode(planNode);
                scenePlan.setData(new JSONObject());
                List<DrawData> points = drawDataService.list(new LambdaQueryWrapper<DrawData>().eq(DrawData::getDeleteFlag, 0)
                        .eq(DrawData::getSceneId, sceneId).eq(DrawData::getPlanNode, planNode));
                if (!CollectionUtils.isEmpty(points)) {
                    scenePlan.getData().put("pointNames", points.stream().map(DrawData::getName).collect(Collectors.toList()));
                }
                List<DrawData> lines = drawDataService.list(new LambdaQueryWrapper<DrawData>().eq(DrawData::getDeleteFlag, 0)
                        .eq(DrawData::getSceneId, sceneId).apply("data->>'customStyle' = '7'"));
                if (!CollectionUtils.isEmpty(lines)) {
                    scenePlan.getData().put("lineNames", lines.stream().map(DrawData::getName).collect(Collectors.toList()));
                }
            }
            return Arrays.asList(scenePlan);
        } else if ("基本情况".equals(planNode)) {
            List<ScenePlan> scenePlanList = baseMapper.getScenePlanOfNode(sceneId, planNode);
            SceneInfo sceneInfo = sceneInfoMapper.selectById(sceneId);
//            if (org.springframework.util.StringUtils.hasText(sceneInfo.getBasicDataId())) {
//                PointInfo pointInfo = pointInfoMapper.selectById(sceneInfo.getBasicDataId());
//                pointInfo.getData().getString("");
//            }
            return scenePlanList;
        } else {
            return baseMapper.getScenePlanOfNode(sceneId, planNode);
        }
    }

    @Override
    public List<ScenePlan> getList(String sceneId, List<String> excludePlanNodes) {
        LambdaQueryWrapper<ScenePlan> lambdaQueryWrapper = new LambdaQueryWrapper<ScenePlan>()
                .eq(ScenePlan::getSceneId, sceneId);
        if (!CollectionUtils.isEmpty(excludePlanNodes)) {
            lambdaQueryWrapper.notIn(ScenePlan::getPlanNode, excludePlanNodes);
        }
        lambdaQueryWrapper.orderByAsc(ScenePlan::getCreateTime);
        return list(lambdaQueryWrapper);
    }


    /**
     * 按位置归类哨位数据
     *
     * @param drawData
     * @return
     */
    private List<JSONObject> policeDataOfPosition(List<Map<String, Object>> drawData) {
        List<JSONObject> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(drawData)) {
            return list;
        }
        //按位置划分
        Object weizhi = drawData.get(0).get("weizhi");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("weizhi", weizhi);
        List<JSONObject> data = new ArrayList<>();
        for (int i = 0; i < drawData.size(); i++) {
            if (drawData.get(i).get("weizhi") == null) {
                jsonObject = new JSONObject();
                data = new ArrayList<>();
                if (i + 1 <= drawData.size() - 1) {
                    weizhi = drawData.get(i + 1).get("weizhi");
                    jsonObject.put("weizhi", weizhi);
                }
                continue;
            }
            if (weizhi.equals(drawData.get(i).get("weizhi"))) {
                JSONObject object = new JSONObject();
                object.put("leixing", drawData.get(i).get("leixing"));
                object.put("num", drawData.get(i).get("num"));
                object.put("id", drawData.get(i).get("id"));
                object.put("buildName", drawData.get(i).get("buildName"));
                object.put("floorNum", drawData.get(i).get("floorNum"));
                object.put("groupId", drawData.get(i).get("groupId"));
                data.add(object);
            } else {
                jsonObject.put("data", data);
                list.add(jsonObject);

                jsonObject = new JSONObject();
                data = new ArrayList<>();

                weizhi = drawData.get(i).get("weizhi");
                jsonObject.put("weizhi", weizhi);
                JSONObject object = new JSONObject();
                object.put("leixing", drawData.get(i).get("leixing"));
                object.put("num", drawData.get(i).get("num"));
                object.put("id", drawData.get(i).get("id"));
                object.put("buildName", drawData.get(i).get("buildName"));
                object.put("floorNum", drawData.get(i).get("floorNum"));
                object.put("groupId", drawData.get(i).get("groupId"));
                data.add(object);
            }
            if (i == drawData.size() - 1) {
                jsonObject.put("data", data);
                list.add(jsonObject);
            }
        }
        return list;
    }

    private List<JSONObject> policeData(List<Map<String, Object>> drawData) {
        List<JSONObject> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(drawData)) {
            return list;
        }
        //按位置划分

        for (int i = 0; i < drawData.size(); i++) {
            if (drawData.get(i).get("weizhi") == null) {
                continue;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("weizhi", drawData.get(i).get("weizhi"));
            List<JSONObject> data = new ArrayList<>();
            JSONObject object = new JSONObject();
            object.put("leixing", drawData.get(i).get("leixing"));
            object.put("num", drawData.get(i).get("num"));
            object.put("id", drawData.get(i).get("id"));
            object.put("buildName", drawData.get(i).get("buildname"));
            object.put("floorNum", drawData.get(i).get("floornum"));
            object.put("groupId", drawData.get(i).get("groupid"));
            data.add(object);
            jsonObject.put("data", data);
            list.add(jsonObject);
        }
        return list;
    }
}
