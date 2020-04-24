/*
package com.changgou.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

*/
/**
 * @Description 重定向登录
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 15:11 2020/4/24
 **//*

@Controller
@RequestMapping("/oauth")
public class LoginRedirect {


    @GetMapping("/login")
    public String login(@RequestParam(value = "ReturnUrl", required = false) String ReturnUrl, Model model) {
        model.addAttribute("ReturnUrl", ReturnUrl);
        return "login";
    }


}
*/
