package com.changgou.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description 自定义全局过滤器
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 12:12 2020/4/20
 **/
@Component
@Slf4j
public class AuthorizeFilter implements GlobalFilter, Ordered {

    // 定义常量
    private static final String AUTHORIZE_TOKEN = "Authorization";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取request、response
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 1.判断用户是否是登陆操作，如果是，则直接放行
        String url = request.getURI().getPath();
        // 登录放行
        if (url.startsWith("/api/user/login")) {
            return chain.filter(exchange);
        }
        // 2.其他请求，需要判断用户是否登录(判断用户是否携带token信息)
        // 2.1从请求参数中获取token
        String token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        // 2.2从请求头中获取token
        if (StringUtils.isEmpty(token)) {
            token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        }
        // 2.3 从cookie中获取token
        if (StringUtils.isEmpty(token)) {
            HttpCookie cookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if (cookie != null) {
                token = cookie.getValue();
            }
        }
        // 3.如果没有token，就不放行
        if (StringUtils.isEmpty(token)) {
            // 设置响应状态码
            log.info("还没有登录-_-");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 4.token存在，需要解析token
        try {
            /**--Jwt使用HS256加密方式--**/
            // 解析成功
            // Claims claims = JwtUtil.parseJWT(token);
            // 手动添加头信息(调用其他微服务，需要网关，因此我们将token添加到头信息中)
            // request.mutate().header("Authorization_Token", token);

            /**--oauth2.0 使用非对称加密RSA--**/
            // 将令牌放入头信息中，供需要的微服务使用public.key去解析
            request.mutate().header(AUTHORIZE_TOKEN,"bearer " + token);
        } catch (Exception e) {
            e.printStackTrace();
            // --解析失败
            // 设置响应状态码
            log.info("存在恶意访问");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        return chain.filter(exchange);
    }


    /**
     * @Description 过滤器执行顺序
     * @Author tangKai
     * @Date 13:38 2020/4/20
     * @param
     * @Return int
     **/
    @Override
    public int getOrder() {
        return 0;
    }
}
