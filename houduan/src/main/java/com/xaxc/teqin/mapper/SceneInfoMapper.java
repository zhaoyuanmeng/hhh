package com.xaxc.teqin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaxc.teqin.model.entity.SceneInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 场景表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-07-24
 */
public interface SceneInfoMapper extends BaseMapper<SceneInfo> {
//    List<Map<String,Object>> getStatistics(String id);

    List<SceneInfo> getSimpleList(@Param("taskId") String taskId, @Param("ids") List<String> ids, @Param("type") String type);

    List<SceneInfo> getListByTaskId(@Param("taskId") String taskId);

    List<SceneInfo> getSimpleListByTaskIdAndType(@Param("taskId") String taskId, @Param("type") String type);

    List<SceneInfo> getSchemeList(@Param("dto") SceneInfo sceneInfo);

    Map getSchemeStatistics();

    SceneInfo getDetailById(@Param("id") String id);

    List<SceneInfo> getDetailByTaskIdAndSceneName(@Param("taskId") String taskId, @Param("sceneName") String sceneName);

    int getNumOfCrossSceneTime(@Param("dto") SceneInfo sceneInfo);
}
