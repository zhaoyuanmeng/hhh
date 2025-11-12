package com.xaxc.teqin.service.impl;

import com.xaxc.teqin.model.entity.CommercialRoomInfo;
import com.xaxc.teqin.mapper.CommercialRoomInfoMapper;
import com.xaxc.teqin.service.ICommercialRoomInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-11-25
 */
@Service
public class CommercialRoomInfoServiceImpl extends ServiceImpl<CommercialRoomInfoMapper, CommercialRoomInfo> implements ICommercialRoomInfoService {

    @Override
    public List<CommercialRoomInfo> getRoomLeaseData(String buildingId, Integer floorNumber) {
        return baseMapper.getRoomLeaseData(buildingId, floorNumber);
    }

    @Override
    public List<CommercialRoomInfo> getLeaseEnterpriseData(int type) {
        return baseMapper.getLeaseEnterpriseData(type);
    }
}
