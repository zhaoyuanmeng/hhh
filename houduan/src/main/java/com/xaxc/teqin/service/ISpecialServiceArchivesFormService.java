package com.xaxc.teqin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.entity.SpecialServiceArchives;
import com.xaxc.teqin.model.entity.SpecialServiceArchivesForm;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 档案自定义表单项信息表 服务类
 * </p>
 *
 * @author author
 * @since 2024-07-16
 */
public interface ISpecialServiceArchivesFormService extends IService<SpecialServiceArchivesForm> {

    List<SpecialServiceArchivesForm> getKV(String archivesId);

    List<SpecialServiceArchivesForm> selectPointInfo(String id);


    ResponseResult selectPointList(String id, String jcxxId);

}
