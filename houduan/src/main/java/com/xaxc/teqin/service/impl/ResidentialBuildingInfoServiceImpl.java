package com.xaxc.teqin.service.impl;

import com.xaxc.teqin.model.entity.ResidentialBuildingInfo;
import com.xaxc.teqin.mapper.ResidentialBuildingInfoMapper;
import com.xaxc.teqin.service.IResidentialBuildingInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-11-25
 */
@Service
public class ResidentialBuildingInfoServiceImpl extends ServiceImpl<ResidentialBuildingInfoMapper, ResidentialBuildingInfo> implements IResidentialBuildingInfoService {

    @Override
    public Map<String, Integer> getResidenceData() {
        return baseMapper.getResidenceData();
    }

    @Override
    public int getHouseholdNumOfFloor(String buildingId, String unitNum) {
        return baseMapper.getHouseholdNumOfFloor(buildingId, unitNum);
    }

    @Override
    public int getHouseFloor(String buildingId, String unitNum) {
        return baseMapper.getHouseFloor(buildingId, unitNum);
    }
}
