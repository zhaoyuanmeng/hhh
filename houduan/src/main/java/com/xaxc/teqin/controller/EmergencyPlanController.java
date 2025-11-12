package com.xaxc.teqin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.entity.DrawData;
import com.xaxc.teqin.model.entity.EmergencyPlan;
import com.xaxc.teqin.service.IEmergencyPlanService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 应急预案 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-10-11
 */
@RestController
@RequestMapping("/emergency-plan")
public class EmergencyPlanController {

    @Resource
    IEmergencyPlanService emergencyPlanService;

    @PostMapping("save")
    public ResponseResult save(@RequestBody EmergencyPlan drawData) {
        return ResponseResult.success(emergencyPlanService.saveOrUpdate(drawData));
    }

    @GetMapping("/delete")
    public ResponseResult delete(@RequestParam("id") String id) {
        return emergencyPlanService.update(new LambdaUpdateWrapper<EmergencyPlan>().set(EmergencyPlan::getDeleteFlag, 1).eq(EmergencyPlan::getId, id))
                ? ResponseResult.success() : ResponseResult.error("操作失败");
    }

    @PostMapping("getList")
    public ResponseResult getList(@RequestBody EmergencyPlan emergencyPlan) {
        emergencyPlan.setDeleteFlag(0);
        return ResponseResult.success(emergencyPlanService.lambdaQuery(emergencyPlan).list());
    }

    @GetMapping("getDetail")
    public ResponseResult getDetail(@RequestParam("id") String id) {
        return ResponseResult.success(emergencyPlanService.getDetail(id));
    }

    /**
     * 获取预案数量
     * @return
     */
    @GetMapping("getEmergencyNum")
    public ResponseResult getEmergencyNum() {
        return ResponseResult.success(emergencyPlanService.count(new LambdaQueryWrapper<EmergencyPlan>()
                .eq(EmergencyPlan::getDeleteFlag, 0)));
    }

}
