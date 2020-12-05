package com.yellowhao.service;

import com.yellowhao.po.User;


public interface Userservice {

    /**
     * 用户登录检测
     * @param
     * @return
     */
    User checkUser(String username,String password);

}
