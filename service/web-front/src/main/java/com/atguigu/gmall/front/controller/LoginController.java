package com.atguigu.gmall.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login.html")
    public String loginPage(@RequestParam(value = "originUrl",defaultValue = "http://www.gmall.com")
                                        String originUrl, Model model){
        //TODO originUrl:?后面的会丢了。。。。
        model.addAttribute("originUrl",originUrl);
        return "login";
    }
}
