package com.xaxc.teqin.model.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;

import com.xaxc.teqin.handler.JsonbTypeHandler;
import com.xaxc.teqin.model.dto.SceneDataDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName(value = "task_plan", autoResultMap = true)
public class TaskPlan implements Serializable {

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
     * 任务ID
     */
    @TableField("task_id")
    private String taskId;

    @TableField(exist = false)
    private int total;

    @TableField(exist = false)
    private List<SceneDataDTO> sceneDataList;

}
