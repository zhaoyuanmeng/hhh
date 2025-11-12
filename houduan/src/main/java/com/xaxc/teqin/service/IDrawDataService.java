package com.xaxc.teqin.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xaxc.teqin.model.entity.DrawData;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标绘数据 服务类
 * </p>
 *
 * @author author
 * @since 2024-09-04
 */
public interface IDrawDataService extends IService<DrawData> {


    boolean saveImport( DrawData drawData);

    JSONObject getLineInterpolatePoint(String sceneId);

    DrawData getDetail(String id);

    List<Map<String, Object>> getPoliceStatisticsOfScenePlanNode(String sceneId, String planNode);

    Integer getPoliceTotalOfScenePlanNode(String sceneId, String planNode);

    Integer getEmergencyPolice(String sceneId);

    List<Map<String, Object>> statisticalQuantity(String sceneId);

    boolean saveDrawData(DrawData drawData);

    boolean importDrawData(DrawData drawData);

    boolean batchImportDrawData(List<DrawData> drawDataList);

    List<DrawData> getPoliceData(String taskId, String planNode);

    Map<String, Object> statisticalByPoliceTypeOfTaskAndPlanNode(String taskId, String planNode, String sceneType, String pointType);

    Map<String, Object> statisticalByPoliceTypeOfScenePlanNode(String sceneId, String planNode, String sceneType);

    List<Map<String, Object>> statisticalByPostOfScenePlanNode(String sceneId, String planNode);

    List<Map<String, Object>> statisticalByPostOfScenePlanNode(String sceneId, String planNode, String lineName);

    Map<String, Object> statisticalOfPoliceType(String sceneId, String planNode, String lineName);

    List<Map<String, Object>> getDefenseLinePoliceDetailOfScenePlanNode(String sceneId, String planNode, String lineName);

    List<Map<String, Object>> getDefenseLinePoliceDataOfScenePlanNode(String sceneId, String planNode, String lineName, String group);

    List<Map<String, Object>> getPlacePoliceDetailOfScenePlanNode(String sceneId, String planNode);

    List<Map<String, Object>> getPoliceDetailOfScenePlanNode(String sceneId, String planNode);

    List<Map<String, Object>> getAllPoliceDetailOfScenePlanNode(String sceneId, String planNode);

    List<Map<String, Object>> getLineOfScenePlanNode(String sceneId, String planNode);

    Integer getPoliceDataNumOfTask(String taskId);

    List<Map> getScenePoliceTypeDataOfTask(String taskId);

    List<Map> getPoliceTypeDataOfTask(String taskId);

    List<Map> getPostPoliceDataOfTask(String taskId);

    List<Map> getPostPoliceDataOfScene(String sceneId);

    List<DrawData> getDrawDataList(DrawData drawData);

    List<DrawData> getLineList(DrawData drawData);

    List<DrawData> getDrawDataListOfBuilding(String buildName);

    Map getDistanceData(String taskId, String sceneId, String point);

    List<String> getPoliceGroup(String sceneId);

    boolean updatePoliceGroup(JSONObject jsonObject);

    List<Map<String, String>> getGroupOfLine(String sceneId, String planNode, String lineName);

    Map<String, Object> statisticalPoliceType(String sceneId, String planNode, String lineName, String group);

    List<Map<String, Object>> statisticalByPostOfGroup(String sceneId, String planNode, String lineName, String group);

    int getPoliceNumOfGroup(String sceneId, String planNode, String lineName, String group);

    List<Map<String, Object>> getEmergencyPoliceOfPosition(String sceneId);

    DrawData updatePoliceDrawData(DrawData drawData, List coordinate, String weizhi, String type);

    boolean saveLineSort(List<DrawData> drawDataList);

}
