package com.xaxc.teqin.model.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xaxc.teqin.common.model.BaseEntity;
import com.xaxc.teqin.handler.JsonbType2ArrayHandler;
import com.xaxc.teqin.handler.JsonbTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 场景表
 * </p>
 *
 * @author author
 * @since 2024-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "scene_info", autoResultMap = true)
public class SceneInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 关联任务ID
     */
    @TableField("task_id")
    private String taskId;

    /**
     * 名称
     */
    @TableField("scene_name")
    private String sceneName;

    /**
     * 类型  1 路线, 2 现场 ,3 住地
     */
    @TableField("type")
    private String type;

    @TableField("scene_desc")
    private String sceneDesc;


    @TableField("basic_data_id")
    private String basicDataId;

    @TableField("origin_scheme_id")
    private String originSchemeId;

    @TableField(exist = false)
    private String basicDataTypeId;

    @TableField(exist = false)
    private String basicDataName;


    @TableField(exist = false)
    private String sourceSceneId;


    @TableField("scene_road_desc")
    private String sceneRoadDesc;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("begin_time")
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("end_time")
    private LocalDateTime endTime;


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标记 0未删除 1已删除
     */
    @JsonIgnore
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    private Integer deleteFlag;



    @TableField("time")
    private int time;

    /**
     * 漫游数据
     */
    @TableField(value = "key_frames", typeHandler = JsonbType2ArrayHandler.class)
    private JSONArray keyFrames;

    /**
     * 第三人称视角漫游数据
     */
    @TableField(value = "third_key_frames", typeHandler = JsonbType2ArrayHandler.class)
    private JSONArray thirdKeyFrames;

    /**
     * 第三人称视角漫游时间
     */
    @TableField("third_time")
    private int thirdTime;


    @TableField(value = "scene_road_length")
    private double sceneRoadLength;

    @TableField(value = "scene_road_time")
    private int sceneRoadTime;


    @TableField(exist = false)
    private List<PointExtInfo> pointInfoList;

    @TableField(exist = false)
    private List<DrawData> drawInfoList;

    @TableField(value = "view_data", typeHandler = JsonbTypeHandler.class)
    private JSONObject viewData;

    @TableField(value = "scheme_flag")
    private Integer schemeFlag;

    @TableField(value = "level")
    private String level;

    @TableField(exist = false)
    private String schemeId;

    @TableField(exist = false)
    private String  fileName;


    @TableField(exist = false)
    private String  fileUrl;


}
