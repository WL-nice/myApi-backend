package com.wanglei.MyApi.model.domain.request.user;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户角色: 0-user, 1-admin
     */
    private Integer userRole;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
