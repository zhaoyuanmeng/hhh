package com.xaxc.teqin.controller;


import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.model.PageCondition;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.dto.OrgDTO;
import com.xaxc.teqin.model.entity.Org;
import com.xaxc.teqin.service.IOrgService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/org")
public class OrgController {

    @Resource
    IOrgService orgService;

    /**
     * 新增机构
     * @param orgEntity
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody Org orgEntity) {
        return orgService.add(orgEntity);
    }

    /**
     * 编辑机构
     * @param orgEntity
     * @return
     */
    @PostMapping("/update")
    public ResponseResult update(@RequestBody Org orgEntity) {
        return orgService.update(orgEntity);
    }

    /**
     * 删除机构
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public ResponseResult delete(@RequestParam("id") String id) {
        return orgService.delete(id);
    }

    /**
     * 获取机构列表
     * @param orgDTO
     * @return
     */
    @PostMapping("/getList")
    public ResponseResult getList(@RequestBody OrgDTO orgDTO) {
        return ResponseResult.success(orgService.getList(orgDTO));
    }

    /**
     * 获取机构分页
     * @param pageCondition
     * @return
     */
    @PostMapping("/getPage")
    public ResponseResult<Page<OrgDTO>> getPage(@RequestBody PageCondition<OrgDTO> pageCondition) {
        Page<OrgDTO> page = pageCondition.getPage();
        OrgDTO orgDTO = pageCondition.getEntity();
        return ResponseResult.success(orgService.getPage(page, orgDTO));
    }


}
