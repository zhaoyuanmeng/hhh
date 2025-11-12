package com.xaxc.teqin.model.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xaxc.teqin.handler.JsonbTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 点位信息表
 * </p>
 *
 * @author author
 * @since 2024-07-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "point_info", autoResultMap = true)
public class PointInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("child_type_id")
    private String childTypeId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "data", typeHandler = JsonbTypeHandler.class)
    private JSONObject data;

    @TableField(exist = false)
    private Map dataFiledName;

    @TableField("jcxx_id")
    private String jcxxId;

    @TableField("parent_type_id")
    private String parentTypeId;

//    @JsonIgnore
//    @TableField(value = "geom", typeHandler = PgGeometryTypeHandler.class)
//    private String geom;

    /**
     * 点位类型 1 高速 2 高铁 3 现场 4 住地
     */
    @JsonIgnore
    @TableField(exist = false)
    private String type;

    /**
     * 重点点位数
     */
    @TableField(exist = false)
    private long pointNum;


    @TableField(exist = false)
    private String refLineName;


    @TableField(exist = false)
    private String position;

}
