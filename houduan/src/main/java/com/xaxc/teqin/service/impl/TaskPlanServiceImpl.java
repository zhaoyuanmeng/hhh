package com.xaxc.teqin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaxc.teqin.mapper.TaskPlanMapper;
import com.xaxc.teqin.model.dto.SceneDataDTO;
import com.xaxc.teqin.model.entity.TaskPlan;
import com.xaxc.teqin.service.ISceneInfoService;
import com.xaxc.teqin.service.ITaskPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 任务规划 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-09-10
 */
@Service
public class TaskPlanServiceImpl extends ServiceImpl<TaskPlanMapper, TaskPlan> implements ITaskPlanService {

    @Resource
    ISceneInfoService sceneInfoService;

    @Override
    public boolean saveTaskPlan(TaskPlan plan) {
        TaskPlan taskPlan = getOne(new LambdaQueryWrapper<TaskPlan>()
                .eq(TaskPlan::getTaskId, plan.getTaskId())
                .eq(TaskPlan::getPlanNode, plan.getPlanNode()), false);
        if (taskPlan != null) {
            plan.setId(taskPlan.getId());
        }
        return saveOrUpdate(plan);
    }

    @Override
    public TaskPlan getTaskPlanNode(String taskId, String planNode) {
        TaskPlan taskPlan = getOne(new LambdaQueryWrapper<TaskPlan>()
                .eq(TaskPlan::getTaskId, taskId)
                .eq(TaskPlan::getPlanNode, planNode), false);
        if (taskPlan == null) {
            taskPlan = new TaskPlan();
        }
        List<SceneDataDTO> scenePlanList = sceneInfoService.getScenePlanNodeByTaskId(taskId, planNode);
        taskPlan.setTotal(scenePlanList.size());
        taskPlan.setSceneDataList(scenePlanList);
        return taskPlan;
    }

    @Override
    public List<TaskPlan> getTaskPlan(String taskId) {
        return list(new LambdaQueryWrapper<TaskPlan>().eq(TaskPlan::getTaskId, taskId));
    }

}
