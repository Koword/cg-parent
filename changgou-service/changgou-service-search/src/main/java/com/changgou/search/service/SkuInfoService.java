package com.changgou.search.service;

import java.util.Map;

/**
 * @Description
 * @Author tangkai
 * @Date 16:46 2020/1/6
 **/
public interface SkuInfoService {


    /**
     * @Description 将数据skuInfo数据库中的数据导入ES
     * @Author tangKai
     * @Date 16:47 2020/1/6
     * @param *null
     * @Return
     **/
    void importSkuInfoToES();




    /**
     * @Description 前台搜索
     * @Author tangKai
     * @Date 18:00 2020/1/13
     * @param searchMap
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String,Object> search(Map<String,String> searchMap);


}
