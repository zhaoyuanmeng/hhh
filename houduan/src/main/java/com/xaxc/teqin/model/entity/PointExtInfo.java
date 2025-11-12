package com.xaxc.teqin.model.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xaxc.teqin.handler.JsonbTypeHandler;
import com.xaxc.teqin.handler.PgGeometryTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "point_ext_info", autoResultMap = true)
public class PointExtInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 空间位置
     */
    @JsonIgnore
    @TableField(value = "geom", typeHandler = PgGeometryTypeHandler.class)
    private String geom;

    @TableField(exist = false, typeHandler = JsonbTypeHandler.class)
    private JSONObject geojson;

    @TableField(exist = false)
    private String parentTypeName;

    @TableField(exist = false)
    private String parentTypeId;

    @TableField(exist = false)
    private String childTypeId;

    @TableField(exist = false, typeHandler = JsonbTypeHandler.class)
    private JSONObject data;


    @TableField(exist = false)
    private String startDis;


    @TableField(exist = false)
    private String endDis;

}
