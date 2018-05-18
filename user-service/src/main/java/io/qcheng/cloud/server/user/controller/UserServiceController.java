package io.qcheng.cloud.server.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.qcheng.cloud.server.user.service.UserService;


@RestController
@RequestMapping(value="/v1.0/user")
public class UserServiceController {
    
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


}
