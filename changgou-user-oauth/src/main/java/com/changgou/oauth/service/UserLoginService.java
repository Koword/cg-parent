package com.changgou.oauth.service;

import com.changgou.oauth.util.AuthToken;

/**
 * @Author tangkai
 * @Date 21:11 2020-4-25 0025
 **/
public interface UserLoginService {

    AuthToken login(String clientId, String clientSecret, String username, String password,
        String grant_type);


}
