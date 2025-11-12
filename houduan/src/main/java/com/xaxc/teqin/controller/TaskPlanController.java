package com.xaxc.teqin.controller;


import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.entity.TaskPlan;
import com.xaxc.teqin.service.ITaskPlanService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 任务规划 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-09-10
 */
@RestController
@RequestMapping("/task-plan")
public class TaskPlanController {

    @Resource
    ITaskPlanService taskPlanService;

    /**
     * 保存规划节点数据
     *
     * @param plan
     * @return
     */
    @PostMapping("/save")
    public ResponseResult save(@RequestBody TaskPlan plan) {
        Assert.hasText(plan.getTaskId(), "任务ID不能为空");
        Assert.hasText(plan.getPlanNode(), "规划节点不能为空");
        return taskPlanService.saveTaskPlan(plan) ? ResponseResult.success() : ResponseResult.error("操作失败");
    }


    @GetMapping("/getTaskPlanNode")
    public ResponseResult getTaskPlanNode(@RequestParam("taskId") String taskId, @RequestParam("planNode") String planNode) {
        return ResponseResult.success(taskPlanService.getTaskPlanNode(taskId, planNode));
    }

    @GetMapping("/getTaskPlan")
    public ResponseResult getTaskPlan(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(taskPlanService.getTaskPlan(taskId));
    }

}
