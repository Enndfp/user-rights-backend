package com.enndfp.userrightsbackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户修改个人信息请求
 *
 * @author Enndfp
 */
@Data
public class UserUpdateMyRequest implements Serializable {

    private static final long serialVersionUID = 5989473121488944593L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别 男 女
     */
    private String gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

}
