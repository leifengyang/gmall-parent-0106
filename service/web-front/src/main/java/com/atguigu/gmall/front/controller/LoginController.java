package com.atguigu.gmall.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login.html")
    public String loginPage(){
        return "login";
    }
}
