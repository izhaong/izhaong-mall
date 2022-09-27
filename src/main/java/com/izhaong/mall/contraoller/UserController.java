package com.izhaong.mall.contraoller;

import com.izhaong.mall.common.ApiRestResponse;
import com.izhaong.mall.common.Constant;
import com.izhaong.mall.exception.MallException;
import com.izhaong.mall.exception.MallExceptionEnum;
import com.izhaong.mall.model.pojo.User;
import com.izhaong.mall.service.UserService;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/test")
    @ResponseBody
    public User personalPage() {
        return userService.getUser();
    }

    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestBody User user) throws MallException {
        String username = user.getUsername();
        String password  = user.getPassword();
        if (StringUtils.isNullOrEmpty(username)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        if (password.length() < 8) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_TOO_SHORT);
        }
        userService.register(username, password);
        return ApiRestResponse.success();
    }


    @PostMapping("/login")
    @ResponseBody
//    public ApiRestResponse login(@RequestParam String username, @RequestParam String password, HttpSession session) {
    public ApiRestResponse login(@RequestBody User user, HttpSession session) {
        if (StringUtils.isNullOrEmpty(user.getUsername())) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isNullOrEmpty(user.getPassword())) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        if (user.getPassword().length() < 6) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_TOO_SHORT);
        }

        User cUser = userService.login(user.getUsername(), user.getPassword());

        cUser.setPassword(null);
        session.setAttribute(Constant.MALL_USER, cUser);

        return ApiRestResponse.success(cUser);
    }

    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestResponse updateUserInfo(HttpSession session, @RequestParam String signature) {
        User currentUser = (User) session.getAttribute(Constant.MALL_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        user.setPersonalizedSignature(signature);
        user.setId(currentUser.getId());

        userService.updateInfo(user);
        return ApiRestResponse.success();
    }


    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session) {
        session.removeAttribute(Constant.MALL_USER);
        return ApiRestResponse.success();
    }

    @PostMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminLogin(@RequestParam String username, @RequestParam String password, HttpSession session) {
        if (StringUtils.isNullOrEmpty(username)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        if (password.length() < 8) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_TOO_SHORT);
        }


        User user = userService.login(username, password);

        if (userService.checkAdminRole(user)) {
            user.setPassword(null);
            session.setAttribute(Constant.MALL_USER, user);

            return ApiRestResponse.success(user);
        } else {
            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
        }


    }


}
