package com.xaxc.teqin.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.common.util.Utils;
import com.xaxc.teqin.mapper.DrawDataMapper;
import com.xaxc.teqin.mapper.ScenePlanMapper;
import com.xaxc.teqin.model.entity.DrawData;
import com.xaxc.teqin.model.entity.SceneInfo;
import com.xaxc.teqin.model.entity.ScenePlan;
import com.xaxc.teqin.service.IDrawDataService;
import com.xaxc.teqin.service.ISceneInfoService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 标绘数据 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-09-04
 */
@Slf4j
@Service
public class DrawDataServiceImpl extends ServiceImpl<DrawDataMapper, DrawData> implements IDrawDataService {

    @Resource
    ScenePlanMapper scenePlanService;

    @Lazy
    @Resource
    ISceneInfoService sceneInfoService;


    @Override
    public JSONObject getLineInterpolatePoint(String sceneId) {
        return JSONObject.parseObject(baseMapper.getLineInterpolatePoint(sceneId));
    }

    @Override
    public DrawData getDetail(String id) {
        DrawData drawData = getOne(new LambdaQueryWrapper<DrawData>().eq(DrawData::getId, id), false);

        if ("basic".equals(drawData.getType())) {
            if (!StringUtils.hasText(drawData.getRefLineName()) && !StringUtils.hasText(drawData.getPosition())) {
                Map<String, Object> map = getDistanceData(null, drawData.getSceneId(), drawData.getGeom());
                if (!CollectionUtils.isEmpty(map)) {
                    drawData.setRefLineName(map.get("name").toString());
                    drawData.setPosition(map.get("start_dis").toString());
                }
            } else if (StringUtils.hasText(drawData.getPosition()) && StringUtils.hasText(drawData.getRefId()) && !StringUtils.hasText(drawData.getRefLineName())) {
                DrawData data = getOne(new LambdaQueryWrapper<DrawData>().eq(DrawData::getId, drawData.getRefId()), false);
                if (data != null) {
                    drawData.setRefLineName(data.getName());
                }
            }
        }

        if ("police".equals(drawData.getType())) {
            List<ScenePlan> scenePlanList = scenePlanService.getScenePlanOfNode(drawData.getSceneId(), "警力部署");
            if (!CollectionUtils.isEmpty(scenePlanList) && scenePlanList.get(0) != null) {
                drawData.setPoliceData(scenePlanList.get(0).getData());
            }
        }
        return drawData;
    }

    @Override
    public List<Map<String, Object>> getPoliceStatisticsOfScenePlanNode(String sceneId, String planNode) {
        return baseMapper.getPoliceStatisticsOfScenePlanNode(sceneId, planNode);
    }

    @Override
    public Integer getPoliceTotalOfScenePlanNode(String sceneId, String planNode) {
        return baseMapper.getPoliceTotalOfScenePlanNode(sceneId, planNode);
    }

    @Override
    public Integer getEmergencyPolice(String sceneId) {
        return baseMapper.getEmergencyPolice(sceneId);
    }


    @Override
    public List<Map<String, Object>> statisticalQuantity(String sceneId) {
        return baseMapper.statisticalQuantity(sceneId);
    }

    private JSONObject getMarkerData(Object object) {
        JSONObject data = JSONObject.parseObject(JSON.toJSONString(object));
        String name = data.getString("text");
        JSONArray coordinate = JSONArray.parseArray(data.get("coordinate").toString());
        ProjCoordinate projCoordinate = Utils.cgcs2000ToWgs84(Double.parseDouble(coordinate.get(0).toString()), Double.parseDouble(coordinate.get(1).toString()));
        String geom = "POINT(" + projCoordinate.x + " " + projCoordinate.y + ")";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("geom", geom);
        return jsonObject;
    }

