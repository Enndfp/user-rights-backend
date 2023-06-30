package com.enndfp.userrightsbackend.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户查询请求
 *
 * @author Enndfp
 */
@Data
public class UserSearchRequest implements Serializable {
    private static final long serialVersionUID = 3441173182436118160L;
    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;


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
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 0-普通用户 1-管理员 2-封号
     */
    private Integer userRole;
}
