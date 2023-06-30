package com.enndfp.userrightsbackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户修改密码请求体
 *
 * @author Enndfp
 */
@Data
public class UserUpdatePasswordRequest implements Serializable {

    private static final long serialVersionUID = 7863184327227859561L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 原密码
     */
    private String userPassword;

    /**
     * 新密码
     */
    private String newPassword;
}
