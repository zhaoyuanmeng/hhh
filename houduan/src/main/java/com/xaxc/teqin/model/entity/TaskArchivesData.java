package com.xaxc.teqin.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xaxc.teqin.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2025-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("task_archives_data")
public class TaskArchivesData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    private String parentId;

    private String businessId;

    private String title;

    private String content;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private List<TaskArchivesData> children;


}
