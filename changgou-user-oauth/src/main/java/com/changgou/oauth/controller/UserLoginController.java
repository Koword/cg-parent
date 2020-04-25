package com.changgou.oauth.controller;

import com.changgou.oauth.service.UserLoginService;
import com.changgou.oauth.util.AuthToken;
import com.changgou.oauth.util.CookieUtil;
import entity.Result;
import entity.StatusCode;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import jdk.net.SocketFlow.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author tangkai
 * @Date 21:26 2020-4-25 0025
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

    //Cookie生命周期
    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;

    @Autowired
    UserLoginService userLoginService;

    @RequestMapping("/login")
    public Result login(String username, String password, HttpServletResponse response) {
        try {
            // 授权方式
            String grant_type = "password";
            // 完成登录操作，并且生成token
            AuthToken authToken = userLoginService
                .login(clientId, clientSecret, username, password, grant_type);
            // 登录成功后，将令牌存储在cookie中
            saveCookie(authToken.getAccessToken());
            return new Result(true, StatusCode.OK, "登录成功", authToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(true,StatusCode.OK,"登录失败-_-");
    }

    /***
     * 将令牌存储到cookie
     * @param token
     */
    private void saveCookie(String token){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response,cookieDomain,"/","Authorization",token,cookieMaxAge,false);
    }

}
