package com.changgou.user.feign;

import com.changgou.user.pojo.User;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 10:59 2020/4/24
 **/
@FeignClient(name = "user")
@RequestMapping("user")
public interface UserFeign {

    @GetMapping("/{id}")
    Result<User> findById(@PathVariable(value = "id") String id);

}
