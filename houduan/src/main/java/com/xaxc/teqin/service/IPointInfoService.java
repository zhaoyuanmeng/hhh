package com.xaxc.teqin.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xaxc.teqin.model.dto.BasicDataDTO;
import com.xaxc.teqin.model.entity.FeaturePositionData;
import com.xaxc.teqin.model.entity.PointExtInfo;
import com.xaxc.teqin.model.entity.PointInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 点位信息表 服务类
 * </p>
 *
 * @author author
 * @since 2024-07-16
 */
public interface IPointInfoService extends IService<PointInfo> {


    String getTypeName(String id);

    PointInfo getPointData(String id);

    String getPointType(String id);

    List<Map<String, List<BasicDataDTO>>> getResourceOfHighWayAndRailWay(List<String> ids);

    boolean addPoint(PointInfo pointInfo);

    boolean updatePoint(PointInfo pointInfo);

    /**
     * 获取高速列表
     *
     * @return
     */
    List<PointInfo> getHighwayList();

    /**
     * 获取高铁列表
     *
     * @return
     */
    List<PointInfo> getRailwayList();

    /**
     * 获取现场列表
     *
     * @return
     */
    List<PointInfo> getSiteList();

    /**
     * 获取住地列表
     *
     * @return
     */
    List<PointInfo> getResidenceList();


    List<PointInfo> getRoadList();

    /**
     * 获取高速统计数据
     *
     * @return
     */
    Map getHighwayStatistics();

    /**
     * 获取高铁统计数据
     *
     * @return
     */
    Map getRailwayStatistics();


    /**
     * 获取现场统计数据
     *
     * @return
     */
    Map getSiteStatistics();


    /**
     * 获取道路统计数据
     *
     * @return
     */
    Map getRoadStatistics();

    /**
     * 获取住地统计数据
     *
     * @return
     */
    Map getResidenceStatistics();

    PointInfo getPointDetail(String id, String taskId, String sceneId);

    /**
     * 获取高速详情数据
     *
     * @param jcxxId
     * @return
     */
    JSONObject getHighwayPointData(String jcxxId);


    JSONObject getRoadPointData(String jcxxId);

    /**
     * 获取高铁详情数据
     *
     * @param jcxxId
     * @return
     */
    JSONObject getRailwayPointData(String jcxxId);

    /**
     * 获取现场详情数据
     *
     * @param jcxxId
     * @return
     */
    JSONObject getSitePointData(String jcxxId);


    /**
     * 获取住地详情数据
     *
     * @param jcxxId
     * @return
     */
    JSONObject getResidencePointData(String jcxxId);

    /**
     * 获取高速点位类型
     *
     * @return
     */
    List<Map<String, Object>> getHighwayPointType();

    /**
     * 获取高铁点位类型
     *
     * @return
     */
    List<Map<String, Object>> getRailwayPointType();

    /**
     * 获取现场点位类型
     *
     * @return
     */
    List<Map<String, Object>> getSitePointType();

    /**
     * 获取住地点位类型
     *
     * @return
     */
    List<Map<String, Object>> getResidencePointType();


    /**
     * 更新表列数据
     */
    boolean updateColumnOfPointTable();

    void importHighwayData(MultipartFile file, String id, String pointTypeId, String nameVerifyFlag) throws InstantiationException, IllegalAccessException;

    void importRailwayData(MultipartFile file, String id, String pointTypeId, String nameVerifyFlag) throws InstantiationException, IllegalAccessException;

    void importSiteArchiveData(MultipartFile file, String id, String pointTypeId, String nameVerifyFlag) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;

    void importResidenceArchiveData(MultipartFile file, String id, String pointTypeId, String nameVerifyFlag) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;

    void downloadTemplate(String pointTypeId, HttpServletResponse response) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

    List<Map<String, String>> getPointsByName(String name);

    boolean updatePosition(String jingdu, String weidu, String id);

    void updatePointExtInfo();

    List getBasicDataNameList(String type);

    List getBasicDataList(String type);


    PointInfo getByName(String name);

    void importPoint(MultipartFile file);

    List<PointInfo> selectPointList(String childTypeId);

    List<Map<String, Object>> selectPointList(String id, String jcxxId);

    long selectPointCount(String jcxxId, String id, String pointId);

    List<PointInfo> queryListByParentId(String parentTypeId);


    List<PointInfo> queryListByJcxxId(String parentTypeId);

    void addPoint(JSONObject map);

    List<PointInfo> queryListSort(JSONObject map, String sortField, String direction);

    void deleteByJcxxId(String jcxxId);

    String saveArchivesInfo(PointInfo pointInfo);

    List getFeatureTypeData(@RequestParam(value = "id") String id);

    List<FeaturePositionData> getFeaturePosition(String jcxxId, String typeId);

}