    private JSONObject getMarkerData(Object object, List pointCoordinate) {
        JSONObject data = JSONObject.parseObject(JSON.toJSONString(object));
        String name = data.getString("text");
        JSONArray coordinate = JSONArray.parseArray(data.get("coordinate").toString());
        ProjCoordinate projCoordinate = Utils.wgs84ToCgcs2000(Double.parseDouble(pointCoordinate.get(0).toString()), Double.parseDouble(pointCoordinate.get(1).toString()));
        List newCoordinate = new ArrayList<>();
        newCoordinate.add(projCoordinate.x);
        newCoordinate.add(projCoordinate.y);
        newCoordinate.add(1);
        data.put("coordinate", newCoordinate);
        String geom = "POINT(" + pointCoordinate.get(0) + " " + pointCoordinate.get(1) + ")";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("geom", geom);
        jsonObject.put("marker", data);
        return jsonObject;
    }

    private JSONObject getPolygonData(Object marker, List pointCoordinate) {
        JSONObject markerData = JSONObject.parseObject(JSON.toJSONString(marker));
        String name = markerData.getString("text");
        ProjCoordinate projCoordinate = Utils.wgs84ToCgcs2000(Double.parseDouble(pointCoordinate.get(0).toString()), Double.parseDouble(pointCoordinate.get(1).toString()));
        List newCoordinate = new ArrayList<>();
        newCoordinate.add(projCoordinate.x);
        newCoordinate.add(projCoordinate.y);
        newCoordinate.add(1);
        log.info("pointCoordinate" + pointCoordinate);
        log.info("newCoordinate" + newCoordinate);
        markerData.put("coordinate", newCoordinate);
        JSONObject jsonObject = new JSONObject();
        String geom = "POINT(" + pointCoordinate.get(0).toString() + " " + pointCoordinate.get(1).toString() + ")";
        jsonObject.put("marker", markerData);
        jsonObject.put("name", name);
        jsonObject.put("geom", geom);
        return jsonObject;
    }

    private JSONObject getPolygonData(Object marker) {
        JSONObject markerData = JSONObject.parseObject(JSON.toJSONString(marker));
        String name = markerData.getString("text");
        JSONArray coordinate = JSONArray.parseArray(markerData.get("coordinate").toString());
        ProjCoordinate projCoordinate = Utils.cgcs2000ToWgs84(Double.parseDouble(coordinate.get(0).toString()), Double.parseDouble(coordinate.get(1).toString()));
        String geom = "POINT(" + projCoordinate.x + " " + projCoordinate.y + ")";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("geom", geom);
        return jsonObject;
    }

    private JSONObject getLineData(JSONObject data) {
        String name = data.getString("name");
        JSONArray coordinates2 = JSONArray.parseArray(data.get("coordinates").toString());
        StringBuilder geom = new StringBuilder("LINESTRING(");
        for (int i = 0; i < coordinates2.size(); i++) {
            Object o = coordinates2.get(i);
            JSONArray coordinates1 = JSONArray.parseArray(JSON.toJSONString(o));
            ProjCoordinate projCoordinate = Utils.cgcs2000ToWgs84(Double.parseDouble(coordinates1.get(0).toString()), Double.parseDouble(coordinates1.get(1).toString()));
            if (i == coordinates2.size() - 1) {
                geom.append(projCoordinate.x).append(" ").append(projCoordinate.y);
            } else {
                geom.append(projCoordinate.x).append(" ").append(projCoordinate.y).append(",");
            }
        }
        geom.append(")");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("geom", geom.toString());
        return jsonObject;
    }


