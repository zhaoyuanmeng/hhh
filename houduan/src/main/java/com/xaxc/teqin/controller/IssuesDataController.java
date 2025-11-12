package com.xaxc.teqin.controller;


import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.entity.IssuesData;
import com.xaxc.teqin.model.entity.Org;
import com.xaxc.teqin.service.IIssuesDataService;
import com.xaxc.teqin.service.impl.IssuesDataServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-01-14
 */
@RestController
@RequestMapping("/issues-data")
public class IssuesDataController {

    @Resource
    IIssuesDataService issuesDataService;


    @PostMapping("/add")
    public ResponseResult add(@RequestBody IssuesData issuesData) {
        issuesData.setDeleteFlag("0");
        return issuesDataService.save(issuesData) ? ResponseResult.success() : ResponseResult.error();
    }

    @PostMapping("/update")
    public ResponseResult update(@RequestBody IssuesData issuesData) {
        return issuesDataService.updateById(issuesData) ? ResponseResult.success() : ResponseResult.error();
    }

    @GetMapping("/getById")
    public ResponseResult getById(@RequestParam("id") String id) {
        return ResponseResult.success(issuesDataService.getById(id));
    }

    @GetMapping("/delete")
    public ResponseResult delete(@RequestParam("id") String id) {
        return ResponseResult.success(issuesDataService.removeById(id));
    }

    @GetMapping("/getList")
    public ResponseResult getList(@RequestParam(value = "id", required = false) String id) {
        return ResponseResult.success(issuesDataService.getList(id));
    }

    @GetMapping("/getIssuesNum")
    public ResponseResult getIssuesNum() {
        return ResponseResult.success(issuesDataService.getIssuesNum());
    }


}
