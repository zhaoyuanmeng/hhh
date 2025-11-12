package com.xaxc.teqin.service;

import com.xaxc.teqin.model.entity.TaskData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author author
 * @since 2025-01-06
 */
public interface ITaskDataService extends IService<TaskData> {

    boolean add(TaskData taskData);

    boolean update(TaskData taskData);

    Map<String,Integer> statisticsByType(String year);

    List<Map<String,Integer>> statisticsByYear();

}
