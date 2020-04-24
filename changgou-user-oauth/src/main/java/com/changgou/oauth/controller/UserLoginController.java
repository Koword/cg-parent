package com.changgou.oauth.controller;

import com.changgou.oauth.service.UserLoginService;
import com.changgou.oauth.util.AuthToken;
import entity.Result;
import entity.StatusCode;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 16:21 2020/4/23
 **/
@RestController
@RequestMapping("/user")
public class UserLoginController {

    //客户端ID
    @Value("${auth.clientId}")
    private String clientId;

    //秘钥
    @Value("${auth.clientSecret}")
    private String clientSecret;

    //Cookie存储的域名
    @Value("${auth.cookieDomain}")
    private String cookieDomain;

    @Autowired
    UserLoginService userLoginService;


    @GetMapping("/login")
    public Result login(String username, String password, HttpServletResponse response) {
        try {
            // 授权方式
            String grant_type = "password";
            // 完成登录操作，并且生成token
            AuthToken authToken = userLoginService.login(clientId, clientSecret, username, password, grant_type);

            // 登录成功后，将令牌存储到cookie中
            Cookie cookie = new Cookie("Authorization", authToken.getAccessToken());
            cookie.setDomain(cookieDomain);
            cookie.setPath("/");
            response.addCookie(cookie);
            return new Result(true, StatusCode.OK, "登录成功", authToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, StatusCode.OK, "登录失败");
    }


}
