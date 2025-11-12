package com.xaxc.teqin.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xaxc.teqin.model.dto.SceneDataDTO;
import com.xaxc.teqin.model.entity.ScenePlan;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 场景规划 服务类
 * </p>
 *
 * @author author
 * @since 2024-09-10
 */
public interface IScenePlanService extends IService<ScenePlan> {

    boolean saveScenePlan(ScenePlan plan);

    List<ScenePlan> getScenePlanNode(String sceneId, String planNode);

    List<ScenePlan> getList(String sceneId,List<String> excludePlanNodes);

}
