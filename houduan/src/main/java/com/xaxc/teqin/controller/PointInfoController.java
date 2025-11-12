package com.xaxc.teqin.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xaxc.teqin.common.model.CustomClaim;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.component.AuthInterceptor;
import com.xaxc.teqin.model.entity.ImportRecords;
import com.xaxc.teqin.model.entity.PointInfo;
import com.xaxc.teqin.service.IFileInfoService;
import com.xaxc.teqin.service.IImportRecordsService;
import com.xaxc.teqin.service.IPointExtInfoService;
import com.xaxc.teqin.service.IPointInfoService;
import com.xaxc.teqin.service.impl.BuildingInfoServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 点位信息表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-07-16
 */
@RestController
@RequestMapping("/point-info")
public class PointInfoController {

    @Resource
    private IPointInfoService pointInfoService;


    @Resource
    private IPointExtInfoService pointExtInfoService;

    @Resource
    IImportRecordsService importRecordsService;

    @Resource
    IFileInfoService fileInfoService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody PointInfo pointInfo) {
        return pointInfoService.addPoint(pointInfo) ? ResponseResult.success() : ResponseResult.error("operation failed");
    }

    @PostMapping("/update")
    public ResponseResult update(@RequestBody PointInfo pointInfo) {
        return pointInfoService.updatePoint(pointInfo) ? ResponseResult.success() : ResponseResult.error("operation failed");
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public ResponseResult delete(@RequestParam("id") String id) {
        return pointInfoService.removeById(id) ? ResponseResult.success() : ResponseResult.error("operation failed");
    }

    /**
     * 获取高速列表
     *
     * @return
     */
    @GetMapping("/getHighwayList")
    public ResponseResult getHighwayList() {
        return ResponseResult.success(pointInfoService.getHighwayList());
    }

    /**
     * 获取高铁列表
     *
     * @return
     */
    @GetMapping("/getRailwayList")
    public ResponseResult getRailwayList() {
        return ResponseResult.success(pointInfoService.getRailwayList());
    }

    /**
     * 获取现场列表
     *
     * @return
     */
    @GetMapping("/getSiteList")
    public ResponseResult getSiteList() {
        return ResponseResult.success(pointInfoService.getSiteList());
    }

    /**
     * 获取住地列表
     *
     * @return
     */
    @GetMapping("/getResidenceList")
    public ResponseResult getResidenceList() {
        return ResponseResult.success(pointInfoService.getResidenceList());
    }

    /**
     * 获取道路列表
     *
     * @return
     */
    @GetMapping("/getRoadList")
    public ResponseResult getRoadList() {
        return ResponseResult.success(pointInfoService.getRoadList());
    }

    /**
     * 获取高速统计数据
     *
     * @return
     */
    @GetMapping("/getHighwayStatistics")
    public ResponseResult getHighwayStatistics() {
        return ResponseResult.success(pointInfoService.getHighwayStatistics());
    }

    /**
     * 获取高铁统计数据
     *
     * @return
     */
    @GetMapping("/getRailwayStatistics")
    public ResponseResult getRailwayStatistics() {
        return ResponseResult.success(pointInfoService.getRailwayStatistics());
    }

    /**
     * 获取现场统计数据
     *
     * @return
     */
    @GetMapping("/getSiteStatistics")
    public ResponseResult getSiteStatistics() {
        return ResponseResult.success(pointInfoService.getSiteStatistics());
    }


    /**
     * 获取道路统计数据
     *
     * @return
     */
    @GetMapping("/getRoadStatistics")
    public ResponseResult getRoadStatistics() {
        return ResponseResult.success(pointInfoService.getRoadStatistics());
    }

    /**
     * 获取住地统计数据
     *
     * @return
     */
    @GetMapping("/getResidenceStatistics")
    public ResponseResult getResidenceStatistics() {
        return ResponseResult.success(pointInfoService.getResidenceStatistics());
    }

    /**
     * 获取高速详情
     *
     * @param id
     * @return
     */
    @GetMapping("/getHighwayPointData")
    public ResponseResult getHighwayPointData(@RequestParam(value = "id") String id) {
        return ResponseResult.success(pointInfoService.getHighwayPointData(id));
    }

    /**
     * 获取道路详情
     *
     * @param id
     * @return
     */
    @GetMapping("/getRoadPointData")
    public ResponseResult getRoadPointData(@RequestParam(value = "id") String id) {
        return ResponseResult.success(pointInfoService.getRoadPointData(id));
    }

    /**
     * 获取高铁详情
     *
     * @param id
     * @return
     */
    @GetMapping("/getRailwayPointData")
    public ResponseResult getRailwayPointData(@RequestParam(value = "id") String id) {
        return ResponseResult.success(pointInfoService.getRailwayPointData(id));
    }

    /**
     * 获取现场详情数据
     *
     * @param id
     * @return
     */
    @GetMapping("/getSitePointData")
    public ResponseResult getSitePointData(@RequestParam(value = "id") String id) {
        return ResponseResult.success(pointInfoService.getSitePointData(id));
    }

    /**
     * 获取住地详情数据
     *
     * @param id
     * @return
     */
    @GetMapping("/getResidencePointData")
    public ResponseResult getResidencePointData(@RequestParam(value = "id") String id) {
        return ResponseResult.success(pointInfoService.getResidencePointData(id));
    }

    /**
     * 获取高速公路点位类型
     *
     * @return
     */
    @GetMapping("/getHighwayPointType")
    public ResponseResult getHighwayPointType() {
        return ResponseResult.success(pointInfoService.getHighwayPointType());
    }

    /**
     * 获取高速铁路点位类型
     *
     * @return
     */
    @GetMapping("/getRailwayPointType")
    public ResponseResult getRailwayPointType() {
        return ResponseResult.success(pointInfoService.getRailwayPointType());
    }

    /**
     * 获取现场点位类型
     *
     * @return
     */
    @GetMapping("/getSitePointType")
    public ResponseResult getSitePointType() {
        return ResponseResult.success(pointInfoService.getSitePointType());
    }

    /**
     * 获取住地点位类型
     *
     * @return
     */
    @GetMapping("/getResidencePointType")
    public ResponseResult getResidencePointType() {
        return ResponseResult.success(pointInfoService.getResidencePointType());
    }

    /**
     * 更新表列数据
     *
     * @return
     */
    @GetMapping("/updateColumnOfPointTable")
    public ResponseResult updateColumnOfPointTable() {
        return ResponseResult.success(pointInfoService.updateColumnOfPointTable());
    }


    /**
     * 上传高速公路点位数据
     *
     * @param file
     * @param id
     * @return
     */
    @PostMapping("importHighwayData")
    public ResponseResult importHighwayData(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "id") String id, @RequestParam(value = "pointTypeId") String pointTypeId, @RequestParam(value = "nameVerifyFlag", required = false, defaultValue = "1") String nameVerifyFlag) throws InstantiationException, IllegalAccessException {
        pointInfoService.importHighwayData(file, id, pointTypeId, nameVerifyFlag);
        return ResponseResult.success();
    }

    /**
     * 上传高速铁路点位数据
     *
     * @param file
     * @param id
     * @return
     */
    @PostMapping("importRailwayData")
    public ResponseResult importRailwayData(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "id") String id, @RequestParam(value = "pointTypeId") String pointTypeId, @RequestParam(value = "nameVerifyFlag", required = false, defaultValue = "1") String nameVerifyFlag) throws InstantiationException, IllegalAccessException {
        pointInfoService.importRailwayData(file, id, pointTypeId, nameVerifyFlag);
        return ResponseResult.success();
    }

    /**
     * 导入现场数据
     *
     * @param file
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @PostMapping("importSiteData")
    public ResponseResult importSiteData(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "id") String id, @RequestParam(value = "pointTypeId") String pointTypeId, @RequestParam(value = "nameVerifyFlag", required = false, defaultValue = "1") String nameVerifyFlag) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        pointInfoService.importSiteArchiveData(file, id, pointTypeId, nameVerifyFlag);
        return ResponseResult.success();
    }

    /**
     * 导入住地数据
     *
     * @param file
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @PostMapping("importResidenceData")
    public ResponseResult importResidenceData(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "id") String id, @RequestParam(value = "pointTypeId") String pointTypeId, @RequestParam(value = "nameVerifyFlag", required = false, defaultValue = "1") String nameVerifyFlag) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        pointInfoService.importResidenceArchiveData(file, id, pointTypeId, nameVerifyFlag);
        return ResponseResult.success();
    }

    /**
     * 下载文件模版
     *
     * @param pointTypeId
     * @param response
     * @throws IOException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @GetMapping("downloadTemplate")
    public void downloadTemplate(@RequestParam(value = "pointTypeId") String pointTypeId, HttpServletResponse response) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        pointInfoService.downloadTemplate(pointTypeId, response);
    }

    /**
     * 根据点位名称查询点位数据
     *
     * @param name
     * @return
     */
    @GetMapping("getPointsByName")
    public ResponseResult getPointsByName(@RequestParam(value = "name") String name) {
        return ResponseResult.success(pointInfoService.getPointsByName(name));
    }

    /**
     * 更新点位坐标
     *
     * @param jingdu
     * @param weidu
     * @param id
     * @return
     */
    @GetMapping("updatePosition")
    public ResponseResult updatePosition(@RequestParam(value = "jingdu") String jingdu, @RequestParam(value = "weidu") String weidu, @RequestParam(value = "id") String id) {
        return ResponseResult.success(pointInfoService.updatePosition(jingdu, weidu, id));
    }

    /**
     * 更新扩展数据
     *
     * @return
     */
    @GetMapping("updatePointExtInfo")
    public ResponseResult updatePointExtInfo() {
        pointInfoService.updatePointExtInfo();
        return ResponseResult.success();
    }


    @PostMapping("queryIntersectsData")
    public ResponseResult queryIntersectsData(@RequestBody List<String[]> polygon) {
        return ResponseResult.success(pointExtInfoService.queryIntersectsData(polygon));
    }

    /**
     * 获取基础数据名称列表（新建场景用）
     * type  1 高速 2 高铁 3 现场 4 住地
     *
     * @return
     */
    @GetMapping("getBasicDataNameList")
    public ResponseResult getBasicDataNameList(@RequestParam("type") String type) {
        return ResponseResult.success(pointInfoService.getBasicDataNameList(type));
    }

    /**
     * 获取详情数据
     *
     * @param id
     * @return
     */
    @GetMapping("getDetail")
    public ResponseResult getDetail(@RequestParam("id") String id, @RequestParam(value = "taskId", required = false) String taskId, @RequestParam(value = "sceneId", required = false) String sceneId) {
        return ResponseResult.success(pointInfoService.getPointDetail(id, taskId, sceneId));
    }

    @PostMapping("importPoint")
    public ResponseResult importPoint(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "id") String id) {
        pointInfoService.importPoint(file);
        CustomClaim customClaim = ((CustomClaim) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getAttribute(AuthInterceptor.TOKEN_CLAIM));
        String currentName = customClaim.getUserName();
        importRecordsService.update(new LambdaUpdateWrapper<ImportRecords>()
                .set(ImportRecords::getUpdateTime, LocalDateTime.now())
                .set(ImportRecords::getUpdateName, currentName).eq(ImportRecords::getId, id));
        return ResponseResult.success();
    }

    /**
     * 上传档案文件
     *
     * @param file
     * @return
     */
    @PostMapping("uploadPointFile")
    public ResponseResult uploadPointFile(@RequestParam(value = "file") MultipartFile file) {
        if (file.getOriginalFilename().indexOf("-") > -1) {
            fileInfoService.uploadPointFile(file);
        } else {
            return ResponseResult.error("上传文件名称格式不正确。");
        }
        return ResponseResult.success();
    }

    @GetMapping("getFeatureTypeData")
    public ResponseResult getFeatureTypeData(@RequestParam(value = "id") String id) {
        return ResponseResult.success(pointInfoService.getFeatureTypeData(id));
    }

}