    @Override
    public boolean saveImport(DrawData drawData) {
        JSONObject data = drawData.getData();
        drawData.setId(data.getString("id"))
                .setDeleteFlag(0);
        if (StringUtils.hasText(drawData.getType())) {
            //根据标绘类型获取坐标数据
            switch (drawData.getType()) {
                case "fbaj":
                case "car":
                case "police":
                    JSONObject carObject = getMarkerData(data.get("marker"));
                    drawData.setName(carObject.getString("name"));
                    drawData.setGeom(carObject.getString("geom"));
                    break;
                case "basic":
                    JSONObject jsonObject = getMarkerData(data.get("marker"));
                    drawData.setName(jsonObject.getString("name"));
                    drawData.setGeom(jsonObject.getString("geom"));
                    if (!StringUtils.hasText(drawData.getId())) {
                        Map<String, Object> map = getDistanceData(null, drawData.getSceneId(), jsonObject.getString("geom"));
                        drawData.setRefId(map.get("id").toString());
                        drawData.setPosition(map.get("start_dis").toString());
                    }
                    break;
                case "lines":
                case "cordon":
                    JSONObject lineObject = getLineData(data);
                    drawData.setName(lineObject.getString("name"));
                    drawData.setGeom(lineObject.getString("geom"));
                    break;
                case "uav":
                    JSONObject polygonObject = getPolygonData(data.get("marker"));
                    drawData.setName(polygonObject.getString("name"));
                    drawData.setGeom(polygonObject.getString("geom"));
                    break;
                default:
                    break;
            }
        }
        // 删除之前的标绘数据，防止重复导入
        this.update(new LambdaUpdateWrapper<DrawData>().set(DrawData::getDeleteFlag, 1).eq(DrawData::getSceneId, drawData.getSceneId()));
        // 清空场景的漫游数据
        sceneInfoService.update(new LambdaUpdateWrapper<SceneInfo>().set(SceneInfo::getKeyFrames, null).set(SceneInfo::getTime, 0)
                .set(SceneInfo::getThirdKeyFrames,null).set(SceneInfo::getThirdTime,0).eq(SceneInfo::getId, drawData.getSceneId()));

         return saveOrUpdate(drawData);
    }

    @Override
    public boolean saveDrawData(DrawData drawData) {
        JSONObject data = drawData.getData();
        drawData.setId(data.getString("id"))
                .setDeleteFlag(0);
        if (StringUtils.hasText(drawData.getType())) {
            //根据标绘类型获取坐标数据
            switch (drawData.getType()) {
                case "fbaj":
                case "car":
                case "police":
                    JSONObject carObject = getMarkerData(data.get("marker"));
                    drawData.setName(carObject.getString("name"));
                    drawData.setGeom(carObject.getString("geom"));
                    break;
                case "basic":
                    JSONObject jsonObject = getMarkerData(data.get("marker"));
                    drawData.setName(jsonObject.getString("name"));
                    drawData.setGeom(jsonObject.getString("geom"));
                    if (!StringUtils.hasText(drawData.getId())) {
                        Map<String, Object> map = getDistanceData(null, drawData.getSceneId(), jsonObject.getString("geom"));
                        drawData.setRefId(map.get("id").toString());
                        drawData.setPosition(map.get("start_dis").toString());
                    }
                    break;
                case "lines":
                case "cordon":
                    JSONObject lineObject = getLineData(data);
                    drawData.setName(lineObject.getString("name"));
                    drawData.setGeom(lineObject.getString("geom"));
                    break;
                case "uav":
                    JSONObject polygonObject = getPolygonData(data.get("marker"));
                    drawData.setName(polygonObject.getString("name"));
                    drawData.setGeom(polygonObject.getString("geom"));
                    break;
                default:
                    break;
            }
        }
        return saveOrUpdate(drawData);
    }

    @Override
    public boolean importDrawData(DrawData drawData) {
        return batchImportDrawData(List.of(drawData));
    }

    public DrawData updatePoliceDrawData(DrawData drawData, List coordinate, String weizhi, String type) {
        JSONObject data = drawData.getData();
        String id = IdWorker.get32UUID();
        data.put("id", id);
        drawData.setId(id)
                .setDeleteFlag(0);
        if (StringUtils.hasText(drawData.getType())) {
            switch (drawData.getType()) {
                case "fbaj":
                case "car":
                case "police":
                    JSONObject carObject = getMarkerData(data.get("marker"), coordinate);
                    drawData.setName(carObject.getString("name"));
                    drawData.setGeom(carObject.getString("geom"));
                    JSONObject marker = carObject.getJSONObject("marker");
                    marker.put("id", id);
                    data.put("marker", marker);
                    JSONObject info = data.getJSONObject("info");
                    info.put("weizhi", weizhi + "公里" + type + "位置");
                    data.put("info", info);
                    break;
                case "uav":
                    JSONObject polygonObject = getPolygonData(data.get("marker"), coordinate);
                    drawData.setName(polygonObject.getString("name"));
                    drawData.setGeom(polygonObject.getString("geom"));
                    JSONObject markerObj = polygonObject.getJSONObject("marker");
                    markerObj.put("id", id);
                    data.put("marker", markerObj);
                    JSONObject info1 = data.getJSONObject("info");
                    info1.put("weizhi", weizhi);
                    data.put("info", info1);
                    break;
                default:
                    break;
            }
        }
        drawData.setData(data);
        return drawData;
    }

