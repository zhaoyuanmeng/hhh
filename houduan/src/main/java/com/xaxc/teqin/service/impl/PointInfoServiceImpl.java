package com.xaxc.teqin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xaxc.teqin.common.model.Constant;
import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.util.Utils;
import com.xaxc.teqin.excel.CellStyleStrategy;
import com.xaxc.teqin.excel.ExcelValid;
import com.xaxc.teqin.excel.ExcelUtils;
import com.xaxc.teqin.mapper.PointInfoMapper;
import com.xaxc.teqin.mapper.SpecialServiceArchivesFormMapper;
import com.xaxc.teqin.mapper.SpecialServiceArchivesMapper;
import com.xaxc.teqin.model.dto.BasicDataDTO;
import com.xaxc.teqin.model.dto.BasicPoint;
import com.xaxc.teqin.model.dto.DynamicDataDTO;
import com.xaxc.teqin.model.entity.*;
import com.xaxc.teqin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 点位信息表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-07-16
 */
@Slf4j
@Service
public class PointInfoServiceImpl extends ServiceImpl<PointInfoMapper, PointInfo> implements IPointInfoService {

    @Value("${highway.typeId}")
    private String highwayTypeId;

    @Value("${highway.basicInfoTypeId}")
    private String highwayBasicInfoTypeId;

    @Value("${railway.typeId}")
    private String railwayTypeId;

    @Value("${railway.basicInfoTypeId}")
    private String railwayBasicInfoTypeId;

    @Value("${site.typeId}")
    private String siteTypeId;

    @Value("${site.basicInfoTypeId}")
    private String siteBasicInfoTypeId;

    @Value("${site.insideOrgTypeId}")
    private String siteInsideOrgTypeId;

    @Value("${site.mainBuildingsTypeId}")
    private String siteMainBuildingsTypeId;

    @Value("${site.importantPartTypeId}")
    private String siteImportantPartTypeId;

    @Value("${site.fourNeighborsTypeId}")
    private String siteFourNeighborsTypeId;

    @Value("${site.parkingTypeId}")
    private String siteParkingTypeId;

    @Value("${site.importantRegionTypeId}")
    private String siteImportantRegionTypeId;

    @Value("${residence.typeId}")
    private String residenceTypeId;

    @Value("${residence.basicInfoTypeId}")
    private String residenceBasicInfoTypeId;

    @Value("${residence.insideOrgTypeId}")
    private String residenceInsideOrgTypeId;

    @Value("${residence.mainBuildingsTypeId}")
    private String residenceMainBuildingsTypeId;

    @Value("${residence.importantPartTypeId}")
    private String residenceImportantPartTypeId;

    @Value("${residence.fourNeighborsTypeId}")
    private String residenceFourNeighborsTypeId;

    @Value("${residence.parkingTypeId}")
    private String residenceParkingTypeId;

    @Value("${pointFieldName}")
    private String pointFieldName;

    @Value("${infoFieldName}")
    private String infoFieldName;

    @Value("${site.mainBuildings.vipNameField}")
    private String siteMainBuildingsVipNameField;

    @Value("${site.mainBuildings.vipSeatField}")
    private String siteMainBuildingsVipSeatField;

    @Value("${site.mainBuildings.meetingRoomNameField}")
    private String siteMainBuildingsMeetingRoomNameField;

    @Value("${site.mainBuildings.meetingRoomSeatField}")
    private String siteMainBuildingsMeetingRoomSeatField;

    @Value("${site.mainBuildings.diningRoomNameField}")
    private String siteMainBuildingsDiningRoomNameField;

    @Value("${site.mainBuildings.diningRoomSeatField}")
    private String siteMainBuildingsDiningRoomSeatField;

    @Value("${residence.mainBuildings.vipNameField}")
    private String residenceMainBuildingsVipNameField;

    @Value("${residence.mainBuildings.vipSeatField}")
    private String residenceMainBuildingsVipSeatField;

    @Value("${residence.mainBuildings.meetingRoomNameField}")
    private String residenceMainBuildingsMeetingRoomNameField;

    @Value("${residence.mainBuildings.meetingRoomSeatField}")
    private String residenceMainBuildingsMeetingRoomSeatField;

    @Value("${residence.mainBuildings.diningRoomNameField}")
    private String residenceMainBuildingsDiningRoomNameField;

    @Value("${residence.mainBuildings.diningRoomSeatField}")
    private String residenceMainBuildingsDiningRoomSeatField;

    @Value("${road.typeId}")
    private String roadTypeId;

    @Value("${road.basicInfoTypeId}")
    private String roadBasicInfoTypeId;

    @Resource
    private SpecialServiceArchivesFormMapper specialServiceArchivesFormMapper;

    @Resource
    private SpecialServiceArchivesMapper specialServiceArchivesMapper;

    @Resource
    private IPointExtInfoService pointExtInfoService;

    @Resource
    SpatialQuery spatialQuery;

    @Resource
    IDrawDataService drawDataService;

    @Override
    public String getTypeName(String id) {
        specialServiceArchivesMapper.selectById(id);
        return null;
    }

    @Override
    public PointInfo getPointData(String id) {
        PointInfo pointInfo = getById(id);
        if (Objects.isNull(pointInfo)) {
            return null;
        }
        if (highwayTypeId.equals(pointInfo.getParentTypeId())) {
            pointInfo.setType("highway");
        } else if (railwayTypeId.equals(pointInfo.getParentTypeId())) {
            pointInfo.setType("railway");
        } else if (siteTypeId.equals(pointInfo.getParentTypeId())) {
            pointInfo.setType("site");
        } else if (residenceTypeId.equals(pointInfo.getParentTypeId())) {
            pointInfo.setType("residence");
        } else if (roadTypeId.equals(pointInfo.getParentTypeId())) {
            pointInfo.setType("road");
        }
        return pointInfo;
    }

    @Override
    public String getPointType(String id) {
        PointInfo pointInfo = getById(id);
        if (Objects.isNull(pointInfo)) {
            return null;
        }

        if (highwayTypeId.equals(pointInfo.getParentTypeId())) {
            return "highway";
        } else if (railwayTypeId.equals(pointInfo.getParentTypeId())) {
            return "railway";
        } else if (siteTypeId.equals(pointInfo.getParentTypeId())) {
            return "site";
        } else if (residenceTypeId.equals(pointInfo.getParentTypeId())) {
            return "residence";
        }
        return null;
    }

    @Override
    public List<Map<String, List<BasicDataDTO>>> getResourceOfHighWayAndRailWay(List<String> ids) {
        Set<String> highwaySet = new HashSet<>();
        Set<String> railwaySet = new HashSet<>();
        List<String> basicIds = new ArrayList<>(new LinkedHashSet<>(ids));
        //根据ID判断是高速还是高铁
        for (String id : basicIds) {
            PointInfo pointInfo = getById(id);
            if (pointInfo == null) {
                continue;
            }
            if (highwayTypeId.equals(pointInfo.getParentTypeId())) {
                highwaySet.add(id);
            } else if (railwayTypeId.equals(pointInfo.getParentTypeId())) {
                railwaySet.add(id);
            }
        }
        Map<String, List<BasicDataDTO>> highwayMap = new HashMap<>();
        if (highwaySet.size() > 0) {
            //高速
            highwayMap = getResourceData(highwaySet, highwayTypeId, highwayBasicInfoTypeId);
        }
        Map<String, List<BasicDataDTO>> railwayMap = new HashMap<>();
        if (railwaySet.size() > 0) {
            //高铁
            railwayMap = getResourceData(railwaySet, railwayTypeId, railwayBasicInfoTypeId);
        }
        return Arrays.asList(highwayMap, railwayMap);
    }

