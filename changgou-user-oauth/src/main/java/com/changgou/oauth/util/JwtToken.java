/*
package com.changgou.oauth.util;

import com.alibaba.fastjson.JSON;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

*/
/**
 * @Description 发放管理员的令牌
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 14:22 2020/4/24
 **//*

public class JwtToken {

    public static String adminJwt() {
        // 证书文件路径
        String key_location = "changgou.jks";
        // 秘钥库密码
        String key_password = "changgou";
        // 秘钥密码
        String keypwd = "changgou";
        // 秘钥别名
        String alias = "changgou";
        // 访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);
        // 创建秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, key_password.toCharArray());
        // 读取秘钥对(公钥、秘钥)
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypwd.toCharArray());
        // 读取私钥
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 定义PayLoad    -> 自定义载荷
        Map<String, Object> tokenMap = new HashMap<>();
        // 给令牌授权
        tokenMap.put("authorities", new String[]{"admin"});
        // 生成Jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(rsaPrivateKey));
        // 取出令牌
        return jwt.getEncoded();

    }

}
*/
