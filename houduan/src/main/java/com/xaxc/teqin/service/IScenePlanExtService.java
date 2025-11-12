package com.xaxc.teqin.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xaxc.teqin.model.entity.ScenePlan;
import com.xaxc.teqin.model.entity.ScenePlanExt;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 任务规划 服务类
 * </p>
 *
 * @author author
 * @since 2024-09-20
 */
public interface IScenePlanExtService extends IService<ScenePlanExt> {

    boolean saveScenePlanExt(ScenePlanExt planExt);

    JSONObject getPoliceStatisticsOfScenePlanNode(String sceneId, String planNode);

    List<Map<String, Object>> getDefenseLinePoliceDetailOfScenePlanNode(String sceneId, String planNode, String lineName);

    List<Map<String, Object>> getAllPoliceDetailOfScenePlanNode(String sceneId, String planNode);

    List<ScenePlanExt> getScenePlanNodeExt(String sceneId, String planNode);

    ScenePlanExt getScenePlanNodeExtOfLine(String sceneId, String planNode, String lineName);
}
