package com.xaxc.teqin.model.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xaxc.teqin.handler.JsonbTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 任务规划
 * </p>
 *
 * @author author
 * @since 2024-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "scene_plan", autoResultMap = true)
public class ScenePlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 规划节点，指导思想，基本情况，组织领导、工作要求
     */
    @TableField("plan_node")
    private String planNode;

    /**
     * 规划节点数据
     */
    @TableField(value = "data", typeHandler = JsonbTypeHandler.class)
    private JSONObject data;

    /**
     * 场景ID
     */
    @TableField("scene_id")
    private String sceneId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private List<ScenePlanExt> extDataList;

    /**
     * 警力大类数据
     */
    @TableField(exist = false)
    private Map policeTypeStatistics;
    /**
     * 哨位数据
     */
    @TableField(exist = false)
    private List postStatistics;


    /**
     * 位置哨位数据
     */
    @TableField(exist = false)
    private List placeStatistics;
}
