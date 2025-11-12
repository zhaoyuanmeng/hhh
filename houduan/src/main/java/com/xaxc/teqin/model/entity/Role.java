package com.xaxc.teqin.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xaxc.teqin.common.model.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@TableName(value = "sys_role")
public class Role extends BaseEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @TableField(value = "role_name")
    private String roleName;

    @TableField(value = "role_code")
    private String roleCode;

    @TableField(value = "resources")
    private String resources;

    @TableField(value = "remark")
    private String remark;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @JsonProperty("deleteFlag")
    @TableField(value = "delete_flag")
    private int deleteFlag;

}
