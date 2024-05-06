package com.wanglei.MyApi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanglei.MyApi.annotation.AuthCheck;
import com.wanglei.MyApi.commmon.BaseResponse;
import com.wanglei.MyApi.commmon.ErrorCode;
import com.wanglei.MyApi.commmon.PageRequest;
import com.wanglei.MyApi.commmon.ResultUtils;
import com.wanglei.MyApi.exception.BusinessException;
import com.wanglei.MyApi.model.domain.dto.UserAkSk;
import com.wanglei.MyApi.model.domain.request.user.*;
import com.wanglei.MyApi.model.domain.vo.ValidCodeVo;
import com.wanglei.MyApi.service.UserService;
import com.wanglei.MyApi.service.ValidCodeService;
import com.wanglei.MyApicommon.model.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.wanglei.MyApi.constant.UserConstant.ADMIN_ROLE;
import static com.wanglei.MyApi.constant.UserConstant.USER_LOGIN_STATE;

@RestController //适用于编写restful风格的API，返回值默认为json类型
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = {"http://localhost:8000","http://192.168.237.129","http://175.24.166.167"}, allowCredentials = "true")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ValidCodeService validCodeService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求体
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.UserRegister(userRegisterRequest);
        return ResultUtils.success(result);

    }

    /**
     * 获取验证码
     *
     * @return
     */
    @PostMapping("/getValidCode")
    public BaseResponse<ValidCodeVo> getValidCode() {
        return ResultUtils.success(validCodeService.getValidCode());
    }

    /**
     * 添加用户
     *
     * @param userAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        return ResultUtils.success(userService.addUser(userAddRequest, request));
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求体
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User user = userService.doLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);

    }

    /**
     * 用户注销
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Long id = currentUser.getId();

        User user = userService.getById(id);
        User safetyuser = userService.getSafetUser(user);
        return ResultUtils.success(safetyuser);

    }

    /**
     * 查询获取用户（分页）
     */
    @PostMapping("list/page")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
                                                   HttpServletRequest request) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        QueryWrapper<User> queryWrapper = userService.getQueryWrapper(userQueryRequest);
        Page<User> userPage = userService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(userPage);
    }


    /**
     * 修该信息(管理员）
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
                                            HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        return ResultUtils.success(result);
    }

    /**
     * 修改密码
     */
    @PostMapping("/updatePwd")
    public BaseResponse<Boolean> updatePwd(@RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest,
                                           HttpServletRequest request) {
        if (userUpdatePasswordRequest == null || userUpdatePasswordRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (!Objects.equals(loginUser.getId(), userUpdatePasswordRequest.getId()) && (!isAdmin(request))){
                throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean result = userService.updatePassword(loginUser,userUpdatePasswordRequest);
        return ResultUtils.success(result);
    }

    /**
     * 修该信息(用户）
     *
     * @param userUpdateByUserRequest
     * @param request
     * @return
     */
    @PostMapping("/update/by_user")
    public BaseResponse<Boolean> updateUserBySelf(@RequestBody UserUpdateByUserRequest userUpdateByUserRequest,
                                                  HttpServletRequest request) {
        if (userUpdateByUserRequest == null || userUpdateByUserRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (!Objects.equals(loginUser.getId(), userUpdateByUserRequest.getId()) && !isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateByUserRequest, user);
        boolean result = userService.updateById(user);
        return ResultUtils.success(result);
    }


    /**
     * 用户删除
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> deleteUser(@RequestParam("id") long id) {
        //仅管理员可删除
        if (id <= 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 判断是否为管理员
     *
     * @return
     */

    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

    /**
     * 获取accessKey和secretKey
     *
     * @param id      用户id
     * @param request
     * @return
     */
    @PostMapping("/get/key")
    public BaseResponse<UserAkSk> getUserKey(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        if (user == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (user.getId() != id && !isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        UserAkSk key = userService.getKey(user);
        return ResultUtils.success(key);
    }

    /**
     * 重建accessKey和secretKey
     *
     * @param id      用户id
     * @param request
     * @return
     */
    @PostMapping("/rebuild_key")
    public BaseResponse<UserAkSk> rebuildUserKey(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        if (user == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        if (user.getId() != id && !isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        UserAkSk newKey = userService.rebuildKey(user);
        return ResultUtils.success(newKey);
    }

}
