package com.xaxc.teqin.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xaxc.teqin.common.model.CustomClaim;
import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.util.Utils;
import com.xaxc.teqin.component.AuthInterceptor;
import com.xaxc.teqin.excel.ExcelUtils;
import com.xaxc.teqin.excel.ExcelValid;
import com.xaxc.teqin.mapper.ResidentialBuildingInfoMapper;
import com.xaxc.teqin.mapper.ResidentialBuildingPositionMapper;
import com.xaxc.teqin.model.dto.*;
import com.xaxc.teqin.model.entity.*;
import com.xaxc.teqin.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-11-25
 */
@Slf4j
@Service
public class BuildingInfoServiceImpl {

    @Value("${file.uploadPath}")
    private String uploadPath;

    @Value("#{'${communityData.tables}'.split(',')}")
    private List<String> communityDataTables;

    @Value("${communityData.view}")
    private String communityDataView;

//    @Value("#{'${communityData.filterChars}'.split(',')}")
//    private List<String> filterChars;


    @Resource
    ICommercialBuildingInfoService commercialBuildingInfoService;

    @Resource
    IResidentialBuildingInfoService residentialBuildingInfoService;

    @Resource
    ICommercialRoomInfoService commercialRoomInfoService;

    @Resource
    ICommercialLeaseInfoService commercialLeaseInfoService;

    @Resource
    ICommercialEnterpriseInfoService commercialEnterpriseInfoService;

    @Resource
    IHouseInfoService houseInfoService;

    @Resource
    IResidenceInfoService residenceInfoService;

    @Resource
    IRegionInfoService regionInfoService;

    @Resource
    IRoleService roleService;

    @Resource
    IFileInfoService fileInfoService;

    @Resource
    ResidentialBuildingPositionMapper buildingPositionMapper;

    @Resource
    ImportRecordsServiceImpl importRecordsService;

    @Resource
    SqlScriptServiceImpl sqlScriptService;


