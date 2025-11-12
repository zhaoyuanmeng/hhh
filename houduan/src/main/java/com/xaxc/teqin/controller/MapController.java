package com.xaxc.teqin.controller;


import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.service.impl.SpatialQuery;
import org.locationtech.jts.io.ParseException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 地图相关 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-07-15
 */
@RestController
@RequestMapping("/map")
public class MapController {

    @Resource
    SpatialQuery spatialQuery;

    /**
     * 获取点位数据
     *
     * @return
     */
    @GetMapping("/getPointDataByName")
    public ResponseResult getPointDataByName(@RequestParam("name") String name) {
        return ResponseResult.success();
    }

    /**
     * 获取面内的数据
     *
     * @param polygon
     * @return
     */
    @PostMapping("queryIntersectsData")
    public ResponseResult queryIntersectsData(@RequestBody List<String[]> polygon) {
        return ResponseResult.success(spatialQuery.queryIntersectsData(polygon));
    }

}
