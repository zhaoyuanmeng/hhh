package com.xaxc.teqin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xaxc.teqin.common.model.CustomClaim;
import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.component.AuthInterceptor;
import com.xaxc.teqin.mapper.RoleMapper;
import com.xaxc.teqin.mapper.UserMapper;
import com.xaxc.teqin.model.dto.RoleDTO;
import com.xaxc.teqin.model.entity.Role;
import com.xaxc.teqin.model.entity.User;
import com.xaxc.teqin.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.util.List;

import static com.xaxc.teqin.component.AuthInterceptor.PARAM_TOKEN;

@Service
@Transactional
@Slf4j
public class RoleServiceImpl implements IRoleService {

    @Resource
    private RoleMapper mapper;

    @Resource
    private UserMapper userMapper;


    @Override
    public boolean hasPermission() {
        CustomClaim customClaim = (CustomClaim) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getAttribute(AuthInterceptor.TOKEN_CLAIM);
        if (customClaim != null && customClaim.getId() != null) {
            String roleId = userMapper.selectById(customClaim.getId()).getRoleId();
            if (roleId != null) {
                return mapper.selectById(roleId).getResources().contains("解密权限");
            }
        }
        return false;
    }

    @Override
    public ResponseResult add(Role roleDTO) {
        RoleDTO roleEntity = mapper.selectRoleByNameOrCode(roleDTO.getRoleName());
        if (roleEntity != null) {
            return ResponseResult.error("角色名称已存在");
        }
        return mapper.insert(roleDTO) > 0 ? ResponseResult.success() : ResponseResult.error("新增角色失败");
    }

    @Override
    public ResponseResult update(Role roleEntity) {
        List<Role> list = mapper.selectList(new LambdaQueryWrapper<Role>().ne(Role::getId, roleEntity.getId())
                .and(r -> {
                    r.eq(Role::getRoleName, roleEntity.getRoleName());
                }));

        if (!CollectionUtils.isEmpty(list)) {
            return ResponseResult.error("角色名称已存在");
        }
        return mapper.update(roleEntity, new LambdaQueryWrapper<Role>().eq(Role::getRoleName, roleEntity.getRoleName())
                .or().eq(Role::getId, roleEntity.getId())) > 0 ?
                ResponseResult.success() : ResponseResult.error("编辑角色失败");
    }


    @Override
    public ResponseResult delete(String id) {
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getRoleId, id).eq(User::getDeleteFlag, 0));
        if (count > 0) {
            return ResponseResult.error("删除角色失败,该角色已有用户关联");
        }
        Role roleEntity = new Role();
        roleEntity.setId(id);
        roleEntity.setDeleteFlag(-1);
        return mapper.update(roleEntity, new LambdaQueryWrapper<Role>().eq(Role::getId, id)) > 0 ?
                ResponseResult.success() : ResponseResult.error("删除角色失败");
    }

    @Override
    public List<RoleDTO> getList(RoleDTO roleDTO) {
        return mapper.selectRoleList(roleDTO);
    }

    @Override
    public Page<RoleDTO> getPage(Page<RoleDTO> page, RoleDTO roleDTO) {
        return mapper.selectRoleList(page, roleDTO);
    }

}
