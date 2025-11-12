package com.xaxc.teqin.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xaxc.teqin.common.model.CustomClaim;
import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.model.PageCondition;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.component.AuthInterceptor;
import com.xaxc.teqin.model.dto.UserDTO;
import com.xaxc.teqin.model.entity.ImportRecords;
import com.xaxc.teqin.model.entity.ResidentialBuildingInfo;
import com.xaxc.teqin.model.entity.ResidentialBuildingPosition;
import com.xaxc.teqin.service.IImportRecordsService;
import com.xaxc.teqin.service.impl.BuildingInfoServiceImpl;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-11-26
 */
@RestController
@RequestMapping("/building-info")
public class BuildingInfoController {

    @Resource
    BuildingInfoServiceImpl buildingInfoService;

    @Resource
    IImportRecordsService importRecordsService;

    @GetMapping("/getBuildingData")
    public ResponseResult getBuildingData() {
        return ResponseResult.success(buildingInfoService.getBuildingList());
    }

    @GetMapping("/getFloorNumber")
    public ResponseResult getFloorNumber(@RequestParam(value = "buildingId") String buildingId) {
        return ResponseResult.success(buildingInfoService.getFloorNumber(buildingId));
    }

    @GetMapping("/getRoomLeaseData")
    public ResponseResult getRoomLeaseData(@RequestParam(value = "buildingId") String buildingId, @RequestParam(value = "floorNumber") Integer floorNumber) {
        return ResponseResult.success(buildingInfoService.getRoomLeaseData(buildingId, floorNumber));
    }


    @GetMapping("/getFloorTenantData")
    public ResponseResult getFloorTenantData(@RequestParam(value = "buildingId") String buildingId, @RequestParam(value = "floorNumber") Integer floorNumber) {
        return ResponseResult.success(buildingInfoService.getFloorTenantData(buildingId, floorNumber));
    }


    @GetMapping("/getTenantData")
    public ResponseResult getTenantData(@RequestParam(value = "roomId", required = false) String roomId, @RequestParam(value = "roomCode", required = false) String roomCode) {
        return ResponseResult.success(buildingInfoService.getTenantData(roomId, roomCode));
    }

    @GetMapping("/getResidentialBuildingInfo")
    public ResponseResult getResidentialBuildingInfo(@RequestParam(value = "buildingId") String buildingId) {
        return ResponseResult.success(buildingInfoService.getResidentialBuildingInfo(null, buildingId));
    }

    @GetMapping("/getResidentialBuilding")
    public ResponseResult getResidentialBuilding(@RequestParam(value = "buildingName") String buildingName) {
        return ResponseResult.success(buildingInfoService.getResidentialBuildingData(buildingName));
    }

    @GetMapping("/getHouseResidenceData")
    public ResponseResult getHouseResidenceData(@RequestParam(value = "houseId") String houseId) {
        return ResponseResult.success(buildingInfoService.getHouseResidenceData(houseId));
    }

    /**
     * 导入社区数据
     *
     * @param file
     * @return
     */
    @PostMapping("/importResidentialData")
    public ResponseResult importResidentialData(@RequestParam(value = "file") MultipartFile file) {
        try {
            File localFile = buildingInfoService.saveResidentialData(file);
            buildingInfoService.importResidentialData(localFile);
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.success("文件上传成功，操作结果请稍后查询数据更新记录");
    }

    /**
     * 导入商业数据
     */
    @PostMapping("/importCommercialData")
    public ResponseResult importCommercialData(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "id") String id) {
        buildingInfoService.importCommercialData(file);
        CustomClaim customClaim = ((CustomClaim) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getAttribute(AuthInterceptor.TOKEN_CLAIM));
        String currentName = customClaim.getUserName();
        importRecordsService.update(new LambdaUpdateWrapper<ImportRecords>()
                .set(ImportRecords::getUpdateTime, LocalDateTime.now())
                .set(ImportRecords::getUpdateName, currentName).eq(ImportRecords::getId, id));

        return ResponseResult.success();
    }

    /**
     * 居民数据统计
     *
     * @return
     */
    @GetMapping("/getResidenceData")
    public ResponseResult getResidenceData() {
        return ResponseResult.success(buildingInfoService.getResidenceData());
    }

    /**
     * 获取商业数据
     *
     * @return
     */
    @GetMapping("/getMerchantData")
    public ResponseResult getMerchantData() {
        return ResponseResult.success(buildingInfoService.getMerchantData());
    }

    /**
     * 获取楼栋及商业数据
     *
     * @param type
     * @return
     */
    @GetMapping("/getBuildingAndEnterpriseData")
    public ResponseResult getBuildingAndEnterpriseData(@RequestParam("type") int type) {
        return ResponseResult.success(buildingInfoService.getBuildingAndEnterpriseData(type));
    }

    /**
     * 获取社区居民楼数据
     *
     * @return
     */
    @GetMapping("/getResidentialBuildingData")
    public ResponseResult getResidentialBuildingData() {
        return ResponseResult.success(buildingInfoService.getResidentialBuildingData());
    }

//    /**
//     * 获取地址列表
//     *
//     * @param buildingPosition
//     * @return
//     */
//    @PostMapping("/getBuildingAddressList")
//    public ResponseResult<List<ResidentialBuildingPosition>> getBuildingAddressList(@RequestBody ResidentialBuildingPosition buildingPosition) {
//        return ResponseResult.success(buildingInfoService.getBuildingAddressList(buildingPosition));
//    }
//
//    /**
//     * 更新地址映射
//     *
//     * @param buildingPosition
//     * @return
//     */
//    @PostMapping("/updateBuildingAddress")
//    public ResponseResult updateBuildingAddress(@RequestBody ResidentialBuildingPosition buildingPosition) {
//        return ResponseResult.success(buildingInfoService.updateBuildingAddress(buildingPosition));
//    }
//
//    /**
//     * 获取数据分页数据
//     *
//     * @param pageCondition
//     * @return
//     */
//    @PostMapping("/getBuildingAddressPage")
//    public ResponseResult<Page<ResidentialBuildingPosition>> getBuildingAddressPage(@RequestBody PageCondition<ResidentialBuildingPosition> pageCondition) {
//        Page<ResidentialBuildingPosition> page = pageCondition.getPage();
//        ResidentialBuildingPosition buildingPosition = pageCondition.getEntity();
//        return ResponseResult.success(buildingInfoService.getBuildingAddressList(page, buildingPosition));
//    }


}
