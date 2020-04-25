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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @Author tangkai
 * @Date 21:13 2020-4-25 0025
 **/
@Service
@AllArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    // 发送请求
    RestTemplate restTemplate;
    // 动态获取服务的实例
    LoadBalancerClient loadBalancerClient;


    @Override
    public AuthToken login(String clientId, String clientSecret, String username, String password,
        String grant_type) {
        // 生成令牌的url
        // String url = "http://localhost:9001/oauth/token";
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-auth");
        String url = serviceInstance.getUri().toString() + "/oauth/token";

        // httpEntity:需要封装的请求体
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.set("grant_type", grant_type);
        body.set("username", username);
        body.set("password", password);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        //                              Base64编码↓
        headers.add("Authorization", httpbasic(clientId, clientSecret));
        HttpEntity httpEntity = new HttpEntity(body, headers);
        // 通过RestTemplate发送请求获取到的响应数据
        ResponseEntity<Map> responseEntity = null;
        try {
            responseEntity = restTemplate
                .exchange(url, HttpMethod.POST, httpEntity, Map.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        Map<String, String> map = responseEntity.getBody();
        if(map == null || map.get("access_token") == null || map.get("refresh_token") == null || map.get("jti") == null) {
            //jti是jwt令牌的唯一标识作为用户身份令牌
            throw new RuntimeException("创建令牌失败！");
        }
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
