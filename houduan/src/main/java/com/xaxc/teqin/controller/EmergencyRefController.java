package com.xaxc.teqin.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.entity.EmergencyRef;
import com.xaxc.teqin.service.IEmergencyRefService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-10-16
 */
@RestController
@RequestMapping("/emergency-ref")
public class EmergencyRefController {

    @Resource
    IEmergencyRefService emergencyRefService;

    /**
     * 添加关联
     * @param emergencyRef
     * @return
     */
    @PostMapping("add")
    public ResponseResult add(@RequestBody EmergencyRef emergencyRef) {
        return ResponseResult.success(emergencyRefService.save(emergencyRef));
    }

    /**
     * 删除关联
     * @param emergencyRef
     * @return
     */
    @PostMapping("delete")
    public ResponseResult delete(@RequestBody EmergencyRef emergencyRef) {
        emergencyRef.setDeleteFlag(0);
        return ResponseResult.success(emergencyRefService.update(emergencyRef, new LambdaUpdateWrapper<EmergencyRef>()
                .eq(EmergencyRef::getEmergencyPlanId, emergencyRef.getEmergencyPlanId())
                .eq(EmergencyRef::getSceneId, emergencyRef.getSceneId())));
    }

}
