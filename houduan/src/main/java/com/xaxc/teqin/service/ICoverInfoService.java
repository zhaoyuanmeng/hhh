package com.xaxc.teqin.service;

import com.xaxc.teqin.model.entity.CoverInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 井盖信息 服务类
 * </p>
 *
 * @author author
 * @since 2025-02-24
 */
public interface ICoverInfoService extends IService<CoverInfo> {
    List<CoverInfo> getList();
}
