package com.xaxc.teqin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.model.dto.UserDTO;
import com.xaxc.teqin.model.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    UserDTO selectUserByUserName(@Param("userName") String userName);


    List<UserDTO> selectUserList(@Param("dto") UserDTO userDTO);


    Page<UserDTO> selectUserList(Page page, @Param("dto") UserDTO userDTO);

}
