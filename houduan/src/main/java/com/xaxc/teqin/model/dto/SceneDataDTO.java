package com.xaxc.teqin.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xaxc.teqin.model.entity.SceneInfo;
import com.xaxc.teqin.model.entity.ScenePlan;
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
public class SceneDataDTO {

    private String sceneName;

    private String sceneRoadDesc;

    private double sceneRoadLength;

    private long sceneRoadTime;

    private List<ScenePlan> scenePlanList;
}
