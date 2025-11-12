package com.xaxc.teqin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xaxc.teqin.model.entity.EmergencyPlan;

/**
 * <p>
 * 应急预案 服务类
 * </p>
 *
 * @author author
 * @since 2024-10-11
 */
public interface IEmergencyPlanService extends IService<EmergencyPlan> {

    EmergencyPlan getDetail(String id);

}