    public JSONObject getBuildingList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("commercialList", commercialBuildingInfoService.list());

//        List<ResidentialBuildingInfo> residentialBuildingInfoList = residentialBuildingInfoService.list(
//                new LambdaQueryWrapper<ResidentialBuildingInfo>().select(List.of(ResidentialBuildingInfo::getId
//                        , ResidentialBuildingInfo::getBuildingName
//                        , ResidentialBuildingInfo::getFocusNumber,
//                        ResidentialBuildingInfo::getX,
//                        ResidentialBuildingInfo::getY,
//                        ResidentialBuildingInfo::getZ)));

//        residentialBuildingInfoList.forEach(building -> {
//            List<String> houseIds = houseInfoService.list(new LambdaQueryWrapper<HouseInfo>()
//                    .eq(HouseInfo::getBuildingId, building.getId())).stream().map(HouseInfo::getId).collect(Collectors.toList());
////            if (StringUtils.hasText(building.getLongitude()) && StringUtils.hasText(building.getLatitude()) && !StringUtils.hasText(building.getX())) {
////                try {
////                    ProjCoordinate projCoordinate = Utils.wgs84ToCgcs2000(Double.parseDouble(building.getLongitude()), Double.parseDouble(building.getLatitude()));
////                    building.setX(projCoordinate.x + "");
////                    building.setY(projCoordinate.y + "");
////                    building.setZ(projCoordinate.z + "");
////                } catch (Exception e) {
////                    log.error(e.getMessage());
////                }
////            }
//            if (!CollectionUtils.isEmpty(houseIds)) {
//                Long num = residenceInfoService.count(new LambdaQueryWrapper<ResidenceInfo>()
//                        .in(ResidenceInfo::getHouseId, houseIds)
//                        .eq(ResidenceInfo::getFocusFlag, "1"));
//                building.setFocusNumber(num.intValue());
//            } else {
//                building.setFocusNumber(0);
//            }
//        });
        jsonObject.put("residentialList", new ArrayList<>());
        jsonObject.put("regionList", regionInfoService.list(new LambdaQueryWrapper<RegionInfo>().isNotNull(RegionInfo::getX)));
        List<CommercialEnterpriseInfo> commercialEnterpriseInfoList = commercialEnterpriseInfoService.list(new LambdaQueryWrapper<CommercialEnterpriseInfo>()
                .eq(CommercialEnterpriseInfo::getKeyFlag, 1));
        commercialEnterpriseInfoList.forEach(commercialEnterpriseInfo -> {
            CommercialLeaseInfo commercialLeaseInfo = commercialLeaseInfoService.getOne(new LambdaQueryWrapper<CommercialLeaseInfo>()
                    .eq(CommercialLeaseInfo::getTenant, commercialEnterpriseInfo.getName()), false);
            if (commercialLeaseInfo != null) {
                CommercialRoomInfo commercialRoomInfo = commercialRoomInfoService.getOne(new LambdaQueryWrapper<CommercialRoomInfo>()
                        .eq(CommercialRoomInfo::getRoomCode, commercialLeaseInfo.getCode()), false);
                if (commercialRoomInfo != null) {
                    CommercialBuildingInfo commercialBuildingInfo = commercialBuildingInfoService.getOne(new LambdaQueryWrapper<CommercialBuildingInfo>()
                            .eq(CommercialBuildingInfo::getId, commercialRoomInfo.getBuildingId()), false);
                    commercialEnterpriseInfo.setX("0".equals(commercialRoomInfo.getX()) || null == commercialRoomInfo.getX() ? commercialBuildingInfo.getX() : commercialRoomInfo.getX());
                    commercialEnterpriseInfo.setY("0".equals(commercialRoomInfo.getY()) || null == commercialRoomInfo.getY() ? commercialBuildingInfo.getY() : commercialRoomInfo.getY());
                    commercialEnterpriseInfo.setZ(commercialBuildingInfo.getZ());
                }
            }
        });
        jsonObject.put("keyEnterpriseList", commercialEnterpriseInfoList);
        return jsonObject;
    }

    public Integer getFloorNumber(String buildingId) {
        ResidentialBuildingInfo residentialBuildingInfo = residentialBuildingInfoService.getById(buildingId);
        if (residentialBuildingInfo != null) {
            return residentialBuildingInfo.getBuildingFloor();
        }
        CommercialBuildingInfo commercialBuildingInfo = commercialBuildingInfoService.getById(buildingId);
        if (commercialBuildingInfo != null) {
            return commercialBuildingInfo.getFloorNumber();
        }
        return null;
    }


    public List<CommercialRoomInfo> getRoomLeaseData(String buildingId, Integer floorNumber) {
        return commercialRoomInfoService.getRoomLeaseData(buildingId, floorNumber);
    }

    public Map getBuildingAndEnterpriseData(int type) {
        Map map = new LinkedHashMap();
        List<CommercialBuildingInfo> commercialBuildingInfoList = commercialBuildingInfoService.list(new LambdaQueryWrapper<CommercialBuildingInfo>()
                .isNotNull(CommercialBuildingInfo::getX)
                .isNotNull(CommercialBuildingInfo::getY)
                .isNotNull(CommercialBuildingInfo::getZ));
        map.put("buildingData", commercialBuildingInfoList);
        List<CommercialRoomInfo> commercialRoomInfoList = commercialRoomInfoService.getLeaseEnterpriseData(type);
        map.put("enterpriseData", commercialRoomInfoList);
        return map;
    }


    public List<CommercialEnterpriseInfo> getFloorTenantData(String buildingId, Integer floorNumber) {
        List<CommercialEnterpriseInfo> commercialEnterpriseInfos = new ArrayList<>();
        List<CommercialRoomInfo> roomInfoList = commercialRoomInfoService.getRoomLeaseData(buildingId, floorNumber);
        roomInfoList = roomInfoList.stream().filter(room -> room.getTenant() != null).collect(Collectors.toList());
        roomInfoList.forEach(room -> {
            commercialEnterpriseInfos.add(getCommercialEnterpriseInfo(room.getRoomCode()));
        });
        return commercialEnterpriseInfos;
    }

    public CommercialEnterpriseInfo getCommercialEnterpriseInfo(String roomCode) {
        CommercialLeaseInfo commercialLeaseInfo = commercialLeaseInfoService.getOne(new LambdaQueryWrapper<CommercialLeaseInfo>()
                .eq(CommercialLeaseInfo::getCode, roomCode));
        if (commercialLeaseInfo != null && commercialLeaseInfo.getTenant() != null) {
            List<String> rooms = commercialLeaseInfoService.list(new LambdaQueryWrapper<CommercialLeaseInfo>().eq(CommercialLeaseInfo::getTenant, commercialLeaseInfo.getTenant()))
                    .stream().map(CommercialLeaseInfo::getTitle).collect(Collectors.toList());
            CommercialEnterpriseInfo commercialEnterpriseInfo = commercialEnterpriseInfoService.getOne(new LambdaQueryWrapper<CommercialEnterpriseInfo>()
                    .eq(CommercialEnterpriseInfo::getName, commercialLeaseInfo.getTenant()));
            if (commercialEnterpriseInfo == null) {
                commercialEnterpriseInfo = new CommercialEnterpriseInfo();
                commercialEnterpriseInfo.setName(commercialLeaseInfo.getTenant());
            }
            commercialEnterpriseInfo.setPositions(rooms);
            commercialEnterpriseInfo.setTitle(commercialLeaseInfo.getTitle());
            return commercialEnterpriseInfo;
        }
        return null;
    }

    public CommercialEnterpriseInfo getTenantData(String id, String roomCode) {
        CommercialRoomInfo commercialRoomInfo = commercialRoomInfoService.getOne(new LambdaQueryWrapper<CommercialRoomInfo>()
                .eq(CommercialRoomInfo::getId, id).or().eq(CommercialRoomInfo::getRoomCode, roomCode));
        return getCommercialEnterpriseInfo(commercialRoomInfo.getRoomCode());
    }

    public List<ResidentialBuildingInfo> getResidentialBuildingData() {
//        return residentialBuildingInfoService.list(new LambdaQueryWrapper<ResidentialBuildingInfo>()
//                .isNotNull(ResidentialBuildingInfo::getX)
//                .isNotNull(ResidentialBuildingInfo::getY)
//                .isNotNull(ResidentialBuildingInfo::getZ));
        return null;
    }

    @Resource
    ResidentialBuildingInfoMapper residentialBuildingInfoMapper;

    @Resource
    IBuildingNameRefService buildingNameRefService;

    public ResidentialBuildingInfo getResidentialBuildingData(String buildingName) {
        ResidentialBuildingInfo residentialBuildingInfo = residentialBuildingInfoMapper.getRdBuildingInfo(buildingName);
        return getRdResidentialBuildingInfo(residentialBuildingInfo, buildingName);
    }

    public ResidentialBuildingInfo getRdResidentialBuildingInfo(ResidentialBuildingInfo buildingInfo, String buildingName) {
        if (buildingInfo == null) {
            //查询映射表是否有数据
            BuildingNameRef buildingNameRef = buildingNameRefService.getOne(new LambdaQueryWrapper<BuildingNameRef>()
                    .eq(BuildingNameRef::getLayerTagName, buildingName), false);
            if (buildingNameRef != null && StringUtils.hasText(buildingNameRef.getBuildingName())) {
                buildingInfo = residentialBuildingInfoMapper.getRdBuildingInfo(buildingNameRef.getBuildingName());
            }
            if (buildingInfo == null) {
                return null;
            }
        }
        boolean permission = roleService.hasPermission();
        JSONObject houseData = new JSONObject();
        Comparator<HouseInfo> houseNumberComparator = Comparator.comparing(HouseInfo::getHouseNumber);
        Comparator<HouseInfo> focusNumberComparator = Comparator.comparingInt(HouseInfo::getFocusNumber);
        for (int i = 1; i <= buildingInfo.getUnitNumber(); i++) {
            //获取每个单元的房屋信息
            List<HouseInfo> hourseInfoList = residentialBuildingInfoMapper.getRdHouseList(buildingInfo.getBuildingName(), i);
            LinkedHashMap floorData = new LinkedHashMap();
            Map<String, String> unitData = residentialBuildingInfoMapper.getRdBuildingUnitData(buildingInfo.getBuildingName(), i);
            int unitFloor = Integer.parseInt(unitData.get("floor_number"));
            int unitHousehold = Integer.parseInt(unitData.get("household_number"));
            for (int j = unitFloor; j > 0; j--) {
                int finalJ = j;
                //房间号去重处理
                List<HouseInfo> hoursCodeList = hourseInfoList.stream().filter(r -> r.getFloorNumber().intValue() == finalJ).collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(houseNumberComparator.thenComparing(focusNumberComparator))), ArrayList::new));
                try {
                    if (CollectionUtils.isEmpty(hoursCodeList)) {
                        hoursCodeList = new ArrayList<>();
                        for (int k = 1; k <= unitHousehold; k++) {
                            //String houseNumber = k > 9 ? j + "" + k : j + "0" + k;
                            String houseNumber = String.format("%d%02d", j, k);
                            hoursCodeList.add(new HouseInfo().setHouseNumber(houseNumber).setId("0_0_0"));
                        }
                    } else {
                        List<HouseInfo> tmphoursCodeList = new ArrayList<>();
                        for (int k = 1; k <= unitHousehold; k++) {
                            //String houseNumber = k > 9 ? j + "" + k : j + "0" + k;
                            String houseNumber = String.format("%d%02d", j, k);
                            List<HouseInfo> macthList = hoursCodeList.stream().filter(h -> houseNumber.equals(h.getHouseNumber())).collect(Collectors.toList());
                            if (macthList.size() > 0) {
                                List<HouseInfo> macthHouseList = macthList.stream().filter(m -> m.getFocusNumber() > 0).collect(Collectors.toList());
                                tmphoursCodeList.add(CollectionUtils.isEmpty(macthHouseList) ? macthList.get(0) : macthHouseList.get(0));
                            } else {
                                tmphoursCodeList.add(new HouseInfo().setHouseNumber(houseNumber).setId("0_0_0"));
                            }
                        }
                        hoursCodeList = tmphoursCodeList;
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                    continue;
                }
                floorData.put(j + "", hoursCodeList);
            }

            hourseInfoList.forEach(houseInfo -> {
                if (!permission) {
                    houseInfo.setOwnerUserName(Utils.encryptName(houseInfo.getOwnerUserName()));
                    houseInfo.setOwnerUserTel(Utils.encryptPhone(houseInfo.getOwnerUserTel()));
                }
            });
            houseData.put(i + "", floorData);
        }
        buildingInfo.setHouseData(houseData);
        return buildingInfo;
    }

    public ResidentialBuildingInfo getResidentialBuildingInfo(ResidentialBuildingInfo residentialBuildingInfo, String buildingId) {
        ResidentialBuildingInfo buildingInfo = null;
        if (residentialBuildingInfo != null) {
            buildingInfo = residentialBuildingInfo;
        } else {
            buildingInfo = residentialBuildingInfoService.getById(buildingId);
        }
        if (buildingInfo == null) {
            return null;
        }
        JSONObject houseData = new JSONObject();
        for (int i = 1; i <= buildingInfo.getUnitNumber(); i++) {
            List<HouseInfo> hourseInfoList = houseInfoService.list(new LambdaQueryWrapper<HouseInfo>().eq(HouseInfo::getBuildingId, buildingInfo.getId())
                    .eq(HouseInfo::getBuildingCell, i + "").orderByAsc(HouseInfo::getHouseNumber));
            LinkedHashMap floorData = new LinkedHashMap();
            int householdNumOfFloor = 0;
            try {
                householdNumOfFloor = residentialBuildingInfoService.getHouseholdNumOfFloor(buildingInfo.getId(), i + "");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            if (buildingInfo.getBuildingFloor() == null || buildingInfo.getBuildingFloor().intValue() == 0) {
                try {
                    int buildingFloor = residentialBuildingInfoService.getHouseFloor(buildingInfo.getId(), i + "");
                    buildingInfo.setBuildingFloor(buildingFloor);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
            for (int j = buildingInfo.getBuildingFloor(); j > 0; j--) {
                int finalJ = j;
                //房间号去重处理
                List<HouseInfo> hoursCodeList = hourseInfoList.stream().filter(r -> r.getFloorNumber().intValue() == finalJ).collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(f -> f.getHouseNumber()))), ArrayList::new));
                try {
                    if (CollectionUtils.isEmpty(hoursCodeList)) {
                        hoursCodeList = new ArrayList<>();
                        for (int k = 1; k <= householdNumOfFloor; k++) {
                            String houseNumber = j + "0" + k;
                            hoursCodeList.add(new HouseInfo().setHouseNumber(houseNumber).setId("0_0_0"));
                        }
                    }
                    if (hoursCodeList.size() < householdNumOfFloor) {
                        List<HouseInfo> tmphoursCodeList = new ArrayList<>();
                        for (int k = 1; k <= householdNumOfFloor; k++) {
                            String houseNumber = j + "0" + k;
                            List<HouseInfo> macthList = hoursCodeList.stream().filter(h -> houseNumber.equals(h.getHouseNumber())).collect(Collectors.toList());
                            if (macthList.size() > 0) {
                                tmphoursCodeList.add(macthList.get(0));
                            } else {
                                tmphoursCodeList.add(new HouseInfo().setHouseNumber(houseNumber).setId("0_0_0"));
                            }
                        }
                        hoursCodeList = tmphoursCodeList;
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                floorData.put(j + "", hoursCodeList);
            }

            hourseInfoList.forEach(houseInfo -> {
                if (!roleService.hasPermission()) {
                    houseInfo.setOwnerUserName(Utils.encryptName(houseInfo.getOwnerUserName()));
                    houseInfo.setOwnerUserTel(Utils.encryptPhone(houseInfo.getOwnerUserTel()));
                }
            });
            houseData.put(i + "", floorData);
        }
        buildingInfo.setHouseData(houseData);
        return buildingInfo;
    }


    public HouseInfo getHouseResidenceData(String houseId) {
        HouseInfo houseInfo = residentialBuildingInfoMapper.getHouseDetail(houseId);
        if (houseInfo == null) {
            return null;
        }
        houseInfo.setTitle(houseInfo.getBuildingName() + houseInfo.getBuildingCell() + "单元" + houseInfo.getHouseNumber());
        boolean permissionFlag = roleService.hasPermission();
        if (!permissionFlag) {
            if (StringUtils.hasText(houseInfo.getOwnerUserName())) {
                houseInfo.setOwnerUserName(Utils.encryptName(houseInfo.getOwnerUserName()));
            }
            if (StringUtils.hasText(houseInfo.getOwnerUserTel())) {
                houseInfo.setOwnerUserTel(Utils.encryptPhone(houseInfo.getOwnerUserTel()));
            }
            if (StringUtils.hasText(houseInfo.getOwnerUserCardCode())) {
                houseInfo.setOwnerUserCardCode(Utils.encryptCard(houseInfo.getOwnerUserCardCode().trim(), 6, 4));
            }
        }
        List<ResidenceInfo> residenceInfoList = residentialBuildingInfoMapper.getResidenceList(houseId);
        if (!permissionFlag) {
            residenceInfoList.forEach(residenceInfo -> {
                residenceInfo.setId(Utils.encryptCard(residenceInfo.getId(), 6, 4));
                residenceInfo.setName(Utils.encryptName(residenceInfo.getName()));
                residenceInfo.setTel(Utils.encryptPhone(residenceInfo.getTel()));
                residenceInfo.setIdentityCard(Utils.encryptCard(residenceInfo.getIdentityCard().trim(), 6, 4));
            });
        }
        houseInfo.setResidenceInfoList(residenceInfoList);
        if (houseInfo.getPerNumber() == null) {
            houseInfo.setPerNumber(residenceInfoList.size());
        }
        return houseInfo;
    }

    public File saveResidentialData(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            updateImportRecord("上传的不是excel文件", "3");
            throw new RuntimeException("上传的不是excel文件");
        }
        String dateFormat = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String dateDirPath = uploadPath + "/" + dateFormat + "/";
        new File(dateDirPath).mkdir();
        log.info("开始接收文件");
        File uploadFile = fileInfoService.transferToFile(multipartFile, dateDirPath);
        log.info("接收文件完成");
        return uploadFile;
    }
