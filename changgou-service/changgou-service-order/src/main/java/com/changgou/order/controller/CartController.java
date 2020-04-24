package com.changgou.order.controller;

import com.changgou.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 17:52 2020/4/21
 **/
@RestController
@RequestMapping("/cart")
@CrossOrigin
@AllArgsConstructor
public class CartController {

    CartService cartService;
    private static final String NAME = "chen";

    @GetMapping("/add")
    public Result add(Long id, Integer num) {
        cartService.add(id, num, NAME);
        return new Result(true, StatusCode.OK, "添加到购物车成功");
    }


}
