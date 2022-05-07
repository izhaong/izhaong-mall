package com.izhaong.mall.service.impl;

import com.izhaong.mall.exception.MallException;
import com.izhaong.mall.exception.MallExceptionEnum;
import com.izhaong.mall.model.dao.UserMapper;
import com.izhaong.mall.model.pojo.User;
import com.izhaong.mall.service.UserService;
import com.izhaong.mall.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;


/**
 * @author izhaong
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User getUser() {
        return userMapper.selectByPrimaryKey(1);
    }

    @Override
    public void register(String username, String password) throws MallException {
        //查询用户名是否存在，不允许重名
        User result = userMapper.selectByName(username);
        if (result != null) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        // 存入sql
        User user = new User();
        user.setUsername(username);
        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.INSERT_FAILED);
        }

    }

    @Override
    public User login(String username, String password) throws MallException {
        String md5Password = null;
        try {
            md5Password = MD5Utils.getMD5Str((password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            throw new MallException(MallExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }

    @Override
    public void updateInfo(User user) {
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 1) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILD);
        }
    }

    @Override
    public boolean checkAdminRole(User user) {
        //1 普通用户 2 管理员
        return user.getRole().equals(2);
    }

}
