package com.xaxc.teqin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xaxc.teqin.entity.SceneGroupRule;
import com.xaxc.teqin.mapper.SceneGroupRuleMapper;
import com.xaxc.teqin.service.ISceneGroupRuleService;
import com.xaxc.teqin.common.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.util.StringUtils;
@Service
public class SceneGroupRuleServiceImpl extends ServiceImpl<SceneGroupRuleMapper, SceneGroupRule> implements ISceneGroupRuleService {

    @Override
    public List<SceneGroupRule> getGroupRulesBySceneId(String sceneId) {
        // 查询未删除的分组规则，按起点距离升序
        return baseMapper.selectBySceneId(sceneId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult<?> saveGroupRules(String sceneId, List<SceneGroupRule> rules) {
        if (rules != null && !rules.isEmpty()) {
            for (SceneGroupRule rule : rules) {
                rule.setSceneId(sceneId);
                rule.setPlanNode("警力部署");
                rule.setDeleteFlag(0);
                rule.setUpdateTime(LocalDateTime.now());

                if (StringUtils.isEmpty(rule.getId())) {
                    // 新增：id为空则生成新记录
                    rule.setCreateTime(LocalDateTime.now());
                    baseMapper.insert(rule);
                } else {
                    // 更新：id不为空则更新原有记录
                    baseMapper.updateById(rule);
                }
            }
        }
        return ResponseResult.success("分组规则保存成功");
    }

    @Override
    public ResponseResult<?> deleteGroupRule(String id) {
        SceneGroupRule rule = new SceneGroupRule();
        rule.setId(id);
        rule.setDeleteFlag(1); // 逻辑删除
        rule.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(rule);
        return ResponseResult.success("分组规则删除成功");
    }
}