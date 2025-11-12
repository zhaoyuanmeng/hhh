package com.xaxc.teqin.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xaxc.teqin.common.model.CustomClaim;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.common.util.Utils;
import com.xaxc.teqin.component.AuthInterceptor;
import com.xaxc.teqin.model.entity.DrawData;
import com.xaxc.teqin.model.entity.ImportRecords;
import com.xaxc.teqin.model.entity.Task;
import com.xaxc.teqin.service.IDrawDataService;
import com.xaxc.teqin.service.impl.SpatialQuery;
import org.apache.ibatis.annotations.Param;
import org.locationtech.jts.io.ParseException;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标绘数据相关 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-07-15
 */
@RestController
@RequestMapping("/draw-data")
public class DrawDataController {

    @Resource
    IDrawDataService drawDataService;


    /**
     * 导入标会坐标
     *
     * @return
     */
    @PostMapping("importDrawData")
    public ResponseResult importDrawData(@RequestParam(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseResult.error("文件为空");
        }
        // 获取文件名
        String fileName = file.getOriginalFilename().split("\\.")[0];
        String fileContent = "";
        String coordinates = "";
        System.out.println("上传的文件名: " + fileName);
        try {
            // 读取文件内容
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
            }
            // 返回文件内容
            fileContent = content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.error("读取失败");
        }

        JSONArray jsonArray = JSONArray.parseArray(fileContent);
        if (jsonArray.size() == 0) {
            return ResponseResult.error("坐标为空，请返回重试");
        }else{
            StringBuilder content = new StringBuilder();
            content.append("[");
            final int[] size = {0};
            jsonArray.forEach(item -> {
                size[0]++;
                JSONArray jsonArray1 = (JSONArray) item;
                Double xPoint =Double.parseDouble(jsonArray1.get(0).toString());
                Double yPoint =Double.parseDouble(jsonArray1.get(1).toString());
                Double zPoint = 6.2;
                if(jsonArray1.size() > 2){
                    // 如果高度为负数，或者小于1的情况下，默认为6.2米的高度
                    zPoint =  Double.parseDouble(jsonArray1.get(2).toString());
                    if(zPoint ==null || zPoint < 1.0){
                        zPoint = 6.2;
                    }
                }

                ProjCoordinate projCoordinate = Utils.wgs84ToCgcs2000(xPoint,yPoint);
                content.append("[");
                content.append(projCoordinate.x);
                content.append(",");
                content.append(projCoordinate.y);
                content.append(",");
                content.append(zPoint);
                content.append("]");
                if(size[0] != jsonArray.size()){
                    content.append(",");
                }
            });
            content.append("]");
            System.out.println(content.toString());
            coordinates = content.toString();
        }
//        drawDataService.importDrawData(fileName,coordinates,drawData);
        Map<String, Object> map = new HashMap<>();
        map.put("fileName", fileName);
        map.put("coordinates", coordinates);
        return ResponseResult.success(map);
    }

    /**
     * 保存标绘数据
     *
     * @param drawData
     * @return
     */
    @PostMapping("saveImport")
    public ResponseResult saveImport(@RequestBody DrawData drawData) {
        return ResponseResult.success(drawDataService.saveImport(drawData));
    }

    /**
     * 保存标绘数据
     *
     * @param drawData
     * @return
     */
    @PostMapping("save")
    public ResponseResult saveDrawData(@RequestBody DrawData drawData) {
        return ResponseResult.success(drawDataService.saveDrawData(drawData));
    }

    @PostMapping("import")
    public ResponseResult importDrawData(@RequestBody DrawData drawData) {
        return ResponseResult.success(drawDataService.importDrawData(drawData));
    }

    @PostMapping("batchImport")
    public ResponseResult importDrawData(@RequestBody List<DrawData> drawDataList) {
        return ResponseResult.success(drawDataService.batchImportDrawData(drawDataList));
    }

    /**
     * 删除标绘
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public ResponseResult delete(@RequestParam("id") String id) {
        return drawDataService.update(new LambdaUpdateWrapper<DrawData>().set(DrawData::getDeleteFlag, 1).eq(DrawData::getId, id))
                ? ResponseResult.success() : ResponseResult.error("操作失败");
    }

    /**
     * 获取规划节点下警力部署数据
     *
     * @param taskId
     * @param planNode
     * @return
     */
    @GetMapping("/getPoliceData")
    public ResponseResult getPoliceData(@RequestParam("taskId") String taskId, @RequestParam("planNode") String planNode) {
        return ResponseResult.success(drawDataService.getPoliceData(taskId, planNode));
    }

    /**
     * 根据ID查询标绘数据
     *
     * @param id
     * @return
     */
    @GetMapping("/getById")
    public ResponseResult getById(@RequestParam("id") String id) {
        return ResponseResult.success(drawDataService.getDetail(id));
    }

    /**
     * 獲取數據
     *
     * @param drawData
     * @return
     */
    @PostMapping("/getList")
    public ResponseResult getList(@RequestBody DrawData drawData) {
        return ResponseResult.success(drawDataService.getDrawDataList(drawData));
    }

    /**
     * 获取常规线路列表
     * @param drawData
     * @return
     */
    @PostMapping("/getLineList")
    public ResponseResult getLineList(@RequestBody DrawData drawData) {
        return ResponseResult.success(drawDataService.getLineList(drawData));
    }

    /**
     * 获取建筑内的标绘数据
     *
     * @param buildName
     * @return
     */
    @GetMapping("/getDrawDataListOfBuilding")
    public ResponseResult getDrawDataListOfBuilding(@RequestParam("buildName") String buildName) {
        return ResponseResult.success(drawDataService.getDrawDataListOfBuilding(buildName));
    }

    @GetMapping("/getLineInterpolatePoint")
    public ResponseResult getLineInterpolatePoint(@RequestParam("sceneId") String sceneId) {
        return ResponseResult.success(drawDataService.getLineInterpolatePoint(sceneId));
    }

    @GetMapping("/getPoliceGroup")
    public ResponseResult getPoliceGroup(@RequestParam("sceneId") String sceneId) {
        return ResponseResult.success(drawDataService.getPoliceGroup(sceneId));
    }

    @PostMapping("/updatePoliceGroup")
    public ResponseResult updatePoliceGroup(@RequestBody JSONObject jsonObject) {
        return ResponseResult.success(drawDataService.updatePoliceGroup(jsonObject));
    }

    @PostMapping("/saveLineSort")
    public ResponseResult saveLineSort(@RequestBody List<DrawData> drawDataList) {
        return ResponseResult.success(drawDataService.saveLineSort(drawDataList));
    }
}
