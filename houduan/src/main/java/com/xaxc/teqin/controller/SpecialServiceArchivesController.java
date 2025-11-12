package com.xaxc.teqin.controller;


import com.alibaba.fastjson.JSONObject;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.entity.SpecialServiceArchives;
import com.xaxc.teqin.service.IFileInfoService;
import com.xaxc.teqin.service.IPointInfoService;
import com.xaxc.teqin.service.ISpecialServiceArchivesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 档案类型信息表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-07-16
 */
@RestController
@RequestMapping("/specialServiceArchives")
public class SpecialServiceArchivesController {

    @Resource
    private ISpecialServiceArchivesService specialServiceArchivesService;

    @Resource
    IPointInfoService pointInfoService;

    @Resource
    IFileInfoService fileInfoService;

    /**
     * 查询特勤重点点位档案类目列表
     *
     * @param specialServiceArchives 筛选条件
     * @return 查询结果
     */
    @GetMapping("/getArchivesList")
    public ResponseResult getArchivesList(SpecialServiceArchives specialServiceArchives) {
        return ResponseResult.success(specialServiceArchivesService.getArchivesList(specialServiceArchives));
    }


    /**
     * 保存排序
     * @param specialServiceArchivesList
     * @return
     */
    @PostMapping("/saveFormItemSort")
    public ResponseResult saveFormItemSort(@RequestBody List<SpecialServiceArchives> specialServiceArchivesList) {
        return specialServiceArchivesService.saveFormItemSort(specialServiceArchivesList) ? ResponseResult.success() : ResponseResult.error();
    }

    /**
     * 根据档案id查询档案内容
     *
     * @return
     */
    @GetMapping("/getInfoByArchivesId")
    public ResponseResult getInfoByArchivesId(@RequestParam("archivesId") String archivesId) {
        return ResponseResult.success(specialServiceArchivesService.getInfoByArchivesId(archivesId));
    }

    /**
     * 点位信息
     *
     * @param archivesId
     * @param size
     * @param page
     * @return
     */
    @GetMapping("/getPointInfoByArchivesId")
    public ResponseResult getPointInfo(@RequestParam("archivesId") String archivesId, @RequestParam("size") int size, @RequestParam("page") int page) {
        return ResponseResult.success(specialServiceArchivesService.getPointInfo(archivesId, size, page));
    }

    /**
     * 根据档案ID查询基本信息表单项
     *
     * @param parentId 档案ID
     * @return 查询结果
     */
    @GetMapping("/getBasicInfoById/{parentId}")
    public ResponseResult getBasicInfoById(@PathVariable("parentId") String parentId) {
        return ResponseResult.success(specialServiceArchivesService.getBasicInfoById(parentId));
    }

    /**
     * 查询档案基本信息列表
     *
     * @return
     */
    @PostMapping("/basicInfoList")
    public ResponseResult basicInfoList(@RequestBody JSONObject map) {
        map.put("jcxxId", null);
        return ResponseResult.success(pointInfoService.queryListSort(map, "id", null));
    }

    /**
     * 上传档案信息图片
     *
     * @return
     */
    @PostMapping("/uploadArchivesIcon")
    public ResponseResult uploadArchivesIcon(@RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            String url = fileInfoService.uploadFile(file);
            if (StringUtils.isEmpty(url)) {
                return ResponseResult.error("上传失败，请检查文件服务器");
            }
            return ResponseResult.success(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
