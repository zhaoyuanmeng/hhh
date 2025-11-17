package com.xaxc.teqin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xaxc.teqin.entity.SceneGroupRule;
import com.xaxc.teqin.common.ResponseResult;
import java.util.List;

public interface ISceneGroupRuleService extends IService<SceneGroupRule> {
    // 查询场景下的所有分组规则
    List<SceneGroupRule> getGroupRulesBySceneId(String sceneId);

    // 保存分组规则（新增/更新，批量操作）
    ResponseResult<?> saveGroupRules(String sceneId, List<SceneGroupRule> rules);

    // 删除单个分组规则
    ResponseResult<?> deleteGroupRule(String id);
}