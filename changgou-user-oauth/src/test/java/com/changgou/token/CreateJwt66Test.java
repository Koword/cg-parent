package com.changgou.token;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CreateJwt66Test
 * @Description
 * @Author 传智播客
 * @Date 18:40 2019/8/21
 * @Version 2.1
 **/
public class CreateJwt66Test {

    /***
     * 创建令牌测试
     */
    @Test
    public void testCreateToken(){
        //证书文件路径
        String key_location="changgou66.jks";
        //秘钥库密码
        String key_password="changgou66";
        //秘钥密码
        String keypwd = "changgou66";
        //秘钥别名
        String alias = "changgou66";

        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);

        //创建秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource,key_password.toCharArray());

        //读取秘钥对(公钥、私钥)
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias,keypwd.toCharArray());

        //获取私钥
        RSAPrivateKey rsaPrivate = (RSAPrivateKey) keyPair.getPrivate();

        //定义Payload
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id", "1");
        tokenMap.put("name", "itheima");
        tokenMap.put("roles", "ROLE_VIP,ROLE_USER");

        //生成Jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(rsaPrivate));

        //取出令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }


    /***
     * 校验令牌
     */
    @Test
    public void testParseToken(){
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJpdGhlaW1hIiwiaWQiOiIxIn0.VeSlUbBkcxDNWdHs0b2VQUcGUJlb2igncPwUWiT7PB-sIRrtAsUYzTayzBY3A3u2bZgKFFWAlgEw2e_6swXEfh3CzQQJ0GpzllQ3P-_CzbSJy1wAKNHq0Dd9bfHFMexl2Puh5goPI_hEHOviEG9CXr8H5hh44FJmNwOjd8vSqogrqqHYiqga2KiwB79txP23IuboOQETl60HP2Ey3WNVHMccywhu8HTVmFllDJqIbrqn2y-AQCGUSce-Q3dobjNxS7LApOAc6xNJCb9CKmKuUlBBjWE6bqG16UmmcrXVvJ-z7XHStANjTGvTLTHGWDsHwOEfFf8ygZTguzZ_vT9BqQ";

        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5CFXPBF6r/WXdxZh+YkSoE2GZ9DCA04+3Sr512br+0jbePudIFsuryomc6IZqkLhbApFdkuKA8zL+cBQ2euYngK8nQnyyqyCsbCJtFFvw1TQpSbwxulrHIaRwZMY7Lw/cBpnjawPwI+FJfzrZ/mES3z09w6nkLpiswUa5NXtCMxk3TSlsyrdyaifxavQEjE9PCKfOQVIY7xcXskZ95I77bbnAnwUg4NihzgXf0Ax09MIVKcGYylMqyXuztG4/GDA/CQEj3B3bCOlZ9tFI+b/Q4J2ZbhuCPDUlH95yGpfXet1f6SPFbbnDwAJbJQJno51jD6e2Sk6zXIh98Jrc99/KQIDAQAB-----END PUBLIC KEY-----";

        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));

        //获取Jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }

}
