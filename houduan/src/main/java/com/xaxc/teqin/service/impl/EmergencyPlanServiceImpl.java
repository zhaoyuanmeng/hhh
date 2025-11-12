package com.xaxc.teqin.service.impl;

import com.xaxc.teqin.mapper.EmergencyPlanMapper;
import com.xaxc.teqin.model.entity.DrawData;
import com.xaxc.teqin.model.entity.EmergencyPlan;
import com.xaxc.teqin.service.IDrawDataService;
import com.xaxc.teqin.service.IEmergencyPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 应急预案 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-10-11
 */
@Service
public class EmergencyPlanServiceImpl extends ServiceImpl<EmergencyPlanMapper, EmergencyPlan> implements IEmergencyPlanService {

    @Resource
    IDrawDataService drawDataService;

    @Override
    public EmergencyPlan getDetail(String id) {
        EmergencyPlan emergencyPlan = getById(id);
        if (emergencyPlan == null) {
            return null;
        }
        //标绘数据
        DrawData drawData = new DrawData();
        drawData.setDeleteFlag(0);
        drawData.setSceneId(id);
        emergencyPlan.setDrawDataList(drawDataService.getDrawDataList(drawData));
        return emergencyPlan;
    }
}
