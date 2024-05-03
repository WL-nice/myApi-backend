package com.wanglei.MyApi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wanglei.MyApi.model.domain.dto.UserAkSk;
import com.wanglei.MyApi.model.domain.request.user.UserAddRequest;
import com.wanglei.MyApi.model.domain.request.user.UserQueryRequest;
import com.wanglei.MyApi.model.domain.request.user.UserRegisterRequest;
import com.wanglei.MyApi.model.domain.request.user.UserUpdatePasswordRequest;
import com.wanglei.MyApicommon.model.User;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户服务
* @author muqiu
*/
public interface UserService extends IService<User>  {


    /**
     * 用户注册
     *
     * @return 用户id
     */
    long UserRegister(UserRegisterRequest userRegisterRequest);

    /**
     *用户登录
     * @param userAccount 账号
     * @param userPassword 密码
     * @return 脱敏后的用户信息
     */
    User doLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param user
     * @return
     */

    User getSafetUser(User user);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 获取登录用户信息
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 判断是否为管理员
     */
    boolean isAdmin(HttpServletRequest request);

    boolean isAdmin(User loginUser);

    UserAkSk getKey(User user);

    /**
     * 添加用户
     */
    Boolean addUser(UserAddRequest userAddRequest, HttpServletRequest request);

    /**
     * 获取查询条件
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);


    /**
     * 重新生成密钥
     */
    UserAkSk rebuildKey(User user);

    /**
     * 更新密码
     */
    boolean updatePassword(User loginUser,UserUpdatePasswordRequest userUpdatePasswordRequest);
}
