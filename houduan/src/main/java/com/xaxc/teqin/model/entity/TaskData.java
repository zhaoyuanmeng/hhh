package com.xaxc.teqin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.xaxc.teqin.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("task_data")
public class TaskData  extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String year;

    private Integer onePlus;

    private Integer one;

    private Integer two;

    private Integer three;

    private Integer security;

    private Integer other;


}
