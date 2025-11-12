package com.xaxc.teqin.mapper;

import com.xaxc.teqin.model.entity.TaskData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-01-06
 */
public interface TaskDataMapper extends BaseMapper<TaskData> {
    Map<String, Integer> statisticsByType(String year);

    List<Map<String, Integer>> statisticsByYear();
}
