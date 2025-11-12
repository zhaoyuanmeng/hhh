package com.xaxc.teqin.service;


import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.dto.UserDTO;
import com.xaxc.teqin.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    ResponseResult login(String userName, String password);

    Optional<UserDTO> refreshToken(String token);

    ResponseResult verifyToken(String token);

    ResponseResult getUserByUserName(String userName);
    ResponseResult add(User userDTO);

    ResponseResult update(User userDTO);

    ResponseResult delete(String id);

    List<UserDTO> getList(UserDTO userDTO);

    Page<UserDTO> getPage(Page<UserDTO> page, UserDTO userDTO);

}
