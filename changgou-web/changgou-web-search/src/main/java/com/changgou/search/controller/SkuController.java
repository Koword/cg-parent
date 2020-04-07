package com.changgou.search.controller;

import com.changgou.search.feign.SkuInfoFeign;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description
 * @Author tangKai
 * @Date 14:37 2020/1/20
 **/
@Controller
@RequestMapping("/search")
@AllArgsConstructor
public class SkuController {

    SkuInfoFeign skuInfoFeign;

    /**
     * @Description 搜索页面回显数据
     * @Author tangKai
     * @Date 10:55 2020/1/14
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @GetMapping("/list")
    public String list(@RequestParam(required = false) Map<String, String> searchMap, Model model) {
        // 替换特殊字符
        handleSearchMap(searchMap);
        // 调用changgou-service-search微服务
        Map<String, Object> resultMap = skuInfoFeign.search(searchMap);
        // 回显搜索服务数据
        model.addAttribute("resultMap", resultMap);
        // 回显检索条件
        model.addAttribute("searchMap", searchMap);

        // 组装url
        String url = getUrl(searchMap);
        model.addAttribute("url", url);

        // 视图地址
        return "search";
    }


    /**
     * @Description 拼接URL地址
     * @Author tangKai
     * @Date 17:02 2020/1/22
     * @Return java.lang.String
     **/
    private String getUrl(Map<String, String> searchMap) {
        // http://ip:port/search/list
        // http://ip:port/search/list?keywords=xxx
        // http://ip:port/search/list?keywords=xxx&catetory=xxx

        String url = "/search/list";
        if (searchMap != null && searchMap.size() > 0) {
            url += "?";

            Set<Entry<String, String>> entries = searchMap.entrySet();
            for (Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                url += key + "=" + value + "&";
            }
            // 去掉最后一个&
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }


    /**
     * @Description 条件拼接替换特殊字符(例如 + > 等)
     * @Author tangKai
     * @Date 14:06 2020/4/7
     * @Return void
     **/
    public void handleSearchMap(Map<String, String> searchMap) {
        if (searchMap != null) {
            searchMap.forEach((key, value) -> {
                if (key.startsWith("spec_")) {
                    searchMap.put(key, value.replace("+", "%2B"));
                }
            });
        }
    }


}
