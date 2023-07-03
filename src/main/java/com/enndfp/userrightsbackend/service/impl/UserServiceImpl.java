package com.enndfp.userrightsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enndfp.userrightsbackend.Mapper.UserMapper;
import com.enndfp.userrightsbackend.common.ErrorCode;
import com.enndfp.userrightsbackend.exception.BusinessException;
import com.enndfp.userrightsbackend.model.domain.User;
import com.enndfp.userrightsbackend.model.request.UserAddRequest;
import com.enndfp.userrightsbackend.model.request.UserUpdateMyRequest;
import com.enndfp.userrightsbackend.model.request.UserUpdatePasswordRequest;
import com.enndfp.userrightsbackend.model.request.UserUpdateRequest;
import com.enndfp.userrightsbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.enndfp.userrightsbackend.constant.UserConstant.DEFAULT_AVATARURL;
import static com.enndfp.userrightsbackend.constant.UserConstant.USER_LOGIN_STATUS;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "enndfp";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String email, String code, String codeInSession) {

        //1.校验
        //StringUtils是apache.commons.lang包下的，isAnyBlank是判断传入的字符串是否为空字符串或者null，返回布尔类型的值
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, email, code, codeInSession)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于4位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度小于8位");
        }

        //账户不能包含特殊字符
        //\\pP：表示匹配标点符号的字符类，包括所有的标点符号字符。
        //\\pS：表示匹配符号字符的字符类，包括所有的符号字符。
        //\\s+：表示匹配空白字符的字符类，包括空格、制表符、换行符等。+ 表示匹配一个或多个空白字符。
        String validPattern = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号含有特殊字符");
        }

        //密码和校验密码是否相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }
        //验证码和校验码是否相同
        if (!code.equals(codeInSession)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }

        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号名称已存在");
        }

        //邮箱不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱已注册");
        }

        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //3.插入数据
        User user = new User();
        user.setUsername(userAccount);
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setAvatarUrl(DEFAULT_AVATARURL);
        user.setEmail(email);
        int insertResult = userMapper.insert(user);
        if (insertResult != 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "保存数据库失败");
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        //1.校验
        //StringUtils是apache.commons.lang包下的，isAnyBlank是判断传入的字符串是否为空字符串或者null，返回布尔类型的值
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号密码不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于4位");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码小于8位");
        }

        //账户不能包含特殊字符
        String validPattern = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符");
        }

        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null) {
            //记录日志
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不存在或密码不正确");
        }

        //3.用户脱敏
        User handleUser = getHandleUser(user);

        //4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATUS, handleUser);

        return handleUser;
    }

    @Override
    public User getHandleUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User handledUser = new User();
        handledUser.setId(originUser.getId());
        handledUser.setUsername(originUser.getUsername());
        handledUser.setUserAccount(originUser.getUserAccount());
        handledUser.setAvatarUrl(originUser.getAvatarUrl());
        handledUser.setGender(originUser.getGender());
        handledUser.setEmail(originUser.getEmail());
        handledUser.setUserStatus(originUser.getUserStatus());
        handledUser.setPhone(originUser.getPhone());
        handledUser.setCreateTime(originUser.getCreateTime());
        handledUser.setUserRole(originUser.getUserRole());
        return handledUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        //移出登录态
        request.getSession().removeAttribute(USER_LOGIN_STATUS);
        return 1;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        //先判断是否登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        Long userId = currentUser.getId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        return user;
    }

    @Override
    public boolean updateUserByAdmin(UserUpdateRequest updateRequest) {
        Long id = updateRequest.getId();
        String userAccount = updateRequest.getUserAccount();
        String gender = updateRequest.getGender();
        String phone = updateRequest.getPhone();
        String email = updateRequest.getEmail();
        Integer userStatus = updateRequest.getUserStatus();
        Integer userRole = updateRequest.getUserRole();
        String avatarUrl = updateRequest.getAvatarUrl();

        if (id == null || userStatus == null || userRole == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求数据为空");
        }
        if (StringUtils.isAnyBlank(userAccount, gender, avatarUrl)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求数据为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于4位");
        }

        //账户不能包含特殊字符
        String validPattern = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符");
        }

        // 处理不合法的电子邮件地址
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(regex)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱地址格式不合法");
        }

        // 处理不合法的手机
        if (StringUtils.isNotEmpty(phone)) {
            regex = "^1[3456789]\\d{9}$";
            if (!phone.matches(regex)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机格式不正确");
            }
        }

        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.ne("id", id);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号名称已存在");
        }

        //邮箱不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        queryWrapper.ne("id", id);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱已注册");
        }

        User user = new User();
        BeanUtils.copyProperties(updateRequest, user);
        int result = userMapper.updateById(user);
        if (result <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public boolean updateUser(UserUpdateMyRequest updateMyRequest) {
        Long id = updateMyRequest.getId();
        String userAccount = updateMyRequest.getUserAccount();
        String phone = updateMyRequest.getPhone();
        String email = updateMyRequest.getEmail();

        if (StringUtils.isAnyBlank(userAccount, email)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "账号或邮箱为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于4位");
        }

        //账户不能包含特殊字符
        String validPattern = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符");
        }

        // 处理不合法的电子邮件地址
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(regex)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱地址格式不合法");
        }

        // 处理不合法的手机
        if (StringUtils.isNotEmpty(phone)) {
            regex = "^1[3456789]\\d{9}$";
            if (!phone.matches(regex)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机格式不正确");
            }
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.ne("id", id);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号名称已存在");
        }

        //邮箱不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        queryWrapper.ne("id", id);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱已注册");
        }

        User user = new User();
        BeanUtils.copyProperties(updateMyRequest, user);
        int result = userMapper.updateById(user);
        if (result <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public boolean updateUserPassword(UserUpdatePasswordRequest updatePasswordRequest) {
        if (updatePasswordRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long id = updatePasswordRequest.getId();
        String userPassword = updatePasswordRequest.getUserPassword();
        String newPassword = updatePasswordRequest.getNewPassword();

        if (id == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (StringUtils.isAnyBlank(userPassword, newPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "原密码或新密码为空");
        }
        if (userPassword.length() < 8 || newPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不得小于8位");
        }
        if (userPassword.equals(newPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "新密码与原密码相同");
        }

        //MD5加密
        String encryptUserPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        String encryptNewPassword = DigestUtils.md5DigestAsHex((SALT + newPassword).getBytes());

        //查询数据库判断旧密码是否相同
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        User user = userMapper.selectOne(queryWrapper);
        if (!encryptUserPassword.equals(user.getUserPassword())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "原密码错误");
        }
        user.setUserPassword(encryptNewPassword);
        int result = userMapper.updateById(user);
        if (result <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;


    }

    @Override
    public User addUser(UserAddRequest addRequest) {
        String username = addRequest.getUsername();
        String userAccount = addRequest.getUserAccount();
        String userPassword = addRequest.getUserPassword();
        String gender = addRequest.getGender();
        String phone = addRequest.getPhone();
        String email = addRequest.getEmail();
        Integer userStatus = addRequest.getUserStatus();
        Integer userRole = addRequest.getUserRole();
        String avatarUrl = addRequest.getAvatarUrl();

        if (userStatus == null || userRole == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求数据为空");
        }
        if (StringUtils.isAnyBlank(username, userAccount, userPassword, gender, phone, email, avatarUrl)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求数据为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于4位");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度小于8位");
        }

        //账户不能包含特殊字符
        String validPattern = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符");
        }

        // 处理不合法的电子邮件地址
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(regex)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱地址格式不合法");
        }

        // 处理不合法的手机
        regex = "^1[3456789]\\d{9}$";
        if (!phone.matches(regex)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机格式不正确");
        }

        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号名称已存在");
        }

        //邮箱不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱已注册");
        }

        User user = new User();
        BeanUtils.copyProperties(addRequest, user, "userPassword");
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        user.setUserPassword(encryptPassword);

        int result = userMapper.insert(user);
        if (result <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return user;
    }
}