    @Override
    public boolean saveLineSort(List<DrawData> drawDataList) {
        return saveOrUpdateBatch(drawDataList);
    }

    @Override
    public boolean batchImportDrawData(@NonNull List<DrawData> drawDataList) {
        List<DrawData> list = new ArrayList<>();
        for (DrawData drawData : drawDataList) {
            JSONObject data = drawData.getData();
            String id = "erp" + UUID.randomUUID().toString().replaceAll("-", "");
            data.put("id", id);
            drawData.setId(id)
                    .setDeleteFlag(0);
            if (StringUtils.hasText(drawData.getType())) {
                //根据标绘类型获取坐标数据
                switch (drawData.getType()) {
                    case "fbaj":
                    case "car":
                    case "police":
                    case "basic":
                        JSONObject jsonObject = getMarkerData(data.get("marker"));
                        drawData.setName(jsonObject.getString("name"));
                        drawData.setGeom(jsonObject.getString("geom"));
                        JSONObject marker = JSON.parseObject(JSON.toJSONString(data.get("marker")));
                        marker.put("id", id);
                        data.put("marker", marker);
                        drawData.setData(data);
                        break;
                    case "lines":
                    case "cordon":
                        JSONObject lineObject = getLineData(data);
                        drawData.setName(lineObject.getString("name"));
                        drawData.setGeom(lineObject.getString("geom"));
                        break;
                    case "uav":
                        JSONObject polygonObject = getPolygonData(data.get("marker"));
                        drawData.setName(polygonObject.getString("name"));
                        drawData.setGeom(polygonObject.getString("geom"));
                        JSONObject markerObj = JSON.parseObject(JSON.toJSONString(data.get("marker")));
                        markerObj.put("id", id);
                        data.put("marker", markerObj);
                        drawData.setData(data);
                        break;
                    default:
                        break;
                }
            }
            list.add(drawData);
        }
        return saveBatch(list);
    }

    @Override
    public List<DrawData> getPoliceData(String taskId, String planNode) {
        //区分现场 住地警力部署
        return list(new LambdaQueryWrapper<DrawData>()
                .eq(DrawData::getDeleteFlag, 0)
                .eq(DrawData::getTaskId, taskId)
                .eq(DrawData::getPlanNode, planNode));
    }

    @Override
    public Map<String, Object> statisticalByPoliceTypeOfTaskAndPlanNode(String taskId, String planNode, String sceneType, String pointType) {
        return baseMapper.statisticalByPoliceTypeOfTaskAndPlanNode(taskId, planNode, sceneType, pointType);
    }

    @Override
    public Map<String, Object> statisticalByPoliceTypeOfScenePlanNode(String sceneId, String planNode, String sceneType) {
        return baseMapper.statisticalByPoliceTypeOfScenePlanNode(sceneId, planNode, sceneType);
    }

    @Override
    public List<Map<String, Object>> statisticalByPostOfScenePlanNode(String sceneId, String planNode) {
        return baseMapper.statisticalByPostOfScenePlanNode(sceneId, planNode);
    }

    @Override
    public List<Map<String, Object>> statisticalByPostOfScenePlanNode(String sceneId, String planNode, String lineName) {
        return baseMapper.statisticalByPostOfLine(sceneId, planNode, lineName);
    }

    @Override
    public Map<String, Object> statisticalOfPoliceType(String sceneId, String planNode, String lineName) {
        return baseMapper.statisticalByPoliceType(sceneId, planNode, lineName);
    }

    @Override
    public List<Map<String, Object>> getDefenseLinePoliceDetailOfScenePlanNode(String sceneId, String planNode, String lineName) {
        return baseMapper.getDefenseLinePoliceDetailOfScenePlanNode(sceneId, planNode, lineName);
    }

    @Override
    public List<Map<String, Object>> getDefenseLinePoliceDataOfScenePlanNode(String sceneId, String planNode, String lineName, String group) {
        return baseMapper.getDefenseLinePoliceDataOfScenePlanNode(sceneId, planNode, lineName, group);
    }

