package com.xaxc.teqin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xaxc.teqin.model.entity.PointExtInfo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author author
 * @since 2024-08-26
 */
public interface IPointExtInfoService extends IService<PointExtInfo> {
    List<PointExtInfo> queryIntersectsData(List<String[]> polygon);

    List<PointExtInfo> getPointExtInfoList(List<String> ids);
}
