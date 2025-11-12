package com.xaxc.teqin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaxc.teqin.model.entity.ScenePlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 场景规划 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-09-10
 */
public interface ScenePlanMapper extends BaseMapper<ScenePlan> {

    List<ScenePlan> getScenePlanNodeByTaskId(@Param("taskId") String taskId, @Param("type") String type);

    List<ScenePlan> getScenePlanOfNode(@Param("sceneId") String sceneId, @Param("planNode") String planNode);
}
