package com.changgou.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.CategoryFeign;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.item.service.PageService;
import entity.Result;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @Description
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 14:21 2020/4/8
 **/
@Service
@AllArgsConstructor
public class PageServiceImpl implements PageService {

    TemplateEngine templateEngine;
    SkuFeign skuFeign;
    SpuFeign spuFeign;
    CategoryFeign categoryFeign;
    @Value("${pagepath}")
    private String pagepath;


    /**
     * @Description 生成静态页
     * @Author tangKai
     * @Date 14:22 2020/4/8
     * @Return void
     **/
    @Override
    public void creatrHtml(Long spuId) {
        try {
            Map<String, Object> dataModel = getDataModel(spuId);
            // 创建模板数据
            Context context = new Context();
            context.setVariables(dataModel);
            // 指定静态页面生成页面的位置
            File dir = new File(pagepath);
            if (!dir.exists()) {
                // 目录为空，就创建
                dir.mkdirs();
            }
            File dest = new File(dir, spuId + ".html");
            // 生成页面
            PrintWriter printWriter = new PrintWriter(dest, "UTF-8");
            templateEngine.process("item", context, printWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * @Description
     * @Author tangKai
     * @Date 14:28 2020/4/8
     * @Return java.util.Map<java.lang.String,*java.lang.Object>
     **/
    private Map<String, Object> getDataModel(Long spuId) {
        // 商品信息
        HashMap<String, Object> dataModel = new HashMap<>();
        Result<Spu> spuResult = spuFeign.findById(spuId);
        Spu spu = spuResult.getData();
        dataModel.put("spu", spu);
        // 库存信息
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        Result<List<Sku>> skuResult = skuFeign.findList(sku);
        List<Sku> skuList = skuResult.getData();
        dataModel.put("skuList", skuList);
        // 商品分类信息
        Category category1 = categoryFeign.findById(spu.getCategory1Id()).getData();
        Category category2 = categoryFeign.findById(spu.getCategory2Id()).getData();
        Category category3 = categoryFeign.findById(spu.getCategory3Id()).getData();
        dataModel.put("category1", category1);
        dataModel.put("category2", category2);
        dataModel.put("category3", category3);
        // 商品小图
        String[] images = spu.getImages().split(",");
        dataModel.put("images", images);
        // 商品规格
        Map<String, String> specificationList = JSON.parseObject(spu.getSpecItems(), Map.class);
        dataModel.put("specificationList", specificationList);
        return dataModel;
    }


}
