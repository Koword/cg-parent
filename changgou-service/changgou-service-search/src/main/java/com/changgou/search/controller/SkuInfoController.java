package com.changgou.search.controller;

import com.changgou.search.service.SkuInfoService;
import entity.Result;
import entity.StatusCode;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 前台检索
 * @Author tangKai
 * @Date 17:01 2020/1/6
 **/
@RestController
@RequestMapping("/search")
@AllArgsConstructor
public class SkuInfoController {

    SkuInfoService skuInfoService;


    /**
     * @Description 将SkuInfo数据导入ES中
     * @Author tangKai
     * @Date 17:04 2020/1/6
     * @Return entity.Result
     **/
    @GetMapping(value = "/import")
    public Result importDataToES() {
        skuInfoService.importSkuInfoToES();
        return new Result(true, StatusCode.OK, "SkuInfo数据已导入ES中!");
    }



    /**
     * @Description 前台检索
     * @Author tangKai
     * @Date 10:55 2020/1/14
     * @param searchMap
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @GetMapping
    public Map<String, Object> search(@RequestParam(required = false) Map<String, String> searchMap) {
        Map<String, Object> resultMap = skuInfoService.search(searchMap);
        return resultMap;
    }

}