    public Map<String, List<BasicDataDTO>> getResourceData(List<String> wkts, Double bufferRadius, String basicDataId) {
        Map<String, List<BasicDataDTO>> highMap = new LinkedHashMap<>();
        PointInfo pointInfo = getById(basicDataId);
        if (highwayTypeId.equals(pointInfo.getParentTypeId())) {
            List<SpecialServiceArchives> highwayArchives = specialServiceArchivesMapper.selectList(new LambdaQueryWrapper<SpecialServiceArchives>()
                    .eq(SpecialServiceArchives::getDelFlag, "0")
                    .ne(SpecialServiceArchives::getId, highwayBasicInfoTypeId)
                    .eq(SpecialServiceArchives::getParentId, highwayTypeId).last("ORDER BY length(sort), sort asc"));
            for (int i = 0; i < highwayArchives.size(); i++) {
                highMap.put(highwayArchives.get(i).getArchivesName(), new ArrayList<>());
            }

            try {
                List<PointExtInfo> pointExtInfoList = spatialQuery.queryLineBufferData(wkts, bufferRadius);
                for (SpecialServiceArchives serviceArchives : highwayArchives) {
                    for (PointExtInfo point : pointExtInfoList) {
                        if (point.getChildTypeId().equals(serviceArchives.getId())) {
                            BasicDataDTO basicDataDTO = new BasicDataDTO();
                            basicDataDTO.setId(point.getId());
                            basicDataDTO.setName(point.getName().replace("\n", "").replaceAll(" ", ""));
                            basicDataDTO.setType(point.getType());
                            basicDataDTO.setChildren(new ArrayList<>());
                            basicDataDTO.setCoordinates(point.getGeojson().getObject("coordinates", List.class));
                            highMap.get(serviceArchives.getArchivesName()).add(basicDataDTO);
                        }
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else if (railwayTypeId.equals(pointInfo.getParentTypeId())) {
            List<SpecialServiceArchives> highwayArchives = specialServiceArchivesMapper.selectList(new LambdaQueryWrapper<SpecialServiceArchives>()
                    .eq(SpecialServiceArchives::getDelFlag, "0")
                    .ne(SpecialServiceArchives::getId, railwayBasicInfoTypeId)
                    .eq(SpecialServiceArchives::getParentId, railwayTypeId).last("ORDER BY length(sort), sort asc"));
            for (int i = 0; i < highwayArchives.size(); i++) {
                highMap.put(highwayArchives.get(i).getArchivesName(), new ArrayList<>());
            }

            try {
                List<PointExtInfo> pointExtInfoList = spatialQuery.queryLineBufferData(wkts, bufferRadius);
                for (SpecialServiceArchives serviceArchives : highwayArchives) {
                    for (PointExtInfo point : pointExtInfoList) {
                        if (point.getChildTypeId().equals(serviceArchives.getId())) {
                            BasicDataDTO basicDataDTO = new BasicDataDTO();
                            basicDataDTO.setId(point.getId());
                            basicDataDTO.setName(point.getName().replace("\n", "").replaceAll(" ", ""));
                            basicDataDTO.setType(point.getType());
                            basicDataDTO.setChildren(new ArrayList<>());
                            basicDataDTO.setCoordinates(point.getGeojson().getObject("coordinates", List.class));
                            highMap.get(serviceArchives.getArchivesName()).add(basicDataDTO);
                        }
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return highMap;
    }

    public Map<String, List<BasicDataDTO>> getResourceData(Set<String> highwaySet, String highwayTypeId, String highwayBasicInfoTypeId) {
        Map<String, List<BasicDataDTO>> highMap = new LinkedHashMap<>();
        List<SpecialServiceArchives> highwayArchives = specialServiceArchivesMapper.selectList(new LambdaQueryWrapper<SpecialServiceArchives>()
                .eq(SpecialServiceArchives::getDelFlag, "0")
                .ne(SpecialServiceArchives::getId, highwayBasicInfoTypeId)
                .eq(SpecialServiceArchives::getParentId, highwayTypeId).last("ORDER BY length(sort), sort asc"));
        for (int i = 0; i < highwayArchives.size(); i++) {
            highMap.put(highwayArchives.get(i).getArchivesName(), new ArrayList<>());
        }
        for (String id : highwaySet) {
            PointInfo pointInfo = getById(id);
            List<PointExtInfo> pointInfoList = baseMapper.getPointListByJcxxId(pointInfo.getId());
            for (SpecialServiceArchives serviceArchives : highwayArchives) {
                for (PointExtInfo point : pointInfoList) {
                    if (point.getChildTypeId().equals(serviceArchives.getId())) {
                        BasicDataDTO basicDataDTO = new BasicDataDTO();
                        basicDataDTO.setId(point.getId());
                        basicDataDTO.setName(point.getName().replace("\n", "").replaceAll(" ", ""));
                        basicDataDTO.setType(point.getType());
                        basicDataDTO.setChildren(new ArrayList<>());
                        basicDataDTO.setCoordinates(point.getGeojson().getObject("coordinates", List.class));
                        highMap.get(serviceArchives.getArchivesName()).add(basicDataDTO);
                    }
                }
            }
        }
        return highMap;
    }

    @Override
    public boolean addPoint(PointInfo pointInfo) {
        if (pointInfo.getType() != null) {
            switch (pointInfo.getType()) {
                case "1"://高速
                    pointInfo.setParentTypeId(highwayTypeId);
                    pointInfo.setChildTypeId(highwayBasicInfoTypeId);
                    break;
                case "2"://高铁
                    pointInfo.setParentTypeId(railwayTypeId);
                    pointInfo.setChildTypeId(railwayBasicInfoTypeId);
                    break;
                case "3"://现场
                    pointInfo.setParentTypeId(siteTypeId);
                    pointInfo.setChildTypeId(siteBasicInfoTypeId);
                    break;
                case "4"://住地
                    pointInfo.setParentTypeId(residenceTypeId);
                    pointInfo.setChildTypeId(residenceBasicInfoTypeId);
                    break;
            }
        }
        if (pointInfo.getParentTypeId() == null || pointInfo.getChildTypeId() == null) {
            throw new RuntimeException("缺少必要的参数，请检查parentTypeId、childTypeId是否已传入");
        }
        String id = null;
        if (pointInfo.getData().get(infoFieldName) != null) {
            //基本信息数据
            id = baseMapper.getIdByFieldName(infoFieldName, pointInfo.getData().get(infoFieldName).toString(), pointInfo.getChildTypeId());
        } else if (pointInfo.getData().get(pointFieldName) != null) {
            id = baseMapper.getIdByFieldName(pointFieldName, pointInfo.getData().get(pointFieldName).toString(), pointInfo.getChildTypeId());
        }
        if (id != null) {
            pointInfo.setId(id);
        }
        return saveOrUpdate(pointInfo);
    }

    @Override
    public boolean updatePoint(PointInfo pointInfo) {
        return updateById(pointInfo);
    }

    @Override
    public List<PointInfo> getHighwayList() {
        return getPointList(highwayBasicInfoTypeId);
    }

    @Override
    public List<PointInfo> getRailwayList() {
        return getPointList(railwayBasicInfoTypeId);
    }

    @Override
    public List<PointInfo> getSiteList() {
        return getPointList(siteBasicInfoTypeId);
    }

    @Override
    public List<PointInfo> getResidenceList() {
        return getPointList(residenceBasicInfoTypeId);
    }

    @Override
    public List<PointInfo> getRoadList() {
        return getPointList(roadBasicInfoTypeId);
    }

    private List<PointInfo> getPointList(String basicInfoTypeId) {
        List<PointInfo> pointInfos = list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getChildTypeId, basicInfoTypeId)
                .isNull(PointInfo::getJcxxId));
        pointInfos.forEach(pointInfo ->
                pointInfo.setPointNum(list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getJcxxId, pointInfo.getId()))
                        .stream().filter(p -> !"无".equals(p.getData().getString(pointFieldName))).count())
        );
        return pointInfos;
    }

    public Map<String, Object> getWayStatistics(String typeId, String basicInfoTypeId) {
        Map<String, Object> map = new HashMap<>();
        List<PointInfo> pointInfoList = list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getChildTypeId, basicInfoTypeId)
                .isNull(PointInfo::getJcxxId));
        //高速数量
        map.put("num", pointInfoList.size());
        if (pointInfoList.size() == 0) {
            //高速总长
            map.put("length", 0);
            //点位数
            map.put("pointNum", 0);
        } else {
            //高速总长
            map.put("length", baseMapper.getSumOfWayTotalLength(basicInfoTypeId));
            //点位数
            map.put("pointNum", baseMapper.getPointNumOfWay(typeId, basicInfoTypeId));
        }
        return map;
    }

    public Map<String, Object> getPointStatistics(String typeId, String basicInfoTypeId) {
        Map<String, Object> map = new HashMap<>();
        List<PointInfo> pointInfoList = list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getChildTypeId, basicInfoTypeId)
                .isNull(PointInfo::getJcxxId));
        //数量
        map.put("num", pointInfoList.size());
        //面积
        map.put("area", baseMapper.getSumOfPointTotalArea(basicInfoTypeId));
        //点位数
        map.put("pointNum", baseMapper.getPointNumOfWay(typeId, basicInfoTypeId));
        return map;
    }