//    public File saveResidentialData(MultipartFile multipartFile) {
//        String fileName = multipartFile.getOriginalFilename();
//        if (!fileName.endsWith(".zip") && !fileName.endsWith(".rar")) {
//            importRecordsService.update(new LambdaUpdateWrapper<ImportRecords>()
//                    .set(ImportRecords::getUpdateResult, "上传的不是压缩文件")
//                    .set(ImportRecords::getUpdateTime, LocalDateTime.now()).eq(ImportRecords::getId, "3"));
//            throw new RuntimeException("请将文件压缩后再上传");
//        }
//        String dateFormat = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//        String dateDirPath = uploadPath + "/" + dateFormat + "/";
//        new File(dateDirPath).mkdir();
//        log.info("开始接收文件");
//        File uploadFile = fileInfoService.transferToFile(multipartFile, dateDirPath);
//        log.info("接收文件完成");
//        return uploadFile;
//    }

    @Resource
    IRdCommunityDataService communityDataService;

    @Async
    public void importResidentialData(File file) {
        sqlScriptService.backupTable("rd_community_data");
        sqlScriptService.truncateTable("rd_community_data", false);

        final AtomicReference<String> error = new AtomicReference<>("");
        List rdCommunityDataList = new ArrayList<>(5000);
        try {
            EasyExcel.read().extraRead(CellExtraTypeEnum.MERGE).ignoreEmptyRow(true).file(file).sheet(0).head(RdCommunityData.class) //按指定类解析
                    .headRowNumber(1) //从指定行开始解析
                    .registerReadListener(new ReadListener<RdCommunityData>() {
                        @Override
                        public void invoke(RdCommunityData object, AnalysisContext analysisContext) {
                            try {
                                if (StringUtils.hasText(error.get())) {
                                    return;
                                }
                                //判断数据是否合法
                                if (!StringUtils.hasText(object.get房屋地址()) || !StringUtils.hasText(object.get房屋地址().trim())) {
                                    error.set(file.getName() + ", sheet【" + analysisContext.readSheetHolder().getSheetName() + "】第"
                                            + (analysisContext.readRowHolder().getRowIndex() + 1)
                                            + "行解析失败");
                                    return;
                                }
                                rdCommunityDataList.add(Utils.trimStrings(object));
                                if (rdCommunityDataList.size() == 5000) {
                                    communityDataService.saveBatch(rdCommunityDataList);
                                    rdCommunityDataList.clear();
                                }
                            } catch (IllegalAccessException e) {
                                log.info("数据处理异常", e.getMessage());
                                error.set(e.getMessage());
                            }
                        }

                        @Override
                        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                            if (!rdCommunityDataList.isEmpty()) {
                                communityDataService.saveBatch(rdCommunityDataList);
                                rdCommunityDataList.clear();
                            }
                            String result = StringUtils.hasText(error.get()) ? error.get() : "社区数据更新完成";
                            updateImportRecord(result, "3");
                            if (StringUtils.hasText(error.get())) {
                                sqlScriptService.dropTable("rd_community_data", false);
                                sqlScriptService.restoreTable("rd_community_data");
                            } else {
                                sqlScriptService.dropTable("rd_community_data", true);
                            }
                        }

                        @Override
                        public void onException(Exception exception, AnalysisContext analysisContext) {
                            // 捕获异常并处理
                            // 可以选择跳过当前行继续执行或进行其他错误处理逻辑
                            if (exception instanceof RuntimeException) {
                                error.set("文件解析失败，sheet【" + analysisContext.readSheetHolder().getSheetName() + "】第"
                                        + (analysisContext.readRowHolder().getRowIndex() + 1)
                                        + "行，" + exception.getMessage());
                                updateImportRecord(error.get(), "3");
                            }
                        }
                    }).doRead();
        } catch (Exception e) {
            log.error(e.getMessage());
            sqlScriptService.dropTable("rd_community_data", false);
            sqlScriptService.restoreTable("rd_community_data");
            updateImportRecord(e.getMessage(), "3");

        }

    }

    public void updateImportRecord(String message, String id) {
        try {
            CustomClaim customClaim = ((CustomClaim) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getAttribute(AuthInterceptor.TOKEN_CLAIM));
            String currentName = customClaim.getRealName();
            importRecordsService.update(new LambdaUpdateWrapper<ImportRecords>()
                    .set(ImportRecords::getUpdateResult, message)
                    .set(ImportRecords::getUpdateName, currentName)
                    .set(ImportRecords::getUpdateTime, LocalDateTime.now()).eq(ImportRecords::getId, "3"));
        } catch (Exception e) {
            importRecordsService.update(new LambdaUpdateWrapper<ImportRecords>()
                    .set(ImportRecords::getUpdateResult, message)
                    .set(ImportRecords::getUpdateTime, LocalDateTime.now()).eq(ImportRecords::getId, "3"));
        }
    }


    public void importCommercialData(MultipartFile file) {
        String fileName = ExcelUtils.receiveFile(file);
        List<RegionInfo> regionInfoList = new ArrayList<>();
        EasyExcel.read().extraRead(CellExtraTypeEnum.MERGE).ignoreEmptyRow(true).file(fileName).sheet(0).head(Region.class) //按指定类解析
                .headRowNumber(2) //从指定行开始解析
                .registerReadListener(new ReadListener<Region>() {
                    @Override
                    public void invoke(Region object, AnalysisContext analysisContext) {
                        RegionInfo regionInfo = new RegionInfo();
                        RegionInfo region = regionInfoService.getOne(new LambdaQueryWrapper<RegionInfo>()
                                .eq(RegionInfo::getName, object.getName()), false);
                        BeanUtils.copyProperties(object, regionInfo);
                        if (region == null) {
                            regionInfo.setId(UUID.randomUUID().toString());
                        } else {
                            regionInfo.setId(region.getId());
                        }
                        regionInfo.setType("商业");
                        regionInfoList.add(regionInfo);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    }

                    @Override
                    public void onException(Exception exception, AnalysisContext analysisContext) {
                        // 捕获异常并处理
                        // 可以选择跳过当前行继续执行或进行其他错误处理逻辑
                        if (exception instanceof RuntimeException) {
                            throw new RuntimeException("文件【" + fileName + "】解析失败，sheet【" + analysisContext.readSheetHolder().getSheetName() + "】第"
                                    + (analysisContext.readRowHolder().getRowIndex() + 1)
                                    + "行，" + exception.getCause()

                            );
                        }
                    }
                }).doRead();

        List<CommercialBuildingInfo> commercialBuildingList = new ArrayList<>();
        EasyExcel.read().extraRead(CellExtraTypeEnum.MERGE).ignoreEmptyRow(true).file(fileName).sheet(1).head(CommercialBuilding.class) //按指定类解析
                .headRowNumber(2) //从指定行开始解析
                .registerReadListener(new ReadListener<CommercialBuilding>() {
                    @Override
                    public void invoke(CommercialBuilding object, AnalysisContext analysisContext) {
                        CommercialBuildingInfo buildingInfo = new CommercialBuildingInfo();
                        List<RegionInfo> regionInfos = regionInfoList.stream().filter(regionInfo -> regionInfo.getName().equals(object.getRegionName())).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(regionInfos)) {
                            RegionInfo region = regionInfoService.getOne(new LambdaQueryWrapper<RegionInfo>()
                                    .eq(RegionInfo::getName, object.getRegionName()), false);
                            //商业区名称、商业楼栋 二者确定唯一标识
                            if (Objects.isNull(region)) {
                                return;
                            }
                            CommercialBuildingInfo commercialBuildingInfo = commercialBuildingInfoService.getOne(new LambdaQueryWrapper<CommercialBuildingInfo>()
                                    .eq(CommercialBuildingInfo::getBuildingName, object.getBuildingName())
                                    .eq(CommercialBuildingInfo::getRegionId, region.getId()), false);
                            if (commercialBuildingInfo == null) {
                                BeanUtils.copyProperties(object, buildingInfo);
                                buildingInfo.setId(UUID.randomUUID().toString());
                            } else {
                                //更新楼栋信息
                                BeanUtils.copyProperties(object, buildingInfo);
                                buildingInfo.setId(commercialBuildingInfo.getId());
                            }
                        } else {
                            CommercialBuildingInfo commercialBuildingInfo = commercialBuildingInfoService.getOne(new LambdaQueryWrapper<CommercialBuildingInfo>()
                                    .eq(CommercialBuildingInfo::getBuildingName, object.getBuildingName())
                                    .eq(CommercialBuildingInfo::getRegionId, regionInfos.get(0).getId()), false);
                            if (commercialBuildingInfo == null) {
                                BeanUtils.copyProperties(object, buildingInfo);
                                buildingInfo.setId(UUID.randomUUID().toString());
                            } else {
                                //更新楼栋信息
                                BeanUtils.copyProperties(object, buildingInfo);
                                buildingInfo.setId(commercialBuildingInfo.getId());
                            }
                            buildingInfo.setRegionId(regionInfos.get(0).getId());
                            buildingInfo.setPullOutFlag("是".equals(object.getPullOut()) ? 1 : 0);
                        }
                        commercialBuildingList.add(buildingInfo);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    }

                    @Override
                    public void onException(Exception exception, AnalysisContext analysisContext) {
                        // 捕获异常并处理
                        // 可以选择跳过当前行继续执行或进行其他错误处理逻辑
                        if (exception instanceof RuntimeException) {
                            throw new RuntimeException("文件【" + fileName + "】解析失败，sheet【" + analysisContext.readSheetHolder().getSheetName() + "】第"
                                    + (analysisContext.readRowHolder().getRowIndex() + 1)
                                    + "行，" + exception.getCause()

                            );
                        }
                    }
                }).doRead();

        List<CommercialRoomInfo> commercialRoomInfoList = new ArrayList<>();
        List<CommercialLeaseInfo> commercialLeaseInfoList = new ArrayList<>();
        List<CommercialEnterpriseInfo> commercialEnterpriseInfoList = new ArrayList<>();
        EasyExcel.read().extraRead(CellExtraTypeEnum.MERGE).ignoreEmptyRow(true).file(fileName).sheet(2).head(CommercialRoomLease.class) //按指定类解析
                .headRowNumber(2) //从指定行开始解析
                .registerReadListener(new ReadListener<CommercialRoomLease>() {
                    @Override
                    public void invoke(CommercialRoomLease object, AnalysisContext analysisContext) {
                        List<RegionInfo> regionInfos = regionInfoList.stream().filter(regionInfo -> regionInfo.getName().equals(object.getRegionName())).collect(Collectors.toList());
                        String regionId = null;
                        if (CollectionUtils.isEmpty(regionInfos)) {
                            RegionInfo region = regionInfoService.getOne(new LambdaQueryWrapper<RegionInfo>()
                                    .eq(RegionInfo::getName, object.getRegionName()), false);
                            if (Objects.isNull(region)) {
                                return;
                            } else {
                                regionId = region.getId();
                            }
                        } else {
                            regionId = regionInfos.get(0).getId();
                        }
                        if (!StringUtils.hasText(regionId)) {
                            return;
                        }
                        String buildId = null;
                        List<CommercialBuildingInfo> commercialBuildingInfos = commercialBuildingList.stream().filter(build -> build.getBuildingName().equals(object.getBuildingName())).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(commercialBuildingInfos)) {
                            CommercialBuildingInfo commercialBuildingInfo = commercialBuildingInfoService.getOne(new LambdaQueryWrapper<CommercialBuildingInfo>()
                                    .eq(CommercialBuildingInfo::getBuildingName, object.getBuildingName())
                                    .eq(CommercialBuildingInfo::getRegionId, regionInfos.get(0).getId()), false);
                            if (commercialBuildingInfo == null) {
                                return;
                            } else {
                                buildId = commercialBuildingInfo.getId();
                            }
                        } else {
                            buildId = commercialBuildingInfos.get(0).getId();
                        }

                        //将房间、租赁、公司数据拆分
                        CommercialRoomInfo commercialRoomInfo = new CommercialRoomInfo();
                        BeanUtils.copyProperties(object, commercialRoomInfo);

                        String roomCode = UUID.randomUUID().toString();
                        String roomId = UUID.randomUUID().toString();

                        CommercialRoomInfo roomInfo = commercialRoomInfoService.getOne(new LambdaQueryWrapper<CommercialRoomInfo>()
                                .eq(CommercialRoomInfo::getBuildingId, buildId)
                                .eq(CommercialRoomInfo::getRoomTitle, object.getRoomTitle()), false);
                        if (roomInfo != null) {
                            roomCode = roomInfo.getRoomCode();
                            roomId = roomInfo.getId();
                        }
                        commercialRoomInfo.setId(roomId);
                        commercialRoomInfo.setRoomCode(roomCode);
                        commercialRoomInfo.setBuildingId(buildId);

                        commercialRoomInfoList.add(commercialRoomInfo);

                        CommercialLeaseInfo commercialLeaseInfo = new CommercialLeaseInfo();
                        BeanUtils.copyProperties(object, commercialLeaseInfo);
                        commercialLeaseInfo.setTitle(object.getRoomTitle());
                        commercialLeaseInfo.setStatus(255);
                        CommercialLeaseInfo leaseInfo = commercialLeaseInfoService.getOne(new LambdaQueryWrapper<CommercialLeaseInfo>()
                                .eq(CommercialLeaseInfo::getCode, roomCode), false);
                        if (leaseInfo == null) {
                            commercialLeaseInfo.setId(UUID.randomUUID().toString());
                        } else {
                            commercialLeaseInfo.setId(leaseInfo.getId());
                        }
                        commercialLeaseInfo.setCode(roomCode);

                        commercialLeaseInfoList.add(commercialLeaseInfo);

                        CommercialEnterpriseInfo commercialEnterpriseInfo = new CommercialEnterpriseInfo();
                        BeanUtils.copyProperties(object, commercialEnterpriseInfo);
                        commercialEnterpriseInfo.setName(object.getTenant());
                        commercialEnterpriseInfo.setCheckedFlag("是".equals(object.getChecked()) ? 1 : 0);
                        commercialEnterpriseInfo.setKeyFlag("是".equals(object.getKey()) ? 1 : 0);
                        commercialEnterpriseInfo.setSettledFlag("是".equals(object.getSettled()) ? 1 : 0);
                        commercialEnterpriseInfo.setType("企业".equals(object.getType()) ? 1 : 2);

                        //填写了租户信息
                        if (object.getTenant() != null) {
                            List<CommercialEnterpriseInfo> enterpriseInfos = commercialEnterpriseInfoList.stream().filter(data -> data.getName().equals(object.getTenant())).collect(Collectors.toList());
                            if (CollectionUtils.isEmpty(enterpriseInfos)) {
                                CommercialEnterpriseInfo enterpriseInfo = commercialEnterpriseInfoService.getOne(new LambdaQueryWrapper<CommercialEnterpriseInfo>().eq(CommercialEnterpriseInfo::getName, object.getTenant()), false);
                                if (enterpriseInfo == null) {
                                    commercialEnterpriseInfo.setId(UUID.randomUUID().toString());
                                } else {
                                    commercialEnterpriseInfo.setId(enterpriseInfo.getId());
                                }
                            } else {
                                commercialEnterpriseInfo.setId(enterpriseInfos.get(0).getId());
                            }
                            commercialEnterpriseInfo.setArea(object.getSigningArea());

                            commercialEnterpriseInfoList.add(commercialEnterpriseInfo);
                        }
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    }

                    @Override
                    public void onException(Exception exception, AnalysisContext analysisContext) {
                        // 捕获异常并处理
                        // 可以选择跳过当前行继续执行或进行其他错误处理逻辑
                        if (exception instanceof RuntimeException) {
                            throw new RuntimeException("文件【" + fileName + "】解析失败，sheet【" + analysisContext.readSheetHolder().getSheetName() + "】第"
                                    + (analysisContext.readRowHolder().getRowIndex() + 1)
                                    + "行，" + exception.getCause()

                            );
                        }
                    }
                }).doRead();
        regionInfoService.saveOrUpdateBatch(regionInfoList);
        commercialBuildingInfoService.saveOrUpdateBatch(commercialBuildingList);
        commercialRoomInfoService.saveOrUpdateBatch(commercialRoomInfoList);
        commercialLeaseInfoService.saveOrUpdateBatch(commercialLeaseInfoList);
        commercialEnterpriseInfoService.saveOrUpdateBatch(commercialEnterpriseInfoList);

    }


    public Map<String, Integer> getResidenceData() {
        return residentialBuildingInfoService.getResidenceData();
    }

    public Map<String, Integer> getMerchantData() {
        return commercialEnterpriseInfoService.getMerchantData();
    }


    @Async
    public void updateCommunityData(File uploadFile) {
        String dateDirPath = uploadFile.getParent();
        String result = "操作成功";
        try {
            log.info("开始解压文件");
            Utils.unzip(uploadFile.getPath(), dateDirPath + "/" + "unzip");
            log.info("解压文件完成");
            File[] files = new File(dateDirPath + "/" + "unzip").listFiles();
            List<String> sqlFiles = new ArrayList<>();
            log.info("开始拆分文件");
            for (File file : files) {
                sqlFiles.addAll(fileInfoService.splitAndReplaceInFile(file, dateDirPath + "/" + "sql/", List.of("`\\", "'\\", "`"), "", 20000));
            }
            log.info("拆分文件完成");
            log.info("开始执行文件");
            for (String table : communityDataTables) {
                sqlScriptService.backupTable(table);
                sqlScriptService.truncateTable(table, false);
                sqlFiles.forEach(sql -> sqlScriptService.executeSqlScript(sql));
                sqlScriptService.dropTable(table, true);
            }
            log.info("执行文件完成");
            log.info("开始刷新视图");
            sqlScriptService.executeViewScript(communityDataView);
            log.info("刷新视图完成");
        } catch (Exception e) {
            result = e.getMessage();
            log.error(e.getMessage());
        } finally {
            try {
                CustomClaim customClaim = ((CustomClaim) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getAttribute(AuthInterceptor.TOKEN_CLAIM));
                String currentName = customClaim.getRealName();
                importRecordsService.update(new LambdaUpdateWrapper<ImportRecords>()
                        .set(ImportRecords::getUpdateResult, result)
                        .set(ImportRecords::getUpdateName, currentName)
                        .set(ImportRecords::getUpdateTime, LocalDateTime.now()).eq(ImportRecords::getId, "3"));
            } catch (Exception e) {
                importRecordsService.update(new LambdaUpdateWrapper<ImportRecords>()
                        .set(ImportRecords::getUpdateResult, result)
                        .set(ImportRecords::getUpdateTime, LocalDateTime.now()).eq(ImportRecords::getId, "3"));
            }
        }
    }

//    public List<ResidentialBuildingPosition> getBuildingAddressList(ResidentialBuildingPosition buildingPosition) {
//        return residentialBuildingInfoService.getBuildingAddressList(buildingPosition);
//    }
//
//    public Page<ResidentialBuildingPosition> getBuildingAddressList(Page page, ResidentialBuildingPosition buildingPosition) {
//        return residentialBuildingInfoService.getBuildingAddressList(page, buildingPosition);
//    }
//
//    public boolean updateBuildingAddress(ResidentialBuildingPosition buildingPosition) {
//        return residentialBuildingInfoService.updateBuildingAddress(buildingPosition);
//    }

}
