package com.xaxc.teqin.service;

import com.alibaba.fastjson.JSONObject;
import com.xaxc.teqin.model.dto.TaskDTO;
import com.xaxc.teqin.model.entity.Task;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xaxc.teqin.model.entity.TaskArchivesData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 任务表 服务类
 * </p>
 *
 * @author author
 * @since 2024-06-21
 */
public interface ITaskService extends IService<Task> {

    boolean copyTask(String id);

    TaskDTO getTaskById(String id);

    List<Task> getList(Task task);


    TaskDTO getTaskDetail(String id);


    /**
     * 统计任务数量
     *
     * @return
     */
    Map<String, Object> statisticalQuantity();


    Map<String, Object> statisticalTotalAndNumOfLevel();

    /**
     * 统计执行中任务的等级分类
     *
     * @return
     */
    List<Map<String, Object>> statisticalExecutingLevel();

    /**
     * 统计所有等级
     *
     * @return
     */
    List<Map<String, Object>> statisticalAllLevel();

    /**
     * 统计已执行任务的等级分类
     *
     * @return
     */
    List<Map<String, Object>> statisticalExecutedLevel();

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

    /**
     * 获取资源树
     *
     * @return
     */
    List getTaskResourceTree();


    List getTaskResourceTree(String taskId);


    List getTaskResourceTree2(String taskId);


    List getTaskResourceTreeNew(String taskId);

    /**
     * 获取日常安排
     *
     * @param taskId
     * @return
     */
    List getTimelineOfTask(String taskId);

    /**
     * 日常安排
     *
     * @param taskId
     * @return
     */
    JSONObject getDailyScheduleOfTask(String taskId);

    JSONObject getDailyScheduleOfTask2(String taskId);


    List<JSONObject> getRelatedFeatureDataOfTask(String taskId, String type);

    /**
     * 获取警力部署
     *
     * @param taskId
     * @return
     */
    JSONObject getPoliceDataOfTask(String taskId);

    /**
     * 获取任务规划基础信息
     *
     * @param taskId
     * @return
     */
    JSONObject getBasicPlanOfTask(String taskId);


    /**
     * 获取任务下的标绘数据
     *
     * @param taskId
     * @return
     */
    List getDrawDataOfTask(String taskId);


    List getSceneDrawDataOfTask(String taskId);


    List getBasicDataOfTask(String taskId);


    List getRomaDataOfTask(String taskId);

    /**
     * 警力部署（驾驶舱）
     *
     * @param taskId
     * @return
     */
    JSONObject getPolicePresenceOfTask(String taskId);

    /**
     * 获取哨位警力部署（一张图）
     */
    List<Map<String, Object>> getPostPoliceDataOfTask(String taskId);

    /**
     * 获取应急力量（一张图）
     */
    List<Map<String, Object>> getEmergencyForcesOfTask(String taskId);

    /**
     * 获取路线安排（一张图）
     */
    List<Map<String, Object>> getRoadPlanOfTask(String taskId);

    void uploadArchives(MultipartFile file, String businessId, String fileName);

    void deleteArchives(String businessId);

    List<TaskArchivesData> getArchivesData(String businessId);

}
