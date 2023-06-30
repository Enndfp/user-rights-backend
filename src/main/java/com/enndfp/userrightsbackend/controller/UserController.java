package com.enndfp.userrightsbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enndfp.userrightsbackend.common.BaseResponse;
import com.enndfp.userrightsbackend.common.ErrorCode;
import com.enndfp.userrightsbackend.exception.BusinessException;
import com.enndfp.userrightsbackend.model.domain.User;
import com.enndfp.userrightsbackend.model.request.*;
import com.enndfp.userrightsbackend.service.UserService;
import com.enndfp.userrightsbackend.utils.ResultUtils;
import com.enndfp.userrightsbackend.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.enndfp.userrightsbackend.constant.UserConstant.ADMIN_ROLE;
import static com.enndfp.userrightsbackend.constant.UserConstant.USER_LOGIN_STATUS;

/**
 * @author Enndfp
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册请求
     *
     * @param userRegisterRequest
     * @param request
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String email = userRegisterRequest.getEmail();
        String code = userRegisterRequest.getCode();
        String codeInSession = (String) request.getSession().getAttribute("email");
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, email, code, codeInSession)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        long userId = userService.userRegister(userAccount, userPassword, checkPassword, email, code, codeInSession);
        return ResultUtils.success(userId);
    }

    /**
     * 用户登录请求
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销请求
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int logoutResult = userService.userLogout(request);
        return ResultUtils.success(logoutResult);
    }

    /**
     * 当前登录用户请求
     *
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        //获取登录态
        User userResult = userService.getLoginUser(request);
        User handleUser = userService.getHandleUser(userResult);
        return ResultUtils.success(handleUser);
    }

    /**
     * 新增用户
     *
     * @param addRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest addRequest, HttpServletRequest request) {
        //仅管理员可新增
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限");
        }
        if (addRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        User user = userService.addUser(addRequest);
        return ResultUtils.success(user.getId());
    }


    /**
     * 删除用户
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody UserDeleteRequest deleteRequest, HttpServletRequest request) {
        //仅管理员可删除
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean removeResult = userService.removeById(deleteRequest.getId());
        if (!removeResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(removeResult);
    }

    /**
     * 修改用户
     *
     * @param updateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest updateRequest, HttpServletRequest request) {
        //仅管理员可修改
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        if (updateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean updateResult = userService.updateUserByAdmin(updateRequest);
        return ResultUtils.success(updateResult);
    }

    /**
     * 用户修改个人信息
     *
     * @param updateMyRequest
     * @return
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest updateMyRequest) {
        if (updateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean updateMyResult = userService.updateUser(updateMyRequest);
        return ResultUtils.success(updateMyResult);
    }

    /**
     * 修改密码
     *
     * @param updatePasswordRequest
     * @return
     */
    @PostMapping("update/password")
    public BaseResponse<Boolean> updateUserPassword(@RequestBody UserUpdatePasswordRequest updatePasswordRequest) {
        if (updatePasswordRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean updatePasswordResult = userService.updateUserPassword(updatePasswordRequest);
        return ResultUtils.success(updatePasswordResult);
    }

    /**
     * 查询用户
     *
     * @param searchRequest
     * @param request
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(UserSearchRequest searchRequest, HttpServletRequest request) {
        //仅管理员可查询
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        String username = searchRequest.getUsername();
        String userAccount = searchRequest.getUserAccount();
        String gender = searchRequest.getGender();
        String phone = searchRequest.getPhone();
        String email = searchRequest.getEmail();
        Integer userStatus = searchRequest.getUserStatus();
        Integer userRole = searchRequest.getUserRole();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        if (StringUtils.isNotBlank(userAccount)) {
            queryWrapper.like("userAccount", userAccount);
        }
        if (StringUtils.isNotBlank(gender)) {
            queryWrapper.eq("gender", gender);
        }
        if (StringUtils.isNotBlank(phone)) {
            queryWrapper.like("phone", phone);
        }
        if (StringUtils.isNotBlank(email)) {
            queryWrapper.like("email", email);
        }
        if (userStatus != null) {
            queryWrapper.eq("userStatus", userStatus);
        }
        if (userRole != null) {
            queryWrapper.eq("userRole", userRole);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> users = userList.stream().map(user -> userService.getHandleUser(user)).collect(Collectors.toList());
        if (users.size() == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "查询失败");
        }
        return ResultUtils.success(users);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private Boolean isAdmin(HttpServletRequest request) {
        //仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

    /**
     * 发送验证码
     *
     * @param map
     * @param request
     * @return
     * @throws MessagingException
     */
    @PostMapping("/sendMsg")
    public BaseResponse<Boolean> sendMsg(@RequestBody Map<String, String> map, HttpServletRequest request) throws MessagingException {
        String email = map.get("email");
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(regex)) {
            // 处理不合法的电子邮件地址
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (StringUtils.isNotBlank(email)) {
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);

            //发送邮箱验证码
            //MailUtils.sendEmail(email, code);

            //需要将生成的验证码保存到Session
            request.getSession().setAttribute("email", code);
            return ResultUtils.success(true);
        }
        throw new BusinessException(ErrorCode.NULL_ERROR);
    }
}
