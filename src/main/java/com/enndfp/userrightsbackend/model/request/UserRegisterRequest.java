package com.enndfp.userrightsbackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author Enndfp
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 2581790542448011603L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String email;

    private String code;

}
