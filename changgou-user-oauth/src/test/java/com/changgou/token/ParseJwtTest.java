package com.changgou.token;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

/*****
 * @Author: shenkunlin
 * @Date: 2019/7/7 13:48
 * @Description: com.changgou.token
 *  使用公钥解密令牌数据
 ****/
public class ParseJwtTest {

    /***
     * 校验令牌
     */
    @Test
    public void testParseToken() {
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJpdGhlaW1hIiwiaWQiOiIxIn0.arZkh65nquEpcKHqgk2kUcR6bM6LYIsgJG5fSXqxUrXPGRGzJ6aOUtt_6XCLgijvKiSY3gpxHDaiYTmHFULn2gZduIKeCxEXST_wMaWOaC8SWftUd19n3IwdBTg15yRVB50rZFIuCvLxEhERt8OjwJ1kt-EyohKxEk32QMFFCsU-5rI0eLduGBaQ7CthMwglVbAOJ4rpt5MehnMF-QVxc09HzeQh860YP9ohD2L3rBcBgZqTXpiiASeKy9QVjBpVqcLqWUam_sQxIUhC8lxOr-tTndQoQSq95V4gaz0rF3L7QwKlS_-NDr4vvrGAlaGvbV36dpnhRMHsii7jDentKw";

        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----\n"
            + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAihfBxAYtWjkunp8TW/zR\n"
            + "5CoNAeHmprIExamsO3FLMzvy/xzm7KwNv/XQI/ECsU04Bx9HR83xBDjiZVjUdb3k\n"
            + "kmaWed5qWLk6rW8aVmK2vtGP0uzoGQnNcpbU8TqchwqHOERY4pKI/HgsjL8FcnTh\n"
            + "0oyp95pAn6XWyziXtmciD799dVluwD1BeJfib+TWdLL4U0LZL1TrEn9o1WSaEpba\n"
            + "jNC6kYvAROaNrqRaw/r+SGWpt0ijytiVM8bRJmwuWv84RUJiL+EWWcZeJ7Fu+LxF\n"
            + "WuQ+j6JrP5fGjCMaLDp/Mix0gNPEkrwLgB0zCKOsJLdV+mkohBonkXRbS1is6YgG\n"
            + "2wIDAQAB\n"
            + "-----END PUBLIC KEY-----";

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
