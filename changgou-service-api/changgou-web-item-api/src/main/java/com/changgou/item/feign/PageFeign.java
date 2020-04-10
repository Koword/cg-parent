package com.changgou.item.feign;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 12:56 2020/4/10
 **/
@FeignClient(name="item")
@RequestMapping("/page")
public interface PageFeign {


    /**
     * @Description 生成静态页
     * @Author tangKai
     * @Date 12:58 2020/4/10
     * @param id
     * @Return entity.Result
     **/
    @GetMapping("/createHtml/{id}")
    Result createHtml(@PathVariable(name = "id") Long id);
}
