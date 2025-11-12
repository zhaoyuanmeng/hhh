package com.xaxc.teqin.service.impl;

import com.xaxc.teqin.model.entity.CommercialEnterpriseInfo;
import com.xaxc.teqin.mapper.CommercialEnterpriseInfoMapper;
import com.xaxc.teqin.service.ICommercialEnterpriseInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
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
public class CommercialEnterpriseInfoServiceImpl extends ServiceImpl<CommercialEnterpriseInfoMapper, CommercialEnterpriseInfo> implements ICommercialEnterpriseInfoService {

    @Override
    public Map<String, Integer> getMerchantData() {
        return baseMapper.getMerchantData();
    }

    @Override
    public List<CommercialEnterpriseInfo> getMerchantDataByType(int type) {
        return baseMapper.getMerchantDataByType(type);
    }
}