    @Override
    public Map<String, Object> getHighwayStatistics() {
        return getWayStatistics(highwayTypeId, highwayBasicInfoTypeId);
    }

    @Override
    public Map<String, Object> getRailwayStatistics() {
        return getWayStatistics(railwayTypeId, railwayBasicInfoTypeId);
    }

    @Override
    public Map<String, Object> getSiteStatistics() {
        return getPointStatistics(siteTypeId, siteBasicInfoTypeId);
    }

    @Override
    public Map getRoadStatistics() {
        return getWayStatistics(roadTypeId, roadBasicInfoTypeId);
    }

    @Override
    public Map<String, Object> getResidenceStatistics() {
        return getPointStatistics(residenceTypeId, residenceBasicInfoTypeId);
    }

    @Override
    public PointInfo getPointDetail(String id, String taskId, String sceneId) {
        PointInfo pointInfo = getById(id);
        if (pointInfo == null) {
            return null;
        }
        //计算离那条绘制的线最近并计算离线段起点距离
        List<PointExtInfo> pointExtInfoList = pointExtInfoService.getPointExtInfoList(List.of(id));
        if (!CollectionUtils.isEmpty(pointExtInfoList)) {
            Map<String, Object> map = drawDataService.getDistanceData(taskId, sceneId, pointExtInfoList.get(0).getGeom());
            if (!CollectionUtils.isEmpty(map)) {
                pointInfo.setRefLineName(map.get("name").toString());
                pointInfo.setPosition(map.get("start_dis").toString());
            }
        }


        JSONObject jsonObject = pointInfo.getData();
        Map fieldObject = new LinkedHashMap();
        List<SpecialServiceArchivesForm> specialServiceArchivesForms = specialServiceArchivesFormMapper.selectList(new LambdaQueryWrapper<SpecialServiceArchivesForm>().eq(SpecialServiceArchivesForm::getArchivesId, pointInfo.getChildTypeId()).eq(SpecialServiceArchivesForm::getDelFlag, "0").orderByAsc(SpecialServiceArchivesForm::getSort));
        for (SpecialServiceArchivesForm specialServiceArchivesForm : specialServiceArchivesForms) {
            String fieldId = specialServiceArchivesForm.getFieldId();
            if (fieldId.contains("(")) {
                fieldId = fieldId.substring(0, fieldId.indexOf("("));
            }
            for (String key : jsonObject.keySet()) {
                if (key.equals(fieldId)) {
                    fieldObject.put(key, specialServiceArchivesForm.getFieldName());
                }
            }
        }
        pointInfo.setDataFiledName(fieldObject);
        return pointInfo;
    }

