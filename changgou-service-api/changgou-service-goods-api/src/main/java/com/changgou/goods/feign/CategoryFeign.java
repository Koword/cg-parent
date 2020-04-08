package com.changgou.goods.feign;

import com.changgou.goods.pojo.Category;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 12:48 2020/4/8
 **/
@FeignClient(name = "goods")
@RequestMapping("/contentCategory")
public interface CategoryFeign {


    /***
     * 根据ID查询Category数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Category> findById(@PathVariable(value = "id") Integer id);
}