    @Override
    public List<Map<String, Object>> getPlacePoliceDetailOfScenePlanNode(String sceneId, String planNode) {
        return baseMapper.getPlacePoliceDetailOfScenePlanNode(sceneId, planNode);
    }

    @Override
    public List<Map<String, Object>> getPoliceDetailOfScenePlanNode(String sceneId, String planNode) {
        return baseMapper.getPoliceDetailOfScenePlanNode(sceneId, planNode);
    }

    @Override
    public List<Map<String, Object>> getAllPoliceDetailOfScenePlanNode(String sceneId, String planNode) {
        return baseMapper.getAllPoliceDetailOfScenePlanNode(sceneId, planNode);
    }

    @Override
    public List<Map<String, Object>> getLineOfScenePlanNode(String sceneId, String planNode) {
        return baseMapper.getLineOfScenePlanNode(sceneId, planNode);
    }

    @Override
    public Integer getPoliceDataNumOfTask(String taskId) {
        return baseMapper.getPoliceDataNumOfTask(taskId);
    }

    @Override
    public List<Map> getScenePoliceTypeDataOfTask(String taskId) {
        return baseMapper.getScenePoliceTypeDataOfTask(taskId);
    }

    @Override
    public List<Map> getPoliceTypeDataOfTask(String taskId) {
        return baseMapper.getPoliceTypeDataOfTask(taskId);
    }

    @Override
    public List<Map> getPostPoliceDataOfTask(String taskId) {
        return baseMapper.getPostPoliceDataOfTask(taskId);
    }

    @Override
    public List<Map> getPostPoliceDataOfScene(String sceneId) {
        return baseMapper.getPostPoliceDataOfScene(sceneId);
    }

    @Override
    public List<DrawData> getDrawDataList(DrawData drawData) {
        return baseMapper.getDrawDataList(drawData);
    }

    @Override
    public List<DrawData> getLineList(DrawData drawData) {
        return baseMapper.getLineList(drawData);
    }

    @Override
    public List<DrawData> getDrawDataListOfBuilding(String buildName) {
        return baseMapper.getDrawDataListOfBuilding(buildName);
    }

    @Override
    public Map getDistanceData(String taskId, String sceneId, String point) {
        return baseMapper.getDistanceData(null, sceneId, point);
    }

    @Override
    public List<String> getPoliceGroup(String sceneId) {
        return baseMapper.getPoliceGroup(sceneId);
    }

    @Override
    public boolean updatePoliceGroup(JSONObject jsonObject) {
        String ids = jsonObject.getString("ids");
        String groupName = jsonObject.getString("group");
        String groupDesc = jsonObject.getString("groupDesc");
        String[] idArray = ids.split(",");
        List<DrawData> drawDataList = list(new LambdaQueryWrapper<DrawData>().in(DrawData::getId, List.of(idArray)));
        drawDataList.forEach(drawData -> {
            JSONObject data = drawData.getData();
            JSONObject info = data.getJSONObject("info");
            info.put("group", groupName);
            info.put("groupDesc", groupDesc);
            data.put("info", info);
            drawData.setData(data);
        });


        return updateBatchById(drawDataList);
    }

    @Override
    public List<Map<String, String>> getGroupOfLine(String sceneId, String planNode, String lineName) {
        return baseMapper.getGroupOfLine(sceneId, planNode, lineName);
    }

    @Override
    public Map<String, Object> statisticalPoliceType(String sceneId, String planNode, String lineName, String group) {
        return baseMapper.statisticalPoliceType(sceneId, planNode, lineName, group);
    }

    @Override
    public List<Map<String, Object>> statisticalByPostOfGroup(String sceneId, String planNode, String lineName, String group) {
        return baseMapper.statisticalByPostOfGroup(sceneId, planNode, lineName, group);
    }

    @Override
    public int getPoliceNumOfGroup(String sceneId, String planNode, String lineName, String group) {
        return baseMapper.getPoliceNumOfGroup(sceneId, planNode, lineName, group);
    }

    @Override
    public List<Map<String, Object>> getEmergencyPoliceOfPosition(String sceneId) {
        return baseMapper.getEmergencyPoliceOfPosition(sceneId);
    }


}

