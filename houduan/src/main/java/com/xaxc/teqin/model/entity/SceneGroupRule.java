package com.xaxc.teqin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SceneGroupRule {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id; // 主键ID

    @TableField("scene_id")
    private String sceneId; // 场景ID（关联一键部署的场景）

    @TableField("plan_node")
    private String planNode; // 固定为"警力部署"

    @TableField("group_name")
    private String groupName; // 分组名称

    @TableField("start_distance")
    private Integer startDistance; // 起点距离（米）

    @TableField("end_distance")
    private Integer endDistance; // 终点距离（米）

    @TableField("delete_flag")
    private Integer deleteFlag = 0; // 删除标识（0-未删，1-已删）

    @TableField("create_time")
    private LocalDateTime createTime = LocalDateTime.now();

    @TableField("update_time")
    private LocalDateTime updateTime = LocalDateTime.now();
}