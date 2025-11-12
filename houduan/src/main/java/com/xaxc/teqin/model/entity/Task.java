package com.xaxc.teqin.model.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xaxc.teqin.common.model.BaseEntity;
import com.xaxc.teqin.handler.JsonbTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 任务表
 * </p>
 *
 * @author author
 * @since 2024-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("task")
public class Task extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务描述
     */
    private String taskDesc;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime taskStartTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime taskEndTime;

    /**
     * 责任人
     */
    private String head;

    /**
     * 任务等级
     */
    private String taskLevel;


    /**
     * 联系电话
     */
    private String phone;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private String createId;

    @JsonIgnore
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    private Integer deleteFlag;

    @TableField(value = "view_data", typeHandler = JsonbTypeHandler.class)
    private JSONObject viewData;


    @TableField(exist = false)
    private String  fileName;


    @TableField(exist = false)
    private String  fileUrl;

}
