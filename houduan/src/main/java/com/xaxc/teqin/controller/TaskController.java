package com.xaxc.teqin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.model.PageCondition;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.entity.SceneInfo;
import com.xaxc.teqin.model.entity.Task;
import com.xaxc.teqin.service.ISceneInfoService;
import com.xaxc.teqin.service.ITaskService;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 任务表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-06-21
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @Resource
    private ITaskService taskService;

    @Resource
    private ISceneInfoService sceneInfoService;

    /**
     * 新增任务
     *
     * @param task
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody Task task) {
        String id = UUID.randomUUID().toString();
        task.setId(id);
        return taskService.save(task) ? ResponseResult.success(id) : ResponseResult.error("operation failed");
    }

    /**
     * 复制任务
     *
     * @param id
     * @return
     */
    @GetMapping("/copy")
    public ResponseResult copy(@RequestParam("id") String id) {
        return ResponseResult.success(taskService.copyTask(id));
    }

    /**
     * 编辑任务
     *
     * @param task
     * @return
     */
    @PostMapping("/update")
    public ResponseResult update(@RequestBody Task task) {
        Assert.hasText(task.getId(), "ID不能为空");
        return taskService.update(task, new LambdaQueryWrapper<Task>().eq(Task::getId, task.getId())) ? ResponseResult.success() : ResponseResult.error("operation failed");
    }


    /**
     * 删除任务
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public ResponseResult delete(@RequestParam("id") String id) {
        boolean flag = taskService.update(new LambdaUpdateWrapper<Task>().set(Task::getDeleteFlag, 1).eq(Task::getId, id));
        if (flag) {
            sceneInfoService.update(new LambdaUpdateWrapper<SceneInfo>().set(SceneInfo::getDeleteFlag, 1).eq(SceneInfo::getTaskId, id));
        }
        return flag ? ResponseResult.success() : ResponseResult.error("operation failed");
    }

    /**
     * 任务列表
     *
     * @param task
     * @return
     */
    @PostMapping("/getList")
    public ResponseResult getList(@RequestBody Task task) {
        return ResponseResult.success(taskService.getList(task));
    }

    /**
     * 任务详情
     *
     * @param id
     * @return
     */
    @GetMapping("/getById")
    public ResponseResult getById(@RequestParam("id") String id) {
        return ResponseResult.success(taskService.getTaskById(id));
    }

    /**
     * 任务详情（含统计）
     *
     * @param id
     * @return
     */
    @GetMapping("/getTaskDetail")
    public ResponseResult getTaskDetail(@RequestParam("id") String id) {
        return ResponseResult.success(taskService.getTaskDetail(id));
    }


    /**
     * 任务分页
     *
     * @param pageCondition
     * @return
     */
    @PostMapping("/getPage")
    public ResponseResult<Page<Task>> getPage(@RequestBody PageCondition<Task> pageCondition) {
        Page<Task> page = pageCondition.getPage();
        Task task = pageCondition.getEntity();
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(task.getCreateId())) {
            queryWrapper.eq(Task::getCreateId, task.getCreateId());
        }
        if (StringUtils.hasText(task.getTaskName())) {
            queryWrapper.like(Task::getTaskName, task.getTaskName());
        }
        if (StringUtils.hasText(task.getTaskLevel())) {
            queryWrapper.like(Task::getTaskLevel, task.getTaskLevel());
        }
        queryWrapper.eq(Task::getDeleteFlag, 0);
        queryWrapper.orderByDesc(Task::getCreateTime);

        return ResponseResult.success(taskService.page(page, queryWrapper));
    }

    /**
     * 统计任务数量
     *
     * @return
     */
    @GetMapping("/statisticalQuantity")
    public ResponseResult statisticalQuantity() {
        return ResponseResult.success(taskService.statisticalQuantity());
    }

    /**
     * 统计任务数量
     *
     * @return
     */
    @GetMapping("/statisticalTotalAndNumOfLevel")
    public ResponseResult statisticalTotalAndNumOfLevel() {
        return ResponseResult.success(taskService.statisticalTotalAndNumOfLevel());
    }

    /**
     * 统计执行中任务的等级分类
     *
     * @return
     */
    @GetMapping("/statisticalExecutingLevel")
    public ResponseResult statisticalExecutingLevel() {
        return ResponseResult.success(taskService.statisticalExecutingLevel());
    }

    /**
     * 统计已执行任务的等级分类
     *
     * @return
     */
    @GetMapping("/statisticalExecutedLevel")
    public ResponseResult statisticalExecutedLevel() {
        return ResponseResult.success(taskService.statisticalExecutedLevel());
    }

    /**
     * 统计所有任务的等级分类
     *
     * @return
     */
    @GetMapping("/statisticalAllLevel")
    public ResponseResult statisticalAllLevel() {
        return ResponseResult.success(taskService.statisticalAllLevel());
    }

    /**
     * 获取已执行的任务列表
     *
     * @return
     */
    @GetMapping("/getExecutedList")
    public ResponseResult getExecutedList() {
        return ResponseResult.success(taskService.getExecutedList());
    }

    /**
     * 获取指定中的任务列表
     *
     * @return
     */
    @GetMapping("/getExecutingList")
    public ResponseResult getExecutingList() {
        return ResponseResult.success(taskService.getExecutingList());
    }

    /**
     * 获取任务资源树
     *
     * @return
     */
    @GetMapping("/getTaskResourceTree")
    public ResponseResult getTaskResourceTree() {
        return ResponseResult.success(taskService.getTaskResourceTree());
    }

    /**
     * 获取任务时间线数据
     *
     * @param taskId
     * @return
     */
    @GetMapping("/getTimelineOfTask")
    public ResponseResult getTimelineOfTask(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(taskService.getTimelineOfTask(taskId));
    }

    @GetMapping("/getDailyScheduleOfTask")
    public ResponseResult getDailyScheduleOfTask(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(taskService.getDailyScheduleOfTask(taskId));
    }

    @GetMapping("/getDailyScheduleOfTask2")
    public ResponseResult getDailyScheduleOfTask2(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(taskService.getDailyScheduleOfTask2(taskId));
    }

    /**
     * 获取指定场景类型的要素数据
     *
     * @param taskId
     * @param sceneType
     * @return
     */
    @GetMapping("/getRelatedFeatureDataOfTask")
    public ResponseResult getRelatedFeatureDataOfTask(@RequestParam("taskId") String taskId, @RequestParam("sceneType") String sceneType) {
        return ResponseResult.success(taskService.getRelatedFeatureDataOfTask(taskId, sceneType));
    }


    /**
     * 获取任务下警力数据
     *
     * @param taskId
     * @return
     */
    @GetMapping("/getPoliceDataOfTask")
    public ResponseResult getPoliceDataOfTask(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(taskService.getPoliceDataOfTask(taskId));
    }

    /**
     * 获取任务下警力数据(最新)
     *
     * @param taskId
     * @return
     */
    @GetMapping("/getPolicePresenceOfTask")
    public ResponseResult getPolicePresenceOfTask(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(taskService.getPolicePresenceOfTask(taskId));
    }

    /**
     * 获取任务基本规划数据
     *
     * @param taskId
     * @return
     */
    @GetMapping("/getBasicPlanOfTask")
    public ResponseResult getBasicPlanOfTask(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(taskService.getBasicPlanOfTask(taskId));
    }


    /**
     * 获取任务下标绘数据
     *
     * @param taskId
     * @return
     */
    @GetMapping("/getDrawDataOfTask")
    public ResponseResult getDrawDataOfTask(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(taskService.getDrawDataOfTask(taskId));
    }

    /**
     * 获取任务下地图上的标绘数据
     *
     * @param taskId
     * @return
     */
    @GetMapping("/getScenePlotDataOfTask")
    public ResponseResult getSceneDrawDataOfTask(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(taskService.getSceneDrawDataOfTask(taskId));
    }


    /**
     * 获取任务下基礎数据
     *
     * @param taskId
     * @return
     */
    @GetMapping("/getBasicDataOfTask")
    public ResponseResult getBasicDataOfTask(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(taskService.getBasicDataOfTask(taskId));
    }

    /**
     * 获取任务下漫遊数据
     *
     * @param taskId
     * @return
     */
    @GetMapping("/getRomaDataOfTask")
    public ResponseResult getRomaDataOfTask(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(taskService.getRomaDataOfTask(taskId));
    }

    /**
     * 获取任务资源树
     *
     * @return
     */
    @GetMapping("/getTaskResources")
    public ResponseResult getTaskResources(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(taskService.getTaskResourceTreeNew(taskId));
    }

    /**
     * 获取哨位警力（一张图）
     *
     * @param taskId
     * @return
     */
    @GetMapping("/getPostPoliceDataOfTask")
    public ResponseResult getPostPoliceDataOfTask(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(taskService.getPostPoliceDataOfTask(taskId));
    }

    /**
     * 获取应急力量（一张图）
     */
    @GetMapping("/getEmergencyForcesOfTask")
    public ResponseResult getEmergencyForcesOfTask(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(taskService.getEmergencyForcesOfTask(taskId));
    }

    /**
     * 获取路线安排（一张图）
     */
    @GetMapping("/getRoadPlanOfTask")
    public ResponseResult getRoadPlanOfTask(@RequestParam("taskId") String taskId) {
        return ResponseResult.success(taskService.getRoadPlanOfTask(taskId));
    }

    /**
     * 上传电子档案
     *
     * @param file
     * @param businessId
     * @return
     */
    @PostMapping("/uploadArchives")
    public ResponseResult uploadArchives(@RequestParam("file") MultipartFile file, @RequestParam("businessId") String businessId) {
        String fileName = file.getOriginalFilename().split("\\.")[0];
        taskService.uploadArchives(file, businessId ,fileName);
        return ResponseResult.success();
    }

    /**
     * 获取档案数据
     *
     * @param businessId
     * @return
     */
    @GetMapping("/getArchivesData")
    public ResponseResult getArchivesData(@RequestParam("businessId") String businessId) {
        return ResponseResult.success(taskService.getArchivesData(businessId));
    }

    /**
     * 删除档案
     *
     * @param businessId
     * @return
     */
    @GetMapping("/deleteArchives")
    public ResponseResult deleteArchives(@RequestParam("businessId") String businessId) {
        taskService.deleteArchives(businessId);
        return ResponseResult.success();
    }

}
