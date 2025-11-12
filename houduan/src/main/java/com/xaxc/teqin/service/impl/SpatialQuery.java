package com.xaxc.teqin.service.impl;

import com.xaxc.teqin.common.util.Utils;
import com.xaxc.teqin.mapper.PointExtInfoMapper;
import com.xaxc.teqin.model.entity.PointExtInfo;
import org.locationtech.jts.io.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class SpatialQuery {
    @Resource
    PointExtInfoMapper pointExtInfoMapper;

    public List<PointExtInfo> queryLineBufferData(String lineWkt, Double radiusInMeters) throws ParseException {
        if (lineWkt == null) {
            return null;
        }
        //计算路的缓冲区域
        String lineBuffer = Utils.getLineBuffer(lineWkt, radiusInMeters == null ? 10 : radiusInMeters);
        //查询路缓冲区域内的基础数据及标绘数据
        return pointExtInfoMapper.queryIntersectsData(lineBuffer);
    }

    public List<PointExtInfo> queryLineBufferDataOfWay(String lineWkt, Double radiusInMeters, String jcxxId) throws ParseException {
        if (lineWkt == null) {
            return null;
        }
        //计算路的缓冲区域
        String lineBuffer = Utils.getLineBuffer(lineWkt, radiusInMeters == null ? 10 : radiusInMeters);
        //查询路缓冲区域内的基础数据
        return pointExtInfoMapper.queryIntersectsDataOfWay(lineBuffer, jcxxId);
    }

    public List<PointExtInfo> queryIntersectsPointData(String lineWkt, Double radiusInMeters) throws ParseException {
        if (lineWkt == null) {
            return null;
        }
        //计算路的缓冲区域
        String lineBuffer = Utils.getLineBuffer(lineWkt, radiusInMeters == null ? 10 : radiusInMeters);
        //查询路缓冲区域内的基础数据
        return pointExtInfoMapper.queryIntersectsPointData(lineBuffer, lineWkt);
    }



    public List<PointExtInfo> queryLineBufferData(List<String> lineWkts, Double radiusInMeters) throws ParseException {
        if (CollectionUtils.isEmpty(lineWkts)) {
            return null;
        }
        List<PointExtInfo> dataList = new ArrayList<>();
        for (String lineWkt : lineWkts) {
            dataList.addAll(queryLineBufferData(lineWkt, radiusInMeters));
        }
        return new ArrayList<>(new LinkedHashSet<>(dataList));
    }

    public List queryIntersectsData(List<String[]> polygon) {
        Assert.isTrue(polygon.size() >= 3, "面的点位不得少于3");
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
        return pointExtInfoMapper.queryIntersectsData(sb.toString());
    }
}
