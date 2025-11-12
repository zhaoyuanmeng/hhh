package com.xaxc.teqin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.model.dto.RoleDTO;
import com.xaxc.teqin.model.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {


    RoleDTO selectRoleByNameOrCode(String keyword);

    List<RoleDTO> selectRoleList(@Param("dto") RoleDTO roleDTO);


    Page<RoleDTO> selectRoleList(Page page, @Param("dto") RoleDTO roleDTO);

}
