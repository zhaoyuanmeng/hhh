package com.xaxc.teqin.controller;

import com.xaxc.teqin.entity.SceneGroupRule;
import com.xaxc.teqin.service.ISceneGroupRuleService;
import com.xaxc.teqin.common.ResponseResult;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/groupRule")
public class SceneGroupRuleController {

    @Resource
    private ISceneGroupRuleService groupRuleService;

    // 1. 查询场景下的分组规则
    @GetMapping("/list")
    public ResponseResult<?> getGroupRules(@RequestParam String sceneId) {
        List<SceneGroupRule> rules = groupRuleService.getGroupRulesBySceneId(sceneId);
        return ResponseResult.success(rules);
    }

    // 2. 保存分组规则（批量新增/更新）
    @PostMapping("/save")
    public ResponseResult<?> saveGroupRules(
            @RequestParam String sceneId,
            @RequestBody List<SceneGroupRule> rules
    ) {
        return groupRuleService.saveGroupRules(sceneId, rules);
    }

    // 3. 删除单个分组规则
    @DeleteMapping("/delete")
    public ResponseResult<?> deleteGroupRule(@RequestParam String id) {
        return groupRuleService.deleteGroupRule(id);
    }
}