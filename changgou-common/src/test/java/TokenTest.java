import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @Description
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 10:06 2020/4/20
 **/
public class TokenTest {


    @Test
    public void createToken() {
        // 创建JWT
        JwtBuilder builder = Jwts.builder();

        // 构建头信息
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("keyId", "JWT");
        builder.setHeader(map);
        // 构建载荷信息
        builder.setId("001");
        builder.setIssuer("kendall");
        builder.setIssuedAt(new Date());

        // 立马到期 → build.setExpiration(new Date())
        // 30 秒后过期 ↓
        builder.setExpiration(new Date(System.currentTimeMillis() + 30000));

        // 声明自定义载荷信息
        Map<String, Object> load = new HashMap<>();
        load.put("address","gz");
        load.put("school","sz");
        builder.setClaims(load);
        // 添加签名
        builder.signWith(SignatureAlgorithm.HS256, "gaoleng");
        // 生成token
        String token = builder.compact();
        System.out.println("token:" + token);
    }

    @Test
    public void testParseToken() {
        // 被解析的令牌
        String token = "eyJrZXlJZCI6IkpXVCIsImFsZyI6IkhTMjU2In0.eyJhZGRyZXNzIjoiZ3oiLCJzY2hvb2wiOiLmt7HogYwifQ.jkS2LX8aP1AZ-5OCsI_BFGrv5CUuLxTM89J7t_cdOhs";
        // 创建解析对象
        JwtParser parser = Jwts.parser();
        parser.setSigningKey("gaoleng");
        Claims claims = parser.parseClaimsJws(token).getBody();
        System.out.println(claims);

    }

}
