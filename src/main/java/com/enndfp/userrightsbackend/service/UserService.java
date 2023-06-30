package com.enndfp.userrightsbackend.service;

import com.enndfp.userrightsbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.enndfp.userrightsbackend.model.request.UserAddRequest;
import com.enndfp.userrightsbackend.model.request.UserUpdateMyRequest;
import com.enndfp.userrightsbackend.model.request.UserUpdatePasswordRequest;
import com.enndfp.userrightsbackend.model.request.UserUpdateRequest;

import javax.servlet.http.HttpServletRequest;


public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param email         邮箱
     * @param code          验证码
     * @param codeInSession 校验码
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String email, String code, String codeInSession);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser 待处理用户
     * @return
     */
    User getHandleUser(User originUser);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 管理员修改用户信息
     *
     * @param updateRequest
     * @return
     */
    boolean updateUserByAdmin(UserUpdateRequest updateRequest);

    /**
     * 用户修改个人信息
     *
     * @param updateMyRequest
     * @return
     */
    boolean updateUser(UserUpdateMyRequest updateMyRequest);

    /**
     * 修改密码
     *
     * @param updatePasswordRequest
     * @return
     */
    boolean updateUserPassword(UserUpdatePasswordRequest updatePasswordRequest);

    /**
     * 新增用户
     *
     * @param addRequest
     * @return
     */
    User addUser(UserAddRequest addRequest);
}
