package com.xaxc.teqin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.entity.PointInfo;
import com.xaxc.teqin.model.entity.SpecialServiceArchives;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 档案类型信息表 服务类
 * </p>
 *
 * @author author
 * @since 2024-07-16
 */
public interface ISpecialServiceArchivesService extends IService<SpecialServiceArchives> {

    List<Map<String,Object>> getBasicInfoById(String parentId);

    List<Map<String,Object>> getArchivesList(SpecialServiceArchives specialServiceArchives);

    List<Map<String,Object>> getInfoByArchivesId(String archivesId);


    Map<String, Object> getPointInfo(String archivesId, int size, int page);

    List<Map<String, Object>> selectPointInfo(String id, String jcxxId);

    ResponseResult selectPointList(String id, String jcxxId);

    Map<String, Object> selectFileId();

    Map<String, Object> getArchivesId(String fileId, String pointName);

    boolean saveFormItemSort(List<SpecialServiceArchives> specialServiceArchivesList);
}
