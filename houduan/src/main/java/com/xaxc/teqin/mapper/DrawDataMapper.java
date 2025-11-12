package com.xaxc.teqin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaxc.teqin.model.entity.DrawData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标绘数据 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-09-04
 */
public interface DrawDataMapper extends BaseMapper<DrawData> {

    Map<String, Object> getDistanceData(@Param("taskId") String taskId,@Param("sceneId") String sceneId, @Param("point") String point);

    List<Map<String, Object>> getPoliceStatisticsOfScenePlanNode(@Param("sceneId") String sceneId, @Param("planNode") String planNode);

    List<Map<String, Object>> getPlacePoliceDetailOfScenePlanNode(@Param("sceneId") String sceneId, @Param("planNode") String planNode);

    List<Map<String, Object>> getPoliceDetailOfScenePlanNode(@Param("sceneId") String sceneId, @Param("planNode") String planNode);

    Integer getPoliceTotalOfScenePlanNode(@Param("sceneId") String sceneId, @Param("planNode") String planNode);

    Integer getEmergencyPolice(@Param("sceneId") String sceneId);

    List<Map<String,Object>> getEmergencyPoliceOfPosition(@Param("sceneId") String sceneId);

    List<Map<String, Object>> getLineOfScenePlanNode(@Param("sceneId") String sceneId, @Param("planNode") String planNode);

    /**
     * 场景警力部署明细 北门	游动哨	4
     *
     * @param sceneId
     * @param planNode
     * @param lineName
     * @return List
     */
    List<Map<String, Object>> getDefenseLinePoliceDetailOfScenePlanNode(@Param("sceneId") String sceneId, @Param("planNode") String planNode, @Param("lineName") String lineName);

    List<Map<String, Object>> getDefenseLinePoliceDataOfScenePlanNode(@Param("sceneId") String sceneId, @Param("planNode") String planNode, @Param("lineName") String lineName,@Param("group") String group);

    List<Map<String, Object>> getAllPoliceDetailOfScenePlanNode(@Param("sceneId") String sceneId, @Param("planNode") String planNode);

    Map<String, Object> statisticalByPoliceType(@Param("sceneId") String sceneId, @Param("planNode") String planNode, @Param("lineName") String lineName);

    Map<String, Object> statisticalByPoliceTypeOfScenePlanNode(@Param("sceneId") String sceneId, @Param("planNode") String planNode, @Param("sceneType") String sceneType);

    Map<String, Object> statisticalPoliceType(@Param("sceneId") String sceneId, @Param("planNode") String planNode,@Param("lineName") String lineName, @Param("group") String group);

    Map<String, Object> statisticalByPoliceTypeOfTaskAndPlanNode(@Param("taskId") String taskId, @Param("planNode") String planNode, @Param("sceneType") String sceneType,@Param("pointType") String pointType);

    List<Map<String, Object>> statisticalByPostOfScenePlanNode(@Param("sceneId") String sceneId, @Param("planNode") String planNode);

    List<Map<String, Object>> statisticalByPostOfLine(@Param("sceneId") String sceneId, @Param("planNode") String planNode,@Param("lineName") String lineName);

    List<Map<String, Object>> statisticalQuantity(@Param("sceneId") String sceneId);

    List<DrawData> getSceneDrawDataByTagId(@Param("tagId") String tagId);


    List<Map<String, Object>> statisticalPolice(@Param("taskId") String taskId, @Param("planNode") String planNode);


    List<Map<String, Integer>> getPoliceDataOfTask(@Param("taskId") String taskId);

    Integer getPoliceDataNumOfTask(@Param("taskId") String taskId);

    List<Map> getScenePoliceTypeDataOfTask(@Param("taskId") String taskId);

    List<Map> getPoliceTypeDataOfTask(@Param("taskId") String taskId);

    List<Map> getScenePoliceDataOfPosition(@Param("sceneId") String sceneId, @Param("planNode") String planNode);

    List<Map> getPostPoliceDataOfTask(@Param("taskId") String taskId);

    List<Map> getPostPoliceDataOfScene(@Param("sceneId") String sceneId);

    List<DrawData> getDrawDataList(@Param("dto") DrawData drawData);

    List<DrawData> getLineList(@Param("dto") DrawData drawData);

    List<DrawData> getDrawDataListOfBuilding(@Param("buildName") String buildName);

    String getLineInterpolatePoint(@Param("sceneId") String sceneId);

    List<String> getPoliceGroup(@Param("sceneId") String sceneId);

    List<Map<String,String>> getGroupOfLine(@Param("sceneId") String sceneId,@Param("planNode") String planNode,@Param("lineName") String lineName);

    List<Map<String, Object>> statisticalByPostOfGroup(@Param("sceneId") String sceneId, @Param("planNode") String planNode,@Param("lineName") String lineName, @Param("group") String group);

    int getPoliceNumOfGroup(@Param("sceneId") String sceneId, @Param("planNode") String planNode,@Param("lineName") String lineName, @Param("group") String group);
}
