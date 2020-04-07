package com.changgou.goods.feign;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description
 * @Author tangkai
 * @Date 16:37 2020/1/6
 **/
@FeignClient(name = "goods")
@RequestMapping("/sku")
public interface SkuFeign {


    /**
     * @Description 根据状态查询sku
     * @Author tangKai
     * @Date 16:38 2020/1/6
     * @param status
     * @Return entity.Result
     **/
    @GetMapping(value = "/findSkusByStatus/{status}")
    Result findSkusByStatus(@PathVariable(value = "status") String status);



}
