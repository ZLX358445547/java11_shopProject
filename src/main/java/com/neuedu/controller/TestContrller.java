package com.neuedu.controller;

import com.neuedu.common.ServerReaponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestContrller {

    @Autowired
    UserInfoMapper userInfoMapper;

    @RequestMapping(value = "/user/{userid}")
    public ServerReaponse<UserInfo> findUser(@PathVariable Integer userid){
       UserInfo  u = userInfoMapper.selectByPrimaryKey(userid);

       if (u!=null){
           return ServerReaponse.createServerResponseBySucess(null,u);
       }else {
           return ServerReaponse.createServerResponseByError("fail");
       }
    }

}
