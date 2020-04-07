package com.changgou.search.feign;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description
 * @Author tangkai
 * @Date 14:31 2020/1/20
 **/
@FeignClient(name = "search")
@RequestMapping("/search")
public interface SkuInfoFeign {

    /**
     * @Description 前台检索
     * @Author tangKai
     * @Date 10:55 2020/1/14
     * @param searchMap
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @GetMapping
    Map<String, Object> search(@RequestParam(required = false) Map<String, String> searchMap);

}
