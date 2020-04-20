package entity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description 辅助生成Jwt令牌信息
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 11:26 2020/4/20
 **/
public class JwtUtil {

    /***
     * 有效期为：60 * 60 * 1000 ,即一个小时
     */
    public static final Long JWT_TTL = 3600000L;

    /**
     * Jwt令牌信息
     */
    public static final String JWT_KEY = "gaoleng";



    /**
     * @Description 创建Jwt
     * @Author tangKai
     * @Date 14:09 2020/4/20
     * @param id 唯一的ID
     * @param subject 主题
     * @param ttlMillis 令牌有效期
     * @Return java.lang.String
     **/
    public static String createJWT(String id, String subject, Long ttlMillis) {
        // 指定算法
        SignatureAlgorithm hs256 = SignatureAlgorithm.HS256;

        // 当前系统时间
        long nowMillis = System.currentTimeMillis();

        // 令牌签发的时间
        Date now = new Date();

        // 如果令牌有效期为null，则默认设置为有效期为1个小时
        if (ttlMillis == null){
            ttlMillis = JWT_TTL;
        }

        // 令牌过期时间设置
        long expMillis =  nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);

        SecretKey secretKey = generalKey();

        // 封装Jwt令牌信息(载荷)
        JwtBuilder builder = Jwts.builder()
            .setId(id)                      // 唯一id
            .setSubject(subject)            // 主题 可以是JSON
            .setIssuer("admin")             // 签发者
            .setIssuedAt(now)               // 签发时间
            .signWith(hs256,secretKey)      // 签名算法以及密钥
            .setExpiration(expDate);        // 设置过期时间
        return builder.compact();
    }
    /**
     * @Description 生成加密 secretKey
     * @Author tangKai
     * @Date 11:39 2020/4/20
     * @param
     * @Return java.lang.String
     **/
    public static SecretKey generalKey() {
        byte[] encodeKey = Base64.getEncoder().encode(JWT_KEY.getBytes());
        return new SecretKeySpec(encodeKey, 0, encodeKey.length, "AES");
    }


    /**
     * @Description 解析令牌数据
     * @Author tangKai
     * @Date 11:54 2020/4/20
     * @param
     * @Return java.lang.String
     **/
    public static Claims parseJWT(String jwt){
        SecretKey secretKey = generalKey();
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(jwt)
            .getBody();
    }
}
