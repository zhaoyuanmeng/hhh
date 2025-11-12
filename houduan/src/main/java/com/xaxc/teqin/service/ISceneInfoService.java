package com.xaxc.teqin.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.dto.BasicDataDTO;
import com.xaxc.teqin.model.dto.DeployPoliceData;
import com.xaxc.teqin.model.dto.SceneDataDTO;
import com.xaxc.teqin.model.dto.SceneInfoDTO;
import com.xaxc.teqin.model.entity.DrawData;
import com.xaxc.teqin.model.entity.SceneInfo;
import com.xaxc.teqin.model.entity.ScenePlan;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 场景表 服务类
 * </p>
 *
 * @author author
 * @since 2024-07-24
 */
public interface ISceneInfoService extends IService<SceneInfo> {


    SceneInfo geSceneById(String id);

    SceneInfoDTO getDetail(String id);

    List<SceneInfoDTO> getDetailByTaskIdAndSceneName(String taskId, String sceneName);

    List<JSONObject> getSceneListByTaskIdAndSceneName(String taskId, String sceneName);

    ResponseResult<?> addScene(SceneInfo sceneInfo);

    ResponseResult<?> updateScene(SceneInfo sceneInfo);

    boolean deleteScene(String id);

    List<SceneInfo> getSimpleListByTaskIdAndType(String taskId, String type);

    List<SceneInfo> getListByTaskId(String taskId);

    JSONObject getBasicPlanOfScene(String sceneId);

    List<SceneDataDTO> getScenePlanNodeByTaskId(String taskId, String planNode);

    List<DrawData> getDrawDataOfScene(String sceneId);

    JSONObject getBasicDrawDataOfScene(List<String> sceneIds);

    List<Map> getPostPoliceDataOfScene(String sceneId);

    List<BasicDataDTO> getSceneResourceTree(List<String> sceneIds);

    int getSceneDangerData(String sceneId);

    JSONObject getDangerData(String sceneId);

    String getRoadName(List<DrawData> list);

    List<DrawData> getDrawLines(String sceneId);

    /**
     * 获取应急力量（一张图）
     */
    ScenePlan getEmergencyForcesOfScene(String sceneId);


    List<BasicDataDTO> getMultiSceneResourceTree(List<String> sceneIds);

    List<BasicDataDTO> querySceneResourcesByCondition( DrawData drawData);


    Map<String, Object> getSceneLineData(String sceneId);

    /**
     * 获取路线安排（一张图）
     */
    List<Map<String, Object>> getRoadPlanOfScene(String sceneId);

    List<SceneInfo> getSchemeList(SceneInfo sceneInfo);

    boolean addScheme(SceneInfo sceneInfo);

    boolean copyScheme(String id);

    boolean copyScene(String oldId, String newSceneId,String taskId);


    List<BasicDataDTO> getTaskResourceTree(String taskId);

    List<BasicDataDTO> getTaskResourceTreeNew(String taskId);


    Map getSchemeStatistics();

    List<JSONObject> getSceneTree(String type,String name);

    List<DrawData> deployPolice(DeployPoliceData deployPoliceData);

}
