package com.xaxc.teqin.model.dto;

import com.alibaba.fastjson.JSONObject;
import com.xaxc.teqin.model.entity.*;
import lombok.*;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO extends Task {
    /**
     * 场景基础数据
     */
    private List<SceneInfo> sceneList;

    /**
     * 地图标绘数据
     */
    private List<DrawData> drawDataList;

    /**
     * 基礎信息數據
     */
    private List<PointExtInfo> pointInfoList;

    /**
     * 场景警力数据
     */
    private List<JSONObject> policePresenceList;


}
