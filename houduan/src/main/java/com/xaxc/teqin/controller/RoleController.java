package com.xaxc.teqin.controller;


import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.model.PageCondition;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.dto.RoleDTO;
import com.xaxc.teqin.model.entity.Role;
import com.xaxc.teqin.service.IRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    IRoleService roleService;

    /**
     * 新增角色
     * @param roleEntity
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody Role roleEntity) {
        return roleService.add(roleEntity);
    }

    /**
     * 编辑角色
     * @param roleEntity
     * @return
     */

    @PostMapping("/update")
    public ResponseResult update(@RequestBody Role roleEntity) {
        return roleService.update(roleEntity);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public ResponseResult delete(@RequestParam("id") String id) {
        return roleService.delete(id);
    }

    /**
     * 获取角色列表
     * @param roleDTO
     * @return
     */
    @PostMapping("/getList")
    public ResponseResult getList(@RequestBody RoleDTO roleDTO) {
        return ResponseResult.success(roleService.getList(roleDTO));
    }

    /**
     * 获取角色分页
     * @param pageCondition
     * @return
     */
    @PostMapping("/getPage")
    public ResponseResult<Page<RoleDTO>> getPage(@RequestBody PageCondition<RoleDTO> pageCondition) {
        Page<RoleDTO> page = pageCondition.getPage();
        RoleDTO roleEntity = pageCondition.getEntity();
        return ResponseResult.success(roleService.getPage(page, roleEntity));
    }


}
