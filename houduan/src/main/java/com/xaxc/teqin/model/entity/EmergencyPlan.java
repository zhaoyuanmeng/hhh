package com.xaxc.teqin.model.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xaxc.teqin.handler.JsonbTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应急预案
 * </p>
 *
 * @author author
 * @since 2024-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "emergency_plan", autoResultMap = true)
public class EmergencyPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 基础数据ID
     */
    private String basicDataId;

    /**
     * 预案名称
     */
    private String name;

    /**
     * 预案类型
     */
    private String type;

    /**
     * 预案数据
     */
    @TableField(value = "data", typeHandler = JsonbTypeHandler.class)
    private JSONObject data;


    @JsonIgnore
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    private Integer deleteFlag;

    @TableField(exist = false)
    private List<DrawData> drawDataList;

}
