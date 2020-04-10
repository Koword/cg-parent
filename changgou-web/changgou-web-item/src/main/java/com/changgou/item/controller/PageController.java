package com.changgou.item.controller;

import com.changgou.item.service.PageService;
import entity.Result;
import entity.StatusCode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 14:18 2020/4/8
 **/
@RestController
@RequestMapping("/page")
@AllArgsConstructor
public class PageController {

    PageService pageService;


    /**
     * @Description 生成静态页
     * @Author tangKai
     * @Date 12:58 2020/4/10
     * @param id
     * @Return entity.Result
     **/
    @GetMapping("/createHtml/{id}")
    public Result createHtml(@PathVariable(name = "id") Long id){
        pageService.creatrHtml(id);
        return new Result(true, StatusCode.OK,"ok");
    }


}
