package com.xaxc.teqin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.model.PageCondition;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.dto.OrgDTO;
import com.xaxc.teqin.model.entity.Task;
import com.xaxc.teqin.model.entity.TaskData;
import com.xaxc.teqin.service.ITaskDataService;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-01-06
 */
@RestController
@RequestMapping("/task-data")
public class TaskDataController {

    @Resource
    ITaskDataService taskDataService;

    /**
     * 新增任务
     *
     * @param taskData
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody TaskData taskData) {
        return taskDataService.add(taskData) ? ResponseResult.success() : ResponseResult.error("operation failed");
    }

    /**
     * 编辑任务
     *
     * @param taskData
     * @return
     */
    @PostMapping("/update")
    public ResponseResult update(@RequestBody TaskData taskData) {
        Assert.hasText(taskData.getId(), "ID不能为空");
        return taskDataService.update(taskData) ? ResponseResult.success() : ResponseResult.error("operation failed");
    }


    /**
     * 删除任务
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public ResponseResult delete(@RequestParam("id") String id) {
        return taskDataService.removeById(id) ? ResponseResult.success() : ResponseResult.error("operation failed");
    }

    /**
     * 获取机构列表
     *
     * @param taskData
     * @return
     */
    @PostMapping("/getList")
    public ResponseResult getList(@RequestBody TaskData taskData) {
        LambdaQueryWrapper<TaskData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(taskData.getYear())) {
            lambdaQueryWrapper.eq(TaskData::getYear, taskData.getYear());
        }
        lambdaQueryWrapper.orderByDesc(TaskData::getYear);
        return ResponseResult.success(taskDataService.list(lambdaQueryWrapper));
    }

    /**
     * 获取机构分页
     *
     * @param pageCondition
     * @return
     */
    @PostMapping("/getPage")
    public ResponseResult<Page<TaskData>> getPage(@RequestBody PageCondition<TaskData> pageCondition) {
        Page<TaskData> page = pageCondition.getPage();
        TaskData taskData = pageCondition.getEntity();
        LambdaQueryWrapper<TaskData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(taskData.getYear())) {
            lambdaQueryWrapper.eq(TaskData::getYear, taskData.getYear());
        }
        lambdaQueryWrapper.orderByDesc(TaskData::getYear);
        return ResponseResult.success(taskDataService.page(page, lambdaQueryWrapper));
    }

    @GetMapping("/statisticsByType")
    public ResponseResult statisticsOfType(@RequestParam(value = "year", required = false) String year) {
        return ResponseResult.success(taskDataService.statisticsByType(year));
    }


    @GetMapping("/statisticsByYear")
    public ResponseResult statisticsOfYear() {
        return ResponseResult.success(taskDataService.statisticsByYear());
    }
}
