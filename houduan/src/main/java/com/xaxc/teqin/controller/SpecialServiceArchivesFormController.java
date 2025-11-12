package com.xaxc.teqin.controller;


import com.alibaba.fastjson.JSONObject;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.entity.PointInfo;
import com.xaxc.teqin.service.IFileInfoService;
import com.xaxc.teqin.service.IPointInfoService;
import com.xaxc.teqin.service.ISpecialServiceArchivesFormService;
import com.xaxc.teqin.service.ISpecialServiceArchivesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 档案自定义表单项信息表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-07-16
 */
@RestController
@RequestMapping("/specialServiceArchivesForm")
public class SpecialServiceArchivesFormController {

    @Resource
    ISpecialServiceArchivesService specialServiceArchives;

    @Resource
    ISpecialServiceArchivesFormService serviceArchivesFormService;

    @Resource
    IPointInfoService pointInfoService;

    @Resource
    IFileInfoService fileInfoService;

    /**
     * 查询重点点位表单，统计点位数量
     *
     * @param id 档案id
     * @return
     */
    @GetMapping("/selectPointInfo/{id}/{jcxxId}")
    public ResponseResult selectPointInfo(@PathVariable("id") String id, @PathVariable("jcxxId") String jcxxId) {
        return ResponseResult.success(specialServiceArchives.selectPointInfo(id, jcxxId));
    }

    /**
     * szy
     * 新增档案项信息
     *
     * @return
     */
    @PostMapping("/addFormItemInfo")
    public ResponseResult addFormItemInfo(@RequestBody JSONObject map) {
        pointInfoService.addPoint(map);
        return ResponseResult.success();
    }

    /**
     * 删除点位信息
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deletePointById{id}")
    @Transactional
    public ResponseResult deletePointById(@PathVariable("id") String id) {
        pointInfoService.deleteByJcxxId(id);
        pointInfoService.removeById(id);
        return ResponseResult.success();
    }

    /**
     * szy
     * 查询重点点位数据列表
     *
     * @param id 档案id
     * @return
     */
    @GetMapping("/selectPointList")
    public ResponseResult selectPointList(@RequestParam(value = "id") String id, @RequestParam(value = "jcxxId") String jcxxId) {
        return serviceArchivesFormService.selectPointList(id, jcxxId);
    }

    /**
     * 上传档案信息图片
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadArchivesFormImg")
    public ResponseResult uploadArchivesFormImg(@RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            String url = fileInfoService.uploadFile(file);
            if (StringUtils.isEmpty(url)) {
                return ResponseResult.error("上传失败，请检查文件服务器");
            }
            return ResponseResult.success(url);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 新增档案自定义表单数据信息
     *
     * @return
     */
    @PostMapping("/addFormInfo")
    public ResponseResult addFormInfo(@RequestBody PointInfo pointInfoModel) {
        return ResponseResult.success(pointInfoService.saveArchivesInfo(pointInfoModel));
    }

    /**
     * 删除档案自定义表单数据信息
     *
     * @return
     */
    @DeleteMapping("/deleteFormInfo/{id}")
    public ResponseResult deleteFormInfo(@PathVariable("id") String id) {
        return ResponseResult.success(pointInfoService.removeById(id));
    }
}
