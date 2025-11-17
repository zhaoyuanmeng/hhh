package com.xaxc.teqin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaxc.teqin.entity.SceneGroupRule;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SceneGroupRuleMapper extends BaseMapper<SceneGroupRule> {
    // 根据场景ID查询分组规则（只查未删除的）
    List<SceneGroupRule> selectBySceneId(@Param("sceneId") String sceneId);


}