package com.changgou.order.service;

/**
 * @Description 购物车
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 17:31 2020/4/21
 **/
public interface CartService {

    /**
     * 商品加入购物车
     * @param id skuId 即库存id
     * @param num 购买数量
     * @param username 当前用户
     */
    void add(Long id, Integer num, String username);

}
