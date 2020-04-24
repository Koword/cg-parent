package com.changgou.order.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @Description 1.获取公钥 2.将公钥作为秘钥(RSA【非对称加密】) 3.令牌的校验
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 14:53 2020/4/22
 **/
@Configuration
@EnableResourceServer
// 激活方法上的PreAuthorize
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    // 公钥
    private static final String PUBLIC_KEY = "public.key";


    /**
     * @Description 定义JwtTokenStore
     * @Author tangKai
     * @Date 14:59 2020/4/22
     * @Return org.springframework.security.oauth2.provider.token.TokenStore
     **/
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }


    /**
     * @Description 定义JJwtAccessTokenConverter
     * @Author tangKai
     * @Date 15:01 2020/4/22
     * @Return org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
     **/
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(getPubKey());      // 秘钥的一部分
        return converter;
    }


    /**
     * @Description 获取非对称加密公钥 key
     * @Author tangKai
     * @Date 15:03 2020/4/22
     * @Return java.lang.String 公钥 key
     **/
    private String getPubKey() {
        ClassPathResource resource = new ClassPathResource(PUBLIC_KEY);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return null;
        }
    }



    /**
     * @Description Http安全配置，对每个到达系统的http请求链接进行校验
     * @Author tangKai
     * @Date 15:56 2020/4/22
     * @param http
     * @Return void
     **/
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 所有请求必须认证通过
        http.authorizeRequests()
            // 下边的路径放行
            .antMatchers("/cart/*")
            .permitAll()
            .anyRequest()
            // 其他地址需要认证授权
            .authenticated();
    }
}
