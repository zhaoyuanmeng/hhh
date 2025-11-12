package com.xaxc.teqin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xaxc.teqin.model.entity.TaskPlan;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 任务规划 服务类
 * </p>
 *
 * @author author
 * @since 2024-09-10
 */
public interface ITaskPlanService extends IService<TaskPlan> {

    boolean saveTaskPlan(TaskPlan plan);

    TaskPlan getTaskPlanNode(String taskId, String planNode);

    List<TaskPlan> getTaskPlan(String taskId);


}
