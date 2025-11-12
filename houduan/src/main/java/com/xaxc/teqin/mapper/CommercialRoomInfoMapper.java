package com.xaxc.teqin.mapper;

import com.xaxc.teqin.model.entity.CommercialRoomInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-11-25
 */
public interface CommercialRoomInfoMapper extends BaseMapper<CommercialRoomInfo> {

 List<CommercialRoomInfo> getRoomLeaseData(@Param("buildingId") String buildingId, @Param("floorNumber") Integer floorNumber);


 List<CommercialRoomInfo> getLeaseEnterpriseData(@Param("type") int type);

}
