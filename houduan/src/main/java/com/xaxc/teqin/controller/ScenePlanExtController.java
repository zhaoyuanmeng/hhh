package com.xaxc.teqin.controller;


import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.entity.ScenePlan;
import com.xaxc.teqin.model.entity.ScenePlanExt;
import com.xaxc.teqin.service.IScenePlanExtService;
import com.xaxc.teqin.service.IScenePlanService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 场景规划 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-09-10
 */
@RestController
@RequestMapping("/scene-plan-ext")
public class ScenePlanExtController {

    @Resource
    IScenePlanExtService scenePlanExtService;

    /**
     * 保存规划节点扩展数据
     *
     * @param plan
     * @return
     */
    @PostMapping("/save")
    public ResponseResult save(@RequestBody ScenePlanExt plan) {
        Assert.hasText(plan.getSceneId(), "sceneId不能为空");
        Assert.hasText(plan.getPlanNode(), "planNode不能为空");
        return scenePlanExtService.saveScenePlanExt(plan) ? ResponseResult.success() : ResponseResult.error("操作失败");
    }

    @GetMapping("/getById")
    public ResponseResult getById(@RequestParam("id") String id) {
        return ResponseResult.success(scenePlanExtService.getById(id));
    }


}
