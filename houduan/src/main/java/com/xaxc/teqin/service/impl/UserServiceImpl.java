package com.xaxc.teqin.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.xaxc.teqin.common.model.CustomClaim;
import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.common.util.JwtUtil;
import com.xaxc.teqin.component.AuthInterceptor;
import com.xaxc.teqin.mapper.UserMapper;
import com.xaxc.teqin.model.dto.UserDTO;
import com.xaxc.teqin.model.entity.User;
import com.xaxc.teqin.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper mapper;

    @Override
    public ResponseResult login(String userName, String password) {

        UserDTO userDTO = mapper.selectUserByUserName(userName);
        if (userDTO == null) {
            return ResponseResult.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("账号不存在")
                    .build();
        }
        if (!password.equals(userDTO.getPassword())) {
            return ResponseResult.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("账号或密码不正确")
                    .build();
        }

        CustomClaim customClaim = new CustomClaim(userDTO.getId(), userName, userDTO.getRealName(), password);
        // create token
        String token = JwtUtil.createToken(customClaim.convertToMap());
        userDTO.setToken(token);
        return ResponseResult.success(userDTO);
    }

    @Override
    public Optional<UserDTO> refreshToken(String token) {
        if (!StringUtils.hasText(token)) {
            return Optional.empty();
        }
        CustomClaim customClaim;
        try {
            DecodedJWT jwt = JwtUtil.verifyToken(token);
            customClaim = new CustomClaim(jwt.getClaims());
        } catch (TokenExpiredException e) {
            customClaim = new CustomClaim(JWT.decode(token).getClaims());
        } catch (Exception e) {
            return Optional.empty();
        }
        String refreshToken = JwtUtil.createToken(customClaim.convertToMap());

        UserDTO user = (UserDTO) getUserByUserName(customClaim.getUserName()).getData();
        if (Objects.isNull(user)) {
            return Optional.empty();
        }
        user.setToken(refreshToken);
        return Optional.of(user);
    }

    @Override
    public ResponseResult verifyToken(String token) {
        if (!StringUtils.hasText(token)) {
            return ResponseResult.error("请传入token");
        }
        try {
            JwtUtil.verifyToken(token);
        } catch (TokenExpiredException e) {
            return ResponseResult.error(e.getMessage());
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult getUserByUserName(String userName) {
        return ResponseResult.success(mapper.selectUserByUserName(userName));
    }


    @Override
    public ResponseResult add(User userDTO) {
        UserDTO user = mapper.selectUserByUserName(userDTO.getUserName());
        if (user != null) {
            return ResponseResult.error("用户名已存在");
        }
        return mapper.insert(userDTO) > 0 ? ResponseResult.success() : ResponseResult.error("新增用户失败");
    }

    @Override
    public ResponseResult update(User userDTO) {
        return mapper.update(userDTO, new LambdaQueryWrapper<User>().eq(User::getUserName, userDTO.getUserName())
                .or().eq(User::getId, userDTO.getId())) > 0 ?
                ResponseResult.success() : ResponseResult.error("编辑用户失败");
    }

    @Override
    public ResponseResult delete(String id) {
        CustomClaim customClaim = ((CustomClaim) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getAttribute(AuthInterceptor.TOKEN_CLAIM));
        String currentName = customClaim.getUserName();
        User userEntity = mapper.selectById(id);
        if ("root".equals(userEntity.getUserName())) {
            return ResponseResult.error("管理员账号不允许删除");
        } else if (userEntity.getUserName().equals(currentName)) {
            return ResponseResult.error("不允许删除当前登录账号");
        }
        User userDTO = new User();
        userDTO.setId(id);
        userDTO.setDeleteFlag(-1);
        return mapper.update(userDTO, new LambdaQueryWrapper<User>().eq(User::getId, id)) > 0 ?
                ResponseResult.success() : ResponseResult.error("删除用户失败");
    }

    @Override
    public List<UserDTO> getList(UserDTO userDTO) {
        return mapper.selectUserList(userDTO);
    }

    @Override
    public Page<UserDTO> getPage(Page page, UserDTO userDTO) {
        return mapper.selectUserList(page, userDTO);
    }
}
