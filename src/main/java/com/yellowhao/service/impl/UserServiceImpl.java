package com.yellowhao.service.impl;

import com.yellowhao.dao.UserRepository;
import com.yellowhao.service.Userservice;
import com.yellowhao.util.MD5Utils;
import com.yellowhao.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements Userservice {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        //System.out.println(username);
        //System.out.println(password);
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        //System.out.println(MD5Utils.code(password));
        return user;
    }
}
