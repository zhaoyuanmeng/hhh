package com.xaxc.teqin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaxc.teqin.model.entity.ScenePlan;
import com.xaxc.teqin.model.entity.ScenePlanExt;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 任务规划 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-09-20
 */
public interface ScenePlanExtMapper extends BaseMapper<ScenePlanExt> {

    List<ScenePlanExt> getScenePlanNodeExt(@Param("sceneId") String sceneId, @Param("planNode") String planNode);

    ScenePlanExt getScenePlanNodeExtOfLine(@Param("sceneId") String sceneId, @Param("planNode") String planNode, @Param("fangxian") String fangxian);
}
