package com.xaxc.teqin.service.impl;

import com.alibaba.excel.util.FileUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xaxc.teqin.common.util.Utils;
import com.xaxc.teqin.model.dto.BasicDataDTO;
import com.xaxc.teqin.model.dto.TaskDTO;
import com.xaxc.teqin.model.entity.*;
import com.xaxc.teqin.mapper.TaskMapper;
import com.xaxc.teqin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 任务表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-06-21
 */
@Slf4j
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Value("#{'${treeNode.keyFocus}'.split(',')}")
    private List<String> keyFocusNodes;

    @Value("#{'${treeNode.site.key1}'.split(',')}")
    private List<String> siteNodes;

    @Value("#{'${treeNode.residence.key1}'.split(',')}")
    private List<String> residenceNodes;

    @Value("#{'${treeNode.cityRoad}'.split(',')}")
    private List<String> cityRoadNodes;

    @Value("${buffer.radius}")
    private double bufferRadius;

    @Resource
    private ISceneInfoService sceneInfoService;
    @Resource
    IScenePlanService scenePlanService;

    @Resource
    ITaskPlanService taskPlanService;

    @Resource
    private IDrawDataService drawDataService;

    @Resource
    IPointInfoService pointInfoService;

    @Resource
    IPointExtInfoService pointExtInfoService;

    @Resource
    ITaskArchivesDataService taskArchivesDataService;

    @Resource
    SpatialQuery spatialQuery;

    @Value("${highway.basicInfoTypeId}")
    private String highwayBasicInfoTypeId;

    @Value("${railway.basicInfoTypeId}")
    private String railwayBasicInfoTypeId;

    @Value("${site.basicInfoTypeId}")
    private String siteBasicInfoTypeId;

    @Value("${residence.basicInfoTypeId}")
    private String residenceBasicInfoTypeId;


    @Resource
    IFileInfoService fileInfoService;

    @Override
    public boolean copyTask(String id) {
        String newId = IdWorker.get32UUID();
        Task task = getById(id);
        task.setId(newId);
        task.setCreateTime(LocalDateTime.now());
        save(task);
        List<TaskPlan> taskPlanList = taskPlanService.list(new LambdaQueryWrapper<TaskPlan>().eq(TaskPlan::getTaskId, id));
        taskPlanList.forEach(taskPlan -> {
            taskPlan.setId(IdWorker.get32UUID());
            taskPlan.setTaskId(newId);
        });
        taskPlanService.saveBatch(taskPlanList);
        List<SceneInfo> sceneInfoList = sceneInfoService.list(new LambdaQueryWrapper<SceneInfo>().eq(SceneInfo::getDeleteFlag, 0));
        sceneInfoList.forEach(sceneInfo -> {
            sceneInfoService.copyScene(sceneInfo.getId(), null, newId);
        });
        return true;
    }

    public TaskDTO getTaskById(String id) {
        Task task = getById(id);
        TaskDTO taskDTO = null;
        if (task != null) {
            taskDTO = new TaskDTO();
            BeanUtils.copyProperties(task, taskDTO);
            //获取任务下的场景数据
            List<SceneInfo> sceneList = sceneInfoService.list(new LambdaQueryWrapper<SceneInfo>()
                    .eq(SceneInfo::getTaskId, taskDTO.getId()).eq(SceneInfo::getDeleteFlag, 0)
                    .orderByAsc(SceneInfo::getBeginTime));
            taskDTO.setSceneList(sceneList);
        }
        return taskDTO;
    }

    @Override
    public List<Task> getList(Task task) {
        LambdaQueryWrapper<Task> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(task.getCreateId())) {
            lambdaQueryWrapper.eq(Task::getCreateId, task.getCreateId());
        }
        if (StringUtils.hasText(task.getTaskName())) {
            lambdaQueryWrapper.like(Task::getTaskName, task.getTaskName());
        }
        if (StringUtils.hasText(task.getTaskLevel())) {
            lambdaQueryWrapper.like(Task::getTaskLevel, task.getTaskLevel());
        }
        lambdaQueryWrapper.eq(Task::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(Task::getTaskStartTime);

        List<Task> taskList = list(lambdaQueryWrapper);
        taskList.forEach(task1 -> {
            FileInfo fileInfo = fileInfoService.getOne(new LambdaQueryWrapper<FileInfo>().eq(FileInfo::getBusinessId, task1.getId()), false);
            if (fileInfo != null) {
                task1.setFileName(fileInfo.getOriginalFileName());
                task1.setFileUrl(fileInfo.getFilePath());
            }
        });
        return taskList;
    }

    @Override
    public TaskDTO getTaskDetail(String id) {
        Task task = getById(id);
        TaskDTO taskDTO = null;
        if (task != null) {
            taskDTO = new TaskDTO();
            //基础信息
            BeanUtils.copyProperties(task, taskDTO);
            FileInfo fileInfo = fileInfoService.getOne(new LambdaQueryWrapper<FileInfo>().eq(FileInfo::getBusinessId, id), false);
            if (fileInfo != null) {
                taskDTO.setFileName(fileInfo.getOriginalFileName());
                taskDTO.setFileUrl(fileInfo.getFilePath());
            }
            //获取任务下的场景数据
            List<SceneInfo> sceneList = sceneInfoService.getListByTaskId(id);
            //警力部署
            List<JSONObject> policeData = new ArrayList<>();
            //标绘数据
            List<DrawData> drawDataList = new ArrayList<>();
            List<String> basicIds = new ArrayList<>();
            if (!CollectionUtils.isEmpty(sceneList)) {
                for (SceneInfo sceneInfo : sceneList) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("sceneName", sceneInfo.getSceneName());
                    jsonObject.put("policeData", drawDataService.statisticalQuantity(sceneInfo.getId()));
                    policeData.add(jsonObject);
                    if (sceneInfo.getBasicDataId() != null) {
                        basicIds.add(sceneInfo.getBasicDataId());
                    }
                }
                List<String> ids = sceneList.stream().map(SceneInfo::getId).collect(Collectors.toList());
                drawDataList = drawDataService.list(new LambdaQueryWrapper<DrawData>().eq(DrawData::getDeleteFlag, 0).in(DrawData::getSceneId, ids).apply("data->'info'->>'featureType' is null"));
            } else {
                taskDTO.setSceneList(new ArrayList<>());
            }
            taskDTO.setSceneList(sceneList);
            taskDTO.setPolicePresenceList(policeData);
            taskDTO.setDrawDataList(drawDataList);

            if (!CollectionUtils.isEmpty(basicIds)) {
                taskDTO.setPointInfoList(pointExtInfoService.getPointExtInfoList(basicIds));
            }
        }
        return taskDTO;
    }

    @Override
    public Map<String, Object> statisticalQuantity() {
        return baseMapper.statisticalQuantity();
    }

    @Override
    public Map<String, Object> statisticalTotalAndNumOfLevel() {
        Map<String, Object> result = new HashMap<>();
        long countNum = count(new LambdaQueryWrapper<Task>().eq(Task::getDeleteFlag, 0));
        result.put("taskTotalNum", countNum);
        List<Map<String, Object>> mapList = baseMapper.statisticalQuantityOfLevel();
        result.put("taskNumDetail", mapList);
        return result;
    }

    @Override
    public List<Map<String, Object>> statisticalExecutingLevel() {
        return baseMapper.statisticalExecutingLevel();
    }

    @Override
    public List<Map<String, Object>> statisticalAllLevel() {
        return baseMapper.statisticalAllLevel();
    }

    @Override
    public List<Map<String, Object>> statisticalExecutedLevel() {
        return baseMapper.statisticalExecutedLevel();
    }

    @Override
    public List<Task> getExecutedList() {
        return baseMapper.getExecutedList();
    }

    @Override
    public List<Task> getExecutingList() {
        return baseMapper.getExecutingList();
    }

    @Override
    public List<JSONObject> getTaskResourceTree() {
        List<JSONObject> tree = new ArrayList<>();
        List<Task> tasks = list(new LambdaQueryWrapper<Task>()
                .eq(Task::getDeleteFlag, 0).orderByAsc(Task::getCreateTime));
        for (Task task : tasks) {
            JSONObject taskObject = new JSONObject();
            taskObject.put("id", task.getId());
            taskObject.put("name", task.getTaskName());
            taskObject.put("type", "task");
            List<SceneInfo> sceneInfos = sceneInfoService.list(new LambdaQueryWrapper<SceneInfo>()
                    .eq(SceneInfo::getDeleteFlag, 0)
                    .eq(SceneInfo::getTaskId, task.getId())
                    .orderByAsc(SceneInfo::getBeginTime));
            List<JSONObject> sceneList = new ArrayList<>();
            for (SceneInfo sceneInfo : sceneInfos) {
                JSONObject scene = new JSONObject();
                scene.put("id", sceneInfo.getId());
                scene.put("name", sceneInfo.getSceneName());
                scene.put("type", "scene");
                scene.put("children", new ArrayList<>());
                sceneList.add(scene);
            }
            taskObject.put("children", sceneList);
            tree.add(taskObject);
        }
        return tree;
    }

    @Override
    public List<BasicDataDTO> getTaskResourceTree(String taskId) {
        List<BasicDataDTO> allList = new ArrayList<>();
        int total = 0;
        List<PointExtInfo> dataList = new ArrayList<>();
        //查询任务下的线路，然后查询线路周边的基础数据
        DrawData data = new DrawData();
        data.setTaskId(taskId);
        data.setType("lines");
        //常规线路
        data.setLineType("0");
        List<DrawData> drawDataList = drawDataService.getLineList(data);

        for (DrawData drawData : drawDataList) {
            try {
                dataList.addAll(spatialQuery.queryLineBufferData(drawData.getGeom(), bufferRadius));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        //取任务中的现场、住地、线路下的数据
//        List<String> basicIds = sceneInfoService.list(new LambdaQueryWrapper<SceneInfo>().eq(SceneInfo::getDeleteFlag, 0)
//                .eq(SceneInfo::getTaskId, taskId).isNotNull(SceneInfo::getBasicDataId)).stream().map(SceneInfo::getBasicDataId).collect(Collectors.toList());
//        List<String> ids = pointInfoService.list(new LambdaQueryWrapper<PointInfo>().in(PointInfo::getId, basicIds).or().in(PointInfo::getJcxxId, basicIds)).stream().map(PointInfo::getId).collect(Collectors.toList());
//        dataList = pointExtInfoService.getPointExtInfoList(ids);

        dataList = new ArrayList<>(new LinkedHashSet<>(dataList));
        List<BasicDataDTO> all = new ArrayList<>();
        BasicDataDTO keyFocus = getKeyFocusResource(dataList, keyFocusNodes, taskId);
        all.add(keyFocus);
        BasicDataDTO residence = getSceneResource(dataList, residenceNodes, taskId, "3", "住地档案");
        all.add(residence);
        BasicDataDTO site = getSceneResource(dataList, siteNodes, taskId, "2", "现场档案");
        all.add(site);
        BasicDataDTO cityRoad = getSceneResource(dataList, cityRoadNodes, taskId, null, null);
        all.add(cityRoad);
        List<String> basicIds = sceneInfoService.list(new LambdaQueryWrapper<SceneInfo>()
                .eq(SceneInfo::getDeleteFlag, 0)
                .eq(SceneInfo::getType, "1")
                .eq(SceneInfo::getTaskId, taskId).ne(SceneInfo::getBasicDataId, "")).stream().map(SceneInfo::getBasicDataId).collect(Collectors.toList());

        List<Map<String, List<BasicDataDTO>>> basicDataList = pointInfoService.getResourceOfHighWayAndRailWay(basicIds);
        //高速
        AtomicInteger gsNum = new AtomicInteger();
        if (basicDataList.get(0).size() > 0) {
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
        AtomicInteger gtNum = new AtomicInteger();
        if (basicDataList.get(1).size() > 0) {
            List<BasicDataDTO> gt = new ArrayList<>();
            final int[] gtIndex = {0};
            basicDataList.get(1).forEach((key, value) -> {
                gtIndex[0]++;
                gtNum.set(gtNum.get() + value.size());
                gt.add(BasicDataDTO.builder().id("6" + gtIndex[0]).name(key).type(key).children(value).num(value.size()).build());
            });
            all.add(BasicDataDTO.builder().id("6").name("高铁").children(gt).num(gtNum.get()).build());
        }
        total = total + keyFocus.getNum() + residence.getNum() + site.getNum() + cityRoad.getNum() + gtNum.get() + gsNum.get();
        allList.add(BasicDataDTO.builder().id("0").name("全部").children(all).num(total).build());
        return allList;
    }

    @Override
    public List<BasicDataDTO> getTaskResourceTree2(String taskId) {
        return sceneInfoService.getTaskResourceTree(taskId);
    }

    @Override
    public List<BasicDataDTO> getTaskResourceTreeNew(String taskId) {
        return sceneInfoService.getTaskResourceTreeNew(taskId);
    }

    public List<BasicDataDTO> getResourceData(String taskId, String sceneType, String featureType, List<BasicDataDTO> dataList) {
        DrawData data = new DrawData();
        data.setTaskId(taskId);
        data.setType("basic");
        data.setSceneType(sceneType);
        data.setFeatureType(featureType);
        List<DrawData> zdryDataList = drawDataService.getDrawDataList(data);
        for (DrawData drawData : zdryDataList) {
            BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(drawData.getId()).floorNum(drawData.getFloorNum()).buildName(drawData.getBuildName()).name(drawData.getName()).type(featureType).children(new ArrayList<>()).coordinates(drawData.getGeojson().getObject("coordinates", List.class)).build();
            dataList.add(basicDataDTO);
        }
        return dataList;
    }

    public BasicDataDTO getSceneResource(List<PointExtInfo> pointExtInfoList, List<String> nodeList, String taskId, String sceneType, String parentType) {
        List<BasicDataDTO> sites = new ArrayList<>();
        int num = 0;
        int index = 0;
        for (String node : nodeList) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> {
                if (parentType != null) {
                    return (pointExtInfo.getType().endsWith(node) || pointExtInfo.getType().endsWith(node + "登记")) && parentType.equals(pointExtInfo.getParentTypeName());
                } else {
                    return (pointExtInfo.getType().endsWith(node) || pointExtInfo.getType().endsWith(node + "登记"));
                }
            }).collect(Collectors.toList());
            for (PointExtInfo pointExtInfo : list) {
                BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).name(pointExtInfo.getName()).type(pointExtInfo.getType()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                basicDataDTOList.add(basicDataDTO);
            }
            basicDataDTOList = getResourceData(taskId, sceneType, node, basicDataDTOList);
            num = num + basicDataDTOList.size();
            sites.add(BasicDataDTO.builder().id(sceneType + (index++)).name(node).type(node).children(basicDataDTOList).num(basicDataDTOList.size()).build());
        }
        return BasicDataDTO.builder().id(sceneType).name("2".equals(sceneType) ? "现场" : "3".equals(sceneType) ? "住地" : "城市道路")
                .children(sites).num(num).build();
    }


    public BasicDataDTO getKeyFocusResource(List<PointExtInfo> pointExtInfoList, List<String> nodeList, String taskId) {
        List<BasicDataDTO> keyFocus = new ArrayList<>();
        int num = 0;
        int index = 0;
        for (String node : nodeList) {
            List<BasicDataDTO> basicDataDTOList = new ArrayList<>();
            List<PointExtInfo> list = pointExtInfoList.stream().filter(pointExtInfo -> pointExtInfo.getType().endsWith(node) || pointExtInfo.getType().endsWith(node + "登记")).collect(Collectors.toList());
            for (PointExtInfo pointExtInfo : list) {
                BasicDataDTO basicDataDTO = BasicDataDTO.builder().id(pointExtInfo.getId()).name(pointExtInfo.getName()).type(pointExtInfo.getType()).children(new ArrayList<>()).coordinates(pointExtInfo.getGeojson().getObject("coordinates", List.class)).build();
                basicDataDTOList.add(basicDataDTO);
            }
            basicDataDTOList = getResourceData(taskId, null, node, basicDataDTOList);
            num = num + basicDataDTOList.size();
            keyFocus.add(BasicDataDTO.builder().id("9" + (index++)).name(node).type(node).children(basicDataDTOList).num(num).build());
        }
        return BasicDataDTO.builder().id("9").name("重点关注")
                .children(keyFocus).num(num).build();
    }


    private String getTimeDivision(LocalDateTime localDateTime) {
        if (localDateTime.getHour() < 12) {
            return "上午";
        } else if (localDateTime.getHour() == 12) {
            if (localDateTime.getMinute() > 0) {
                return "下午";
            } else {
                return "上午";
            }
        } else if (localDateTime.getHour() > 12) {
            return "下午";
        } else {
            return null;
        }
    }

    @Override
    public List<JSONObject> getTimelineOfTask(String taskId) {
        List<JSONObject> data = new ArrayList<>();
        List<SceneInfo> sceneList = sceneInfoService.getSimpleListByTaskIdAndType(taskId, null);
        if (CollectionUtils.isEmpty(sceneList)) {
            return new ArrayList<>();
        }
        //将同一天数据归在一起
        JSONObject jsonObject = new JSONObject();
        List<JSONObject> times = new ArrayList<>();
        LocalDate localDate = sceneList.get(0).getBeginTime().toLocalDate();

        String division = getTimeDivision(sceneList.get(0).getBeginTime());
        JSONObject o = new JSONObject();
        o.put("time", division);
        o.put("desc", "");
        times.add(o);

        for (int i = 0; i < sceneList.size(); i++) {
            if (localDate.equals(sceneList.get(i).getBeginTime().toLocalDate())) {
                //同一天区分上下午
                if (!Objects.equals(division, getTimeDivision(sceneList.get(i).getBeginTime()))) {
                    division = getTimeDivision(sceneList.get(i).getBeginTime());
                    JSONObject object = new JSONObject();
                    object.put("time", division);
                    object.put("desc", "");
                    times.add(object);
                }

                JSONObject object = new JSONObject();
                object.put("time", sceneList.get(i).getBeginTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                object.put("desc", sceneList.get(i).getSceneDesc());
                object.put("id", sceneList.get(i).getId());
                object.put("type", sceneList.get(i).getType());
                times.add(object);
            } else {
                jsonObject.put("time", Utils.getTimeData(localDate));
                jsonObject.put("data", times);
                data.add(jsonObject);

                jsonObject = new JSONObject();
                localDate = sceneList.get(i).getBeginTime().toLocalDate();
                times = new ArrayList<>();

                division = getTimeDivision(sceneList.get(i).getBeginTime());
                JSONObject object1 = new JSONObject();
                object1.put("time", division);
                object1.put("desc", "");
                times.add(object1);

                JSONObject object = new JSONObject();
                object.put("time", sceneList.get(i).getBeginTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                object.put("desc", sceneList.get(i).getSceneDesc());
                object.put("id", sceneList.get(i).getId());
                object.put("type", sceneList.get(i).getType());
                times.add(object);
            }
            if (i == sceneList.size() - 1) {
                jsonObject.put("time", Utils.getTimeData(localDate));
                jsonObject.put("data", times);
                data.add(jsonObject);
            }
        }
        return data;
    }

    private JSONObject getSceneDailyScheduleData(String taskId, String sceneType) {
        List<SceneInfo> sceneInfos = sceneInfoService.list(new LambdaQueryWrapper<SceneInfo>()
                .eq(SceneInfo::getDeleteFlag, 0)
                .eq(SceneInfo::getType, sceneType)
                .eq(SceneInfo::getTaskId, taskId).orderByAsc(SceneInfo::getBeginTime));
        List<String> xcList = sceneInfos.stream().map(SceneInfo::getBasicDataId).distinct().collect(Collectors.toList());
        JSONObject sceneObject = new JSONObject();
        sceneObject.put("num", xcList.size());
        List<JSONObject> scenes = new ArrayList<>();
        for (String basicId : xcList) {
            JSONObject detail = new JSONObject();
            detail.put("basicDataId", basicId);
            SceneInfo sceneDataInfo = sceneInfos.stream().filter(zd -> basicId.equals(zd.getBasicDataId())).findFirst().get();
            detail.put("sceneName", sceneDataInfo.getSceneName());
            List<SceneInfo> sceneInfoList = sceneInfoService.list(new LambdaQueryWrapper<SceneInfo>()
                    .eq(SceneInfo::getDeleteFlag, 0)
                    .eq(SceneInfo::getType, sceneType)
                    .eq(SceneInfo::getTaskId, taskId)
                    .eq(SceneInfo::getBasicDataId, basicId).orderByAsc(SceneInfo::getBeginTime));
            List<JSONObject> sceneData = new ArrayList<>();
            for (SceneInfo sceneInfo : sceneInfoList) {
                JSONObject scene = new JSONObject();
                scene.put("sceneId", sceneInfo.getId());
                scene.put("viewData", sceneInfo.getViewData());
                LocalDate localDate = sceneInfo.getBeginTime().toLocalDate();
                scene.put("sceneDate", Utils.getTimeData(localDate));
                scene.put("sceneTimeRange", sceneInfo.getBeginTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "-" + sceneInfo.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                sceneData.add(scene);
            }
            detail.put("timeList", sceneData);
            scenes.add(detail);
        }
        sceneObject.put("details", scenes);
        return sceneObject;
    }

    private JSONObject getDailyScheduleData(String taskId, JSONObject jsonObject) {
        List<SceneInfo> sceneInfos = sceneInfoService.list(new LambdaQueryWrapper<SceneInfo>()
                .eq(SceneInfo::getDeleteFlag, 0)
                .eq(SceneInfo::getTaskId, taskId).orderByAsc(SceneInfo::getBeginTime));
        int roadNum = 0;
        double totalLength = 0;
        int siteNum = 0;
        int residenceNum = 0;
        Set<String> roadDataIds = new HashSet<>();
        Set<String> siteDataIds = new HashSet();
        Set<String> residenceDataIds = new HashSet();
        List<JSONObject> detail = new ArrayList<>();
        for (SceneInfo sceneInfo : sceneInfos) {
            if ("1".equals(sceneInfo.getType())) {
                roadNum++;
                List<String> ids = updateSceneRoadData(sceneInfo);
                totalLength = totalLength + sceneInfo.getSceneRoadLength();
                JSONObject road = new JSONObject();
                roadDataIds.addAll(ids);
                road.put("drawDataIds", ids);
                road.put("sceneId", sceneInfo.getId());
                road.put("sceneName", sceneInfo.getSceneName());
                road.put("sceneType", sceneInfo.getType());
                road.put("roadName", sceneInfo.getSceneRoadDesc());
                road.put("roadLength", sceneInfo.getSceneRoadLength());
                road.put("usedTime", sceneInfo.getSceneRoadTime());
                road.put("viewData", sceneInfo.getViewData());
                detail.add(road);
            } else if ("2".equals(sceneInfo.getType())) {
                siteNum++;
                JSONObject scene = new JSONObject();
                scene.put("basicDataId", sceneInfo.getBasicDataId());
                siteDataIds.add(sceneInfo.getBasicDataId());
                scene.put("sceneId", sceneInfo.getId());
                scene.put("sceneName", sceneInfo.getSceneName());
                scene.put("sceneType", sceneInfo.getType());
                scene.put("viewData", sceneInfo.getViewData());
                LocalDate localDate = sceneInfo.getBeginTime().toLocalDate();
                scene.put("sceneDate", Utils.getTimeData(localDate));
                scene.put("sceneTimeRange", sceneInfo.getBeginTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "-" + sceneInfo.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                detail.add(scene);
            } else if ("3".equals(sceneInfo.getType())) {
                residenceNum++;
                JSONObject scene = new JSONObject();
                scene.put("basicDataId", sceneInfo.getBasicDataId());
                residenceDataIds.add(sceneInfo.getBasicDataId());
                scene.put("sceneId", sceneInfo.getId());
                scene.put("sceneName", sceneInfo.getSceneName());
                scene.put("sceneType", sceneInfo.getType());
                scene.put("viewData", sceneInfo.getViewData());
                LocalDate localDate = sceneInfo.getBeginTime().toLocalDate();
                scene.put("sceneDate", Utils.getTimeData(localDate));
                scene.put("sceneTimeRange", sceneInfo.getBeginTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "-" + sceneInfo.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                detail.add(scene);
            }
        }

        jsonObject.put("roadNum", roadNum);
        jsonObject.put("roadTotalLength", totalLength);
        jsonObject.put("roadDataIds", roadDataIds);
        jsonObject.put("siteNum", siteNum);
        jsonObject.put("siteDataIds", siteDataIds);
        jsonObject.put("residenceNum", residenceNum);
        jsonObject.put("residenceDataIds", residenceDataIds);
        jsonObject.put("sceneDetail", detail);
        return jsonObject;
    }

    private JSONObject getRoadDailyScheduleData(String taskId) {
        List<SceneInfo> roadInfos = sceneInfoService.list(new LambdaQueryWrapper<SceneInfo>()
                .eq(SceneInfo::getDeleteFlag, 0)
                .eq(SceneInfo::getType, "1")
                .eq(SceneInfo::getTaskId, taskId).orderByAsc(SceneInfo::getBeginTime));
        JSONObject roadObject = new JSONObject();
        roadObject.put("num", roadInfos.size());
        List<JSONObject> roads = new ArrayList<>();
        double totalLength = 0;
        for (SceneInfo sceneInfo : roadInfos) {
            List<String> ids = updateSceneRoadData(sceneInfo);
            totalLength = totalLength + sceneInfo.getSceneRoadLength();
            JSONObject road = new JSONObject();
            road.put("drawDataIds", ids);
            road.put("sceneName", sceneInfo.getSceneName());
            road.put("roadName", sceneInfo.getSceneRoadDesc());
            road.put("roadLength", sceneInfo.getSceneRoadLength());
            road.put("usedTime", sceneInfo.getSceneRoadTime());
            road.put("sceneId", sceneInfo.getId());
            road.put("viewData", sceneInfo.getViewData());
            roads.add(road);
        }
        roadObject.put("totalLength", totalLength);
        roadObject.put("details", roads);
        return roadObject;
    }

    @Override
    public JSONObject getDailyScheduleOfTask(String taskId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timeline", getTimelineOfTask(taskId));
        Task task = getById(taskId);
        jsonObject.put("taskName", task.getTaskName());
        jsonObject.put("taskLevel", task.getTaskLevel());
        jsonObject.put("taskBeginTime", task.getTaskStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        jsonObject.put("taskEndTime", task.getTaskStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        jsonObject.put("taskRange", "xxx");
        jsonObject.put("viewData", task.getViewData());
        //现场
        jsonObject.put("siteData", getSceneDailyScheduleData(taskId, "2"));
        //住地
        jsonObject.put("residenceData", getSceneDailyScheduleData(taskId, "3"));
        //道路
        jsonObject.put("roadData", getRoadDailyScheduleData(taskId));

        return jsonObject;
    }

    @Override
    public JSONObject getDailyScheduleOfTask2(String taskId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timeline", getTimelineOfTask(taskId));
        Task task = getById(taskId);
        jsonObject.put("taskName", task.getTaskName());
        jsonObject.put("taskLevel", task.getTaskLevel());
        jsonObject.put("taskBeginTime", task.getTaskStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        jsonObject.put("taskEndTime", task.getTaskStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        jsonObject.put("taskRange", "xxx");
        jsonObject.put("viewData", task.getViewData());
        return getDailyScheduleData(taskId, jsonObject);
    }

    @Override
    public List<JSONObject> getRelatedFeatureDataOfTask(String taskId, String type) {
        List<JSONObject> list = new ArrayList<>();
        List<SceneInfo> sceneInfoList = sceneInfoService.getSimpleListByTaskIdAndType(taskId, type);
        if ("1".equals(type)) {
            for (SceneInfo sceneInfo : sceneInfoList) {
                JSONObject object = new JSONObject();
                object.put("sceneInfo", sceneInfo);
                DrawData drawData = new DrawData();
                drawData.setSceneId(sceneInfo.getId());
                drawData.setType("lines");
                //常规线路
                drawData.setLineType("0");
                object.put("drawLineList", drawDataService.getLineList(drawData));
                if (sceneInfo.getBasicDataId() != null && !"".equals(sceneInfo.getBasicDataId())) {
                    object.put("basicDataList", pointExtInfoService.getPointExtInfoList(List.of(sceneInfo.getBasicDataId())));
                }
                list.add(object);
            }
        } else {
            for (SceneInfo sceneInfo : sceneInfoList) {
                JSONObject object = new JSONObject();
                object.put("sceneInfo", sceneInfo);
                object.put("basicDataList", pointExtInfoService.getPointExtInfoList(List.of(sceneInfo.getBasicDataId())));
                list.add(object);
            }
        }
        return list;
    }

    @Override
    public JSONObject getPoliceDataOfTask(String taskId) {
        JSONObject jsonObject = new JSONObject();
        int total = drawDataService.getPoliceDataNumOfTask(taskId);
        jsonObject.put("total", total);
        List<Map> taskDetail = drawDataService.getPoliceTypeDataOfTask(taskId);
        jsonObject.put("taskDetail", taskDetail);
        List<Map> sceneDetail = drawDataService.getScenePoliceTypeDataOfTask(taskId);
        jsonObject.put("sceneDetail", sceneDetail);
        return jsonObject;
    }

    @Override
    public JSONObject getBasicPlanOfTask(String taskId) {
        JSONObject jsonObject = new JSONObject();
        Task task = getById(taskId);
        jsonObject.put("taskLevel", task.getTaskLevel());
        jsonObject.put("taskName", task.getTaskName());
        jsonObject.put("basicInfo", taskPlanService.getTaskPlanNode(taskId, "基本情况"));
        jsonObject.put("guidingIdeology", taskPlanService.getTaskPlanNode(taskId, "指导思想"));
        jsonObject.put("jobRequirements", taskPlanService.getTaskPlanNode(taskId, "工作要求"));
        jsonObject.put("orgLeadership", taskPlanService.getTaskPlanNode(taskId, "组织领导"));
        return jsonObject;
    }

    @Override
    public List getDrawDataOfTask(String taskId) {
        return drawDataService.list(new LambdaQueryWrapper<DrawData>()
                .eq(DrawData::getDeleteFlag, 0)
                .eq(DrawData::getTaskId, taskId));
    }

    @Override
    public List getSceneDrawDataOfTask(String taskId) {
        List<SceneInfo> sceneList = sceneInfoService.getSimpleListByTaskIdAndType(taskId, null);
        if (!CollectionUtils.isEmpty(sceneList)) {
            for (SceneInfo sceneInfo : sceneList) {
                if (sceneInfo.getBasicDataId() != null && !"".equals(sceneInfo.getBasicDataId())) {
                    sceneInfo.setPointInfoList(pointExtInfoService.getPointExtInfoList(List.of(sceneInfo.getBasicDataId())));
                }
                DrawData drawData = new DrawData();
                drawData.setSceneId(sceneInfo.getId());
                drawData.setType("lines");
                drawData.setLineType("0");
                sceneInfo.setDrawInfoList(drawDataService.getLineList(drawData));
            }
        }
        return sceneList;
    }

    @Override
    public List getBasicDataOfTask(String taskId) {
        //获取任务下的场景数据
        List<SceneInfo> sceneList = sceneInfoService.getSimpleListByTaskIdAndType(taskId, null);
        List<String> basicIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sceneList)) {
            for (SceneInfo sceneInfo : sceneList) {
                if (sceneInfo.getBasicDataId() != null) {
                    basicIds.add(sceneInfo.getBasicDataId());
                }
            }
        }
        if (!CollectionUtils.isEmpty(basicIds)) {
            return pointExtInfoService.getPointExtInfoList(basicIds);
        }
        return null;
    }

    @Override
    public List getRomaDataOfTask(String taskId) {
        return sceneInfoService.getListByTaskId(taskId);
    }

    public List updateSceneRoadData(SceneInfo sceneInfo) {
        DrawData data = new DrawData();
        data.setType("lines");
        data.setSceneId(sceneInfo.getId());
        //常规线路
        data.setLineType("0");
        List<DrawData> drawDataList = drawDataService.getLineList(data);
        List ids = drawDataList.stream().map(DrawData::getId).collect(Collectors.toList());
        if (sceneInfo.getSceneRoadTime() == 0) {
            double length = drawDataList.stream().mapToDouble(DrawData::getLength).sum() / 1000;
            //用时 每小时60公里算
            long usedTime = length > 0 && length < 1 ? 1L : Math.round(length);
            //公里数
            double result = Double.parseDouble(String.format("%.2f", length));
            String roadName = sceneInfoService.getRoadName(drawDataList);
            sceneInfo.setSceneRoadTime(Long.valueOf(usedTime).intValue()).setSceneRoadLength(result).setSceneRoadDesc(roadName);
        }
        return ids;
    }

    @Override
    public JSONObject getPolicePresenceOfTask(String taskId) {

        List<SceneInfo> sceneList = sceneInfoService.getSimpleListByTaskIdAndType(taskId, null);
        Map idMap = new LinkedHashMap();
        for (int i = 0; i < sceneList.size(); i++) {
            idMap.put(sceneList.get(i).getId(), i + 1);
        }

        JSONObject jsonObject = new JSONObject();
        int policeTotal = drawDataService.getPoliceDataNumOfTask(taskId);
        jsonObject.put("policeTotal", policeTotal);

        //交通保障组
        TaskPlan roadTaskPlan = taskPlanService.getOne(new LambdaQueryWrapper<TaskPlan>()
                .eq(TaskPlan::getTaskId, taskId)
                .eq(TaskPlan::getPlanNode, "交通保障组"), false);
        JSONObject roadData = new JSONObject();
        //责任人信息
        roadData.put("basicData", roadTaskPlan != null ? roadTaskPlan.getData() : null);
        List<SceneInfo> citySceneList = sceneInfoService.getSimpleListByTaskIdAndType(taskId, "1");
        List<SceneInfo> roadSceneList = citySceneList.stream().filter(sceneInfo -> "".equals(sceneInfo.getBasicDataId())).collect(Collectors.toList());
        roadData.put("num", roadSceneList.size());
        roadData.put("policeData", drawDataService.statisticalByPoliceTypeOfTaskAndPlanNode(taskId, "警力部署", "1", ""));
        List<JSONObject> roadDetails = new ArrayList<>();
        int roadDangerSet = 0;
        for (SceneInfo sceneInfo : roadSceneList) {
            JSONObject roadDetailData = new JSONObject();
            int dangerSet = sceneInfoService.getSceneDangerData(sceneInfo.getId());
            roadDangerSet += dangerSet;
            roadDetailData.put("dangerNum", dangerSet);

            List ids = updateSceneRoadData(sceneInfo);
            roadDetailData.put("name", sceneInfo.getSceneName());
            roadDetailData.put("id", sceneInfo.getId());
            roadDetailData.put("viewData", sceneInfo.getViewData());
            roadDetailData.put("drawDataIds", ids);
            roadDetailData.put("roadName", sceneInfo.getSceneRoadDesc());
            roadDetailData.put("length", sceneInfo.getSceneRoadLength());
            roadDetailData.put("usedTime", sceneInfo.getSceneRoadTime());
            roadDetailData.put("index", idMap.get(sceneInfo.getId()));
            roadDetailData.put("type", 1);
            //警力数据
            roadDetailData.put("policeData", drawDataService.statisticalByPoliceTypeOfScenePlanNode(sceneInfo.getId(), "警力部署", sceneInfo.getType()));
            roadDetailData.put("planNodeData", scenePlanService.getScenePlanNode(sceneInfo.getId(), "警力部署"));
            roadDetails.add(roadDetailData);
        }
        jsonObject.put("roadDetails", roadDetails);
        roadData.put("dangerNum", roadDangerSet);
        jsonObject.put("roadData", roadData);
        //高速
        JSONObject highwayData = new JSONObject();
        //责任人信息
        highwayData.put("basicData", roadTaskPlan != null ? roadTaskPlan.getData() : null);
        List<SceneInfo> highwayList = citySceneList.stream().filter(sceneInfo ->
                "highway".equals(pointInfoService.getPointType(sceneInfo.getBasicDataId()))).collect(Collectors.toList());
        highwayData.put("num", highwayList.size());
        highwayData.put("policeData", drawDataService.statisticalByPoliceTypeOfTaskAndPlanNode(taskId, "警力部署", "1", highwayBasicInfoTypeId));
        List<JSONObject> highwayDetails = new ArrayList<>();
        int highwayDangerSet = 0;
        for (SceneInfo sceneInfo : highwayList) {
            JSONObject highwayDetailData = new JSONObject();
            int dangerSet = sceneInfoService.getSceneDangerData(sceneInfo.getId());
            highwayDangerSet += dangerSet;
            highwayDetailData.put("dangerNum", dangerSet);

            List ids = updateSceneRoadData(sceneInfo);
            highwayDetailData.put("name", sceneInfo.getSceneName());
            highwayDetailData.put("id", sceneInfo.getId());
            highwayDetailData.put("viewData", sceneInfo.getViewData());
            highwayDetailData.put("drawDataIds", ids);
            highwayDetailData.put("roadName", sceneInfo.getSceneRoadDesc());
            highwayDetailData.put("length", sceneInfo.getSceneRoadLength());
            highwayDetailData.put("usedTime", sceneInfo.getSceneRoadTime());
            highwayDetailData.put("index", idMap.get(sceneInfo.getId()));
            highwayDetailData.put("type", 1);
            //警力数据
            highwayDetailData.put("policeData", drawDataService.statisticalByPoliceTypeOfScenePlanNode(sceneInfo.getId(), "警力部署", sceneInfo.getType()));
            highwayDetailData.put("planNodeData", scenePlanService.getScenePlanNode(sceneInfo.getId(), "警力部署"));
            highwayDetails.add(highwayDetailData);
        }
        jsonObject.put("highwayDetails", highwayDetails);
        highwayData.put("dangerNum", highwayDangerSet);
        jsonObject.put("highwayData", highwayData);
        //高铁
        JSONObject railwayData = new JSONObject();
        //责任人信息
        railwayData.put("basicData", roadTaskPlan != null ? roadTaskPlan.getData() : null);
        List<SceneInfo> railwayList = citySceneList.stream().filter(sceneInfo -> "railway".equals(pointInfoService.getPointType(sceneInfo.getBasicDataId()))).collect(Collectors.toList());
        railwayData.put("num", railwayList.size());
        railwayData.put("policeData", drawDataService.statisticalByPoliceTypeOfTaskAndPlanNode(taskId, "警力部署", "1", railwayBasicInfoTypeId));
        List<JSONObject> railwayDetails = new ArrayList<>();
        int railwayDangerSet = 0;
        for (SceneInfo sceneInfo : railwayList) {
            JSONObject railwayDetailData = new JSONObject();
            int dangerSet = sceneInfoService.getSceneDangerData(sceneInfo.getId());
            railwayDangerSet += dangerSet;
            railwayDetailData.put("dangerNum", dangerSet);

            List ids = updateSceneRoadData(sceneInfo);
            railwayDetailData.put("name", sceneInfo.getSceneName());
            railwayDetailData.put("id", sceneInfo.getId());
            railwayDetailData.put("viewData", sceneInfo.getViewData());
            railwayDetailData.put("drawDataIds", ids);
            railwayDetailData.put("roadName", sceneInfo.getSceneRoadDesc());
            railwayDetailData.put("length", sceneInfo.getSceneRoadLength());
            railwayDetailData.put("usedTime", sceneInfo.getSceneRoadTime());
            railwayDetailData.put("index", idMap.get(sceneInfo.getId()));
            railwayDetailData.put("type", 1);
            //警力数据
            railwayDetailData.put("policeData", drawDataService.statisticalByPoliceTypeOfScenePlanNode(sceneInfo.getId(), "警力部署", sceneInfo.getType()));
            railwayDetailData.put("planNodeData", scenePlanService.getScenePlanNode(sceneInfo.getId(), "警力部署"));
            railwayDetails.add(railwayDetailData);
        }
        jsonObject.put("railwayDetails", railwayDetails);
        railwayData.put("dangerNum", railwayDangerSet);
        jsonObject.put("railwayData", railwayData);

        //住地警卫组
        TaskPlan residenceTaskPlan = taskPlanService.getOne(new LambdaQueryWrapper<TaskPlan>()
                .eq(TaskPlan::getTaskId, taskId)
                .eq(TaskPlan::getPlanNode, "住地警卫组"), false);
        JSONObject residenceData = new JSONObject();
        //责任人信息
        residenceData.put("basicData", residenceTaskPlan != null ? residenceTaskPlan.getData() : null);
        List<SceneInfo> residenceSceneList = sceneInfoService.getSimpleListByTaskIdAndType(taskId, "3");
        residenceData.put("num", residenceSceneList.size());
        residenceData.put("policeData", drawDataService.statisticalByPoliceTypeOfTaskAndPlanNode(taskId, "警力部署", "3", residenceBasicInfoTypeId));
        int residenceSet = 0;
        List<JSONObject> residenceDetails = new ArrayList<>();
        for (SceneInfo sceneInfo : residenceSceneList) {
            JSONObject residenceDetailData = new JSONObject();
            int dangerSet = sceneInfoService.getSceneDangerData(sceneInfo.getId());
            residenceSet += dangerSet;
            if (sceneInfo.getBasicDataId() == null || "".equals(sceneInfo.getBasicDataId())) {
                PointInfo pointInfo = pointInfoService.getByName(sceneInfo.getSceneName());
                if (pointInfo != null) {
                    residenceDetailData.put("basicDataId", pointInfo.getId());
                }
            } else {
                residenceDetailData.put("basicDataId", sceneInfo.getBasicDataId());
            }

            residenceDetailData.put("name", sceneInfo.getSceneName());
            residenceDetailData.put("id", sceneInfo.getId());
            residenceDetailData.put("viewData", sceneInfo.getViewData());
            residenceDetailData.put("dangerNum", dangerSet);
            residenceDetailData.put("index", idMap.get(sceneInfo.getId()));
            residenceDetailData.put("type", 3);
            //警力数据
            residenceDetailData.put("policeData", drawDataService.statisticalByPoliceTypeOfScenePlanNode(sceneInfo.getId(), "警力部署", sceneInfo.getType()));
            residenceDetailData.put("planNodeData", scenePlanService.getScenePlanNode(sceneInfo.getId(), "警力部署"));
            residenceDetails.add(residenceDetailData);
        }
        jsonObject.put("residenceDetails", residenceDetails);
        residenceData.put("dangerNum", residenceSet);
        jsonObject.put("residenceData", residenceData);

        //现场警卫组
        TaskPlan siteTaskPlan = taskPlanService.getOne(new LambdaQueryWrapper<TaskPlan>()
                .eq(TaskPlan::getTaskId, taskId)
                .eq(TaskPlan::getPlanNode, "现场警卫组"), false);
        JSONObject siteData = new JSONObject();
        //责任人信息
        siteData.put("basicData", siteTaskPlan != null ? siteTaskPlan.getData() : null);
        List<SceneInfo> siteSceneList = sceneInfoService.getSimpleListByTaskIdAndType(taskId, "2");
        siteData.put("num", siteSceneList.size());
        siteData.put("policeData", drawDataService.statisticalByPoliceTypeOfTaskAndPlanNode(taskId, "警力部署", "2", siteBasicInfoTypeId));
        int siteSet = 0;
        List<JSONObject> siteDetails = new ArrayList<>();
        for (SceneInfo sceneInfo : siteSceneList) {
            JSONObject siteDetailData = new JSONObject();
            int dangerSet = sceneInfoService.getSceneDangerData(sceneInfo.getId());
            siteSet += dangerSet;
            if (sceneInfo.getBasicDataId() == null || "".equals(sceneInfo.getBasicDataId())) {
                PointInfo pointInfo = pointInfoService.getByName(sceneInfo.getSceneName());
                if (pointInfo != null) {
                    siteDetailData.put("basicDataId", pointInfo.getId());
                }
            } else {
                siteDetailData.put("basicDataId", sceneInfo.getBasicDataId());
            }

            siteDetailData.put("name", sceneInfo.getSceneName());
            siteDetailData.put("id", sceneInfo.getId());
            siteDetailData.put("viewData", sceneInfo.getViewData());
            siteDetailData.put("dangerNum", dangerSet);
            siteDetailData.put("index", idMap.get(sceneInfo.getId()));
            siteDetailData.put("type", 2);
            //警力数据
            siteDetailData.put("policeData", drawDataService.statisticalByPoliceTypeOfScenePlanNode(sceneInfo.getId(), "警力部署", sceneInfo.getType()));
            siteDetailData.put("planNodeData", scenePlanService.getScenePlanNode(sceneInfo.getId(), "警力部署"));
            siteDetails.add(siteDetailData);
        }
        jsonObject.put("siteDetails", siteDetails);
        siteData.put("dangerNum", siteSet);
        jsonObject.put("siteData", siteData);

        List<JSONObject> jsonObjectList = new ArrayList<>();
        jsonObjectList.addAll(roadDetails);
        jsonObjectList.addAll(siteDetails);
        jsonObjectList.addAll(residenceDetails);
        jsonObjectList.addAll(highwayDetails);
        jsonObjectList.addAll(railwayDetails);
        jsonObjectList.sort(new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                if (o1.getIntValue("index") > o2.getIntValue("index")) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        jsonObject.put("SceneDetail", jsonObjectList);
        List<BasicDataDTO> basicDataDTOList = getTaskResourceTreeNew(taskId);
        jsonObject.put("dangerTotal", CollectionUtils.isEmpty(basicDataDTOList) ? 0 : basicDataDTOList.get(0).getChildren().get(0).getNum());
        return jsonObject;
    }

    @Override
    public List getPostPoliceDataOfTask(String taskId) {
        return drawDataService.getPostPoliceDataOfTask(taskId);
    }

    @Override
    public List<Map<String, Object>> getEmergencyForcesOfTask(String taskId) {
        Map<String, Object> map = new HashMap<>();
        long num = drawDataService.count(new LambdaQueryWrapper<DrawData>().eq(DrawData::getDeleteFlag, 0)
                .eq(DrawData::getTaskId, taskId).eq(DrawData::getPoliceType, "应急处突警力"));
        map.put("emergency", num);
        return List.of(map);
    }

    @Override
    public List<Map<String, Object>> getRoadPlanOfTask(String taskId) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<DrawData> points = drawDataService.list(new LambdaQueryWrapper<DrawData>().eq(DrawData::getDeleteFlag, 0)
                .eq(DrawData::getTaskId, taskId).apply("data->>'customStyle' = '4'"));

        if (!CollectionUtils.isEmpty(points)) {
            Map<String, Object> map = new HashMap<>();
            map.put("hedgingLine", points.stream().map(DrawData::getName).collect(Collectors.toList()));
            list.add(map);
        }
        List<DrawData> lines = drawDataService.list(new LambdaQueryWrapper<DrawData>().eq(DrawData::getDeleteFlag, 0)
                .eq(DrawData::getTaskId, taskId).apply("data->>'customStyle' = '7'"));
        if (!CollectionUtils.isEmpty(lines)) {
            Map<String, Object> map = new HashMap<>();
            map.put("hospitalLine", lines.stream().map(DrawData::getName).collect(Collectors.toList()));
            list.add(map);
        }
        return list;
    }

    @Override
    public void uploadArchives(MultipartFile file, String businessId) {
        try {
            String filePath = fileInfoService.uploadFileAndGetFilePath(file);
            taskArchivesDataService.parseToDb(filePath, businessId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteArchives(String businessId) {
        taskArchivesDataService.remove(new LambdaQueryWrapper<TaskArchivesData>()
                .eq(TaskArchivesData::getBusinessId, businessId));
    }

    @Override
    public List<TaskArchivesData> getArchivesData(String businessId) {
        return taskArchivesDataService.getArchivesData(businessId);
    }
}
