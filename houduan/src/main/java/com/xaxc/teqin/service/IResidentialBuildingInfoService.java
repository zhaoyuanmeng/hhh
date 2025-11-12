package com.xaxc.teqin.service;

import com.xaxc.teqin.model.entity.ResidentialBuildingInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-11-25
 */
public interface IResidentialBuildingInfoService extends IService<ResidentialBuildingInfo> {

    Map<String,Integer> getResidenceData();

    int getHouseholdNumOfFloor(String buildingId,String unitNum);

    int getHouseFloor(String buildingId,String unitNum);
}
