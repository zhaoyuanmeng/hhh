package com.xaxc.teqin.model.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xaxc.teqin.handler.JsonbTypeHandler;
import com.xaxc.teqin.handler.PgGeometryTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 标绘数据
 * </p>
 *
 * @author author
 * @since 2024-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "draw_data", autoResultMap = true)
public class DrawData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("name")
    private String name;

    @TableField(value = "geom", typeHandler = PgGeometryTypeHandler.class)
    private String geom;

    @TableField(value = "data", typeHandler = JsonbTypeHandler.class)
    private JSONObject data;

    @TableField("police_type")
    private String policeType;

    /**
     * car、lines、police、uav、basic
     */
    @TableField("type")
    private String type;

    @TableField(exist = false)
    private List<String> typeList;

    @TableField("scene_id")
    private String sceneId;

    @TableField("task_id")
    private String taskId;

    @TableField("plan_node")
    private String planNode;

    @JsonIgnore
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    private Integer deleteFlag;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField("position")
    private String position;
    @TableField("ref_id")
    private String refId;

    @TableField("ref_line_name")
    private String refLineName;

    @TableField("sort")
    private Integer sort;

    @TableField(exist = false)
    private int num;

    @TableField(exist = false)
    private double length;

    /**
     * 建筑名称
     */
    @TableField(exist = false)
    private String buildName;
    /**
     * 楼层名称
     */
    @TableField(exist = false)
    private String floorNum;

    /**
     * 要素类型
     */
    @TableField(exist = false)
    private String featureType;

    /**
     * 警力数据类型
     */
    @TableField(exist = false)
    private String policeDataType;


    /**
     * 场景类型
     */
    @TableField(exist = false)
    private String sceneType;

    @TableField(exist = false)
    private String lineType;

    @TableField(exist = false)
    private String groupId;

    @TableField(exist = false, typeHandler = JsonbTypeHandler.class)
    private JSONObject geojson;


    @TableField(exist = false)
    private List<String> sceneIdList;

    @TableField(exist = false, typeHandler = JsonbTypeHandler.class)
    private JSONObject middlePoint;


    @TableField(exist = false)
    private JSONObject policeData;

    @TableField(exist = false)
    private String indoorFlag;

    @TableField(exist = false)
    private String keyFlag;

    @TableField(exist = false)
    private String fangxian;

    @TableField(exist = false)
    private String sceneName;

}
