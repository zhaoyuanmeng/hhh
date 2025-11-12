package com.xaxc.teqin.model.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xaxc.teqin.handler.JsonbTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2025-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("task_statistics")
public class TaskStatistics implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private Integer total;
    @TableField(value = "level_data", typeHandler = JsonbTypeHandler.class)
    private JSONObject levelData;
    @TableField(value = "year_data", typeHandler = JsonbTypeHandler.class)
    private JSONObject yearData;


}
