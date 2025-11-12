package com.xaxc.teqin.mapper;

import com.xaxc.teqin.model.entity.HouseInfo;
import com.xaxc.teqin.model.entity.ResidenceInfo;
import com.xaxc.teqin.model.entity.ResidentialBuildingInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-11-25
 */
public interface ResidentialBuildingInfoMapper extends BaseMapper<ResidentialBuildingInfo> {

    Map<String, Integer> getResidenceData();

    int getHouseholdNumOfFloor(@Param("buildingId") String buildingId, @Param("unitNum") String unitNum);

    int getHouseFloor(@Param("buildingId") String buildingId, @Param("unitNum") String unitNum);


    ResidentialBuildingInfo getRdBuildingInfo(@Param("buildingName") String buildingName);


    List<HouseInfo> getRdHouseList(@Param("buildingName") String buildingId, @Param("unitNum") Integer unitNum);

    Map<String, String> getRdBuildingUnitData(@Param("buildingName") String buildingId, @Param("unitNum") Integer unitNum);

    HouseInfo getHouseDetail(@Param("roomAddress") String roomAddress);

    List<ResidenceInfo> getResidenceList(@Param("roomAddress") String roomAddress);
}
