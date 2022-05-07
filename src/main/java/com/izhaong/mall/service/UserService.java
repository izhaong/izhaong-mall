package com.izhaong.mall.service;

import com.izhaong.mall.exception.MallException;
import com.izhaong.mall.model.pojo.User;


public interface UserService {
    User getUser();

    void register(String username, String password) throws MallException;

    User login(String username, String password) throws MallException;

    void updateInfo(User user);

    boolean checkAdminRole(User user);

}