    public JSONObject getPointDataDetail(String typeId, String infoId) {
        JSONObject result = new JSONObject();
        //基本信息
        PointInfo pointInfo = getById(infoId);
        result.put("id", infoId);
        //描述
        result.put("data", pointInfo.getData());
        //点位基本数据
        List<Map<String, Object>> pintDataList = baseMapper.getPointData(typeId, infoId);
        AtomicInteger pointNum = new AtomicInteger();
        List<JSONObject> pointList = new ArrayList<>();
        for (Map<String, Object> map : pintDataList) {
            AtomicInteger validPointNum = new AtomicInteger();
            List<PointInfo> pointInfoList = list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getChildTypeId, map.get("id")).eq(PointInfo::getJcxxId, infoId));
            if (!CollectionUtils.isEmpty(pointInfoList)) {
                Map jsonObject2 = new LinkedHashMap();
                JSONObject jsonObject1 = pointInfoList.get(0).getData();
                List<SpecialServiceArchivesForm> specialServiceArchivesForms = specialServiceArchivesFormMapper.selectList(new LambdaQueryWrapper<SpecialServiceArchivesForm>().eq(SpecialServiceArchivesForm::getArchivesId, pointInfoList.get(0).getChildTypeId()).eq(SpecialServiceArchivesForm::getDelFlag, "0").orderByAsc(SpecialServiceArchivesForm::getSort));
                for (SpecialServiceArchivesForm specialServiceArchivesForm : specialServiceArchivesForms) {
                    String fieldId = specialServiceArchivesForm.getFieldId();
                    //去除（）的字段
                    if (fieldId.contains("(")) {
                        fieldId = fieldId.substring(0, fieldId.indexOf("("));
                    }
                    for (String key : jsonObject1.keySet()) {
                        if (key.equals(fieldId)) {
                            jsonObject2.put(key, specialServiceArchivesForm.getFieldName());
                        }
                    }
                }
                pointInfoList.forEach(pointInfo1 -> {
                    pointInfo1.setDataFiledName(jsonObject2);
                    JSONObject dataMap =   pointInfo1.getData();
                    if(dataMap.get("jingdu")!=null){
                        dataMap.put("jingdu", Utils.transterJson(dataMap.get("jingdu")));
                    }
                    if(dataMap.get("weidu")!=null){
                        dataMap.put("weidu", Utils.transterJson(dataMap.get("weidu")));
                    }
                    if(dataMap.get("juli")!=null){
                        dataMap.put("juli", Utils.transterJson(dataMap.get("juli")));
                    }
                    if(dataMap.get("weizhi")!=null){
                        dataMap.put("weizhi", Utils.transterJson(dataMap.get("weizhi")));
                    }
                    if(dataMap.get("gaodu")!=null){
                        dataMap.put("gaodu", Utils.transterJson(dataMap.get("gaodu")));
                    }

                    if (!"无".equals(pointInfo1.getData().getString(pointFieldName))) {
                        validPointNum.getAndIncrement();
                    }
                });
            }
            pointNum.addAndGet(validPointNum.get());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", map.get("name"));
            jsonObject.put("num", validPointNum.get());
            jsonObject.put("data", validPointNum.get() > 0 ? pointInfoList : new ArrayList<>());
            pointList.add(jsonObject);
        }
        //点位数
        result.put("pointNum", pointNum.get());
        //点位数据
        result.put("pointList", pointList);
        return result;
    }

    @Override
    public JSONObject getHighwayPointData(String infoId) {
        return getPointDataDetail(highwayTypeId, infoId);
    }

    @Override
    public JSONObject getRoadPointData(String infoId) {
        return getPointDataDetail(roadTypeId, infoId);
    }

    @Override
    public JSONObject getRailwayPointData(String infoId) {
        return getPointDataDetail(railwayTypeId, infoId);
    }

    @Override
    public JSONObject getSitePointData(String infoId) {
        return getPointDataDetail(siteTypeId, infoId);
    }

    @Override
    public JSONObject getResidencePointData(String infoId) {
        return getPointDataDetail(residenceTypeId, infoId);
    }

    @Override
    public List<Map<String, Object>> getHighwayPointType() {
        return baseMapper.getPointType(highwayTypeId, highwayBasicInfoTypeId);
    }

    @Override
    public List<Map<String, Object>> getRailwayPointType() {
        return baseMapper.getPointType(railwayTypeId, railwayBasicInfoTypeId);
    }

    @Override
    public List<Map<String, Object>> getSitePointType() {
        return baseMapper.getPointSpecialType(siteTypeId, siteBasicInfoTypeId);
    }

    @Override
    public List<Map<String, Object>> getResidencePointType() {
        return baseMapper.getPointSpecialType(residenceTypeId, residenceBasicInfoTypeId);
    }

    public String object2String(Object object) {
        if (object == null) {
            return null;
        }
        return object.toString();
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public boolean updateColumnOfPointTable() {
        List<PointInfo> pointInfos = list();
        pointInfos.forEach(pointInfo -> {
            pointInfo.setChildTypeId(object2String(pointInfo.getData().get("jcxxId")));
            pointInfo.setParentTypeId(object2String(pointInfo.getData().get("parentTypeId")));
            //updateExt(pointInfo);
        });
        return saveOrUpdateBatch(pointInfos, 1000);
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void importData(MultipartFile file, String id, String typeId, String childTypeId) {
        saveOrUpdateBatch(getImportData(file, id, typeId, childTypeId), 1000);
    }

    @SneakyThrows
    public List<PointInfo> getImportData(MultipartFile file, String id, String typeId, String childTypeId) {
        List<PointInfo> pointInfoList = new ArrayList<>();
        //接收文件
        String fileName = ExcelUtils.receiveFile(file);
        //查询点位类型对应的档案配置
        List<SpecialServiceArchivesForm> specialServiceArchivesForms = getArchivesForm(typeId, childTypeId);
        Class<? extends DynamicDataDTO> dynamicDataDTO = ExcelUtils.getDynamicClassByArchivesForm(specialServiceArchivesForms);
        List<JSONObject> dynamicDataDTOList = ExcelUtils.getSheetData(fileName, 0, dynamicDataDTO.getDeclaredConstructor().newInstance());

        for (JSONObject data : dynamicDataDTOList) {
            PointInfo pointInfo = new PointInfo();
            pointInfo.setJcxxId(id);
            pointInfo.setParentTypeId(typeId);
            pointInfo.setChildTypeId(childTypeId);
            pointInfo.setData(data);
            //pointInfo = updateExt(pointInfo);
            //根据名称查询数据是否已存在，存在则更新
            String pointId = baseMapper.getIdByFieldName(pointFieldName, data.get(pointFieldName).toString(), childTypeId);
            if (pointId != null) {
                pointInfo.setId(pointId);
            }
            pointInfoList.add(pointInfo);
        }
        return pointInfoList;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void importHighwayData(MultipartFile file, String id, String pointTypeId, String nameVerifyFlag) {
        importData(file, id, highwayTypeId, pointTypeId);
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void importRailwayData(MultipartFile file, String id, String pointTypeId, String nameVerifyFlag) {
        importData(file, id, railwayTypeId, pointTypeId);
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void importSiteArchiveData(MultipartFile file, String id, String pointTypeId, String nameVerifyFlag) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<String> partNames = specialServiceArchivesMapper.selectList(new LambdaQueryWrapper<SpecialServiceArchives>()
                        .eq(SpecialServiceArchives::getDelFlag, "0")
                        .eq(SpecialServiceArchives::getParentId, siteTypeId)
                        .eq(SpecialServiceArchives::getSpecialType, 1)
                        .ne(SpecialServiceArchives::getId, siteBasicInfoTypeId)
                        .orderByAsc(SpecialServiceArchives::getSort)).stream().map(SpecialServiceArchives::getArchivesName)
                .collect(Collectors.toList());
        //接收文件
        String fileName = ExcelUtils.receiveFile(file);
        int[] partRowIndex = ExcelUtils.getArchivesPartRowIndex(fileName, 0, partNames);

        List<PointInfo> pointInfoList = new ArrayList<>();
        if (siteInsideOrgTypeId.equals(pointTypeId)) {
            List<PointInfo> siteInsideOrgList = getPointData(fileName, partRowIndex[0] + 1, partRowIndex[1], id, siteTypeId, siteInsideOrgTypeId);
            log.info("内设机构： {}", siteInsideOrgList);
            pointInfoList.addAll(siteInsideOrgList);
        } else if (siteMainBuildingsTypeId.equals(pointTypeId)) {
            List<PointInfo> mainBuildingsList = getSiteMainBuildingData(fileName, 0, partRowIndex[1] + 1, partRowIndex[2], id, siteTypeId, siteMainBuildingsTypeId);
            log.info("主要建筑： {}", mainBuildingsList);
            pointInfoList.addAll(mainBuildingsList);
        } else if (siteImportantPartTypeId.equals(pointTypeId)) {
            List<PointInfo> importantPartList = getPointData(fileName, partRowIndex[2] + 1, partRowIndex[3], id, siteTypeId, siteImportantPartTypeId);
            log.info("重要部位： {}", importantPartList);
            pointInfoList.addAll(importantPartList);
        } else if (siteFourNeighborsTypeId.equals(pointTypeId)) {
            List<PointInfo> fourNeighborsList = getPointData(fileName, partRowIndex[3] + 1, partRowIndex[4], id, siteTypeId, siteFourNeighborsTypeId);
            log.info("四邻情况： {}", fourNeighborsList);
            pointInfoList.addAll(fourNeighborsList);
        } else if (siteParkingTypeId.equals(pointTypeId)) {
            List<PointInfo> siteParkingList = getPointData(fileName, partRowIndex[4] + 1, partRowIndex[5], id, siteTypeId, siteParkingTypeId);
            log.info("停车场： {}", siteParkingList);
            pointInfoList.addAll(siteParkingList);
        } else if (siteImportantRegionTypeId.equals(pointTypeId)) {
            List<SpecialServiceArchivesForm> importantRegionForms = getArchivesForm(siteTypeId, siteImportantRegionTypeId);
            List<ReadSheet> sheetList = ExcelUtils.getSheetList(fileName);
            for (ReadSheet sheet : sheetList) {
                if (sheet.getSheetNo() != 0) {
                    JSONObject importantRegion = ExcelUtils.getArchivesBasicInfo(fileName, sheet.getSheetNo(), 100, importantRegionForms);
                    PointInfo point = new PointInfo().setData(importantRegion)
                            .setChildTypeId(siteImportantRegionTypeId)
                            .setParentTypeId(siteTypeId)
                            .setJcxxId(id);
                    String titleId = baseMapper.getIdByFieldName(pointFieldName, importantRegion.get(pointFieldName).toString(), siteImportantRegionTypeId);
                    if (titleId != null) {
                        point.setId(titleId);
                    }
                    pointInfoList.add(point);
                }
            }
        } else {
            pointInfoList = getImportData(file, id, null, pointTypeId);
        }
        saveOrUpdateBatch(pointInfoList, 1000);
    }

    List<PointInfo> getPointData(String fileName, int startRowNum, int endRowNum, String id, String parentTypeId, String childTypeId) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<PointInfo> result = new ArrayList<>();
        List<SpecialServiceArchivesForm> dataForms = getArchivesForm(parentTypeId, childTypeId);
        Class<? extends DynamicDataDTO> dataClass = ExcelUtils.getDynamicClassByArchivesForm(dataForms);
        List<JSONObject> dataList = ExcelUtils.getSheetData(fileName, 0, dataClass.getDeclaredConstructor().newInstance(), startRowNum, endRowNum);
        dataList.forEach(data -> {
            PointInfo point = new PointInfo().setData(data)
                    .setChildTypeId(childTypeId)
                    .setParentTypeId(parentTypeId)
                    .setJcxxId(id);
            //根据名称查询数据是否已存在，存在则更新
            if (data.get(pointFieldName) != null) {
                String pointId = baseMapper.getIdByFieldName(pointFieldName, data.get(pointFieldName).toString(), childTypeId);
                if (pointId != null) {
                    point.setId(pointId);
                }
                result.add(point);
            }
        });
        return result;
    }

    public List<PointInfo> getSiteMainBuildingData(String fileName, int sheetNo, Integer startRowNum, Integer endRowNum, String id, String parentTypeId, String childTypeId) throws InstantiationException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List<PointInfo> result = new ArrayList<>();
        List<SpecialServiceArchivesForm> dataForms = getArchivesForm(parentTypeId, childTypeId);
        Class<? extends DynamicDataDTO> dataClass = ExcelUtils.getDynamicClassByArchivesForm(dataForms);
        List<JSONObject> buildDTOList = new ArrayList<>();
        final JSONObject[] buildDTO = {new JSONObject()};
        EasyExcel.read().extraRead(CellExtraTypeEnum.MERGE).file(fileName).ignoreEmptyRow(true).head(dataClass.getDeclaredConstructor().newInstance().getClass()).sheet(sheetNo).headRowNumber(startRowNum)
                .registerReadListener(new ReadListener<>() {
                    @Override
                    public void invoke(Object object, AnalysisContext analysisContext) {
                        if (analysisContext.readRowHolder().getRowIndex() >= endRowNum) {
                            if (buildDTO[0] != null) {
                                buildDTO[0] = null;
                            }
                            return;
                        }
                        try {
                            Field nameField = object.getClass().getField(pointFieldName);
                            Object nameValue = nameField.get(object);
                            if (nameValue != null) {
                                JSONObject jsonObject = new JSONObject();
                                //去除内容中多余的换行符
                                for (Field field : object.getClass().getDeclaredFields()) {
                                    if (siteMainBuildingsDiningRoomNameField.equals(field.getName()) || siteMainBuildingsDiningRoomSeatField.equals(field.getName())
                                            || siteMainBuildingsMeetingRoomSeatField.equals(field.getName()) || siteMainBuildingsMeetingRoomNameField.equals(field.getName())
                                            || siteMainBuildingsVipNameField.equals(field.getName()) || siteMainBuildingsVipSeatField.equals(field.getName())) {
                                        continue;
                                    }
                                    try {
                                        field.setAccessible(true);
                                        Object value = field.get(object);
                                        if (!ObjectUtils.isEmpty(value)) {
                                            String newValue = value.toString().replaceAll(" ", "").replaceAll("\n", "");
                                            jsonObject.put(field.getName(), newValue);
                                        } else {
                                            //是否包含必填校验注解
                                            boolean isExcelValid = field.isAnnotationPresent(ExcelValid.class);
                                            if (isExcelValid) {
                                                throw new RuntimeException("文件【" + fileName + "】解析失败，sheet【" + analysisContext.readSheetHolder().getSheetName() + "】第"
                                                        + (analysisContext.readRowHolder().getRowIndex() + 1)
                                                        + "行" + field.getAnnotation(ExcelValid.class).message()
                                                );
                                            }
                                        }
                                    } catch (IllegalAccessException e) {
                                        log.error("setAccessible {} ", e.getMessage());
                                        throw new RuntimeException("导入参数检查失败");
                                    }
                                }
                                buildDTO[0] = jsonObject;
                                buildDTOList.add(buildDTO[0]);
                            }


                            getRoomData(object, siteMainBuildingsVipNameField, siteMainBuildingsVipSeatField, siteMainBuildingsVipNameField.replace(pointFieldName, ""), buildDTO[0]);
                            getRoomData(object, siteMainBuildingsMeetingRoomNameField, siteMainBuildingsMeetingRoomSeatField, siteMainBuildingsMeetingRoomNameField.replace(pointFieldName, ""), buildDTO[0]);
                            getRoomData(object, siteMainBuildingsDiningRoomNameField, siteMainBuildingsDiningRoomSeatField, siteMainBuildingsDiningRoomNameField.replace(pointFieldName, ""), buildDTO[0]);


                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                    }
                }).doRead();
        buildDTOList.forEach(data -> {
            PointInfo point = new PointInfo().setData(data)
                    .setChildTypeId(childTypeId)
                    .setParentTypeId(parentTypeId)
                    .setJcxxId(id);
            //根据名称查询数据是否已存在，存在则更新
            if (data.get(pointFieldName) != null) {
                String pointId = baseMapper.getIdByFieldName(pointFieldName, data.get(pointFieldName).toString(), childTypeId);
                if (pointId != null) {
                    point.setId(pointId);
                }
                result.add(point);
            }
        });
        return result;
    }

    public List<PointInfo> getResidenceMainBuildingData(String fileName, int sheetNo, Integer startRowNum, Integer endRowNum, String id, String parentTypeId, String childTypeId) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<PointInfo> result = new ArrayList<>();
        List<SpecialServiceArchivesForm> dataForms = getArchivesForm(parentTypeId, childTypeId);
        Class<? extends DynamicDataDTO> dataClass = ExcelUtils.getDynamicClassByArchivesForm(dataForms);
        List<JSONObject> buildDTOList = new ArrayList<>();
        final JSONObject[] buildDTO = {new JSONObject()};
        EasyExcel.read().extraRead(CellExtraTypeEnum.MERGE).file(fileName).ignoreEmptyRow(true).head(dataClass.getDeclaredConstructor().newInstance().getClass()).sheet(sheetNo).headRowNumber(startRowNum)
                .registerReadListener(new ReadListener() {
                    @Override
                    public void invoke(Object object, AnalysisContext analysisContext) {
                        if (analysisContext.readRowHolder().getRowIndex() >= endRowNum) {
                            if (buildDTO[0] != null) {
                                buildDTO[0] = null;
                            }
                            return;
                        }
                        try {
                            Field nameField = object.getClass().getField(pointFieldName);
                            Object nameValue = nameField.get(object);
                            if (nameValue != null) {
                                JSONObject jsonObject = new JSONObject();
                                //去除内容中多余的换行符
                                for (Field field : object.getClass().getDeclaredFields()) {
                                    if (residenceMainBuildingsDiningRoomNameField.equals(field.getName()) || residenceMainBuildingsDiningRoomSeatField.equals(field.getName())
                                            || residenceMainBuildingsMeetingRoomSeatField.equals(field.getName()) || residenceMainBuildingsMeetingRoomNameField.equals(field.getName())
                                            || residenceMainBuildingsVipNameField.equals(field.getName()) || residenceMainBuildingsVipSeatField.equals(field.getName())) {
                                        continue;
                                    }
                                    try {
                                        field.setAccessible(true);
                                        Object value = field.get(object);
                                        if (!ObjectUtils.isEmpty(value)) {
                                            String newValue = value.toString().replaceAll(" ", "").replaceAll("\n", "");
                                            jsonObject.put(field.getName(), newValue);
                                        } else {
                                            //是否包含必填校验注解
                                            boolean isExcelValid = field.isAnnotationPresent(ExcelValid.class);
                                            if (isExcelValid) {
                                                throw new RuntimeException("文件【" + fileName + "】解析失败，sheet【" + analysisContext.readSheetHolder().getSheetName() + "】第"
                                                        + (analysisContext.readRowHolder().getRowIndex() + 1)
                                                        + "行" + field.getAnnotation(ExcelValid.class).message()
                                                );
                                            }
                                        }
                                    } catch (IllegalAccessException e) {
                                        log.error("setAccessible {} ", e.getMessage());
                                        throw new RuntimeException("导入参数检查失败");
                                    }
                                }
                                buildDTO[0] = jsonObject;
                                buildDTOList.add(buildDTO[0]);
                            }


                            getRoomData(object, residenceMainBuildingsVipNameField, residenceMainBuildingsVipSeatField, residenceMainBuildingsVipNameField.replace(pointFieldName, ""), buildDTO[0]);
                            getRoomData(object, residenceMainBuildingsMeetingRoomNameField, residenceMainBuildingsMeetingRoomSeatField, residenceMainBuildingsMeetingRoomNameField.replace(pointFieldName, ""), buildDTO[0]);
                            getRoomData(object, residenceMainBuildingsDiningRoomNameField, residenceMainBuildingsDiningRoomSeatField, residenceMainBuildingsDiningRoomNameField.replace(pointFieldName, ""), buildDTO[0]);


                        } catch (IllegalAccessException | NoSuchFieldException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                    }
                }).doRead();
        buildDTOList.forEach(data -> {
            PointInfo point = new PointInfo().setData(data)
                    .setChildTypeId(childTypeId)
                    .setParentTypeId(parentTypeId)
                    .setJcxxId(id);
            //根据名称查询数据是否已存在，存在则更新
            if (data.get(pointFieldName) != null) {
                String pointId = baseMapper.getIdByFieldName(pointFieldName, data.get(pointFieldName).toString(), childTypeId);
                if (pointId != null) {
                    point.setId(pointId);
                }
                result.add(point);
            }
        });
        return result;
    }

    private JSONObject getRoomData(Object object, String name, String seat, String listName, JSONObject buildDTO) {
        JSONObject viPMap = new JSONObject();
        try {
            Field vipNameField = object.getClass().getField(name);
            Object vipNameValue = vipNameField.get(object);
            if (vipNameValue != null) {
                viPMap.put(name, vipNameValue);
            }
            Field vipSeatField = object.getClass().getField(seat);
            Object vipSeatValue = vipSeatField.get(object);
            if (vipSeatValue != null) {
                viPMap.put(seat, vipSeatValue);
            }
            List list = (List) buildDTO.get(listName);
            if (list == null) {
                List<JSONObject> a = new ArrayList<>();
                if (!CollectionUtils.isEmpty(viPMap)) {
                    a.add(viPMap);
                    buildDTO.put(listName, a);
                }
            } else {
                if (!CollectionUtils.isEmpty(viPMap)) {
                    list.add(viPMap);
                    buildDTO.put(listName, list);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return buildDTO;

    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void importResidenceArchiveData(MultipartFile file, String id, String pointTypeId, String nameVerifyFlag) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        //接收文件
        String fileName = ExcelUtils.receiveFile(file);
        List<String> partNames = specialServiceArchivesMapper.selectList(new LambdaQueryWrapper<SpecialServiceArchives>()
                        .eq(SpecialServiceArchives::getDelFlag, "0")
                        .eq(SpecialServiceArchives::getParentId, residenceTypeId)
                        .eq(SpecialServiceArchives::getSpecialType, 1)
                        .ne(SpecialServiceArchives::getId, residenceBasicInfoTypeId)
                        .orderByAsc(SpecialServiceArchives::getSort)).stream().map(SpecialServiceArchives::getArchivesName)
                .collect(Collectors.toList());
        int[] partRowIndex = ExcelUtils.getArchivesPartRowIndex(fileName, 0, partNames);


        List<PointInfo> pointInfoList = new ArrayList<>();

        if (residenceInsideOrgTypeId.equals(pointTypeId)) {
            List<PointInfo> siteInsideOrgList = getPointData(fileName, partRowIndex[0] + 1, partRowIndex[1], id, residenceTypeId, residenceInsideOrgTypeId);
            log.info("内设机构： {}", siteInsideOrgList);
            pointInfoList.addAll(siteInsideOrgList);
        } else if (residenceMainBuildingsTypeId.equals(pointTypeId)) {
            List<PointInfo> mainBuildingsList = getResidenceMainBuildingData(fileName, 0, partRowIndex[1] + 1, partRowIndex[2], id, residenceTypeId, residenceMainBuildingsTypeId);
            log.info("主要建筑： {}", mainBuildingsList);
            pointInfoList.addAll(mainBuildingsList);
        } else if (residenceImportantPartTypeId.equals(pointTypeId)) {
            List<PointInfo> importantPartList = getPointData(fileName, partRowIndex[2] + 1, partRowIndex[3], id, residenceTypeId, residenceImportantPartTypeId);
            log.info("重要部位： {}", importantPartList);
            pointInfoList.addAll(importantPartList);
        } else if (residenceFourNeighborsTypeId.equals(pointTypeId)) {
            List<PointInfo> fourNeighborsList = getPointData(fileName, partRowIndex[3] + 1, partRowIndex[4], id, residenceTypeId, residenceFourNeighborsTypeId);
            log.info("四邻情况： {}", fourNeighborsList);
            pointInfoList.addAll(fourNeighborsList);
        } else if (residenceParkingTypeId.equals(pointTypeId)) {
            List<PointInfo> siteParkingList = getPointData(fileName, partRowIndex[4] + 1, partRowIndex[5], id, residenceTypeId, residenceParkingTypeId);
            log.info("停车场： {}", siteParkingList);
            pointInfoList.addAll(siteParkingList);
        } else {
            pointInfoList = getImportData(file, id, null, pointTypeId);
        }
        saveOrUpdateBatch(pointInfoList, 1000);
    }

    @Override
    public void downloadTemplate(String pointTypeId, HttpServletResponse response) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        SpecialServiceArchives serviceArchives = specialServiceArchivesMapper.selectById(pointTypeId);
        String title = serviceArchives.getArchivesName();
        String parentTitle = specialServiceArchivesMapper.selectById(serviceArchives.getParentId()).getArchivesName();
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(parentTitle + "-" + title + ".xls", StandardCharsets.UTF_8));
        //photo video point
        List<SpecialServiceArchivesForm> specialServiceArchivesForms = specialServiceArchivesFormMapper.selectList(new LambdaQueryWrapper<SpecialServiceArchivesForm>()
                .eq(SpecialServiceArchivesForm::getDelFlag, "0")
                .eq(SpecialServiceArchivesForm::getArchivesId, pointTypeId)
                //不需要处理的类型
                .notIn(SpecialServiceArchivesForm::getFieldType, List.of("photo", "video", "point"))
                .orderByAsc(SpecialServiceArchivesForm::getSort));
        //需要特殊处理的行
        List<Integer> rowIndexes = Arrays.asList(0, 1);
        //自定义标题和内容策略(具体定义在下文)
        CellStyleStrategy cellStyleStrategy =
                new CellStyleStrategy(rowIndexes, new WriteCellStyle(), new WriteCellStyle());
        Class<? extends DynamicDataDTO> dataClass = ExcelUtils.getDynamicClass(title + "表", specialServiceArchivesForms);
        EasyExcel.write(response.getOutputStream(), dataClass.getDeclaredConstructor().newInstance().getClass()).sheet(title)
                .registerWriteHandler(new SimpleColumnWidthStyleStrategy(15)).registerWriteHandler(cellStyleStrategy).doWrite(new ArrayList<>());
    }

    @Override
    public List<Map<String, String>> getPointsByName(String name) {
        return baseMapper.getPointsByName(name);
    }

    @Override
    public boolean updatePosition(String longitude, String latitude, String id) {
        PointInfo pointInfo = getById(id);
        pointInfo.getData().put(Constant.longitude, longitude);
        pointInfo.getData().put(Constant.latitude, latitude);
        return updatePoint(pointInfo);
    }

    @Override
    public void updatePointExtInfo() {
        List<PointExtInfo> pointExtInfoList = new ArrayList<>();
        Page<PointInfo> pointInfoPage = new Page<>(1, 1000);
        while (true) {
            Page<PointInfo> page = baseMapper.getPointInfoPage(pointInfoPage);
            if (page.getTotal() == 0) {
                break;
            }
            for (PointInfo pointInfo : page.getRecords()) {
                PointExtInfo pointExtInfo = new PointExtInfo();
                pointExtInfo.setId(pointInfo.getId());
                String name = "";
                if (pointInfo.getData().get(pointFieldName) != null) {
                    name = pointInfo.getData().get(pointFieldName).toString();
                } else if (pointInfo.getData().get(infoFieldName) != null) {
                    name = pointInfo.getData().get(infoFieldName).toString();
                }
                if ("".equals(name) || "无".equals(name)) {
                    continue;
                }
                pointExtInfo.setName(name);
                pointExtInfo.setType(pointInfo.getType());
                //经纬度特殊处理
                String longitude = object2String(pointInfo.getData().get(Constant.longitude));
                String latitude = object2String(pointInfo.getData().get(Constant.latitude));
                if (longitude != null && latitude != null) {
                    try {
                        String point = "POINT(" + Double.parseDouble(longitude) + " " + Double.parseDouble(latitude) + ")";
                        //空间位置特殊处理
                        pointExtInfo.setGeom(point);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
                pointExtInfoList.add(pointExtInfo);
            }
            try {
                pointExtInfoService.saveOrUpdateBatch(pointExtInfoList);
            } catch (Exception e) {
                log.error(e.getMessage());
                break;
            }
            if (pointInfoPage.getCurrent() == pointInfoPage.getTotalPages()) {
                break;
            }
            pointInfoPage.setCurrent(pointInfoPage.getCurrent() + 1);
        }
    }

    @Override
    public List getBasicDataNameList(String type) {
        List nameList = new ArrayList();
        List<PointInfo> dataList = getBasicDataList(type);
        for (PointInfo pointInfo : dataList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", pointInfo.getId());
            jsonObject.put("name", pointInfo.getData().getString("title"));
            nameList.add(jsonObject);
        }
        return nameList;
    }

    @Override
    public List getBasicDataList(String type) {
        List<PointInfo> dataList = new ArrayList();
        switch (type) {
            case "1"://高速
                dataList = list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getChildTypeId, highwayBasicInfoTypeId).isNull(PointInfo::getJcxxId));
                break;
            case "2"://高铁
                dataList = list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getChildTypeId, railwayBasicInfoTypeId).isNull(PointInfo::getJcxxId));
                break;
            case "3"://现场
                dataList = list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getChildTypeId, siteBasicInfoTypeId).isNull(PointInfo::getJcxxId));
                break;
            case "4"://住地
                dataList = list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getChildTypeId, residenceBasicInfoTypeId).isNull(PointInfo::getJcxxId));
                break;
        }
        return dataList;
    }

    private JSONObject getPointData(String id, String name, String title, String typeId) {
        List crkList = new ArrayList();
        list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getChildTypeId, typeId)).forEach(crk -> {
            JSONObject highwayCrk = new JSONObject();
            highwayCrk.put("id", crk.getId());
            highwayCrk.put("name", crk.getData().get(title));
            highwayCrk.put("data", crk.getData());
            highwayCrk.put("children", new ArrayList<>());
            crkList.add(highwayCrk);
        });
        JSONObject crkData = new JSONObject();
        crkData.put("id", id);
        crkData.put("name", name);
        crkData.put("data", "");
        crkData.put("children", crkList);
        return crkData;
    }

    @Override
    public PointInfo getByName(String name) {
        return baseMapper.getByName(name);
    }

    @Override
    public void importPoint(MultipartFile file) {
        String fileName = ExcelUtils.receiveFile(file);
        List<PointInfo> pointInfoList = new ArrayList<>();
        EasyExcel.read().extraRead(CellExtraTypeEnum.MERGE).ignoreEmptyRow(true).file(fileName).sheet(0).head(BasicPoint.class) //按指定类解析
                .headRowNumber(1) //从指定行开始解析
                .registerReadListener(new ReadListener<BasicPoint>() {
                    @Override
                    public void invoke(BasicPoint object, AnalysisContext analysisContext) {
                        PointInfo pointInfo = new PointInfo();
                        BeanUtils.copyProperties(object, pointInfo);
                        pointInfo.setId(object.get_id());
                        pointInfoList.add(pointInfo);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    }
                }).doRead();
        saveOrUpdateBatch(pointInfoList);
        updatePointExtInfo();
    }

    @Override
    public List<PointInfo> selectPointList(String childTypeId) {
        if ("a9bc87316f394049a5e2f593a6ab42ae".equals(childTypeId)) {
            return list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getChildTypeId, childTypeId).isNotNull(PointInfo::getJcxxId));
        } else {
            return list(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getChildTypeId, childTypeId));
        }

    }

    @Override
    public List<Map<String, Object>> selectPointList(String id, String jcxxId) {
        return listMaps(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getChildTypeId, id)
                .eq(PointInfo::getJcxxId, jcxxId));
    }

    @Override
    public long selectPointCount(String jcxxId, String id, String pointId) {
        return count(new LambdaQueryWrapper<PointInfo>()
                .eq(PointInfo::getJcxxId, jcxxId)
                .eq(PointInfo::getParentTypeId, id)
                .eq(PointInfo::getChildTypeId, pointId));
    }

    @Override
    public List<PointInfo> queryListByParentId(String parentTypeId) {
        return baseMapper.queryListByParentId(parentTypeId);
    }

    @Override
    public List<PointInfo> queryListByJcxxId(String jcxxId) {
        return baseMapper.queryListByJcxxId(jcxxId);
    }

    @Override
    public void addPoint(JSONObject map) {
        PointInfo pointInfo = new PointInfo();
        //  没有id 的时候保存
        if (ObjectUtil.isEmpty(map.get("id"))) {
            pointInfo.setId(IdWorker.get32UUID());
            // 添加时间
            pointInfo.setCreateTime(LocalDateTime.now());
            pointInfo.setParentTypeId(map.get("parentTypeId").toString());
            pointInfo.setChildTypeId(map.get("childTypeId").toString());
            pointInfo.setData(map.getJSONObject("data"));
            save(pointInfo);
        } else if (ObjectUtil.isNotEmpty(map.get("id"))) {
            pointInfo.setId(map.get("id").toString());
            pointInfo.setData(map.getJSONObject("data"));
            updateById(pointInfo);
        }
        PointExtInfo pointExtInfo = new PointExtInfo();
        pointExtInfo.setId(pointInfo.getId());
        String name = "";
        if (pointInfo.getData().get(pointFieldName) != null) {
            name = pointInfo.getData().get(pointFieldName).toString();
        } else if (pointInfo.getData().get(infoFieldName) != null) {
            name = pointInfo.getData().get(infoFieldName).toString();
        }
        pointExtInfo.setName(name);
        String type = baseMapper.getPointDataType(pointInfo.getId());
        pointExtInfo.setType(type);
        //经纬度特殊处理
        String longitude = object2String(pointInfo.getData().get(Constant.longitude));
        String latitude = object2String(pointInfo.getData().get(Constant.latitude));
        if (longitude != null && latitude != null) {
            try {
                String point = "POINT(" + Double.parseDouble(longitude) + " " + Double.parseDouble(latitude) + ")";
                //空间位置特殊处理
                pointExtInfo.setGeom(point);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        pointExtInfoService.saveOrUpdate(pointExtInfo);
    }

    @Override
    public List<PointInfo> queryListSort(JSONObject map, String sortField, String direction) {
        QueryWrapper<PointInfo> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isEmpty(direction)) {
            direction = "DESC";
        }
        if (direction.equals("ASC")) {
            queryWrapper.orderByAsc(sortField);
        } else {
            queryWrapper.orderByDesc(sortField);
        }
        LambdaQueryWrapper<PointInfo> lambdaQueryWrapper = queryWrapper.lambda();
        if (ObjectUtil.isNotEmpty(map.get("parentTypeId"))) {
            lambdaQueryWrapper.eq(PointInfo::getParentTypeId, map.get("parentTypeId"));
        }
        if (ObjectUtil.isNotEmpty(map.get("jcxxId"))) {
            lambdaQueryWrapper.eq(PointInfo::getJcxxId, map.get("jcxxId"));
        } else {
            lambdaQueryWrapper.isNull(PointInfo::getJcxxId);
        }
        if (ObjectUtil.isNotEmpty(map.get("id"))) {
            lambdaQueryWrapper.eq(PointInfo::getId, map.get("id"));
        }

        return list(lambdaQueryWrapper);
    }

    @Override
    public void deleteByJcxxId(String jcxxId) {
        remove(new LambdaQueryWrapper<PointInfo>().eq(PointInfo::getJcxxId, jcxxId));
    }

    @Override
    public String saveArchivesInfo(PointInfo pointInfo) {
        if (ObjectUtil.isEmpty(pointInfo.getId())) {
            pointInfo.setId(IdWorker.get32UUID());
        }
        AtomicBoolean flag = new AtomicBoolean(false);
        pointInfo.getData().entrySet().forEach(entry -> {
            if (entry.getValue() != null && StringUtils.hasText(entry.getValue().toString())) {
                if (entry.getValue().getClass() == String.class && !"point".equals(entry.getKey())) {
                    entry.getValue().toString().startsWith("$number");
                    flag.set(true);
                }
            }
        });
        if (!flag.get()) {
            return "";
        }
        saveOrUpdate(pointInfo);
        PointExtInfo pointExtInfo = new PointExtInfo();
        pointExtInfo.setId(pointInfo.getId());

        String name = "";
        if (pointInfo.getData().get(pointFieldName) != null) {
            name = pointInfo.getData().get(pointFieldName).toString();
        } else if (pointInfo.getData().get(infoFieldName) != null) {
            name = pointInfo.getData().get(infoFieldName).toString();
        }
        if ("".equals(name) || "无".equals(name)) {
            return pointInfo.getId();
        }
        pointExtInfo.setName(name);
        String type = baseMapper.getPointDataType(pointInfo.getId());
        pointExtInfo.setType(type);
        //经纬度特殊处理
        String longitude = object2String(pointInfo.getData().get(Constant.longitude));
        String latitude = object2String(pointInfo.getData().get(Constant.latitude));
        if (longitude != null && latitude != null) {
            try {
                String point = "POINT(" + Double.parseDouble(longitude) + " " + Double.parseDouble(latitude) + ")";
                //空间位置特殊处理
                pointExtInfo.setGeom(point);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        pointExtInfoService.saveOrUpdate(pointExtInfo);
        return pointInfo.getId();
    }

    @Override
    public List getFeatureTypeData(String id) {
        return baseMapper.getFeatureTypeData(id);
    }

    @Override
    public List<FeaturePositionData> getFeaturePosition(String jcxxId, String typeId) {
        return baseMapper.getFeaturePosition(jcxxId, typeId);
    }


    private List<SpecialServiceArchivesForm> getArchivesForm(String typeId, String childTypeId) {
        LambdaQueryWrapper<SpecialServiceArchivesForm> lambdaQueryWrapper = new LambdaQueryWrapper<SpecialServiceArchivesForm>()
                .eq(SpecialServiceArchivesForm::getDelFlag, "0")
                .ne(SpecialServiceArchivesForm::getMandatory, "2")
                .eq(SpecialServiceArchivesForm::getArchivesId, childTypeId);
        if (StringUtils.hasText(typeId)) {
            lambdaQueryWrapper.eq(SpecialServiceArchivesForm::getArchivesParentId, typeId);
        }
        List<SpecialServiceArchivesForm> specialServiceArchivesForms = specialServiceArchivesFormMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(specialServiceArchivesForms)) {
            throw new RuntimeException("没有匹配的表单项配置");
        }
        return specialServiceArchivesForms;
    }

}
