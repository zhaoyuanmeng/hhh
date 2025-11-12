package com.xaxc.teqin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xaxc.teqin.mapper.ScenePlanExtMapper;
import com.xaxc.teqin.model.entity.ScenePlan;
import com.xaxc.teqin.model.entity.ScenePlanExt;
import com.xaxc.teqin.service.IDrawDataService;
import com.xaxc.teqin.service.IScenePlanExtService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 场景规划扩展 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-09-20
 */
@Service
public class ScenePlanExtServiceImpl extends ServiceImpl<ScenePlanExtMapper, ScenePlanExt> implements IScenePlanExtService {

    @Resource
    IDrawDataService drawDataService;

    @Override
    public boolean saveScenePlanExt(ScenePlanExt planExt) {
        return saveOrUpdate(planExt);
    }

    @Override
    public JSONObject getPoliceStatisticsOfScenePlanNode(String sceneId, String planNode) {
        JSONObject jsonObject = new JSONObject();
        List<Map<String, Object>> data = drawDataService.getPoliceStatisticsOfScenePlanNode(sceneId, planNode);
        int total = drawDataService.getPoliceTotalOfScenePlanNode(sceneId, planNode);
        jsonObject.put("total", total);
        jsonObject.put("detail", data);
        return jsonObject;
    }

    @Override
    public List<Map<String, Object>> getDefenseLinePoliceDetailOfScenePlanNode(String sceneId, String planNode, String lineName) {
        return drawDataService.getDefenseLinePoliceDetailOfScenePlanNode(sceneId, planNode, lineName);
    }

    @Override
    public List<Map<String, Object>> getAllPoliceDetailOfScenePlanNode(String sceneId, String planNode) {
        return drawDataService.getAllPoliceDetailOfScenePlanNode(sceneId, planNode);
    }

    @Override
    public List<ScenePlanExt> getScenePlanNodeExt(String sceneId, String planNode) {
        return baseMapper.getScenePlanNodeExt(sceneId, planNode);
    }

    @Override
    public ScenePlanExt getScenePlanNodeExtOfLine(String sceneId, String planNode, String lineName) {
        return baseMapper.getScenePlanNodeExtOfLine(sceneId, planNode, lineName);
    }
}
