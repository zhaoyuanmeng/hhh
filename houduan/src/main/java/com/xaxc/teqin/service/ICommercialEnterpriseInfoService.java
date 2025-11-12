package com.xaxc.teqin.service;

import com.xaxc.teqin.model.entity.CommercialEnterpriseInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-11-25
 */
public interface ICommercialEnterpriseInfoService extends IService<CommercialEnterpriseInfo> {
    Map<String,Integer> getMerchantData();

    List<CommercialEnterpriseInfo> getMerchantDataByType(int type);
}
