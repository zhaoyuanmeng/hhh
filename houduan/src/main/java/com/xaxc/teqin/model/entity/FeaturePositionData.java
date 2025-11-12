package com.xaxc.teqin.model.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xaxc.teqin.handler.JsonbTypeHandler;
import com.xaxc.teqin.handler.PgGeometryTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class FeaturePositionData implements Serializable {

    @TableField(exist = false, typeHandler = JsonbTypeHandler.class)
    private JSONObject geojson;

    @TableField(exist = false)
    private String weizhi;

    @TableField(exist = false)
    private String type;

}
