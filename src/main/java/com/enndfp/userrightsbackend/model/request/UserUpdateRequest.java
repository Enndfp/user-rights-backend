package com.enndfp.userrightsbackend.model.request;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Enndfp
 */
@Data
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = -7278526695305051752L;

    private Long id;

    /**
     * 用户名
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

    /**
     * 状态  0-正常 1-注销 2-封号
     */
    private Integer userStatus;

    /**
     * user-普通用户 admin-管理员 ban-封号
     */
    private Integer userRole;
}
