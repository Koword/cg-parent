package com.changgou.oauth.service;

import com.changgou.oauth.util.AuthToken;

/**
 * @Description
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 15:27 2020/4/23
 **/
public interface UserLoginService {

    /**
     *
     * @param clientId 客户端id
     * @param clientSecret  客户端秘钥
     * @param username  用户名
     * @param password  密码
     * @param grant_type   授权类型
     * @return
     */
    AuthToken login(String clientId,String clientSecret,String username,String password,String grant_type);

}
