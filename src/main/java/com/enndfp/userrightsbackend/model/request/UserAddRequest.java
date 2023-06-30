package com.enndfp.userrightsbackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Enndfp
 */
@Data
public class UserAddRequest implements Serializable {

    private static final long serialVersionUID = 51579411445243344L;

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
    private String avatarUrl;

    /**
     * 性别
     */
    private String gender;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0-正常 1-注销 2-封号
     */
    private Integer userStatus;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户角色 0-普通用户 1-管理员
     */
    private Integer userRole;
}
