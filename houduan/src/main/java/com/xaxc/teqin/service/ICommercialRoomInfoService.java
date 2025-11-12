package com.xaxc.teqin.service;

import com.xaxc.teqin.model.entity.CommercialRoomInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-11-25
 */
public interface ICommercialRoomInfoService extends IService<CommercialRoomInfo> {

    public List<CommercialRoomInfo> getRoomLeaseData(String buildingId, Integer floorNumber);

    List<CommercialRoomInfo> getLeaseEnterpriseData(int type);

}
