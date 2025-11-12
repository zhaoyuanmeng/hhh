package com.xaxc.teqin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaxc.teqin.model.entity.TaskData;
import com.xaxc.teqin.mapper.TaskDataMapper;
import com.xaxc.teqin.service.ITaskDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-01-06
 */
@Service
public class TaskDataServiceImpl extends ServiceImpl<TaskDataMapper, TaskData> implements ITaskDataService {

    @Override
    public boolean add(TaskData taskData) {
        TaskData data = getOne(new LambdaQueryWrapper<TaskData>().eq(TaskData::getYear, taskData.getYear()), false);
        if (data != null) {
            throw new RuntimeException("该年份已经创建请选择其他年份");
        }
        return save(taskData);
    }

    @Override
    public boolean update(TaskData taskData) {
        TaskData data = getOne(new LambdaQueryWrapper<TaskData>()
                .eq(TaskData::getYear, taskData.getYear())
                .ne(TaskData::getId, taskData.getId()), false);
        if (data != null) {
            throw new RuntimeException("该年份已经创建请选择其他年份");
        }
        return updateById(taskData);
    }

    @Override
    public Map<String, Integer> statisticsByType(String year) {
        return baseMapper.statisticsByType(year);
    }

    @Override
    public List<Map<String, Integer>> statisticsByYear() {
        return baseMapper.statisticsByYear();
    }
}
