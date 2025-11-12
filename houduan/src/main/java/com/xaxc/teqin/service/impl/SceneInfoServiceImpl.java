package com.xaxc.teqin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.common.util.Utils;
import com.xaxc.teqin.mapper.SceneInfoMapper;
import com.xaxc.teqin.mapper.TaskMapper;
import com.xaxc.teqin.model.dto.BasicDataDTO;
import com.xaxc.teqin.model.dto.DeployPoliceData;
import com.xaxc.teqin.model.dto.SceneDataDTO;
import com.xaxc.teqin.model.dto.SceneInfoDTO;
import com.xaxc.teqin.model.entity.*;
import com.xaxc.teqin.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * 场景表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-07-24
 */
@Slf4j
@Service
public class SceneInfoServiceImpl extends ServiceImpl<SceneInfoMapper, SceneInfo> implements ISceneInfoService {

    @Value("#{'${treeNode.keyFocus}'.split(',')}")
    private List<String> keyFocusNodes;

    @Value("#{'${treeNode.site.key1}'.split(',')}")
    private List<String> siteNodes;

    @Value("#{'${treeNode.securityCheck}'.split(',')}")
    private List<String> securityCheckNodes;

    @Value("#{'${treeNode.residence.key1}'.split(',')}")
    private List<String> residenceNodes;

    @Value("#{'${treeNode.cityRoad}'.split(',')}")
    private List<String> cityRoadNodes;


    @Value("#{'${treeNode.railway}'.split(',')}")
    private List<String> railwayNodes;

    @Value("#{'${treeNode.police}'.split(',')}")
    private List<String> policeNodes;

    @Value("#{'${treeNode.car}'.split(',')}")
    private List<String> carNodes;

    @Value("#{'${treeNode.cordon}'.split(',')}")
    private List<String> cordonNodes;


    @Value("#{'${treeNode.highway}'.split(',')}")
    private List<String> highwayNodes;


    @Value("${buffer.radius}")
    private double bufferRadius;


    @Value("#{'${treeNode.site.key1}'.split(',')}")
    private List<String> siteKey1;

    @Value("#{'${treeNode.site.key2}'.split(',')}")
    private List<String> siteKey2;

    @Value("#{'${treeNode.site.key3}'.split(',')}")
    private List<String> siteKey3;

    @Value("#{'${treeNode.siteAround.key1}'.split(',')}")
    private List<String> siteAroundKey1;

    @Value("#{'${treeNode.siteAround.key2}'.split(',')}")
    private List<String> siteAroundKey2;


    @Value("#{'${treeNode.residence.key1}'.split(',')}")
    private List<String> residenceKey1;

    @Value("#{'${treeNode.residence.key2}'.split(',')}")
    private List<String> residenceKey2;

    @Value("#{'${treeNode.residence.key3}'.split(',')}")
    private List<String> residenceKey3;

    @Value("#{'${treeNode.residenceAround.key1}'.split(',')}")
    private List<String> residenceAroundKey1;

    @Value("#{'${treeNode.residenceAround.key2}'.split(',')}")
    private List<String> residenceAroundKey2;

    @Value("${treeNode.emergency}")
    private List<String> emergencyNodes;

    @Value("${highway.basicInfoTypeId}")
    private String highwayTypeId;

    @Value("${railway.basicInfoTypeId}")
    private String railwayTypeId;

    @Resource
    IResidentialBuildingInfoService residentialBuildingInfoService;

    @Resource
    ICommercialEnterpriseInfoService commercialEnterpriseInfoService;

    @Resource
    IDrawDataService drawDataService;

    @Resource
    IScenePlanService scenePlanService;
    @Resource
    IScenePlanExtService scenePlanExtService;

    @Resource
    IPointInfoService pointInfoService;

    @Resource
    IPointExtInfoService pointExtInfoService;

    @Resource
    SpatialQuery spatialQuery;

    @Resource
    TaskMapper taskMapper;

    @Resource
    IFileInfoService fileInfoService;

    @Resource
    ITaskArchivesDataService taskArchivesDataService;

    @Override
    public SceneInfo geSceneById(String id) {
        return baseMapper.getDetailById(id);
    }

    public void updateSceneRoadData(SceneInfo sceneInfo) {
        List<DrawData> drawDataList = getDrawLines(sceneInfo.getId());
        String roadName = getRoadName(drawDataList);
        if (sceneInfo.getSceneRoadTime() == 0) {
            double length = drawDataList.stream().mapToDouble(DrawData::getLength).sum() / 1000;
            //用时 每小时60是公里算
            long usedTime = length > 0 && length < 1 ? 1L : Math.round(length);
            //公里数
            double result = Double.parseDouble(String.format("%.2f", length));
            sceneInfo.setSceneRoadDesc(roadName).setSceneRoadLength(result).setSceneRoadTime(Long.valueOf(usedTime).intValue());
        }
    }

    @Override
    public SceneInfoDTO getDetail(String id) {
        SceneInfo sceneInfo = baseMapper.getDetailById(id);
        if (sceneInfo == null) {
            return null;
        }
        FileInfo fileInfo = fileInfoService.getOne(new LambdaQueryWrapper<FileInfo>().eq(FileInfo::getBusinessId, id), false);
        if (fileInfo != null) {
            sceneInfo.setFileName(fileInfo.getOriginalFileName());
            sceneInfo.setFileUrl(fileInfo.getFilePath());
        }
        //查询标绘的线路，组装成线路名
        if ("1".equals(sceneInfo.getType())) {
            updateSceneRoadData(sceneInfo);
        }
        SceneInfoDTO sceneInfoDTO = new SceneInfoDTO();
        BeanUtils.copyProperties(sceneInfo, sceneInfoDTO);
        sceneInfoDTO.setPolicePresenceList(drawDataService.statisticalQuantity(sceneInfo.getId()));
        sceneInfoDTO.setDrawDataList(drawDataService.list(new LambdaQueryWrapper<DrawData>().eq(DrawData::getDeleteFlag, 0).eq(DrawData::getSceneId, id).apply(" (data -> 'info' ->> 'floorNum' is null or data -> 'info' ->> 'floorNum' = '')")));
        if (sceneInfo.getBasicDataId() != null) {
            sceneInfoDTO.setPointExtInfoList(pointExtInfoService.getPointExtInfoList(List.of(sceneInfo.getBasicDataId())));
        }
        return sceneInfoDTO;
    }

    @Override
    public List<SceneInfoDTO> getDetailByTaskIdAndSceneName(String taskId, String sceneName) {
        List<SceneInfo> sceneInfos = baseMapper.getDetailByTaskIdAndSceneName(taskId, sceneName);
        if (CollectionUtils.isEmpty(sceneInfos)) {
            return null;
        }
        List<SceneInfoDTO> sceneInfoDTOS = new ArrayList<>();
        for (SceneInfo sceneInfo : sceneInfos) {
            SceneInfoDTO sceneInfoDTO = new SceneInfoDTO();
            BeanUtils.copyProperties(sceneInfo, sceneInfoDTO);
            sceneInfoDTO.setDrawDataList(drawDataService.list(new LambdaQueryWrapper<DrawData>().eq(DrawData::getDeleteFlag, 0).eq(DrawData::getSceneId, sceneInfo.getId()).apply(" data->'info'->>'feature' is null")));
            sceneInfoDTOS.add(sceneInfoDTO);
        }
        return sceneInfoDTOS;
    }

