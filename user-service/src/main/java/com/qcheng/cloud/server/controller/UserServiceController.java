package com.qcheng.cloud.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qcheng.cloud.server.service.UserService;


@RestController
@RequestMapping(value="/api/v2.0/user")
public class UserServiceController {
    
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


}
