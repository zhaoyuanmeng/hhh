package com.xaxc.teqin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xaxc.teqin.mapper.PointExtInfoMapper;
import com.xaxc.teqin.model.entity.PointExtInfo;
import com.xaxc.teqin.service.IPointExtInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-08-26
 */
@Service
public class PointExtInfoServiceImpl extends ServiceImpl<PointExtInfoMapper, PointExtInfo> implements IPointExtInfoService {

    @Override
    public List<PointExtInfo> queryIntersectsData(List<String[]> polygon) {
        //polygon [[],[],[]]
        Assert.isTrue(polygon.size() >= 3, "数组长度不得小于3");
        //POLYGON((-71.064544 42.28787, -122.1 45.51, -122.1 45.54, -122.69 45.54, -71.064544 42.28787))
        StringBuilder sb = new StringBuilder();
        sb.append("POLYGON((");
        for (int i = 0; i < polygon.size(); i++) {
            Assert.isTrue(polygon.get(i).length >= 2, "坐标数据错误");
            if (i == polygon.size() - 1) {
                sb.append(polygon.get(i)[0] + " " + polygon.get(i)[1]);
            } else {
                sb.append(polygon.get(i)[0] + " " + polygon.get(i)[1] + ",");
            }
        }
        //面第一个点和最后一个点须相同
        if (!Arrays.equals(polygon.get(0), polygon.get(polygon.size() - 1))) {
            sb.append(", " + polygon.get(0)[0] + " " + polygon.get(0)[1]);
        }
        sb.append("))");
        return baseMapper.queryIntersectsData(sb.toString());
    }

    @Override
    public List<PointExtInfo> getPointExtInfoList(List<String> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return null;
        }
        return baseMapper.getPointExtInfoList(ids);
    }
}
