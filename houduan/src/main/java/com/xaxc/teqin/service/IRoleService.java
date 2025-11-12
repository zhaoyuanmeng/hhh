package com.xaxc.teqin.service;



import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.dto.RoleDTO;
import com.xaxc.teqin.model.entity.Role;

import java.util.List;

public interface IRoleService {

    ResponseResult add(Role role);

    ResponseResult update(Role role);

    ResponseResult delete(String id);

    List<RoleDTO> getList(RoleDTO roleDTO);

    Page<RoleDTO> getPage(Page<RoleDTO> page, RoleDTO roleDTO);

    public boolean hasPermission();

}
