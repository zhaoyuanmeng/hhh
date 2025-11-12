package com.xaxc.teqin.controller;


import com.xaxc.teqin.common.model.CustomClaim;
import com.xaxc.teqin.common.model.Page;
import com.xaxc.teqin.common.model.PageCondition;
import com.xaxc.teqin.common.model.ResponseResult;
import com.xaxc.teqin.model.dto.UserDTO;
import com.xaxc.teqin.model.dto.UserLoginDTO;
import com.xaxc.teqin.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static com.xaxc.teqin.component.AuthInterceptor.PARAM_TOKEN;
import static com.xaxc.teqin.component.AuthInterceptor.TOKEN_CLAIM;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    IUserService userService;

    /**
     * 用户登录
     *
     * @param loginDTO
     * @return
     */
    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserLoginDTO loginDTO) {
        return userService.login(loginDTO.getUserName(), loginDTO.getPassword());
    }

    /**
     * 验证token
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/token/verify")
    public ResponseResult verifyToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(PARAM_TOKEN);
        return userService.verifyToken(token);
    }

    /**
     * 刷新token
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/token/refresh")
    public ResponseResult refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(PARAM_TOKEN);
        Optional<UserDTO> user = userService.refreshToken(token);

        if (user.isEmpty()) {
            response.setStatus(200);
            return ResponseResult.error("token invalid");
        }

        return ResponseResult.success(user.get());
    }

    /**
     * 获取当前用户信息
     *
     * @param request
     * @return
     */
    @GetMapping("/current")
    public ResponseResult getCurrentUserInfo(HttpServletRequest request) {
        CustomClaim customClaim = (CustomClaim) request.getAttribute(TOKEN_CLAIM);
        return userService.getUserByUserName(customClaim.getUserName());
    }

    /**
     * 新增用户
     *
     * @param userDTO
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody UserDTO userDTO) {
        return userService.add(userDTO);
    }

    /**
     * 编辑用户
     *
     * @param userDTO
     * @return
     */
    @PostMapping("/update")
    public ResponseResult update(@RequestBody UserDTO userDTO) {
        return userService.update(userDTO);
    }

    /**
     * 编辑用户
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public ResponseResult delete(@RequestParam("id") String id) {
        return userService.delete(id);
    }

    @PostMapping("/getList")
    public ResponseResult<List<UserDTO>> getList(@RequestBody UserDTO userDTO) {
        return ResponseResult.success(userService.getList(userDTO));
    }

    /**
     * 获取用户分页数据
     *
     * @param pageCondition
     * @return
     */
    @PostMapping("/getPage")
    public ResponseResult<Page<UserDTO>> getPage(@RequestBody PageCondition<UserDTO> pageCondition) {
        Page<UserDTO> page = pageCondition.getPage();
        UserDTO userDTO = pageCondition.getEntity();
        return ResponseResult.success(userService.getPage(page, userDTO));
    }


}
