package com.changgou.oauth.service.impl;

import com.changgou.oauth.service.UserLoginService;
import com.changgou.oauth.util.AuthToken;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @Description
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 15:30 2020/4/23
 **/
@Service
@AllArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    RestTemplate restTemplate;
    LoadBalancerClient loadBalancerClient;


    /**
     * @param clientId 客户端id
     * @param clientSecret 客户端秘钥
     * @param username 用户名
     * @param password 密码
     * @param grant_type 授权类型
     * @Description 登录
     * @Author tangKai
     * @Date 15:48 2020/4/23
     * @Return com.changgou.oauth.util.AuthToken
     **/
    @Override
    public AuthToken login(String clientId, String clientSecret, String username, String password, String grant_type) {
        // 生成令牌的url
        // String url = "http://localhost:9001/oauth/token";
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-auth");
        String url = serviceInstance.getUri().toString() + "/oauth/token";

        // httpEntity:需要封装的请求体
        MultiValueMap body = new LinkedMultiValueMap();
        body.set("grant_type", grant_type);
        body.set("username", username);
        body.set("password", password);

        MultiValueMap headers = new LinkedMultiValueMap();
        // base64编码
        String encodeMsg = httpbasic(clientId, clientSecret);
        headers.add("Authorization", encodeMsg);
        HttpEntity httpEntity = new HttpEntity<>(body, headers);

        // 通过RestTemplate发送请求获取到的响应数据
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
        Map<String, String> map = responseEntity.getBody();

        // 将数据封装到AuthToken
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(map.get("access_token"));
        authToken.setRefreshToken(map.get("refresh_token"));
        authToken.setJti(map.get("jti"));
        return authToken;
    }

    /***
     * base64编码
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String httpbasic(String clientId, String clientSecret) {
        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = clientId + ":" + clientSecret;
        //进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        try {
            return "Basic " + new String(encode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}