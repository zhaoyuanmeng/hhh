package com.xaxc.teqin.mapper;

import com.xaxc.teqin.model.entity.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 任务表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-06-21
 */
public interface TaskMapper extends BaseMapper<Task> {

    List<Map<String, Object>> selectCountByYear();

    /**
     * 统计任务数量
     *
     * @return
     */
    Map<String, Object> statisticalQuantity();

    /**
     * 统计各等级任务数
     *
     * @return
     */
    List<Map<String, Object>> statisticalQuantityOfLevel();

    /**
     * 统计执行中任务的等级分类
     *
     * @return
     */
    List<Map<String, Object>> statisticalExecutingLevel();

    /**
     * 统计已执行任务的等级分类
     *
     * @return
     */
    List<Map<String, Object>> statisticalExecutedLevel();

    /**
     * 统计所有等级
     *
     * @return
     */
    List<Map<String, Object>> statisticalAllLevel();

    /**
     * 获取已执行的任务列表
     *
     * @return
     */
    List<Task> getExecutedList();

    /**
     * 获取指定中的任务列表
     *
     * @return
     */
    List<Task> getExecutingList();

}
