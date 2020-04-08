package com.changgou.item.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 17:26 2020/4/8
 **/
@Configuration
@ControllerAdvice
public class EnableMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 对资源放行
        registry.addResourceHandler("/items/**").addResourceLocations("classpath:/templates/items");
    }
}
