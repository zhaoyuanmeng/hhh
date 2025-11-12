package com.xaxc.teqin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.model.PageCondition;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.entity.ImportRecords;
import com.xaxc.teqin.model.entity.TaskData;
import com.xaxc.teqin.service.ICommercialBuildingInfoService;
import com.xaxc.teqin.service.IImportRecordsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-01-09
 */
@RestController
@RequestMapping("/import-records")
public class ImportRecordsController {

    @Resource
    IImportRecordsService importRecordsService;

    /**
     * 获取列表
     *
     * @param importRecords
     * @return
     */
    @PostMapping("/getList")
    public ResponseResult getList(@RequestBody ImportRecords importRecords) {
        LambdaQueryWrapper<ImportRecords> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(ImportRecords::getId);
        return ResponseResult.success(importRecordsService.list(lambdaQueryWrapper));
    }

    /**
     * 获取分页
     *
     * @param pageCondition
     * @return
     */
    @PostMapping("/getPage")
    public ResponseResult<Page<ImportRecords>> getPage(@RequestBody PageCondition<ImportRecords> pageCondition) {
        Page<ImportRecords> page = pageCondition.getPage();
        ImportRecords taskData = pageCondition.getEntity();
        LambdaQueryWrapper<ImportRecords> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(ImportRecords::getId);
        return ResponseResult.success(importRecordsService.page(page, lambdaQueryWrapper));
    }
}
