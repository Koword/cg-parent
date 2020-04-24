package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 17:35 2020/4/21
 **/
@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    SkuFeign skuFeign;
    SpuFeign spuFeign;
    RedisTemplate redisTemplate;

    @Override
    public void add(Long id, Integer num, String username) {
        Result<Sku> skuResult = skuFeign.findById(id);
        if (skuResult != null) {
            Sku sku = skuResult.getData();
            Spu spu = spuFeign.findById(sku.getSpuId()).getData();
            // 将商品信息封装到购物车对象中
            OrderItem orderItem = goods2OrderItem(sku, spu, num);
            // 将购物城保存在redis
            redisTemplate.boundHashOps("cart_" + username).put(id, orderItem);
        }
    }


    /**
     * @Description 将商品信息封装到购物车对象中
     * @Author tangKai
     * @Date 17:40 2020/4/21
     * @Return com.changgou.order.pojo.OrderItem
     **/
    private OrderItem goods2OrderItem(Sku sku, Spu spu, Integer num) {
        OrderItem orderItem = new OrderItem();
        // 分类id
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        orderItem.setSkuId(sku.getId());
        orderItem.setSpuId(spu.getId());
        // 商品名称
        orderItem.setName(sku.getName());
        // 商品单价
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        // 商品总金额
        orderItem.setMoney(num * sku.getPrice());
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight());
        // 是否退货
        orderItem.setIsReturn("0");
        return orderItem;
    }



}
