package com.xaxc.teqin.mapper;

import com.xaxc.teqin.model.entity.CommercialEnterpriseInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-11-25
 */
public interface CommercialEnterpriseInfoMapper extends BaseMapper<CommercialEnterpriseInfo> {

    Map<String,Integer> getMerchantData();

    List<CommercialEnterpriseInfo> getMerchantDataByType(@Param("type") int type);
 }
