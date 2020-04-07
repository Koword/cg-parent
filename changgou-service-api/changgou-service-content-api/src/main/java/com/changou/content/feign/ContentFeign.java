package com.changou.content.feign;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/****
 * @Author:shenkunlin
 * @Description:
 * @Date 2019/6/18 13:58
 *****/
@FeignClient(name="content")
@RequestMapping("/content")
public interface ContentFeign {

    /**
     * @Description 根据分类id查询广告列表
     * @Author tangKai
     * @Date 16:24 2020/1/4
     * @Param [id]
     * @Return entity.Result
     **/
    @GetMapping("/list/category/{id}")
    public Result findByCategory(@PathVariable(value = "id") Long id);




}