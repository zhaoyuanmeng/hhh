package com.xaxc.teqin.controller;


import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.entity.DrawData;
import com.xaxc.teqin.service.ICoverInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 井盖信息 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-02-24
 */
@RestController
@RequestMapping("/cover-info")
public class CoverInfoController {

    @Resource
    ICoverInfoService coverInfoService;


    /**
     * 獲取數據
     *
     * @return
     */
    @GetMapping("/getList")
    public ResponseResult getList() {
        return ResponseResult.success(coverInfoService.getList());
    }

}
