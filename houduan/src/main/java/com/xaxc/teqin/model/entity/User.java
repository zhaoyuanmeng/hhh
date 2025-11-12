package com.xaxc.teqin.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xaxc.teqin.common.model.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@TableName(value = "sys_user")
@Data
public class User extends BaseEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @JsonProperty("realName")
    @TableField(value = "real_name")
    private String realName;

    @JsonProperty("userName")
    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "password")
    private String password;

    @JsonProperty("roleId")
    @TableField(value = "role_id")
    private String roleId;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "sex")
    private int sex;

    @JsonProperty("orgId")
    @TableField(value = "org_id")
    private String orgId;

    @TableField(value = "status")
    private int status;

    @JsonProperty("createTime")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonProperty("updateTime")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


    @JsonIgnore
    @TableField(value = "delete_flag")
    private int deleteFlag;

}
