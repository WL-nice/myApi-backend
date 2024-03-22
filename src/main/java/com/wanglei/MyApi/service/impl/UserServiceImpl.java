package com.wanglei.MyApi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanglei.MyApi.commmon.ErrorCode;
import com.wanglei.MyApi.exception.BusinessException;
import com.wanglei.MyApi.service.UserService;
import com.wanglei.MyApi.mapper.UserMapper;
import com.wanglei.MyApicommon.model.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wanglei.MyApi.constant.UserConstant.ADMIN_ROLE;
import static com.wanglei.MyApi.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author master
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private UserMapper userMapper;
    /**
     * 盐值，混淆密码
     */
    private static final String SALT="muqiu";



    @Override
    public long UserRegister(String userAccount, String userPassword, String checkPassword) {
        //1、校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {

            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度小于4位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度小于8位");
        }

        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"不能包含特殊字符");
        }
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入的密码不同");
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号已注册");
        }

//        //校验编号不能重复
//        queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("acptCode", acptCode);
//        count = userMapper.selectCount(queryWrapper);
//        if (count > 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编号重复");
//        }

        //2、加密
        String entryptfPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        String accessKey = userAccount+"myapia";
        String secretKey = userAccount+"myapis";

        //3、插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(entryptfPassword);
        user.setAccessKey(accessKey);
        user.setSecretKey(secretKey);
        user.setUsername(userAccount);

        boolean saveResult = this.save(user);
        if(!saveResult){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return user.getId();
    }

    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1、校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度小于4位");
        }
        if (userPassword.length() < 8 ) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度小于8位");
        }

        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号不能包含特殊字符");
        }

        //2、加密
        String entryptfPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", entryptfPassword);
        User user = userMapper.selectOne(queryWrapper);
        //
        if(user == null){
            log.info("user login failed,UserAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码错误");
        }


        //3、脱敏
        User safetUser = getSafetUser(user);
        //4、记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetUser);
        return safetUser;
    }

    /**
     * 用户脱敏
     * @param user 用户
     * @return 脱敏后的信息
     */
    @Override
    public User getSafetUser(User user){
        User safetUser = new User();
        safetUser.setId(user.getId());
        safetUser.setUsername(user.getUsername());
        safetUser.setUserAccount(user.getUserAccount());
        safetUser.setAvatarUrl(user.getAvatarUrl());
        safetUser.setGender(user.getGender());
        safetUser.setPhone(user.getPhone());
        safetUser.setEmail(user.getEmail());
        safetUser.setAcptCode(user.getAcptCode());
        safetUser.setUserRole(user.getUserRole());
        safetUser.setUserStatus(user.getUserStatus());
        safetUser.setCreateTime(user.getCreateTime());
        return safetUser;

    }

    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;

    }

    /**
     * 判断是否为管理员
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

    @Override
    public boolean isAdmin(User loginUser) {
        return loginUser != null && loginUser.getUserRole() == ADMIN_ROLE;
    }


    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        return (User) userObj;
    }

}




