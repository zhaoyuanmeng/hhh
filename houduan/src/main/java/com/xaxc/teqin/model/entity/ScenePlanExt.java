package com.xaxc.teqin.model.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.xaxc.teqin.handler.JsonbTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务规划
 * </p>
 *
 * @author author
 * @since 2024-09-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "scene_plan_ext", autoResultMap = true)
public class ScenePlanExt implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 规划节点，警力部署、应急力量
     */
    private String planNode;

    /**
     * 数据
     */
    @TableField(value = "data", typeHandler = JsonbTypeHandler.class)
    private JSONObject data;

    /**
     * 场景ID
     */
    private String sceneId;


    /**
     * 按防线统计警力数据
     */
    @TableField(exist = false)
    private JSONObject policeData;
    /**
     * 按位置统计警力
     */
    @TableField(exist = false)
    private List placePoliceData;
    /**
     * 按警力类型统计警力
     */
    @TableField(exist = false)
    private JSONObject statisticalData;


}
