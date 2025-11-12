package com.xaxc.teqin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.entity.ScenePlan;
import com.xaxc.teqin.service.IScenePlanExtService;
import com.xaxc.teqin.service.IScenePlanService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 场景规划 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-09-10
 */
@RestController
@RequestMapping("/scene-plan")
public class ScenePlanController {

    @Resource
    IScenePlanService scenePlanService;

    @Resource
    IScenePlanExtService scenePlanExtService;

    /**
     * 保存规划节点数据
     *
     * @param plan
     * @return
     */
    @PostMapping("/save")
    public ResponseResult save(@RequestBody ScenePlan plan) {
        Assert.hasText(plan.getSceneId(), "sceneId不能为空");
        Assert.hasText(plan.getPlanNode(), "planNode不能为空");
        return scenePlanService.saveScenePlan(plan) ? ResponseResult.success() : ResponseResult.error("操作失败");
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public ResponseResult delete(@RequestParam("id") String id) {
        return ResponseResult.success(scenePlanService.removeById(id));
    }

    /**
     * 获取场景下节点数据
     *
     * @param sceneId
     * @param planNode
     * @return
     */
    @GetMapping("/getScenePlanNode")
    public ResponseResult getTaskPlanNode(@RequestParam("sceneId") String sceneId, @RequestParam("planNode") String planNode) {
        return ResponseResult.success(scenePlanService.getScenePlanNode(sceneId, planNode));
    }

    /**
     * 获取场景规划数据
     *
     * @param sceneId
     * @param excludePlanNodes
     * @return
     */
    @GetMapping("/getList")
    public ResponseResult getList(@RequestParam("sceneId") String sceneId, @RequestParam(value = "excludePlanNodes", required = false) List excludePlanNodes) {
        return ResponseResult.success(scenePlanService.getList(sceneId, excludePlanNodes));
    }


    /**
     * 获取场景下节点警力统计数据
     *
     * @param sceneId
     * @param planNode
     * @return
     */
    @GetMapping("/getPoliceStatisticsOfScenePlanNode")
    public ResponseResult getPoliceStatisticsOfScenePlanNode(@RequestParam("sceneId") String sceneId, @RequestParam("planNode") String planNode) {
        return ResponseResult.success(scenePlanExtService.getPoliceStatisticsOfScenePlanNode(sceneId, planNode));
    }

    /**
     * 获取指定节点下指定防线警力部署数据
     *
     * @param sceneId
     * @param planNode
     * @param lineName
     * @return
     */
    @GetMapping("/getDefenseLinePoliceDetailOfScenePlanNode")
    public ResponseResult getDefenseLinePoliceDetailOfScenePlanNode(@RequestParam("sceneId") String sceneId, @RequestParam("planNode") String planNode, @RequestParam(value = "lineName", required = false) String lineName) {
        return ResponseResult.success(scenePlanExtService.getDefenseLinePoliceDetailOfScenePlanNode(sceneId, planNode, lineName));
    }

    @GetMapping("/getAllPoliceDetailOfScenePlanNode")
    public ResponseResult getAllPoliceDetailOfScenePlanNode(@RequestParam("sceneId") String sceneId, @RequestParam("planNode") String planNode) {
        return ResponseResult.success(scenePlanExtService.getAllPoliceDetailOfScenePlanNode(sceneId, planNode));
    }
}
