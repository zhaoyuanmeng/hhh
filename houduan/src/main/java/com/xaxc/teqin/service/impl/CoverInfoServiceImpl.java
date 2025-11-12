package com.xaxc.teqin.service.impl;

import com.xaxc.teqin.common.util.Utils;
import com.xaxc.teqin.model.entity.CoverInfo;
import com.xaxc.teqin.mapper.CoverInfoMapper;
import com.xaxc.teqin.service.ICoverInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 井盖信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-02-24
 */
@Service
public class CoverInfoServiceImpl extends ServiceImpl<CoverInfoMapper, CoverInfo> implements ICoverInfoService {

    @Override
    public List<CoverInfo> getList() {
        List<CoverInfo> list = list();
        list.stream().forEach(cover -> {
            double[] wgs84 = Utils.gcj02ToWgs84(Double.parseDouble(cover.getLat()), Double.parseDouble(cover.getLng()));
            cover.setLat(wgs84[0] + "");
            cover.setLng(wgs84[1] + "");
        });
        return list;
    }
}