    @Override
    public List<JSONObject> getSceneListByTaskIdAndSceneName(String taskId, String sceneName) {
        List<JSONObject> result = new ArrayList<>();
        List<SceneInfo> sceneInfoList = list(new LambdaQueryWrapper<SceneInfo>().eq(SceneInfo::getDeleteFlag, 0)
                .eq(SceneInfo::getTaskId, taskId)
                .eq(SceneInfo::getSceneName, sceneName).select(SceneInfo::getId, SceneInfo::getSceneName, SceneInfo::getBeginTime, SceneInfo::getEndTime));
        for (SceneInfo sceneInfo : sceneInfoList) {
            JSONObject jsonObject = new JSONObject();
            LocalDate localDate = sceneInfo.getBeginTime().toLocalDate();
            jsonObject.put("sceneDate", Utils.getTimeData(localDate));
            jsonObject.put("sceneTimeRange", sceneInfo.getBeginTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "-" + sceneInfo.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            jsonObject.put("sceneName", sceneInfo.getSceneName());
            jsonObject.put("sceneId", sceneInfo.getId());
            result.add(jsonObject);
        }
        return result;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public ResponseResult<?> addScene(SceneInfo sceneInfo) {

        //同一任务下各日程的时间不能有交叉
        /*int num = baseMapper.getNumOfCrossSceneTime(sceneInfo);
        if (num > 0) {
            return ResponseResult.error("当前日程开始结束时间与该任务下其它日程开始结束时间有交叉");
        }*/
        if (!sceneInfo.getBeginTime().toLocalDate().equals(sceneInfo.getEndTime().toLocalDate())) {
            return ResponseResult.error("当前日程开始结束时间不在同一天");
        }
        boolean successFlag = save(sceneInfo);
        if (!StringUtils.isEmpty(sceneInfo.getSchemeId()) || !StringUtils.isEmpty(sceneInfo.getSourceSceneId())) {
            String sceneId = !StringUtils.isEmpty(sceneInfo.getSchemeId()) ? sceneInfo.getSchemeId() : sceneInfo.getSourceSceneId();
            //保存常规方案数据
            //漫游
            SceneInfo scheme = getById(sceneId);
            sceneInfo.setKeyFrames(scheme.getKeyFrames());
            sceneInfo.setTime(scheme.getTime());
            sceneInfo.setThirdKeyFrames(scheme.getThirdKeyFrames());
            sceneInfo.setThirdTime(scheme.getThirdTime());
            sceneInfo.setViewData(scheme.getViewData());
            updateById(sceneInfo);
            //plan
            List<ScenePlan> scenePlanList = scenePlanService.list(new LambdaQueryWrapper<ScenePlan>()
                    .eq(ScenePlan::getSceneId, sceneId));
            scenePlanList.forEach(scenePlan -> {
                scenePlan.setSceneId(sceneInfo.getId());
                scenePlan.setId(IdWorker.getIdStr());
            });
            scenePlanService.saveBatch(scenePlanList);
            //plan_ext
            List<ScenePlanExt> scenePlanExtList = scenePlanExtService.list(new LambdaQueryWrapper<ScenePlanExt>()
                    .eq(ScenePlanExt::getId, sceneId));
            scenePlanExtList.forEach(scenePlanExt -> {
                scenePlanExt.setSceneId(sceneInfo.getId());
                scenePlanExt.setId(IdWorker.getIdStr());
            });
            scenePlanExtService.saveBatch(scenePlanExtList);
            //draw_data
            saveDrawData(sceneId, sceneInfo.getId(), sceneInfo.getTaskId());
            // 电子档案
            List<TaskArchivesData> taskArchivesDataList = taskArchivesDataService.getArchivesData(sceneId);
            if (!CollectionUtils.isEmpty(taskArchivesDataList)) {
                List<TaskArchivesData> newArchivesDataList = new ArrayList<>();
                taskArchivesDataList.forEach(taskArchivesData -> {
                    if (CollectionUtils.isEmpty(taskArchivesData.getChildren())) {
                        taskArchivesData.setId(IdWorker.getIdStr());
                        taskArchivesData.setBusinessId(sceneInfo.getId());
                        newArchivesDataList.add(taskArchivesData);
                    } else {
                        String id = IdWorker.getIdStr();
                        taskArchivesData.setId(id);
                        taskArchivesData.setBusinessId(sceneInfo.getId());
                        newArchivesDataList.add(taskArchivesData);
                        taskArchivesData.getChildren().forEach(children -> {
                            children.setId(IdWorker.getIdStr());
                            children.setParentId(id);
                            children.setBusinessId(sceneInfo.getId());
                            newArchivesDataList.add(children);
                        });
                    }
                });
                taskArchivesDataService.saveBatch(newArchivesDataList);
            }
        }

        return successFlag ? ResponseResult.success() : ResponseResult.error();
    }

    public void saveDrawData(String schemeId, String sceneId, String taskId) {
        List<DrawData> drawDataList = drawDataService.list(new LambdaQueryWrapper<DrawData>().eq(DrawData::getDeleteFlag, 0)
                .eq(DrawData::getSceneId, schemeId));
        drawDataList.forEach(drawData -> {
            drawData.setSceneId(sceneId);
            drawData.setTaskId(taskId);
            String id = IdWorker.getIdStr();
            drawData.setId(id);
            drawData.getData().put("id", id);
            JSONObject marker = drawData.getData().getJSONObject("marker");
            if (Objects.nonNull(marker)) {
                marker.put("id", id);
                drawData.getData().put("marker", marker);
            }
            JSONObject uav = drawData.getData().getJSONObject("uav");
            if (Objects.nonNull(uav)) {
                uav.put("id", id);
                drawData.getData().put("uav", uav);
            }
            JSONObject info = drawData.getData().getJSONObject("info");
            if (Objects.nonNull(info)) {
                JSONObject lineData = info.getJSONObject("lineData");
                if (Objects.nonNull(lineData)) {
                    lineData.put("id", IdWorker.getIdStr());
                    drawData.getData().getJSONObject("info").put("lineData", lineData);
                }
                JSONObject custom = info.getJSONObject("custom");
                if (Objects.nonNull(custom)) {
                    custom.put("id", IdWorker.getIdStr());
                    drawData.getData().getJSONObject("info").put("custom", custom);
                }
            }
        });
        drawDataService.saveBatch(drawDataList);
    }

    @Override
    public ResponseResult<?> updateScene(SceneInfo sceneInfo) {
        //同一任务下各场景的时间不能有交叉
        if (sceneInfo.getBeginTime() != null && sceneInfo.getEndTime() != null) {
            /*int num = baseMapper.getNumOfCrossSceneTime(sceneInfo);
            if (num > 0) {
                return ResponseResult.error("当前日程开始结束时间与该任务下其它日程开始结束时间有交叉");
            }*/
            if (!sceneInfo.getBeginTime().toLocalDate().equals(sceneInfo.getEndTime().toLocalDate())) {
                return ResponseResult.error("当前日程开始结束时间不在同一天");
            }
        }
        //是否更换场景
        SceneInfo oldScene = getById(sceneInfo.getId());
        if (sceneInfo.getBasicDataId() != null && !sceneInfo.getBasicDataId().equals(oldScene.getBasicDataId())) {
            //清理旧场景关联的数据
            drawDataService.update(new DrawData().setDeleteFlag(1), new LambdaQueryWrapper<DrawData>().eq(DrawData::getSceneId, sceneInfo.getId()));
            scenePlanService.remove(new LambdaQueryWrapper<ScenePlan>().eq(ScenePlan::getSceneId, sceneInfo.getId()));
            scenePlanExtService.remove(new LambdaQueryWrapper<ScenePlanExt>().eq(ScenePlanExt::getSceneId, sceneInfo.getId()));
            //清除电子档案
            taskArchivesDataService.remove(new LambdaQueryWrapper<TaskArchivesData>().eq(TaskArchivesData::getBusinessId, sceneInfo.getId()));
        }
        //是否选择常备方案
        if (org.springframework.util.StringUtils.hasText(sceneInfo.getSchemeId())) {
            SceneInfo scheme = getById(sceneInfo.getSchemeId());
            copyScene(scheme.getId(), sceneInfo.getId(), sceneInfo.getTaskId());
        } else if (org.springframework.util.StringUtils.hasText(sceneInfo.getSourceSceneId())) {
            //是否选场景复用
            SceneInfo scheme = getById(sceneInfo.getSourceSceneId());
            copyScene(scheme.getId(), sceneInfo.getId(), sceneInfo.getTaskId());
        }
        boolean successFlag = update(sceneInfo, new LambdaQueryWrapper<SceneInfo>().eq(SceneInfo::getId, sceneInfo.getId()));
        return successFlag ? ResponseResult.success() : ResponseResult.error();
    }

    @Override
    public boolean deleteScene(String id) {
        boolean successFlag = update(new SceneInfo().setDeleteFlag(1), new LambdaQueryWrapper<SceneInfo>().eq(SceneInfo::getId, id));
        if (successFlag) {
            drawDataService.update(new DrawData().setDeleteFlag(1), new LambdaQueryWrapper<DrawData>().eq(DrawData::getSceneId, id));
        }
        return successFlag;
    }

    @Override
    public List<SceneInfo> getSimpleListByTaskIdAndType(String taskId, String type) {
        return baseMapper.getSimpleListByTaskIdAndType(taskId, type);
    }

    @Override
    public List<SceneInfo> getListByTaskId(String taskId) {
        List<SceneInfo> sceneInfoList = baseMapper.getListByTaskId(taskId);
        sceneInfoList.forEach(sceneInfo -> {
            FileInfo fileInfo = fileInfoService.getOne(new LambdaQueryWrapper<FileInfo>().eq(FileInfo::getBusinessId, sceneInfo.getId()), false);
            if (fileInfo != null) {
                sceneInfo.setFileName(fileInfo.getOriginalFileName());
                sceneInfo.setFileUrl(fileInfo.getFilePath());
            }
        });
        return sceneInfoList;
    }

    @Override
    public JSONObject getBasicPlanOfScene(String sceneId) {
        JSONObject jsonObject = new JSONObject();
        SceneInfo sceneInfo = getById(sceneId);
        if (Objects.isNull(sceneInfo)) {
            return jsonObject;
        }
        jsonObject.put("sceneName", sceneInfo.getSceneName());
        jsonObject.put("sceneType", sceneInfo.getType());
        jsonObject.put("basicInfo", scenePlanService.getScenePlanNode(sceneId, "基本情况"));
        jsonObject.put("guidingIdeology", scenePlanService.getScenePlanNode(sceneId, "指导思想"));
        jsonObject.put("jobRequirements", scenePlanService.getScenePlanNode(sceneId, "工作要求"));
        jsonObject.put("orgLeadership", scenePlanService.getScenePlanNode(sceneId, "组织领导"));
        return jsonObject;
    }

    @Override
    public List<SceneDataDTO> getScenePlanNodeByTaskId(String taskId, String planNode) {
        List<SceneDataDTO> sceneDataDTOList = new ArrayList<>();
        String type = "0";
        String scenePlanNode = "";
        if ("现场警卫组".equals(planNode)) {
            type = "2";
            scenePlanNode = "警力部署";
        }
        if ("交通保障组".equals(planNode)) {
            type = "1";
            scenePlanNode = "警力部署";
        } else if ("住地警卫组".equals(planNode)) {
            type = "3";
            scenePlanNode = "警力部署";
        } else if ("警力部署".equals(planNode)) {
            //一张图
            type = null;
            scenePlanNode = "警力部署";
        } else if ("应急处突组".equals(planNode)) {
            //"应急力量";
            List<SceneInfo> sceneInfos = getSimpleListByTaskIdAndType(taskId, null);
            for (SceneInfo sceneInfo : sceneInfos) {
                SceneDataDTO sceneDataDTO = new SceneDataDTO();
                sceneDataDTO.setSceneName(sceneInfo.getSceneName());
                List<ScenePlan> scenePlanList = new ArrayList<>();
                scenePlanList.addAll(scenePlanService.getScenePlanNode(sceneInfo.getId(), "应急力量"));
                //应急避险点
                scenePlanList.addAll(scenePlanService.getScenePlanNode(sceneInfo.getId(), "应急避险点"));
                //应急医院
                scenePlanList.addAll(scenePlanService.getScenePlanNode(sceneInfo.getId(), "应急避险点"));
                sceneDataDTO.setScenePlanList(scenePlanList);
                sceneDataDTOList.add(sceneDataDTO);
            }
        }
        if ("警力部署".equals(scenePlanNode)) {
            //先查询任务下现场数
            List<SceneInfo> sceneInfos = getSimpleListByTaskIdAndType(taskId, type);
            //再查每个现场的警力部署
            for (SceneInfo sceneInfo : sceneInfos) {
                List<ScenePlan> scenePlanList = scenePlanService.getScenePlanNode(sceneInfo.getId(), scenePlanNode);

                SceneDataDTO.SceneDataDTOBuilder builder = SceneDataDTO.builder().sceneName(sceneInfo.getSceneName()).scenePlanList(scenePlanList);
                //道路
                if ("1".equals(sceneInfo.getType())) {
                    updateSceneRoadData(sceneInfo);
                    builder.sceneRoadDesc(sceneInfo.getSceneRoadDesc())
                            .sceneRoadLength(sceneInfo.getSceneRoadLength())
                            .sceneRoadTime(sceneInfo.getSceneRoadTime());
                }
                sceneDataDTOList.add(builder.build());
            }
        }

        return sceneDataDTOList;
    }


    @Override
    public String getRoadName(List<DrawData> drawDataList) {
        String roadName = "";
        for (int i = 0; i < drawDataList.size(); i++) {
            if (i == 0) {
                roadName = drawDataList.get(i).getName();
            } else {
                roadName = roadName + "——" + drawDataList.get(i).getName();
            }
        }
        return roadName;
    }

    @Override
    public List<DrawData> getDrawDataOfScene(String sceneId) {
        return drawDataService.list(new LambdaQueryWrapper<DrawData>()
                .eq(DrawData::getDeleteFlag, 0)
                .eq(DrawData::getSceneId, sceneId).apply(" (data -> 'info' ->> 'floorNum' is null or data -> 'info' ->> 'floorNum' = '')"));
    }

    @Override
    public JSONObject getBasicDrawDataOfScene(List<String> sceneIds) {
        JSONObject jsonObject = new JSONObject();
        if (CollectionUtils.isEmpty(sceneIds)) {
            return jsonObject;
        }
        SceneInfo sceneInfo = getById(sceneIds.get(0));
        jsonObject.put("sceneName", sceneInfo.getSceneName());
        jsonObject.put("drawLineData", getDrawLines(sceneIds));
        jsonObject.put("basicData", pointExtInfoService.getPointExtInfoList(Collections.singletonList(sceneInfo.getBasicDataId())));
        return jsonObject;
    }

    @Override
    public List<Map> getPostPoliceDataOfScene(String sceneId) {
        return drawDataService.getPostPoliceDataOfScene(sceneId);
    }

    public BasicDataDTO getRoadResource(List<String> sceneIds, String type) {
        //道路
        List<BasicDataDTO> dl = new ArrayList<>();
        int total = 0;
        int index = 0;
        for (String node : cityRoadNodes) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            basicDataDTOList = getResourceData(sceneIds, type, node, basicDataDTOList);
            dl.add(BasicDataDTO.builder().id("7" + (index++)).name(node).type(node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            total = total + basicDataDTOList.size();
        }
        return BasicDataDTO.builder().id("7").name("城市道路").children(dl)
                .num(total).build();

    }


    public List<BasicDataDTO> getKeyResourceTree(String sceneId) {
        //取场景数据
        List<SceneInfo> sceneInfoList = baseMapper.getSimpleList(null, List.of(sceneId), null);
        //自定义线路
        List<SceneInfo> cityRoadScenes = sceneInfoList.stream().filter(s -> "1".equals(s.getType()) && ("".equals(s.getBasicDataId()) || s.getBasicDataId() == null)).collect(Collectors.toList());
        //高速
        Map<String, List<SceneInfo>> highwayMap = new LinkedHashMap<>();
        sceneInfoList.stream().filter(s -> "1".equals(s.getType()) && (!"".equals(s.getBasicDataId()) && s.getBasicDataId() != null)
                        && highwayTypeId.equals(s.getBasicDataTypeId()))
                .forEach(sceneInfo -> {
                    List<SceneInfo> sceneInfos = highwayMap.get(sceneInfo.getBasicDataId());
                    if (CollectionUtils.isEmpty(sceneInfos)) {
                        sceneInfos = new ArrayList<>();
                    }
                    sceneInfos.add(sceneInfo);
                    highwayMap.put(sceneInfo.getBasicDataId(), sceneInfos);
                });
        // 高铁
        Map<String, List<SceneInfo>> railwayMap = new LinkedHashMap<>();
        sceneInfoList.stream().filter(s -> "1".equals(s.getType()) && (!"".equals(s.getBasicDataId()) && s.getBasicDataId() != null)
                        && railwayTypeId.equals(s.getBasicDataTypeId()))
                .forEach(sceneInfo -> {
                    List<SceneInfo> sceneInfos = railwayMap.get(sceneInfo.getBasicDataId());
                    if (CollectionUtils.isEmpty(sceneInfos)) {
                        sceneInfos = new ArrayList<>();
                    }
                    sceneInfos.add(sceneInfo);
                    railwayMap.put(sceneInfo.getBasicDataId(), sceneInfos);
                });
        //现场
        Map<String, List<SceneInfo>> siteMap = new LinkedHashMap<>();
        sceneInfoList.stream().filter(s -> "2".equals(s.getType())).forEach(sceneInfo -> {
            List<SceneInfo> sceneInfos = siteMap.get(sceneInfo.getBasicDataId());
            if (CollectionUtils.isEmpty(sceneInfos)) {
                sceneInfos = new ArrayList<>();
            }
            sceneInfos.add(sceneInfo);
            siteMap.put(sceneInfo.getBasicDataId(), sceneInfos);
        });

        //住地
        Map<String, List<SceneInfo>> residenceMap = new LinkedHashMap<>();
        sceneInfoList.stream().filter(s -> "3".equals(s.getType())).forEach(sceneInfo -> {
            List<SceneInfo> sceneInfos = residenceMap.get(sceneInfo.getBasicDataId());
            if (CollectionUtils.isEmpty(sceneInfos)) {
                sceneInfos = new ArrayList<>();
            }
            sceneInfos.add(sceneInfo);
            residenceMap.put(sceneInfo.getBasicDataId(), sceneInfos);
        });
        //基础要素
        BasicDataDTO basicFeature = getBasicFeatureData(siteMap, residenceMap, cityRoadScenes, railwayMap, highwayMap, "1", "1");
        return List.of(basicFeature);
    }

    @Override
    public List<BasicDataDTO> getMultiSceneResourceTree(List<String> sceneIds) {
        //取场景数据
        List<SceneInfo> sceneInfoList = baseMapper.getSimpleList(null, sceneIds, null);
        String taskLevel = "";
        if (!CollectionUtils.isEmpty(sceneInfoList)) {
            Task task = taskMapper.selectById(sceneInfoList.get(0).getTaskId());
            if (task != null) {
                taskLevel = task.getTaskLevel();
            }
        }
        //自定义线路
        List<SceneInfo> cityRoadScenes = sceneInfoList.stream().filter(s -> "1".equals(s.getType()) && ("".equals(s.getBasicDataId()) || s.getBasicDataId() == null)).collect(Collectors.toList());

        //高速
        Map<String, List<SceneInfo>> highwayMap = new LinkedHashMap<>();
        sceneInfoList.stream().filter(s -> ("1".equals(s.getType()) || "4".equals(s.getType())) && (!"".equals(s.getBasicDataId()) && s.getBasicDataId() != null)
                        && highwayTypeId.equals(s.getBasicDataTypeId()))
                .forEach(sceneInfo -> {
                    List<SceneInfo> sceneInfos = highwayMap.get(sceneInfo.getBasicDataId());
                    if (CollectionUtils.isEmpty(sceneInfos)) {
                        sceneInfos = new ArrayList<>();
                    }
                    sceneInfos.add(sceneInfo);
                    highwayMap.put(sceneInfo.getBasicDataId(), sceneInfos);
                });


        // 高铁
        Map<String, List<SceneInfo>> railwayMap = new LinkedHashMap<>();
        sceneInfoList.stream().filter(s -> ("1".equals(s.getType()) || "5".equals(s.getType())) && (!"".equals(s.getBasicDataId()) && s.getBasicDataId() != null)
                        && railwayTypeId.equals(s.getBasicDataTypeId()))
                .forEach(sceneInfo -> {
                    List<SceneInfo> sceneInfos = railwayMap.get(sceneInfo.getBasicDataId());
                    if (CollectionUtils.isEmpty(sceneInfos)) {
                        sceneInfos = new ArrayList<>();
                    }
                    sceneInfos.add(sceneInfo);
                    railwayMap.put(sceneInfo.getBasicDataId(), sceneInfos);
                });

        //现场
        Map<String, List<SceneInfo>> siteMap = new LinkedHashMap<>();
        sceneInfoList.stream().filter(s -> "2".equals(s.getType())).forEach(sceneInfo -> {
            List<SceneInfo> sceneInfos = siteMap.get(sceneInfo.getBasicDataId());
            if (CollectionUtils.isEmpty(sceneInfos)) {
                sceneInfos = new ArrayList<>();
            }
            sceneInfos.add(sceneInfo);
            siteMap.put(sceneInfo.getBasicDataId(), sceneInfos);
        });

        //住地
        Map<String, List<SceneInfo>> residenceMap = new LinkedHashMap<>();
        sceneInfoList.stream().filter(s -> "3".equals(s.getType())).forEach(sceneInfo -> {
            List<SceneInfo> sceneInfos = residenceMap.get(sceneInfo.getBasicDataId());
            if (CollectionUtils.isEmpty(sceneInfos)) {
                sceneInfos = new ArrayList<>();
            }
            sceneInfos.add(sceneInfo);
            residenceMap.put(sceneInfo.getBasicDataId(), sceneInfos);
        });
        //基础要素
        BasicDataDTO basicFeature = getBasicFeatureData(siteMap, residenceMap, cityRoadScenes, railwayMap, highwayMap, "1", "");
        //警力
        BasicDataDTO police = getPoliceData(siteMap, residenceMap, cityRoadScenes, railwayMap, highwayMap, "1", taskLevel);
        //防爆安检
        BasicDataDTO securityCheck = getSecurityCheckData(siteMap, residenceMap, cityRoadScenes, railwayMap, highwayMap, "1");
        //任务路线
        BasicDataDTO line = getLineData(siteMap, residenceMap, cityRoadScenes, railwayMap, highwayMap, "1");
        //应急方案
        BasicDataDTO emergency = getEmergencyData(siteMap, residenceMap, cityRoadScenes, railwayMap, highwayMap, "1");

        //int enterpriseNum = commercialEnterpriseInfoService.getMerchantDataByType(1).size();
        BasicDataDTO enterprise = BasicDataDTO.builder().id(IdWorker.getIdStr()).name("企业数据").type("企业数据").dataLevelFlag("1").build();
        //int businessNum = commercialEnterpriseInfoService.getMerchantDataByType(2).size();
        BasicDataDTO business = BasicDataDTO.builder().id(IdWorker.getIdStr()).name("商业数据").type("商业数据").dataLevelFlag("1").build();
        //int residentialNum = getResidenceData().size();
        BasicDataDTO residential = BasicDataDTO.builder().id(IdWorker.getIdStr()).name("社区数据").type("社区数据").dataLevelFlag("1").build();

        BasicDataDTO resource = BasicDataDTO.builder().id(IdWorker.getIdStr()).type("资源列表").name("全部").children(Arrays.asList(basicFeature, police, securityCheck, line, emergency, enterprise, business, residential)).num(basicFeature.getNum() + police.getNum() + securityCheck.getNum() + line.getNum() + emergency.getNum()).build();
        return List.of(resource);
    }

    @Override
    public List<BasicDataDTO> querySceneResourcesByCondition(DrawData drawData) {
        List<BasicDataDTO> resourceList = new ArrayList<>();
        // 设置为
        drawData.setType("lines");
        drawData.setLineType("0");
        drawData.setSceneType("1");
        List<DrawData> drawDataList = drawDataService.getLineList(drawData);
        if(StringUtils.isNotBlank(drawData.getSceneId())){
            // 如果场景不为空，则直接返回路线数据
            List coorList = new ArrayList<>();
            StringBuffer name = new StringBuffer();
            for (DrawData data : drawDataList) {
                coorList.addAll(data.getGeojson().getObject("coordinates", List.class));
                name.append(data.getName()+" ");
            }
            resourceList.add(BasicDataDTO.builder().id(drawData.getSceneId()).name(name.toString()).coordinates(coorList).build()) ;
            return resourceList;
        }else {
            // 如果场景为空，则根据场景进行分组划分
            // 按照 id 分组
            Map<String, List<DrawData>> groupedById = drawDataList.stream()
                    .collect(Collectors.groupingBy(DrawData::getSceneId));
            groupedById.forEach((key, value) -> {
                BasicDataDTO  basicItem = BasicDataDTO.builder().id(key).name(value.get(0).getSceneName()).build();
                System.out.println("Group Key: " + key);
                List coorList = new ArrayList<>();

//                StringBuffer name = new StringBuffer();
                for (DrawData data : value) {
                    List coor = data.getGeojson().getObject("coordinates", List.class);
                    // 获取z坐标，并将其添加到coorList中
                    JSONArray jsonArray = (JSONArray) data.getData().get("coordinates");
                    for (int i = 0; i < coor.size(); i++) {

                        if(jsonArray.get(i) != null
                                && jsonArray.get(i) instanceof JSONArray
                                && ((JSONArray) jsonArray.get(i)).size() > 2
                            && ((JSONArray) jsonArray.get(i)).get(2) != null ){
                            JSONArray corrArr = (JSONArray) coor.get(i);
                            corrArr.add(((JSONArray) jsonArray.get(i)).get(2));
                        }
                    }
                    coorList.addAll(coor);
//                    name.append(data.getName()+" ");
                }
                basicItem.setCoordinates(coorList);
                // 使用场景名称，注释此处代码
//                basicItem.setName(name.toString());
                resourceList.add(basicItem);
            });

            return resourceList;
        }

    }


    public List getResidenceData() {
        return residentialBuildingInfoService.list(new LambdaQueryWrapper<ResidentialBuildingInfo>()
                .isNotNull(ResidentialBuildingInfo::getX)
                .isNotNull(ResidentialBuildingInfo::getY)
                .isNotNull(ResidentialBuildingInfo::getZ));
    }

    @Override
    public Map<String, Object> getSceneLineData(String sceneId) {
        Map map = new LinkedHashMap();
        DrawData drawData = new DrawData();
        drawData.setSceneId(sceneId);
        drawData.setType("lines");
        drawData.setLineType("0");

        List<DrawData> drawDataList = drawDataService.getLineList(drawData);
        if (CollectionUtils.isEmpty(drawDataList)) {
            SceneInfo sceneInfo = getOne(new LambdaQueryWrapper<SceneInfo>().eq(SceneInfo::getId, sceneId), false);
            if (org.springframework.util.StringUtils.hasText(sceneInfo.getBasicDataId())) {
                PointInfo pointInfo = pointInfoService.getPointData(sceneInfo.getBasicDataId());
                map.put("totalLength", pointInfo.getData().getString("TotalLengthOfMileage"));
            }
        } else {
            List detail = new LinkedList();
            AtomicReference<Double> totalLength = new AtomicReference<>(0.0);
            drawDataList.forEach(data -> {
                JSONObject jsonObject = new JSONObject();
                totalLength.set(totalLength.get() + data.getLength() / 1000);
                jsonObject.put("id", data.getId());
                jsonObject.put("name", data.getName());
                jsonObject.put("length", data.getLength() / 1000);
                detail.add(jsonObject);
            });
            map.put("totalLength", totalLength.get());
            map.put("detail", detail);
        }

        return map;
    }


    @Override
    public List<BasicDataDTO> getTaskResourceTree(String taskId) {
        int total = 0;
        List<BasicDataDTO> allList = new ArrayList<>();
        //取任务下场景下
        List<SceneInfo> sceneInfoList = list(new LambdaQueryWrapper<SceneInfo>().eq(SceneInfo::getDeleteFlag, 0).eq(SceneInfo::getTaskId, taskId));
        //自定义线路
        List<String> zdydlSceneIds = sceneInfoList.stream().filter(s -> "1".equals(s.getType()) && ("".equals(s.getBasicDataId()) || s.getBasicDataId() == null))
                .map(SceneInfo::getId).collect(Collectors.toList());
        //城市道路
        List<PointExtInfo> dataList = new ArrayList<>();
        //查询任务下的线路，然后查询线路周边的基础数据
        if (!CollectionUtils.isEmpty(zdydlSceneIds)) {
            List<DrawData> drawDataList = getDrawLines(zdydlSceneIds);
            for (DrawData drawData : drawDataList) {
                try {
                    dataList.addAll(spatialQuery.queryLineBufferData(drawData.getGeom(), bufferRadius));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        dataList = new ArrayList<>(new LinkedHashSet<>(dataList));

        List<PointExtInfo> pointExtInfoList = dataList;

        List<BasicDataDTO> all = new ArrayList<>();

        //城市道路
        List<BasicDataDTO> cityRoadList = new ArrayList<>();
        int cityRoadNum = 0;
        int cityIndex = 0;
        for (String node : cityRoadNodes) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointExtInfoList)) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
            }
            basicDataDTOList = getResourceData(zdydlSceneIds, "1", node, basicDataDTOList);
            cityRoadList.add(BasicDataDTO.builder().id("7" + (cityIndex++)).name(node).type(node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            cityRoadNum = cityRoadNum + basicDataDTOList.size();
        }
        total = total + cityRoadNum;


        List<String> xcSceneIds = sceneInfoList.stream().filter(s -> "2".equals(s.getType())).map(SceneInfo::getId).collect(Collectors.toList());

        List<String> xcIds = sceneInfoList.stream().filter(s -> "2".equals(s.getType())).map(SceneInfo::getBasicDataId).collect(Collectors.toList());
        //重点关注 现场 城市道路
        List<PointExtInfo> xcPointExtInfoList = null;
        List<PointInfo> pointInfos = null;
        if (!CollectionUtils.isEmpty(xcIds)) {
            pointInfos = pointInfoService.list(new LambdaQueryWrapper<PointInfo>().in(PointInfo::getJcxxId, xcIds));
        }
        if (!CollectionUtils.isEmpty(pointInfos)) {
            List<String> ids = pointInfos.stream().map(PointInfo::getId).collect(Collectors.toList());
            ids.addAll(xcIds);
            xcPointExtInfoList = pointExtInfoService.getPointExtInfoList(ids);
        } else {
            xcPointExtInfoList = pointExtInfoService.getPointExtInfoList(xcIds);
        }

        //重点关注
        List<BasicDataDTO> gzList = new ArrayList<>();

        int gzNum = 0;
        int gzindex = 0;
        //现场
        List<BasicDataDTO> xxcList = new ArrayList<>();
        int xcNum = 0;
        int xcIndex = 0;
        for (String node : siteNodes) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(xcPointExtInfoList)) {
                List<PointExtInfo> list = xcPointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
            }
            basicDataDTOList = getResourceData(xcSceneIds, "2", node, basicDataDTOList);
            xxcList.add(BasicDataDTO.builder().id("2" + (xcIndex++)).name(node).type(node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            xcNum = xcNum + basicDataDTOList.size();
        }
        total = total + xcNum;

        for (String node : cityRoadNodes) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            basicDataDTOList = getResourceData(xcSceneIds, "2", node, basicDataDTOList);
            cityRoadList.add(BasicDataDTO.builder().id("7" + (cityIndex++)).name(node).type(node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            cityRoadNum = cityRoadNum + basicDataDTOList.size();
            total = total + basicDataDTOList.size();
        }


        List<String> zdSceneIds = sceneInfoList.stream().filter(s -> "3".equals(s.getType())).map(SceneInfo::getId).collect(Collectors.toList());

        List<String> zdIds = sceneInfoList.stream().filter(s -> "3".equals(s.getType())).map(SceneInfo::getBasicDataId).collect(Collectors.toList());
        //重点关注 住地 城市道路

        List<PointExtInfo> zdPointExtInfoList = null;

        List<PointInfo> zdPointInfos = null;
        if (!CollectionUtils.isEmpty(zdIds)) {
            zdPointInfos = pointInfoService.list(new LambdaQueryWrapper<PointInfo>().in(PointInfo::getJcxxId, zdIds));
        }
        if (!CollectionUtils.isEmpty(zdPointInfos)) {
            List<String> ids = zdPointInfos.stream().map(PointInfo::getId).collect(Collectors.toList());
            ids.addAll(zdIds);
            zdPointExtInfoList = pointExtInfoService.getPointExtInfoList(ids);
        } else {
            zdPointExtInfoList = pointExtInfoService.getPointExtInfoList(zdIds);
        }

        if (!CollectionUtils.isEmpty(keyFocusNodes)) {
            for (String node : keyFocusNodes) {
                List<BasicDataDTO> zdryList = new ArrayList<>();
                List<PointExtInfo> list = new ArrayList<>();
                if (!CollectionUtils.isEmpty(xcPointExtInfoList)) {
                    list.addAll(xcPointExtInfoList.stream().filter(pointExtInfo -> pointExtInfo.getType().endsWith(node)).collect(Collectors.toList()));
                }
                if (!CollectionUtils.isEmpty(zdPointExtInfoList)) {
                    list.addAll(zdPointExtInfoList.stream().filter(pointExtInfo -> pointExtInfo.getType().endsWith(node)).collect(Collectors.toList()));
                }
                for (PointExtInfo pointExtInfo : list) {
                    if (pointExtInfo.getGeojson() != null) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        zdryList.add(basicDataDTO);
                    }
                }
                zdryList = getResourceData(xcSceneIds, null, node, zdryList);

                gzList.add(BasicDataDTO.builder().id("9" + (gzindex++)).name(node).type(node).children(zdryList).num(zdryList.size()).build());
                gzNum = gzNum + zdryList.size();
            }
        }

        //住地
        List<BasicDataDTO> zdList = new ArrayList<>();
        int zdNum = 0;
        int zdIndex = 0;
        for (String node : residenceNodes) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointExtInfoList)) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        BasicDataDTO basicData = BasicDataDTO.builder().id(pointExtInfo.getId()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicData);
                    }
                }
            }
            basicDataDTOList = getResourceData(zdSceneIds, "3", node, basicDataDTOList);
            zdList.add(BasicDataDTO.builder().id("3" + (zdIndex++)).name(node).type(node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            zdNum = zdNum + basicDataDTOList.size();
        }

        for (String node : cityRoadNodes) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            basicDataDTOList = getResourceData(zdSceneIds, "3", node, basicDataDTOList);
            cityRoadList.add(BasicDataDTO.builder().id("7" + (cityIndex++)).name(node).type(node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            cityRoadNum = cityRoadNum + basicDataDTOList.size();
            total = total + basicDataDTOList.size();
        }
        total = total + zdNum;


        all.add(BasicDataDTO.builder().id("1").name("重点关注").num(gzNum).children(gzList).build());

        all.add(BasicDataDTO.builder().id("2").name("现场").num(xcNum).children(xxcList).build());

        all.add(BasicDataDTO.builder().id("3").name("住地").num(zdNum).children(zdList).build());


        //高速、高铁
//        List<String> waySceneIds = sceneInfoList.stream().filter(s -> "1".equals(s.getType()) && (!"".equals(s.getBasicDataId()) && s.getBasicDataId() != null))
//                .map(SceneInfo::getId).collect(Collectors.toList());
        AtomicInteger gsNum = new AtomicInteger();
        AtomicInteger gtNum = new AtomicInteger();
        List<String> wayIds = sceneInfoList.stream().filter(s -> "1".equals(s.getType()) && (!"".equals(s.getBasicDataId()) && s.getBasicDataId() != null))
                .map(SceneInfo::getBasicDataId).collect(Collectors.toList());

        List<Map<String, List<BasicDataDTO>>> basicDataList = pointInfoService.getResourceOfHighWayAndRailWay(wayIds);
        //高速
        if (!basicDataList.get(0).isEmpty()) {
            List<BasicDataDTO> gs = new ArrayList<>();
            final int[] gsIndex = {0};
            basicDataList.get(0).forEach((key, value) -> {
                gsIndex[0]++;
                gsNum.set(gsNum.get() + value.size());
                gs.add(BasicDataDTO.builder().id("5" + gsIndex[0]).name(key).type(key).children(value).num(value.size()).build());
            });
            all.add(BasicDataDTO.builder().id("5").name("高速").children(gs).num(gsNum.get()).build());
        }
        //高铁
        if (!basicDataList.get(1).isEmpty()) {
            List<BasicDataDTO> gt = new ArrayList<>();
            final int[] gtIndex = {0};
            basicDataList.get(1).forEach((key, value) -> {
                gtIndex[0]++;
                gtNum.set(gtNum.get() + value.size());
                gt.add(BasicDataDTO.builder().id("6" + gtIndex[0]).name(key).type(key).children(value).num(value.size()).build());
            });
            all.add(BasicDataDTO.builder().id("6").name("高铁").children(gt).num(gtNum.get()).build());
        }
        total = total + gsNum.get() + gtNum.get();


        all.add(BasicDataDTO.builder().id("7").name("城市道路").num(cityRoadNum).children(cityRoadList).build());

        allList.add(BasicDataDTO.builder().id("0").name("全部").children(all).num(total).build());


        return allList;
    }

    @Override
    public List<BasicDataDTO> getTaskResourceTreeNew(String taskId) {
        //取任务下场景下
        List<SceneInfo> sceneInfoList = baseMapper.getSimpleList(taskId, null, null);
        Task task = taskMapper.selectById(taskId);
        String taskLevel = "";
        if (task != null) {
            taskLevel = task.getTaskLevel();
        }
        //自定义线路
        List<SceneInfo> cityRoadScenes = sceneInfoList.stream().filter(s -> "1".equals(s.getType()) && ("".equals(s.getBasicDataId()) || s.getBasicDataId() == null)).collect(Collectors.toList());
        //高速
        Map<String, List<SceneInfo>> highwayMap = new LinkedHashMap<>();
        sceneInfoList.stream().filter(s -> "1".equals(s.getType()) && (!"".equals(s.getBasicDataId()) && s.getBasicDataId() != null)
                        && highwayTypeId.equals(s.getBasicDataTypeId()))
                .forEach(sceneInfo -> {
                    List<SceneInfo> sceneInfos = highwayMap.get(sceneInfo.getBasicDataId());
                    if (CollectionUtils.isEmpty(sceneInfos)) {
                        sceneInfos = new ArrayList<>();
                    }
                    sceneInfos.add(sceneInfo);
                    highwayMap.put(sceneInfo.getBasicDataId(), sceneInfos);
                });


        // 高铁
        Map<String, List<SceneInfo>> railwayMap = new LinkedHashMap<>();
        sceneInfoList.stream().filter(s -> "1".equals(s.getType()) && (!"".equals(s.getBasicDataId()) && s.getBasicDataId() != null)
                        && railwayTypeId.equals(s.getBasicDataTypeId()))
                .forEach(sceneInfo -> {
                    List<SceneInfo> sceneInfos = railwayMap.get(sceneInfo.getBasicDataId());
                    if (CollectionUtils.isEmpty(sceneInfos)) {
                        sceneInfos = new ArrayList<>();
                    }
                    sceneInfos.add(sceneInfo);
                    railwayMap.put(sceneInfo.getBasicDataId(), sceneInfos);
                });

        //现场
        Map<String, List<SceneInfo>> siteMap = new LinkedHashMap<>();
        sceneInfoList.stream().filter(s -> "2".equals(s.getType())).forEach(sceneInfo -> {
            List<SceneInfo> sceneInfos = siteMap.get(sceneInfo.getBasicDataId());
            if (CollectionUtils.isEmpty(sceneInfos)) {
                sceneInfos = new ArrayList<>();
            }
            sceneInfos.add(sceneInfo);
            siteMap.put(sceneInfo.getBasicDataId(), sceneInfos);
        });

        //住地
        Map<String, List<SceneInfo>> residenceMap = new LinkedHashMap<>();
        sceneInfoList.stream().filter(s -> "3".equals(s.getType())).forEach(sceneInfo -> {
            List<SceneInfo> sceneInfos = residenceMap.get(sceneInfo.getBasicDataId());
            if (CollectionUtils.isEmpty(sceneInfos)) {
                sceneInfos = new ArrayList<>();
            }
            sceneInfos.add(sceneInfo);
            residenceMap.put(sceneInfo.getBasicDataId(), sceneInfos);
        });
        //基础要素
        BasicDataDTO basicFeature = getBasicFeatureData(siteMap, residenceMap, cityRoadScenes, railwayMap, highwayMap, null, null);
        //警力
        BasicDataDTO police = getPoliceData(siteMap, residenceMap, cityRoadScenes, railwayMap, highwayMap, null, taskLevel);
        //防爆安检
        BasicDataDTO securityCheck = getSecurityCheckData(siteMap, residenceMap, cityRoadScenes, railwayMap, highwayMap, null);
        //任务路线
        BasicDataDTO line = getLineData(siteMap, residenceMap, cityRoadScenes, railwayMap, highwayMap, null);
        //应急方案
        BasicDataDTO emergency = getEmergencyData(siteMap, residenceMap, cityRoadScenes, railwayMap, highwayMap, null);

        int enterpriseNum = commercialEnterpriseInfoService.getMerchantDataByType(1).size();
        BasicDataDTO enterprise = BasicDataDTO.builder().id(IdWorker.getIdStr()).name("企业数据").type("企业数据").dataLevelFlag("1").num(enterpriseNum).build();
        int businessNum = commercialEnterpriseInfoService.getMerchantDataByType(2).size();
        BasicDataDTO business = BasicDataDTO.builder().id(IdWorker.getIdStr()).name("商业数据").type("商业数据").dataLevelFlag("1").num(businessNum).build();
        //int residentialNum = getResidenceData().size();
        BasicDataDTO residential = BasicDataDTO.builder().id(IdWorker.getIdStr()).name("社区数据").type("社区数据").dataLevelFlag("1").num(0).build();

        BasicDataDTO resource = BasicDataDTO.builder().id(IdWorker.getIdStr()).type("资源列表").name("全部").children(Arrays.asList(basicFeature, police, securityCheck, line, emergency, enterprise, business, residential)).num(basicFeature.getNum() + police.getNum() + securityCheck.getNum() + line.getNum() + emergency.getNum()).build();

        return List.of(resource);
    }

    private BasicDataDTO getLineData(Map<String, List<SceneInfo>> siteMap, Map<String, List<SceneInfo>> residenceMap, List<SceneInfo> cityRoadScenes, Map<String, List<SceneInfo>> railwayMap, Map<String, List<SceneInfo>> highwayMap, String sceneFlag) {
        List<BasicDataDTO> policeList = new ArrayList<>();
        List<BasicDataDTO> xcPoliceList = new ArrayList<>();
        AtomicInteger xcPoliceNum = new AtomicInteger();
        List<BasicDataDTO> zdPoliceList = new ArrayList<>();
        AtomicInteger zdPoliceNum = new AtomicInteger();
        List<BasicDataDTO> dlPoliceList = new ArrayList<>();
        AtomicInteger dlPoliceNum = new AtomicInteger();
        List<BasicDataDTO> gtPoliceList = new ArrayList<>();
        AtomicInteger gtPoliceNum = new AtomicInteger();
        List<BasicDataDTO> gsPoliceList = new ArrayList<>();
        AtomicInteger gsPoliceNum = new AtomicInteger();
        //现场
        siteMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getLineResourceTree("现场", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag);
            xcPoliceList.add(basicDataDTO);
            xcPoliceNum.set(xcPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(xcPoliceList)) {
            return xcPoliceList.get(0);
        }
        //住地
        residenceMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getLineResourceTree("住地", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag);
            zdPoliceList.add(basicDataDTO);
            zdPoliceNum.set(zdPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(zdPoliceList)) {
            return zdPoliceList.get(0);
        }
        //道路
        cityRoadScenes.forEach(sceneInfo -> {
            BasicDataDTO basicDataDTO = getLineResourceTree("道路", List.of(sceneInfo.getId()), sceneInfo.getSceneName(), sceneFlag);
            dlPoliceList.add(basicDataDTO);
            dlPoliceNum.set(dlPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(dlPoliceList)) {
            return dlPoliceList.get(0);
        }
        //高铁
        railwayMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getLineResourceTree("高铁", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag);
            gtPoliceList.add(basicDataDTO);
            gtPoliceNum.set(gtPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(gtPoliceList)) {
            return gtPoliceList.get(0);
        }
        //高速
        highwayMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getLineResourceTree("高速", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag);
            gsPoliceList.add(basicDataDTO);
            gsPoliceNum.set(gsPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(gsPoliceList)) {
            return gsPoliceList.get(0);
        }
        if (!CollectionUtils.isEmpty(xcPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("现场").type("现场线路").children(xcPoliceList).num(xcPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(zdPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("住地").type("住地线路").children(zdPoliceList).num(zdPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(dlPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("道路").type("道路线路").children(dlPoliceList).num(dlPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(gtPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("高铁").type("高铁线路").children(gtPoliceList).num(gtPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(gsPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("高速").type("高速线路").children(gsPoliceList).num(gsPoliceNum.get()).build());
        }
        return BasicDataDTO.builder().id(IdWorker.getIdStr()).name("任务路线").type("任务路线").children(policeList).num(xcPoliceNum.get()
                + zdPoliceNum.get() + dlPoliceNum.get() + gtPoliceNum.get() + gsPoliceNum.get()).build();
    }

    private BasicDataDTO getSecurityCheckData(Map<String, List<SceneInfo>> siteMap, Map<String, List<SceneInfo>> residenceMap, List<SceneInfo> cityRoadScenes, Map<String, List<SceneInfo>> railwayMap, Map<String, List<SceneInfo>> highwayMap, String sceneFlag) {
        //防爆安检
        List<BasicDataDTO> policeList = new ArrayList<>();
        List<BasicDataDTO> xcPoliceList = new ArrayList<>();
        AtomicInteger xcPoliceNum = new AtomicInteger();
        List<BasicDataDTO> zdPoliceList = new ArrayList<>();
        AtomicInteger zdPoliceNum = new AtomicInteger();
        List<BasicDataDTO> dlPoliceList = new ArrayList<>();
        AtomicInteger dlPoliceNum = new AtomicInteger();
        List<BasicDataDTO> gtPoliceList = new ArrayList<>();
        AtomicInteger gtPoliceNum = new AtomicInteger();
        List<BasicDataDTO> gsPoliceList = new ArrayList<>();
        AtomicInteger gsPoliceNum = new AtomicInteger();
        //现场
        siteMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getSecurityCheckResourceTree("现场", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag);
            xcPoliceList.add(basicDataDTO);
            xcPoliceNum.set(xcPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(xcPoliceList)) {
            return xcPoliceList.get(0);
        }
        //住地
        residenceMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getSecurityCheckResourceTree("住地", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag);
            zdPoliceList.add(basicDataDTO);
            zdPoliceNum.set(zdPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(zdPoliceList)) {
            return zdPoliceList.get(0);
        }
        //道路
        cityRoadScenes.forEach(sceneInfo -> {
            BasicDataDTO basicDataDTO = getSecurityCheckResourceTree("道路", List.of(sceneInfo.getId()), sceneInfo.getSceneName(), sceneFlag);
            dlPoliceList.add(basicDataDTO);
            dlPoliceNum.set(dlPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(dlPoliceList)) {
            return dlPoliceList.get(0);
        }
        //高铁
        railwayMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getSecurityCheckResourceTree("高铁", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag);
            gtPoliceList.add(basicDataDTO);
            gtPoliceNum.set(gtPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(gtPoliceList)) {
            return gtPoliceList.get(0);
        }
        //高速
        highwayMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getSecurityCheckResourceTree("高速", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag);
            gsPoliceList.add(basicDataDTO);
            gsPoliceNum.set(gsPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(gsPoliceList)) {
            return gsPoliceList.get(0);
        }
        if (!CollectionUtils.isEmpty(xcPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("现场").type("现场防爆安检").children(xcPoliceList).num(xcPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(zdPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("住地").type("住地防爆安检").children(zdPoliceList).num(zdPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(dlPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("道路").type("道路防爆安检").children(dlPoliceList).num(dlPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(gtPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("高铁").type("高铁防爆安检").children(gtPoliceList).num(gtPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(gsPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("高速").type("高速防爆安检").children(gsPoliceList).num(gsPoliceNum.get()).build());
        }
        return BasicDataDTO.builder().id(IdWorker.getIdStr()).name("防爆安检").type("防爆安检").children(policeList).num(xcPoliceNum.get()
                + zdPoliceNum.get() + dlPoliceNum.get() + gtPoliceNum.get() + gsPoliceNum.get()).build();
    }

    private BasicDataDTO getPoliceData(Map<String, List<SceneInfo>> siteMap, Map<String, List<SceneInfo>> residenceMap, List<SceneInfo> cityRoadScenes, Map<String, List<SceneInfo>> railwayMap, Map<String, List<SceneInfo>> highwayMap, String sceneFlag, String taskLevel) {
        //警力部署
        List<BasicDataDTO> policeList = new ArrayList<>();
        List<BasicDataDTO> xcPoliceList = new ArrayList<>();
        AtomicInteger xcPoliceNum = new AtomicInteger();
        List<BasicDataDTO> zdPoliceList = new ArrayList<>();
        AtomicInteger zdPoliceNum = new AtomicInteger();
        List<BasicDataDTO> dlPoliceList = new ArrayList<>();
        AtomicInteger dlPoliceNum = new AtomicInteger();
        List<BasicDataDTO> gtPoliceList = new ArrayList<>();
        AtomicInteger gtPoliceNum = new AtomicInteger();
        List<BasicDataDTO> gsPoliceList = new ArrayList<>();
        AtomicInteger gsPoliceNum = new AtomicInteger();
        //现场
        siteMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getPoliceResourceTree("现场", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag, taskLevel);
            xcPoliceList.add(basicDataDTO);
            xcPoliceNum.set(xcPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(xcPoliceList)) {
            return xcPoliceList.get(0);
        }
        //住地
        residenceMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getPoliceResourceTree("住地", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag, taskLevel);
            zdPoliceList.add(basicDataDTO);
            zdPoliceNum.set(zdPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(zdPoliceList)) {
            return zdPoliceList.get(0);
        }
        //道路
        cityRoadScenes.forEach(sceneInfo -> {
            BasicDataDTO basicDataDTO = getPoliceResourceTree("道路", List.of(sceneInfo.getId()), sceneInfo.getSceneName(), sceneFlag, taskLevel);
            dlPoliceList.add(basicDataDTO);
            dlPoliceNum.set(dlPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(dlPoliceList)) {
            return dlPoliceList.get(0);
        }
        //高铁
        railwayMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getPoliceResourceTree("高铁", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag, taskLevel);
            gtPoliceList.add(basicDataDTO);
            gtPoliceNum.set(gtPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(gtPoliceList)) {
            return gtPoliceList.get(0);
        }
        //高速
        highwayMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getPoliceResourceTree("高速", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag, taskLevel);
            gsPoliceList.add(basicDataDTO);
            gsPoliceNum.set(gsPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(gsPoliceList)) {
            return gsPoliceList.get(0);
        }
        if (!CollectionUtils.isEmpty(xcPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("现场").type("现场警力").children(xcPoliceList).num(xcPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(zdPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("住地").type("住地警力").children(zdPoliceList).num(zdPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(dlPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("道路").type("道路警力").children(dlPoliceList).num(dlPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(gtPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("高铁").type("高铁警力").children(gtPoliceList).num(gtPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(gsPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("高速").type("高速警力").children(gsPoliceList).num(gsPoliceNum.get()).build());
        }
        return BasicDataDTO.builder().id(IdWorker.getIdStr()).name("警力部署").type("警力部署").children(policeList).num(xcPoliceNum.get()
                + zdPoliceNum.get() + dlPoliceNum.get() + gtPoliceNum.get() + gsPoliceNum.get()).build();
    }

    private BasicDataDTO getBasicFeatureData(Map<String, List<SceneInfo>> siteMap, Map<String, List<SceneInfo>> residenceMap, List<SceneInfo> cityRoadScenes, Map<String, List<SceneInfo>> railwayMap, Map<String, List<SceneInfo>> highwayMap, String sceneFlag, String keyFlag) {
        //基础要素
        List<BasicDataDTO> basicFeatureList = new ArrayList<>();
        List<BasicDataDTO> xcFeatureList = new ArrayList<>();
        AtomicInteger xcFeatureNum = new AtomicInteger();
        List<BasicDataDTO> zdFeatureList = new ArrayList<>();
        AtomicInteger zdFeatureNum = new AtomicInteger();
        List<BasicDataDTO> dlFeatureList = new ArrayList<>();
        AtomicInteger dlFeatureNum = new AtomicInteger();
        List<BasicDataDTO> gtFeatureList = new ArrayList<>();
        AtomicInteger gtFeatureNum = new AtomicInteger();
        List<BasicDataDTO> gsFeatureList = new ArrayList<>();
        AtomicInteger gsFeatureNum = new AtomicInteger();
        //现场
        siteMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getSiteResourceTree(value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), key, sceneFlag, keyFlag);
            xcFeatureList.add(basicDataDTO);
            xcFeatureNum.set(xcFeatureNum.get() + basicDataDTO.getNum());
        });

        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(xcFeatureList)) {
            return xcFeatureList.get(0);
        }

        //住地
        residenceMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getResidenceResourceTree(value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), key, sceneFlag);
            zdFeatureList.add(basicDataDTO);
            zdFeatureNum.set(zdFeatureNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(zdFeatureList)) {
            return zdFeatureList.get(0);
        }
        //道路
        cityRoadScenes.forEach(sceneInfo -> {
            BasicDataDTO basicDataDTO = getCityRoadResourceTree(sceneInfo.getId(), sceneInfo.getSceneName(), sceneFlag);
            dlFeatureList.add(basicDataDTO);
            dlFeatureNum.set(dlFeatureNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(dlFeatureList)) {
            return dlFeatureList.get(0);
        }
        //高铁
        railwayMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getRailwayResourceTree(value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), key, sceneFlag);
            gtFeatureList.add(basicDataDTO);
            gtFeatureNum.set(gtFeatureNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(gtFeatureList)) {
            return gtFeatureList.get(0);
        }
        //高速
        highwayMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getHighwayResourceTree(value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), key, sceneFlag);
            gsFeatureList.add(basicDataDTO);
            gsFeatureNum.set(gsFeatureNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(gsFeatureList)) {
            return gsFeatureList.get(0);
        }
        if (!CollectionUtils.isEmpty(xcFeatureList)) {
            basicFeatureList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("现场").type("现场要素").children(xcFeatureList).num(xcFeatureNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(zdFeatureList)) {
            basicFeatureList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("住地").type("住地要素").children(zdFeatureList).num(zdFeatureNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(dlFeatureList)) {
            basicFeatureList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("道路").type("道路要素").children(dlFeatureList).num(dlFeatureNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(gtFeatureList)) {
            basicFeatureList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("高铁").type("高铁要素").children(gtFeatureList).num(gtFeatureNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(gsFeatureList)) {
            basicFeatureList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("高速").type("高速要素").children(gsFeatureList).num(gsFeatureNum.get()).build());
        }
        BasicDataDTO basicFeature = BasicDataDTO.builder().id(IdWorker.getIdStr()).name("基础要素").type("基础要素").children(basicFeatureList).num(xcFeatureNum.get()
                + zdFeatureNum.get() + dlFeatureNum.get() + gtFeatureNum.get() + gsFeatureNum.get()).build();
        return basicFeature;
    }


    private BasicDataDTO getEmergencyData(Map<String, List<SceneInfo>> siteMap, Map<String, List<SceneInfo>> residenceMap, List<SceneInfo> cityRoadScenes, Map<String, List<SceneInfo>> railwayMap, Map<String, List<SceneInfo>> highwayMap, String sceneFlag) {
        //应急方案
        List<BasicDataDTO> policeList = new ArrayList<>();
        List<BasicDataDTO> xcPoliceList = new ArrayList<>();
        AtomicInteger xcPoliceNum = new AtomicInteger();
        List<BasicDataDTO> zdPoliceList = new ArrayList<>();
        AtomicInteger zdPoliceNum = new AtomicInteger();
        List<BasicDataDTO> dlPoliceList = new ArrayList<>();
        AtomicInteger dlPoliceNum = new AtomicInteger();
        List<BasicDataDTO> gtPoliceList = new ArrayList<>();
        AtomicInteger gtPoliceNum = new AtomicInteger();
        List<BasicDataDTO> gsPoliceList = new ArrayList<>();
        AtomicInteger gsPoliceNum = new AtomicInteger();
        //现场
        siteMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getEmergencyResourceTree("现场", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag);
            xcPoliceList.add(basicDataDTO);
            xcPoliceNum.set(xcPoliceNum.get() + basicDataDTO.getNum());
        });
        //住地
        residenceMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getEmergencyResourceTree("住地", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag);
            zdPoliceList.add(basicDataDTO);
            zdPoliceNum.set(zdPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(zdPoliceList)) {
            return zdPoliceList.get(0);
        }
        //道路
        cityRoadScenes.forEach(sceneInfo -> {
            BasicDataDTO basicDataDTO = getEmergencyResourceTree("道路", List.of(sceneInfo.getId()), sceneInfo.getSceneName(), sceneFlag);
            dlPoliceList.add(basicDataDTO);
            dlPoliceNum.set(dlPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(dlPoliceList)) {
            return dlPoliceList.get(0);
        }
        //高铁
        railwayMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getEmergencyResourceTree("高铁", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag);
            gtPoliceList.add(basicDataDTO);
            gtPoliceNum.set(gtPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(gtPoliceList)) {
            return gtPoliceList.get(0);
        }
        //高速
        highwayMap.forEach((key, value) -> {
            BasicDataDTO basicDataDTO = getEmergencyResourceTree("高速", value.stream().map(SceneInfo::getId).collect(Collectors.toList()), value.get(0).getSceneName(), sceneFlag);
            gsPoliceList.add(basicDataDTO);
            gsPoliceNum.set(gsPoliceNum.get() + basicDataDTO.getNum());
        });
        if (!StringUtils.isEmpty(sceneFlag) && !CollectionUtils.isEmpty(gsPoliceList)) {
            return gsPoliceList.get(0);
        }
        if (!CollectionUtils.isEmpty(xcPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("现场").type("现场应急").children(xcPoliceList).num(xcPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(zdPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("住地").type("住地应急").children(zdPoliceList).num(zdPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(dlPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("道路").type("道路应急").children(dlPoliceList).num(dlPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(gtPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("高铁").type("高铁应急").children(gtPoliceList).num(gtPoliceNum.get()).build());
        }
        if (!CollectionUtils.isEmpty(gsPoliceList)) {
            policeList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("高速").type("高速应急").children(gsPoliceList).num(gsPoliceNum.get()).build());
        }
        BasicDataDTO police = BasicDataDTO.builder().id(IdWorker.getIdStr()).name("应急方案").type("应急方案").children(policeList).num(xcPoliceNum.get()
                + zdPoliceNum.get() + dlPoliceNum.get() + gtPoliceNum.get() + gsPoliceNum.get()).build();
        return police;
    }

    /**
     * 应急点位资源
     *
     * @param type
     * @param sceneIds
     * @param sceneName
     * @param sceneFlag
     * @return
     */
    public BasicDataDTO getEmergencyResourceTree(String type, List<String> sceneIds, String sceneName, String sceneFlag) {
        DrawData drawData = new DrawData();
        drawData.setSceneIdList(sceneIds);
        List<DrawData> drawDataList = drawDataService.getDrawDataList(drawData);
        List<BasicDataDTO> resourceList = new ArrayList<>();
        int resourceNum = 0;
        for (String node : emergencyNodes) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            List<DrawData> dataList = drawDataList.stream().filter(data -> node.equals(data.getPoliceDataType())
                    || node.equals(data.getFeatureType())
                    || (node.equals("应急路线") && ("4".equals(data.getLineType()) || "7".equals(data.getLineType())))).collect(Collectors.toList());
            for (DrawData data : dataList) {
                BasicDataDTO basicDataDTO = BasicDataDTO.builder().middlePoint(data.getMiddlePoint()).dataLevelFlag("1").data(data.getData()).groupId(data.getGroupId()).id(data.getId()).name(data.getName()).type(node).floorNum(data.getFloorNum()).buildName(data.getBuildName()).children(new ArrayList<>()).coordinates(data.getGeojson().getObject("coordinates", List.class)).build();
                basicDataDTOList.add(basicDataDTO);
            }
            BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(IdWorker.getIdStr()).name(node).type(node).children(basicDataDTOList).num(basicDataDTOList.size()).build();
            resourceNum += basicDataDTOList.size();
            resourceList.add(basicDataDTO);
        }
        if (StringUtils.isEmpty(sceneFlag)) {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).type(type + "应急方案").name(sceneName).num(resourceNum).children(resourceList).build();
        } else {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).type("应急方案").name("应急方案").num(resourceNum).children(resourceList).build();
        }

    }

    /**
     * 任务路线资源
     *
     * @param type
     * @param sceneIds
     * @param sceneName
     * @return
     */
    public BasicDataDTO getLineResourceTree(String type, List<String> sceneIds, String sceneName, String sceneFlag) {
        DrawData drawData = new DrawData();
        drawData.setSceneIdList(sceneIds);
        drawData.setType("lines");
        drawData.setLineType("0");
        List<DrawData> drawDataList = drawDataService.getLineList(drawData);
        List<BasicDataDTO> resourceList = new ArrayList<>();
        for (DrawData data : drawDataList) {
            BasicDataDTO basicDataDTO = BasicDataDTO.builder().middlePoint(data.getMiddlePoint()).dataLevelFlag("1").data(data.getData()).groupId(data.getGroupId()).id(data.getId()).name(data.getName()).type("场景路线").floorNum(data.getFloorNum()).buildName(data.getBuildName()).children(new ArrayList<>()).coordinates(data.getGeojson().getObject("coordinates", List.class)).build();
            resourceList.add(basicDataDTO);
        }
        if (StringUtils.isEmpty(sceneFlag)) {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).type(type).name(sceneName).num(resourceList.size()).children(resourceList).build();
        } else {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).type("任务路线").name("任务路线").num(resourceList.size()).children(resourceList).build();
        }

    }

    public BasicDataDTO getSecurityCheckResourceTree(String type, List<String> sceneIds, String basicDataName, String sceneFlag) {
        //安检
        DrawData drawData = new DrawData();
        drawData.setSceneIdList(sceneIds);
        drawData.setTypeList(List.of("fbaj"));
        List<DrawData> drawDataList = drawDataService.getDrawDataList(drawData);
        List<BasicDataDTO> securityCheckList = new ArrayList<>();
        int policeNum = 0;
        for (String node : securityCheckNodes) {
            int nodeNum = 0;
            if (CollectionUtils.isEmpty(drawDataList)) {
                BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(IdWorker.getIdStr()).name(node).type(node).children(new ArrayList<>()).build();
                securityCheckList.add(basicDataDTO);
            } else {
                List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
                List<DrawData> dataDTOList = drawDataList.stream().filter(data -> node.equals(data.getPoliceDataType())).collect(Collectors.toList());
                for (DrawData data : dataDTOList) {
                    BasicDataDTO basicDataDTO = BasicDataDTO.builder().dataLevelFlag("1").data(data.getData()).groupId(data.getGroupId()).id(data.getId()).name(data.getName()).type(node).floorNum(data.getFloorNum()).buildName(data.getBuildName()).children(new ArrayList<>()).coordinates(data.getGeojson().getObject("coordinates", List.class)).build();
                    basicDataDTOList.add(basicDataDTO);
                    nodeNum = data.getNum();
                    policeNum += data.getNum();
                }
                BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(IdWorker.getIdStr()).name(node).type(node).children(basicDataDTOList).num(nodeNum).build();
                securityCheckList.add(basicDataDTO);
            }
        }
        if (StringUtils.isEmpty(sceneFlag)) {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).name(basicDataName).type(type + "防爆安检").children(securityCheckList).num(policeNum).build();
        } else {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).name("防爆安检").type("防爆安检").children(securityCheckList).num(policeNum).build();
        }
    }

    /**
     * 警力资源
     *
     * @param type
     * @param sceneIds
     * @param basicDataName
     * @return
     */
    public BasicDataDTO getPoliceResourceTree(String type, List<String> sceneIds, String basicDataName, String sceneFlag, String taskLevel) {
        //警力
        DrawData drawData = new DrawData();
        drawData.setSceneIdList(sceneIds);
        drawData.setTypeList(List.of("police", "car", "cordon", "uav"));
        List<DrawData> drawDataList = drawDataService.getDrawDataList(drawData);

        List<Map<String, Object>> drawFangxian = new ArrayList<>();
        if (org.springframework.util.StringUtils.hasText(sceneFlag)) {
            //查询防线数据
            drawFangxian = drawDataService.getLineOfScenePlanNode(sceneIds.get(0), "警力部署");
        }
        List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
        int policeNum = 0;
        List<BasicDataDTO> policeList = new ArrayList<>();
        if (CollectionUtils.isEmpty(drawFangxian)) {
            for (String node : policeNodes) {
                int nodeNum = 0;
                //快反力量
                String name = !"一级加强".equals(taskLevel) && "快反力量".equals(node) ? "机动力量" : node;
                if (CollectionUtils.isEmpty(drawDataList)) {
                    BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(IdWorker.getIdStr()).name(name).type(node).children(new ArrayList<>()).build();
                    policeList.add(basicDataDTO);
                } else {
                    List<BasicDataDTO> policeDataList = new ArrayList<>();
                    List<DrawData> dataDTOList = drawDataList.stream().filter(data -> "快反力量".equals(node) ? (node.equals(data.getPoliceDataType()) || "机动力量".equals(data.getPoliceDataType())) : node.equals(data.getPoliceDataType())).collect(Collectors.toList());
                    for (DrawData data : dataDTOList) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().dataLevelFlag("1").data(data.getData()).groupId(data.getGroupId()).id(data.getId()).name(data.getName()).type(node).floorNum(data.getFloorNum()).num(data.getNum()).buildName(data.getBuildName()).children(new ArrayList<>()).coordinates(data.getGeojson().getObject("coordinates", List.class)).build();
                        policeDataList.add(basicDataDTO);
                        nodeNum += data.getNum();
                        policeNum += data.getNum();
                    }
                    BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(IdWorker.getIdStr()).name(name).type(node).children(policeDataList).num(nodeNum).build();
                    policeList.add(basicDataDTO);
                }
            }
            BasicDataDTO police = BasicDataDTO.builder().id(IdWorker.getIdStr()).name("警力").type("警力").children(policeList).num(policeNum).build();
            //警车
            //List<BasicDataDTO> carList = new ArrayList<>();
            int carNum = 0;
            //for (String node : carNodes) {
            List<BasicDataDTO> carDataList = new ArrayList<>();
            List<DrawData> carDataDTOList = drawDataList.stream().filter(data -> "car".equals(data.getType())).collect(Collectors.toList());
            for (DrawData data : carDataDTOList) {
                BasicDataDTO basicDataDTO = BasicDataDTO.builder().dataLevelFlag("1").data(data.getData()).groupId(data.getGroupId()).id(data.getId()).name(data.getName()).type("警车").floorNum(data.getFloorNum()).buildName(data.getBuildName()).children(new ArrayList<>()).coordinates(data.getGeojson().getObject("coordinates", List.class)).build();
                carDataList.add(basicDataDTO);
                carNum += data.getNum();
                policeNum += data.getNum();
            }
//                BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(IdWorker.getIdStr()).name(node).type(node).children(carDataList).num(carDataList.size()).build();
//                carList.add(basicDataDTO);
            //}
            BasicDataDTO car = BasicDataDTO.builder().id(IdWorker.getIdStr()).name("警车").type("警车").children(carDataList).num(carNum).build();

            //警戒线
            int cordonNum = 0;
            List<BasicDataDTO> cordonList = new ArrayList<>();
            List<DrawData> dataDTOList = drawDataList.stream().filter(data -> "cordon".equals(data.getType())).collect(Collectors.toList());
            for (DrawData data : dataDTOList) {
                BasicDataDTO basicDataDTO = BasicDataDTO.builder().dataLevelFlag("1").data(data.getData()).groupId(data.getGroupId()).id(data.getId()).name(data.getName()).type("警戒线").floorNum(data.getFloorNum()).buildName(data.getBuildName()).children(new ArrayList<>()).coordinates(data.getGeojson().getObject("coordinates", List.class)).build();
                cordonList.add(basicDataDTO);
                cordonNum++;
                policeNum++;
            }
            BasicDataDTO cordon = BasicDataDTO.builder().id(IdWorker.getIdStr()).name("警戒线").type("警戒线").children(cordonList).num(cordonNum).build();
            basicDataDTOList.add(police);
            basicDataDTOList.add(car);
            basicDataDTOList.add(cordon);
        } else {
            //有防线
            for (int i = 0; i < drawFangxian.size(); i++) {
                List<BasicDataDTO> linePoliceList = new ArrayList<>();
                int linePoliceNum = 0;
                //按防线划分
                Object fangxian = drawFangxian.get(i).get("fangxian");

                List<BasicDataDTO> personList = new ArrayList<>();
                int personNum = 0;

                for (String node : policeNodes) {
                    int nodeNum = 0;
                    //快反力量
                    String name = !"一级加强".equals(taskLevel) && "快反力量".equals(node) ? "机动力量" : node;
                    if (CollectionUtils.isEmpty(drawDataList)) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(IdWorker.getIdStr()).name(name).type(node).children(new ArrayList<>()).build();
                        personList.add(basicDataDTO);
                    } else {
                        List<BasicDataDTO> policeDataList = new ArrayList<>();
                        List<DrawData> dataDTOList = drawDataList.stream().filter(data -> fangxian.toString().equals(data.getFangxian()) && ("快反力量".equals(node) ? (node.equals(data.getPoliceDataType()) || "机动力量".equals(data.getPoliceDataType())) : node.equals(data.getPoliceDataType()))).collect(Collectors.toList());
                        for (DrawData data : dataDTOList) {
                            BasicDataDTO basicDataDTO = BasicDataDTO.builder().dataLevelFlag("1").data(data.getData()).groupId(data.getGroupId()).id(data.getId()).name(data.getName()).type(node).floorNum(data.getFloorNum()).num(data.getNum()).buildName(data.getBuildName()).children(new ArrayList<>()).coordinates(data.getGeojson().getObject("coordinates", List.class)).build();
                            policeDataList.add(basicDataDTO);
                            nodeNum += data.getNum();
                            personNum += data.getNum();
                            policeNum += data.getNum();
                            linePoliceNum += data.getNum();
                        }
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(IdWorker.getIdStr()).name(name).type(node).children(policeDataList).num(nodeNum).build();
                        personList.add(basicDataDTO);
                    }
                }
                BasicDataDTO person = BasicDataDTO.builder().id(IdWorker.getIdStr()).name("警力").type("警力").children(personList).num(personNum).build();
                //警车
                //List<BasicDataDTO> carList = new ArrayList<>();
                int carNum = 0;
                //for (String node : carNodes) {
                int nodeNum = 0;
                List<BasicDataDTO> carDataList = new ArrayList<>();
                List<DrawData> carDataDTOList = drawDataList.stream().filter(data -> fangxian.toString().equals(data.getFangxian()) && "car".equals(data.getType())).collect(Collectors.toList());
                for (DrawData data : carDataDTOList) {
                    BasicDataDTO basicDataDTO = BasicDataDTO.builder().dataLevelFlag("1").data(data.getData()).groupId(data.getGroupId()).id(data.getId()).name(data.getName()).type("警车").floorNum(data.getFloorNum()).buildName(data.getBuildName()).children(new ArrayList<>()).coordinates(data.getGeojson().getObject("coordinates", List.class)).build();
                    carDataList.add(basicDataDTO);
                    nodeNum += data.getNum();
                    carNum += data.getNum();
                    linePoliceNum += data.getNum();
                    policeNum += data.getNum();
                }
//                    BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(IdWorker.getIdStr()).name(node).type(node).children(carDataList).num(nodeNum).build();
//                    carList.add(basicDataDTO);
                //}
                BasicDataDTO car = BasicDataDTO.builder().id(IdWorker.getIdStr()).name("警车").type("警车").children(carDataList).num(carNum).build();

                //警戒线
                List<BasicDataDTO> cordonList = new ArrayList<>();
                List<DrawData> dataDTOList = drawDataList.stream().filter(data -> fangxian.toString().equals(data.getFangxian()) && "cordon".equals(data.getType())).collect(Collectors.toList());
                for (DrawData data : dataDTOList) {
                    BasicDataDTO basicDataDTO = BasicDataDTO.builder().dataLevelFlag("1").data(data.getData()).groupId(data.getGroupId()).id(data.getId()).name(data.getName()).type("警戒线").floorNum(data.getFloorNum()).buildName(data.getBuildName()).children(new ArrayList<>()).coordinates(data.getGeojson().getObject("coordinates", List.class)).build();
                    cordonList.add(basicDataDTO);
                    linePoliceNum++;
                    policeNum++;
                }
                BasicDataDTO cordon = BasicDataDTO.builder().id(IdWorker.getIdStr()).name("警戒线").type("警戒线").children(cordonList).num(cordonList.size()).build();
                //防线数据
                if ("".equals(fangxian.toString())) {
                    basicDataDTOList.addAll(List.of(person, car, cordon));
                } else {
                    linePoliceList.addAll(List.of(person, car, cordon));
                    basicDataDTOList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name(fangxian.toString()).type("防线").children(linePoliceList).num(linePoliceNum).build());
                }
            }
        }
        if (StringUtils.isEmpty(sceneFlag)) {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).name(basicDataName).type(type + "场景警力").children(basicDataDTOList).num(policeNum).build();
        } else {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).name("警力部署").type("警力部署").children(basicDataDTOList).num(policeNum).build();
        }
    }


    /**
     * 高速资源列表
     *
     * @param sceneIds
     * @param basicDataName
     * @param basicDataId
     * @return
     */
    public BasicDataDTO getHighwayResourceTree(List<String> sceneIds, String basicDataName, String basicDataId, String sceneFlag) {
        DrawData drawData = new DrawData();
        drawData.setSceneIdList(sceneIds);
        drawData.setType("lines");
        drawData.setLineType("0");
        List<DrawData> drawDataList = drawDataService.getLineList(drawData);
        List<PointExtInfo> pointExtInfoList = null;
        if (CollectionUtils.isEmpty(drawDataList)) {
            List<PointInfo> pointInfos = pointInfoService.list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getJcxxId, basicDataId));
            List<String> ids = pointInfos.stream().map(PointInfo::getId).collect(Collectors.toList());
            pointExtInfoList = pointExtInfoService.getPointExtInfoList(ids);
        } else {
            List<PointExtInfo> dataList = new ArrayList<>();
            for (DrawData data : drawDataList) {
                try {
                    dataList.addAll(spatialQuery.queryLineBufferDataOfWay(data.getGeom(), bufferRadius, basicDataId));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            pointExtInfoList = new ArrayList<>(new LinkedHashSet<>(dataList));
        }

        int gtNum = 0;
        List<BasicDataDTO> gtList = new ArrayList<>();
        for (String node : highwayNodes) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointExtInfoList)) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        //Map<String, Object> disData = drawDataService.getDistanceData(sceneIds.get(0),pointExtInfo.getGeom());
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).dataLevelFlag("1").data(pointExtInfo.getData()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
            }
            basicDataDTOList = getResourceData(sceneIds, "1", node, basicDataDTOList);
            gtList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name(node).type(node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            gtNum = gtNum + basicDataDTOList.size();
        }
        if (StringUtils.isEmpty(sceneFlag)) {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).type("高速要素").name(basicDataName).children(gtList).num(gtNum).build();
        } else {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).type("基础要素").name("基础要素").children(gtList).num(gtNum).build();
        }
    }

    /**
     * 高铁资源列表
     *
     * @param sceneIds
     * @param basicDataName
     * @param basicDataId
     * @return
     */
    public BasicDataDTO getRailwayResourceTree(List<String> sceneIds, String basicDataName, String basicDataId, String sceneFlag) {
        DrawData drawData = new DrawData();
        drawData.setSceneIdList(sceneIds);
        drawData.setType("lines");
        drawData.setLineType("0");
        List<DrawData> drawDataList = drawDataService.getLineList(drawData);
        List<PointExtInfo> pointExtInfoList = null;
        if (CollectionUtils.isEmpty(drawDataList)) {
            List<PointInfo> pointInfos = pointInfoService.list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getJcxxId, basicDataId));
            List<String> ids = pointInfos.stream().map(PointInfo::getId).collect(Collectors.toList());
            pointExtInfoList = pointExtInfoService.getPointExtInfoList(ids);
        } else {
            List<PointExtInfo> dataList = new ArrayList<>();
            for (DrawData data : drawDataList) {
                try {
                    dataList.addAll(spatialQuery.queryLineBufferDataOfWay(data.getGeom(), bufferRadius, basicDataId));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            pointExtInfoList = new ArrayList<>(new LinkedHashSet<>(dataList));
        }

        int gtNum = 0;
        List<BasicDataDTO> gtList = new ArrayList<>();
        for (String node : railwayNodes) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointExtInfoList)) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).dataLevelFlag("1").data(pointExtInfo.getData()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
            }
            basicDataDTOList = getResourceData(sceneIds, "1", node, basicDataDTOList);
            gtList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name(node).type(node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            gtNum = gtNum + basicDataDTOList.size();
        }
        if (StringUtils.isEmpty(sceneFlag)) {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).type("高铁要素").name(basicDataName).children(gtList).num(gtNum).build();
        } else {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).type("基础要素").name("基础要素").children(gtList).num(gtNum).build();
        }

    }

    /**
     * 城市道路资源
     *
     * @param sceneId
     * @param sceneName
     * @return
     */
    public BasicDataDTO getCityRoadResourceTree(String sceneId, String sceneName, String sceneFlag) {
        List<BasicDataDTO> resourceList = new ArrayList<>();
        DrawData drawData = new DrawData();
        drawData.setSceneId(sceneId);
        drawData.setType("basic");
        List<DrawData> drawDataList = drawDataService.getDrawDataList(drawData);

        //根据任务路线查询缓冲半径内的采集的基础数据
        List<PointExtInfo> pointExtInfoList = new ArrayList<>();
        List<DrawData> lines = getDrawLines(sceneId);

        for (DrawData data : lines) {
            try {
                pointExtInfoList.addAll(spatialQuery.queryIntersectsPointData(data.getGeom(), 50.0));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        int num = 0;
        for (String node : cityRoadNodes) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            List<DrawData> dataList = drawDataList.stream().filter(data -> node.equals(data.getFeatureType())).collect(Collectors.toList());
            for (DrawData data : dataList) {
                BasicDataDTO basicDataDTO = BasicDataDTO.builder().data(data.getData()).dataLevelFlag("1").groupId(data.getGroupId()).id(data.getId()).name(data.getName()).type(node).floorNum(data.getFloorNum()).buildName(data.getBuildName()).children(new ArrayList<>()).coordinates(data.getGeojson().getObject("coordinates", List.class)).build();
                basicDataDTOList.add(basicDataDTO);
                num++;
            }
            for (PointExtInfo data : pointExtInfoList) {
                //只取需要的类型
                if (node.equals(data.getType())) {
                    BasicDataDTO basicDataDTO = BasicDataDTO.builder().data(data.getData()).dataLevelFlag("1").id(data.getId()).name(data.getName()).type(node).children(new ArrayList<>()).coordinates(data.getGeojson().getObject("coordinates", List.class)).build();
                    basicDataDTOList.add(basicDataDTO);
                    num++;
                }
            }
            BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(IdWorker.getIdStr()).type(node).name(node).num(basicDataDTOList.size()).children(basicDataDTOList).build();
            resourceList.add(basicDataDTO);
        }
        if (StringUtils.isEmpty(sceneFlag)) {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).type("道路要素").name(sceneName).num(num).children(resourceList).build();
        } else {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).type("基础要素").name("基础要素").num(num).children(resourceList).build();
        }
    }

    /**
     * 住地资源
     *
     * @param sceneIds
     * @param basicDataName
     * @param basicDataId
     * @return
     */
    public BasicDataDTO getResidenceResourceTree(List<String> sceneIds, String basicDataName, String basicDataId, String sceneFlag) {
        List<PointInfo> pointInfos = pointInfoService.list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getJcxxId, basicDataId));

        List<BasicDataDTO> xcsnList = new ArrayList<>();
        List<BasicDataDTO> xczbList = new ArrayList<>();

        List<String> ids = pointInfos.stream().map(PointInfo::getId).collect(Collectors.toList());
        List<PointExtInfo> pointExtInfoList = pointExtInfoService.getPointExtInfoList(ids);
        int xcNum = 0;
        int xczbNum = 0;
        List<BasicDataDTO> jgList = new ArrayList<>();
        int jgNum = 0;
        //内设机构 siteKey1
        for (String node : residenceKey1) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointExtInfoList)) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).dataLevelFlag("1").data(pointExtInfo.getData()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
            }
            basicDataDTOList = getResourceData(sceneIds, "1", node, basicDataDTOList);
            jgList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("内设机构".equals(node) ? "其他" + node : node).type("内设机构".equals(node) ? "其他" + node : node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            jgNum = jgNum + basicDataDTOList.size();
            xcNum = xcNum + basicDataDTOList.size();
        }

        xcsnList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).type("内设机构").name("内设机构").children(jgList).num(jgNum).build());
        //主要建筑
        List<BasicDataDTO> jzList = new ArrayList<>();
        int jzNum = 0;
        for (String node : residenceKey2) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointExtInfoList)) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).dataLevelFlag("1").data(pointExtInfo.getData()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
            }
            basicDataDTOList = getDrawResourceData(sceneIds, "1", node, basicDataDTOList, null);
            jzList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("主要建筑".equals(node) ? "其他" + node : node).type("主要建筑".equals(node) ? "其他" + node : node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            jzNum = jzNum + basicDataDTOList.size();
            xcNum = xcNum + basicDataDTOList.size();
        }
        xcsnList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).type("主要建筑").name("主要建筑").children(jzList).num(jzNum).build());
        //重要部位
        List<BasicDataDTO> bwList = new ArrayList<>();
        int bwNum = 0;
        for (String node : residenceKey3) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointExtInfoList)) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).dataLevelFlag("1").data(pointExtInfo.getData()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
            }
            basicDataDTOList = getDrawResourceData(sceneIds, "1", node, basicDataDTOList, null);
            bwList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("重要部位".equals(node) ? "其他" + node : node).type("重要部位".equals(node) ? "其他" + node : node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            bwNum = bwNum + basicDataDTOList.size();
            xcNum = xcNum + basicDataDTOList.size();
        }
        xcsnList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).type("重要部位").name("重要部位").children(bwList).num(bwNum).build());

        //四邻情况
        for (String node : residenceAroundKey1) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointExtInfoList)) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).dataLevelFlag("1").data(pointExtInfo.getData()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
            }
            basicDataDTOList = getDrawResourceData(sceneIds, "0", node, basicDataDTOList, null);
            xczbList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).type("四邻情况").name("四邻情况").children(basicDataDTOList).num(basicDataDTOList.size()).build());
            xczbNum = xczbNum + basicDataDTOList.size();
        }

        //重要部位
        List<BasicDataDTO> zybwList = new ArrayList<>();
        int zybwNum = 0;
        for (String node : residenceAroundKey2) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointExtInfoList)) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).dataLevelFlag("1").data(pointExtInfo.getData()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
            }
            basicDataDTOList = getDrawResourceData(sceneIds, "0", node, basicDataDTOList, null);
            zybwList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("重要部位".equals(node) ? "其他" + node : node).type("重要部位".equals(node) ? "其他" + node : node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            zybwNum = zybwNum + basicDataDTOList.size();
            xczbNum = xczbNum + basicDataDTOList.size();
        }
        xczbList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).type("重要部位").name("重要部位").children(zybwList).num(zybwNum).build());

        List<BasicDataDTO> xcList = Arrays.asList(BasicDataDTO.builder().id(IdWorker.getIdStr()).type("住地室内").name("住地室内").children(xcsnList).num(xcNum).build(),
                BasicDataDTO.builder().id(IdWorker.getIdStr()).type("住地周边").name("住地周边").children(xczbList).num(xczbNum).build());
        if (StringUtils.isEmpty(sceneFlag)) {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).type("住地要素").name(basicDataName).children(xcList).num(xczbNum + xcNum).build();
        } else {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).type("基础要素").name("基础要素").children(xcList).num(xczbNum + xcNum).build();
        }
    }

    /**
     * 现场资源树
     *
     * @param sceneIds
     * @return
     */
    public BasicDataDTO getSiteResourceTree(List<String> sceneIds, String basicDataName, String basicDataId, String sceneFlag, String keyFlag) {
        List<PointExtInfo> pointExtInfoList = null;
        if (StringUtils.isEmpty(keyFlag)) {
            List<PointInfo> pointInfos = pointInfoService.list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getJcxxId, basicDataId));
            if (!CollectionUtils.isEmpty(pointInfos)) {
                List<String> ids = pointInfos.stream().map(PointInfo::getId).collect(Collectors.toList());
                pointExtInfoList = pointExtInfoService.getPointExtInfoList(ids);
            }
        }
        int xcNum = 0;
        int xczbNum = 0;
        List<BasicDataDTO> jgList = new ArrayList<>();
        int jgNum = 0;
        //内设机构 siteKey1
        for (String node : siteKey1) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointExtInfoList)) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).dataLevelFlag("1").data(pointExtInfo.getData()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
            }
            basicDataDTOList = getDrawResourceData(sceneIds, "1", node, basicDataDTOList, keyFlag);
            jgList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("内设机构".equals(node) ? "其他" + node : node).type("内设机构".equals(node) ? "其他" + node : node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            jgNum = jgNum + basicDataDTOList.size();
            xcNum = xcNum + basicDataDTOList.size();
        }

        List<BasicDataDTO> xcsnList = new ArrayList<>();
        List<BasicDataDTO> xczbList = new ArrayList<>();

        xcsnList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).type("内设机构").name("内设机构").children(jgList).num(jgNum).build());
        //主要建筑
        List<BasicDataDTO> jzList = new ArrayList<>();
        int jzNum = 0;
        for (String node : siteKey2) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointExtInfoList)) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).dataLevelFlag("1").data(pointExtInfo.getData()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
            }
            basicDataDTOList = getDrawResourceData(sceneIds, "1", node, basicDataDTOList, keyFlag);
            jzList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("主要建筑".equals(node) ? "其他" + node : node).type("主要建筑".equals(node) ? "其他" + node : node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            jzNum = jzNum + basicDataDTOList.size();
            xcNum = xcNum + basicDataDTOList.size();
        }
        xcsnList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).type("主要建筑").name("主要建筑").children(jzList).num(jzNum).build());
        //重要部位
        List<BasicDataDTO> bwList = new ArrayList<>();
        int bwNum = 0;
        for (String node : siteKey3) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointExtInfoList)) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).dataLevelFlag("1").data(pointExtInfo.getData()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
            }
            basicDataDTOList = getDrawResourceData(sceneIds, "1", node, basicDataDTOList, keyFlag);
            bwList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("重要部位".equals(node) ? "其他" + node : node).type("重要部位".equals(node) ? "其他" + node : node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            bwNum = bwNum + basicDataDTOList.size();
            xcNum = xcNum + basicDataDTOList.size();
        }
        xcsnList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).type("重要部位").name("重要部位").children(bwList).num(bwNum).build());

        //四邻情况
        for (String node : siteAroundKey1) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointExtInfoList)) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).dataLevelFlag("1").data(pointExtInfo.getData()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
            }
            basicDataDTOList = getDrawResourceData(sceneIds, "0", node, basicDataDTOList, keyFlag);
            xczbList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).type("四邻情况").name("四邻情况").children(basicDataDTOList).num(basicDataDTOList.size()).build());
            xczbNum = xczbNum + basicDataDTOList.size();
        }
        //重要部位
        List<BasicDataDTO> zybwList = new ArrayList<>();
        int zybwNum = 0;
        for (String node : siteAroundKey2) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointExtInfoList)) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).dataLevelFlag("1").data(pointExtInfo.getData()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
            }
            basicDataDTOList = getDrawResourceData(sceneIds, "0", node, basicDataDTOList, keyFlag);
            zybwList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).name("重要部位".equals(node) ? "其他" + node : node).type("重要部位".equals(node) ? "其他" + node : node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            zybwNum = zybwNum + basicDataDTOList.size();
            xczbNum = xczbNum + basicDataDTOList.size();
        }
        xczbList.add(BasicDataDTO.builder().id(IdWorker.getIdStr()).type("重要部位").name("重要部位").children(zybwList).num(zybwNum).build());
        List<BasicDataDTO> xcList = Arrays.asList(BasicDataDTO.builder().id(IdWorker.getIdStr()).type("现场室内").name("现场室内").children(xcsnList).num(xcNum).build(),
                BasicDataDTO.builder().id(IdWorker.getIdStr()).type("现场周边").name("现场周边").children(xczbList).num(xczbNum).build());
        if (StringUtils.isEmpty(sceneFlag)) {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).type("现场").name(basicDataName).children(xcList).num(xczbNum + xcNum).build();
        } else {
            return BasicDataDTO.builder().id(IdWorker.getIdStr()).type("基础要素").name("基础要素").children(xcList).num(xczbNum + xcNum).build();
        }
    }

    @Override
    public Map getSchemeStatistics() {
        return baseMapper.getSchemeStatistics();
    }

    @Override
    public List<JSONObject> getSceneTree(String type, String name) {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        LambdaQueryWrapper<SceneInfo> lambdaQueryWrapper = new LambdaQueryWrapper<SceneInfo>()
                .eq(SceneInfo::getDeleteFlag, 0)
                .isNull(SceneInfo::getSchemeFlag)
                .eq(SceneInfo::getType, type);
        if (StringUtils.isEmpty(name)) {
            lambdaQueryWrapper.eq(SceneInfo::getBasicDataId, "");
        } else {
            lambdaQueryWrapper.eq(SceneInfo::getSceneName, name);
        }
        List<SceneInfo> sceneInfoList = list(lambdaQueryWrapper);
        Map<String, List<SceneInfo>> sceneMap = sceneInfoList.stream().collect(Collectors.groupingBy(SceneInfo::getTaskId));
        sceneMap.forEach((key, value) -> {
            JSONObject jsonObject = new JSONObject();
            Task task = taskMapper.selectById(key);
            if (task != null) {
                jsonObject.put("id", task.getId());
                jsonObject.put("name", task.getTaskName());
                List<JSONObject> children = new ArrayList<>();
                value.stream().forEach(sceneInfo -> {
                    JSONObject scene = new JSONObject();
                    scene.put("id", sceneInfo.getId());
                    scene.put("name", sceneInfo.getSceneDesc());
                    scene.put("children", new ArrayList<>());
                    children.add(scene);
                });
                jsonObject.put("children", children);
                jsonObjectList.add(jsonObject);
            }
        });
        return jsonObjectList;
    }

    @Override
    public List<DrawData> deployPolice(DeployPoliceData deployPoliceData) {
        String taskId = deployPoliceData.getTaskId();
        String sceneId = deployPoliceData.getSceneId();
        String basicDataId = deployPoliceData.getBasicDataId();
        List<DrawData> policeData = new ArrayList<>();
        for (JSONObject jsonObject : deployPoliceData.getPoliceData()) {
            String featureTypeId = jsonObject.getString("featureTypeId");
            List<FeaturePositionData> positions = pointInfoService.getFeaturePosition(basicDataId, featureTypeId);
            for (FeaturePositionData data : positions) {
                DrawData drawData = jsonObject.getObject("drawData", DrawData.class);
                if (drawData == null) {
                    continue;
                }
                drawData.setTaskId(taskId);
                drawData.setSceneId(sceneId);
                policeData.add(drawDataService.updatePoliceDrawData(drawData, data.getGeojson().getObject("coordinates", List.class), data.getWeizhi(), data.getType()));
            }
        }
        drawDataService.saveOrUpdateBatch(policeData);
        return policeData;
    }


    @Override
    public List<BasicDataDTO> getSceneResourceTree(List<String> sceneIds) {
        if (CollectionUtils.isEmpty(sceneIds)) {
            return null;
        }
        //根据场景类型查询数据
        SceneInfo sceneInfo = geSceneById(sceneIds.get(0));
        if (sceneInfo == null) {
            return null;
        }
        //道路，自定义、高速、高铁三种情况下的隐患处理
        if ("1".equals(sceneInfo.getType())) {
            AtomicInteger gsNum = new AtomicInteger();
            AtomicInteger gtNum = new AtomicInteger();
            if (sceneInfo.getBasicDataId() != null && !"".equals(sceneInfo.getBasicDataId())) {
                List<BasicDataDTO> all = new ArrayList<>();
                List<Map<String, List<BasicDataDTO>>> basicDataList = pointInfoService.getResourceOfHighWayAndRailWay(List.of(sceneInfo.getBasicDataId()));
                //高速
                if (!basicDataList.get(0).isEmpty()) {
                    List<BasicDataDTO> gs = new ArrayList<>();
                    final int[] gsIndex = {0};
                    basicDataList.get(0).forEach((key, value) -> {
                        gsIndex[0]++;
                        gsNum.set(gsNum.get() + value.size());
                        gs.add(BasicDataDTO.builder().id("5" + gsIndex[0]).name(key).type(key).children(value).num(value.size()).build());
                    });
                    all.add(BasicDataDTO.builder().id("5").name("高速").children(gs).num(gsNum.get()).build());
                }
                //高铁
                if (!basicDataList.get(1).isEmpty()) {
                    List<BasicDataDTO> gt = new ArrayList<>();
                    final int[] gtIndex = {0};
                    basicDataList.get(1).forEach((key, value) -> {
                        gtIndex[0]++;
                        gtNum.set(gtNum.get() + value.size());
                        gt.add(BasicDataDTO.builder().id("6" + gtIndex[0]).name(key).type(key).children(value).num(value.size()).build());
                    });
                    all.add(BasicDataDTO.builder().id("6").name("高铁").children(gt).num(gtNum.get()).build());
                }
                List<BasicDataDTO> allList = new ArrayList<>();
                int total = gsNum.get() + gtNum.get();
                allList.add(BasicDataDTO.builder().id("0").name("全部").children(all).num(total).build());
                return allList;
            } else {
                List<PointExtInfo> dataList = new ArrayList<>();
                //查询任务下的线路，然后查询线路周边的基础数据
                List<DrawData> drawDataList = getDrawLines(sceneIds);
                for (DrawData drawData : drawDataList) {
                    try {
                        dataList.addAll(spatialQuery.queryLineBufferData(drawData.getGeom(), bufferRadius));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                //取线路下的数据
//            if (sceneInfo.getBasicDataId() != null) {
//                List<String> ids = pointInfoService.list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getId, sceneInfo.getBasicDataId()).or().eq(PointInfo::getJcxxId, sceneInfo.getBasicDataId())).stream().map(PointInfo::getId).collect(Collectors.toList());
//                dataList = pointExtInfoService.getPointExtInfoList(ids);
//            }

                dataList = new ArrayList<>(new LinkedHashSet<>(dataList));
                return getResourceData("1", sceneIds, sceneInfo.getBasicDataId(), new ArrayList<>(), cityRoadNodes, dataList);
            }
        } else if ("2".equals(sceneInfo.getType())) {
            //现场
            return getResourceData(sceneInfo.getType(), sceneIds, sceneInfo.getBasicDataId(), keyFocusNodes, siteNodes, null);
        } else if ("3".equals(sceneInfo.getType())) {
            return getResourceData(sceneInfo.getType(), sceneIds, sceneInfo.getBasicDataId(), keyFocusNodes, residenceNodes, null);
        }
        return null;
    }


    List<BasicDataDTO> getResourceData(String sceneType, List<String> sceneIds, String basicDataId, List<String> zdNodeList, List<String> xcNodeList, List<PointExtInfo> dataList) {
        List<PointExtInfo> pointExtInfoList = null;
        if (dataList == null) {
            PointInfo pointInfo = pointInfoService.getById(basicDataId);
            if (pointInfo != null) {
                List<PointInfo> pointInfos = pointInfoService.list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getJcxxId, pointInfo.getId()));
                if (!CollectionUtils.isEmpty(pointInfos)) {
                    List<String> ids = pointInfoService.list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getJcxxId, pointInfo.getId()))
                            .stream().map(PointInfo::getId).collect(Collectors.toList());
                    ids.add(pointInfo.getId());
                    pointExtInfoList = pointExtInfoService.getPointExtInfoList(ids);
                }
            }
        } else {
            pointExtInfoList = dataList;
        }

        List<BasicDataDTO> all = new ArrayList<>();
        int zdNum = 0;
        if (!CollectionUtils.isEmpty(zdNodeList) && !CollectionUtils.isEmpty(pointExtInfoList)) {
            //重点关注
            List<BasicDataDTO> zdList = new ArrayList<>();
            int zd = 0;
            for (String node : zdNodeList) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> pointExtInfo.getType().endsWith(node)).collect(Collectors.toList());
                List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
                for (PointExtInfo pointExtInfo : list) {
                    BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                    if (!pointExtInfo.getGeojson().isEmpty()) {
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
                basicDataDTOList = getResourceData(sceneIds, sceneType, node, basicDataDTOList);
                zdList.add(BasicDataDTO.builder().id("9" + (zd++)).name(node).type(node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
                zdNum = zdNum + basicDataDTOList.size();
            }
            all.add(BasicDataDTO.builder().id("9").name("重点关注").num(zdList.size())
                    .children(zdList).build());
        }
        //现场
        List<BasicDataDTO> xxc = new ArrayList<>();
        int xcNum = 0;
        int xc = 0;
        for (String node : xcNodeList) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointExtInfoList)) {
                List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> node.equals(pointExtInfo.getType())).collect(Collectors.toList());
                for (PointExtInfo pointExtInfo : list) {
                    if (!Objects.isNull(pointExtInfo.getGeojson())) {
                        BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).type(pointExtInfo.getType()).name(pointExtInfo.getName()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                        basicDataDTOList.add(basicDataDTO);
                    }
                }
            }
            basicDataDTOList = getResourceData(sceneIds, sceneType, node, basicDataDTOList);
            xxc.add(BasicDataDTO.builder().id(sceneType + (xc++)).name(node).type(node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
            xcNum = xcNum + basicDataDTOList.size();
        }
        all.add(BasicDataDTO.builder().id(sceneType).name("2".equals(sceneType) ? "现场" : "3".equals(sceneType) ? "住地" : "城市道路").children(xxc)
                .num(xcNum).build());
        BasicDataDTO basicDataDTO = new BasicDataDTO();
        if (!"1".equals(sceneType)) {
            basicDataDTO = getRoadResource(sceneIds, sceneType);
            all.add(basicDataDTO);
        }
        List<BasicDataDTO> allList = new ArrayList<>();
        int total = zdNum + xcNum + basicDataDTO.getNum();
        allList.add(BasicDataDTO.builder().id("0").name("全部").children(all).num(total).build());

        return allList;

    }


    public List<BasicDataDTO> getDrawResourceData(List<String> sceneIds, String indoorFlag, String
            featureType, List<BasicDataDTO> dataList, String keyFlag) {
        DrawData data = new DrawData();
        data.setSceneIdList(sceneIds);
        data.setType("basic");
        data.setFeatureType(featureType);
        data.setIndoorFlag(indoorFlag);
        data.setKeyFlag(keyFlag);
        List<DrawData> zdryDataList = drawDataService.getDrawDataList(data);
        for (DrawData drawData : zdryDataList) {
            BasicDataDTO basicDataDTO = BasicDataDTO.builder().dataLevelFlag("1").data(drawData.getData()).groupId(drawData.getGroupId()).id(drawData.getId()).name(drawData.getName()).type(featureType).floorNum(drawData.getFloorNum()).buildName(drawData.getBuildName()).children(new ArrayList<>()).coordinates(drawData.getGeojson().getObject("coordinates", List.class)).build();
            dataList.add(basicDataDTO);
        }
        return dataList;
    }

    public List<BasicDataDTO> getResourceData(List<String> sceneIds, String sceneType, String
            featureType, List<BasicDataDTO> dataList) {
        if (CollectionUtils.isEmpty(sceneIds)) {
            return new ArrayList<>();
        }
        DrawData data = new DrawData();
        data.setSceneIdList(sceneIds);
        data.setType("basic");
        //data.setSceneType(sceneType);
        data.setFeatureType(featureType);
        List<DrawData> zdryDataList = drawDataService.getDrawDataList(data);
        for (DrawData drawData : zdryDataList) {
            BasicDataDTO basicDataDTO = BasicDataDTO.builder().data(drawData.getData()).dataLevelFlag("1").groupId(drawData.getGroupId()).id(drawData.getId()).name(drawData.getName()).type(featureType).floorNum(drawData.getFloorNum()).buildName(drawData.getBuildName()).children(new ArrayList<>()).coordinates(drawData.getGeojson().getObject("coordinates", List.class)).build();
            dataList.add(basicDataDTO);
        }
        return dataList;
    }

    @Override
    public int getSceneDangerData(String sceneId) {
        List<BasicDataDTO> basicDataDTOS = getMultiSceneResourceTree(List.of(sceneId));
        //Set<String> set = new HashSet<>();
        //System.out.println(basicDataDTOS.get(0).getNum()+basicDataDTOS.get(0).getName());
        //System.out.println(sceneId+"---"+basicDataDTOS.get(0).getChildren().get(0).getNum()+basicDataDTOS.get(0).getChildren().get(0).getName());
        //return getChildrenData(set, basicDataDTOS.get(0).getChildren());
        return basicDataDTOS.get(0).getChildren().get(0).getNum();
    }

    @Override
    public JSONObject getDangerData(String sceneId) {
        JSONObject jsonObject = new JSONObject();
        int dangerNum = getMultiSceneResourceTree(List.of(sceneId)).get(0).getNum();
        List<BasicDataDTO> basicDataDTOList = getKeyResourceTree(sceneId);
        jsonObject.put("dangerNum", dangerNum);
        jsonObject.put("keyNum", basicDataDTOList.get(0).getNum());
        jsonObject.put("keyResource", basicDataDTOList);
        return jsonObject;
    }

    public Set<String> getChildrenData(Set<String> set, List<BasicDataDTO> basicDataDTOS) {
        for (BasicDataDTO basicDataDTO : basicDataDTOS) {
            if (!StringUtils.isEmpty(basicDataDTO.getDataLevelFlag())) {
                set.add(basicDataDTO.getId());
            }
            List<BasicDataDTO> basicDataDTOList = basicDataDTO.getChildren();
            if (!CollectionUtils.isEmpty(basicDataDTOList)) {
                getChildrenData(set, basicDataDTOList);
            }
        }
        return set;
    }

    @Override
    public List<DrawData> getDrawLines(String sceneId) {
        DrawData data = new DrawData();
        data.setSceneId(sceneId);
        data.setType("lines");
        data.setLineType("0");
        return drawDataService.getLineList(data);
    }

    @Override
    public ScenePlan getEmergencyForcesOfScene(String sceneId) {
        List<ScenePlan> scenePlanList = scenePlanService.getScenePlanNode(sceneId, "应急力量");
        if (CollectionUtils.isEmpty(scenePlanList)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("emergency", 0);
            return new ScenePlan().setData(jsonObject);
        } else {
            return scenePlanList.get(0);
        }
    }

    @Override
    public List<Map<String, Object>> getRoadPlanOfScene(String sceneId) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<DrawData> points = drawDataService.list(new LambdaQueryWrapper<DrawData>().eq(DrawData::getDeleteFlag, 0)
                .eq(DrawData::getSceneId, sceneId).apply("data->>'customStyle' = '4'"));

        if (!CollectionUtils.isEmpty(points)) {
            Map<String, Object> map = new HashMap<>();
            map.put("hedgingLine", points.stream().map(DrawData::getName).collect(Collectors.toList()));
            list.add(map);
        }
        List<DrawData> lines = drawDataService.list(new LambdaQueryWrapper<DrawData>().eq(DrawData::getDeleteFlag, 0)
                .eq(DrawData::getSceneId, sceneId).apply("data->>'customStyle' = '7'"));
        if (!CollectionUtils.isEmpty(lines)) {
            Map<String, Object> map = new HashMap<>();
            map.put("hospitalLine", lines.stream().map(DrawData::getName).collect(Collectors.toList()));
            list.add(map);
        }
        return list;
    }

    @Override
    public List<SceneInfo> getSchemeList(SceneInfo sceneInfo) {
        return baseMapper.getSchemeList(sceneInfo);
    }

    @Override
    public boolean addScheme(SceneInfo sceneInfo) {
        sceneInfo.setSchemeFlag(1);
        sceneInfo.setDeleteFlag(0);
        return save(sceneInfo);
    }

    @Override
    public boolean copyScheme(String id) {
        return copyScene(id, null, null);
    }

    @Override
    public boolean copyScene(String oldId, String newSceneId, String newTaskId) {
        //保存常规方案数据
        String newId = org.springframework.util.StringUtils.hasText(newSceneId) ? newSceneId : IdWorker.get32UUID();
        //漫游
        SceneInfo scheme = getById(oldId);
        scheme.setId(newId);
        scheme.setCreateTime(LocalDateTime.now());
        if (org.springframework.util.StringUtils.hasText(newTaskId)) {
            scheme.setTaskId(newTaskId);
        }
        //如果是常规方案复制
        if (!org.springframework.util.StringUtils.hasText(newSceneId) && !org.springframework.util.StringUtils.hasText(newTaskId)) {
            long total = count(new LambdaQueryWrapper<SceneInfo>()
                    .eq(SceneInfo::getOriginSchemeId, oldId)
                    .eq(SceneInfo::getDeleteFlag, 0));
            int num = (int) (total + 1);
            String name = scheme.getSceneName() + "（副本" + Utils.numberToChinese(num) + "）";
            scheme.setSceneName(name);
            scheme.setOriginSchemeId(oldId);
        }
        saveOrUpdate(scheme);
        //plan
        List<ScenePlan> scenePlanList = scenePlanService.list(new LambdaQueryWrapper<ScenePlan>()
                .eq(ScenePlan::getSceneId, oldId));
        scenePlanList.forEach(scenePlan -> {
            scenePlan.setSceneId(newId);
            scenePlan.setId(IdWorker.get32UUID());
        });
        scenePlanService.saveBatch(scenePlanList);
        //plan_ext
        List<ScenePlanExt> scenePlanExtList = scenePlanExtService.list(new LambdaQueryWrapper<ScenePlanExt>()
                .eq(ScenePlanExt::getId, oldId));
        scenePlanExtList.forEach(scenePlanExt -> {
            scenePlanExt.setSceneId(newId);
            scenePlanExt.setId(IdWorker.get32UUID());
        });
        scenePlanExtService.saveBatch(scenePlanExtList);
        //draw_data
        List<DrawData> drawDataList = drawDataService.list(new LambdaQueryWrapper<DrawData>().eq(DrawData::getDeleteFlag, 0)
                .eq(DrawData::getSceneId, oldId));
        drawDataList.forEach(drawData -> {
            if (org.springframework.util.StringUtils.hasText(newTaskId)) {
                drawData.setTaskId(newTaskId);
            }
            drawData.setSceneId(newId);
            String drawId = IdWorker.get32UUID();
            drawData.setId(drawId);
            drawData.getData().put("id", drawId);
            JSONObject marker = drawData.getData().getJSONObject("marker");
            if (Objects.nonNull(marker)) {
                marker.put("id", drawId);
                drawData.getData().put("marker", marker);
            }
            JSONObject uav = drawData.getData().getJSONObject("uav");
            if (Objects.nonNull(uav)) {
                uav.put("id", drawId);
                drawData.getData().put("uav", uav);
            }
            JSONObject info = drawData.getData().getJSONObject("info");
            if (Objects.nonNull(info)) {
                JSONObject lineData = info.getJSONObject("lineData");
                if (Objects.nonNull(lineData)) {
                    lineData.put("id", IdWorker.get32UUID());
                    drawData.getData().getJSONObject("info").put("lineData", lineData);
                }
                JSONObject custom = info.getJSONObject("custom");
                if (Objects.nonNull(custom)) {
                    custom.put("id", IdWorker.get32UUID());
                    drawData.getData().getJSONObject("info").put("custom", custom);
                }
            }
        });
        drawDataService.saveBatch(drawDataList);

        // 电子档案
        List<TaskArchivesData> taskArchivesDataList = taskArchivesDataService.getArchivesData(oldId);
        if (!CollectionUtils.isEmpty(taskArchivesDataList)) {
            List<TaskArchivesData> newArchivesDataList = new ArrayList<>();
            taskArchivesDataList.forEach(taskArchivesData -> {
                if (CollectionUtils.isEmpty(taskArchivesData.getChildren())) {
                    taskArchivesData.setId(IdWorker.getIdStr());
                    taskArchivesData.setBusinessId(newId);
                    newArchivesDataList.add(taskArchivesData);
                } else {
                    String id = IdWorker.getIdStr();
                    taskArchivesData.setId(id);
                    taskArchivesData.setBusinessId(newId);
                    newArchivesDataList.add(taskArchivesData);
                    taskArchivesData.getChildren().forEach(children -> {
                        children.setId(IdWorker.getIdStr());
                        children.setParentId(id);
                        children.setBusinessId(newId);
                        newArchivesDataList.add(children);
                    });
                }
            });
            taskArchivesDataService.saveBatch(newArchivesDataList);
        }
        return true;
    }

    public List<DrawData> getDrawLines(List<String> sceneIds) {
        DrawData data = new DrawData();
        data.setSceneIdList(sceneIds);
        data.setType("lines");
        return drawDataService.getDrawDataList(data);
    }
}
