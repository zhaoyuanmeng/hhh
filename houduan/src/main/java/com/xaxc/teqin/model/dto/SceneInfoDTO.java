package com.xaxc.teqin.model.dto;

import com.xaxc.teqin.model.entity.DrawData;
import com.xaxc.teqin.model.entity.PointExtInfo;
import com.xaxc.teqin.model.entity.SceneInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SceneInfoDTO extends SceneInfo {
    /**
     * 地图标绘数据
     */
    private List<DrawData> drawDataList;

    /**
     * 基础数据
     */
    private List<PointExtInfo> pointExtInfoList;

    /**
     * 场景警力数据
     */
    private List<Map<String, Object>> policePresenceList;

}
