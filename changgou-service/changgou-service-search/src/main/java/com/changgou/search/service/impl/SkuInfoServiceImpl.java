package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuInfoMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuInfoService;
import entity.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Description
 * @Author tangKai
 * @Date 16:48 2020/1/6
 **/
@Service
@AllArgsConstructor
public class SkuInfoServiceImpl implements SkuInfoService {

    SkuInfoMapper skuInfoMapper;
    SkuFeign skuFeign;
    ElasticsearchTemplate elasticsearchTemplate;

    /**
     * @Description 将数据库数据导入索引库中
     * @Author tangKai
     * @Date 16:50 2020/1/6
     * @Return void
     **/
    @Override
    public void importSkuInfoToES() {
        // 通过feign调用商品微服务接口
        Result<List<Sku>> result = skuFeign.findSkusByStatus("1");
        String text = JSON.toJSONString(result.getData());
        if (text != null && text.length() > 0){
            // 将数据转换成SkuInfo
            List<SkuInfo> skuInfos = JSON.parseArray(text, SkuInfo.class);
            // 处理动态字段{"手机屏幕尺寸":"5.5寸","网络":"联通4G","颜色":"紫","测试":"测试","机身内存":"16G","储存":"32G","像素":"800万像素"}
            for (SkuInfo skuInfo : skuInfos) {
                String spec = skuInfo.getSpec();
                Map<String, Object> specMap = JSON.parseObject(spec, Map.class);
                skuInfo.setSpecMap(specMap);
            }
            // 将数据导入es中
            skuInfoMapper.saveAll(skuInfos);
        }
    }


    /**
     * @Description 前台搜索
     * @Author tangKai
     * @Date 18:00 2020/1/13
     * @Return java.util.Map<java.lang.String , java.lang.Object>
     **/
    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        // 构建检索条件(后期有多个条件检索，因此我们专门封装一个方法)
        NativeSearchQueryBuilder builder = builderBasicQuery(searchMap);
        // 根据关键字检索
        Map<String, Object> resultMap = searchForPage(builder);

        Long totalElements = (Long) resultMap.get("totalElements");
        if (totalElements < 1) {
            totalElements = 100000L;
        }

        // 分类查询
//        List<String> categoryList = searchCategoryList(builder);
//        resultMap.put("categoryList", categoryList);
        // 统计品牌列表
//        List<String> brandList = searchBrandList(builder);
//        resultMap.put("brandList", brandList);
        // 统计规格列表
//        Map<String, Set<String>> specMap = searchSpecMap(builder);
//        resultMap.put("specMap", specMap);

        // 获取所有分组结果
        Map<String, Object> map = groupList(builder, totalElements);
        resultMap.putAll(map);
        return resultMap;
    }


    /**
     * @Description 获取所有分组结果
     * @Author tangKai
     * @Date 16:07 2020/1/16
     * @Return java.util.Map<java.lang.String   ,   java.lang.Object>
     **/
    private Map<String, Object> groupList(NativeSearchQueryBuilder builder, Long totalElements) {
        // 多字段分组
        builder.addAggregation(
            AggregationBuilders.terms("skuBrand").field("brandName").size(Math.toIntExact(totalElements)));
        builder.addAggregation(
            AggregationBuilders.terms("skuCategory").field("categoryName").size(Math.toIntExact(totalElements)));
        builder.addAggregation(
            AggregationBuilders.terms("skuSpec").field("spec.keyword").size(Math.toIntExact(totalElements)));
        // 根据条件查询
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        // 处理分组结果集合
        Aggregations aggregations = aggregatedPage.getAggregations();

        // 获取分组结果集
        List<String> brandList = getList(aggregations, "skuBrand");
        List<String> categoryList = getList(aggregations, "skuCategory");
        List<String> specList = getList(aggregations, "skuSpec");

        Map<String, Set<String>> resultMap = pullMap(specList);

        // 封装数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("brandList", brandList);
        map.put("categoryList", categoryList);
        map.put("specList", resultMap);
        return map;
    }

    /**
     * @Description 处理结果集
     * @Author tangKai
     * @Date 16:31 2020/1/14
     * @Return java.util.Map<java.lang.String , java.util.Set < java.lang.String>>
     **/
    private Map<String, Set<String>> pullMap(List<String> list) {
        Map<String, Set<String>> resultMap = new HashMap<>();
        if (list != null && list.size() > 0) {
            for (String spec : list) {
                // 将json转换成map
                Map<String, String> map = JSON.parseObject(spec, Map.class);
                Set<Map.Entry<String, String>> entries = map.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    // 规格
                    String key = entry.getKey();
                    // 规格选项
                    String value = entry.getValue();
                    // 判断是否有规格
                    Set<String> set = resultMap.get(key);
                    if (set == null) {
                        set = new HashSet<>();
                    }
                    set.add(value);
                    resultMap.put(key, set);
                }
            }
        }
        return resultMap;
    }

    /**
     * @Description 处理分组结果集
     * @Author tangKai
     * @Date 16:22 2020/1/16
     * @Return java.util.List<java.lang.String>
     **/
    private List<String> getList(Aggregations aggregations, String groupName) {
        StringTerms stringTerms = aggregations.get(groupName);
        List<Bucket> buckets = stringTerms.getBuckets();
        List<String> list = new ArrayList<>();
        for (Bucket bucket : buckets) {
            list.add(bucket.getKeyAsString());
        }
        return list;
    }


    /**
     * @Description 根据条件查询商品列表
     * @Author tangKai
     * @Date 16:30 2020/1/14
     * @Return java.util.Map<java.lang.String , java.lang.Object>
     **/
    private Map<String, Object> searchForPage(NativeSearchQueryBuilder builder) {
        // 设置高亮条件
        HighlightBuilder.Field field = new HighlightBuilder.Field("name");
        field.preTags("<span style='color:red;'>");
        field.postTags("</span>");
//        field.fragmentSize(100);  // 显示数据的字符个数
        builder.withHighlightFields(field);
        SearchResultMapper searchResultMapper = new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                // 封装高亮结果集
                List<T> content = new ArrayList<>();
                // 获取高亮结果集
                SearchHits hits = response.getHits();
                if (hits != null) {
                    for (SearchHit hit : hits) {
                        String result = hit.getSourceAsString();        // 获取普通结果集合
                        // 将结果转换成pojo
                        SkuInfo skuInfo = JSON.parseObject(result, SkuInfo.class);
                        HighlightField highlightField = hit.getHighlightFields().get("name");
                        if (highlightField != null) {
                            Text[] texts = highlightField.getFragments();       // 替换成普通结果
                            skuInfo.setName(texts[0].toString());               // 替换普通结果
                        }
                        content.add((T) skuInfo);
                    }
                }
                return new AggregatedPageImpl<>(content, pageable, hits.getTotalHits());
            }
        };

        NativeSearchQuery build = builder.build();
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(build, SkuInfo.class, searchResultMapper);

        List<SkuInfo> rows = page.getContent();             // 结果集
        long totalElements = page.getTotalElements();       // 总条数
        int totalPages = page.getTotalPages();              // 总页数

        Map<String, Object> map = new HashMap<String,Object>();
        map.put("rows", rows);
        map.put("totalElements", totalElements);
        map.put("totalPages", totalPages);

        map.put("pageNum", build.getPageable().getPageNumber() + 1);         // 当前页码
        map.put("pageSize", build.getPageable().getPageSize());              // 每页显示的条数
        return map;
    }


    /**
     * @Description 封装检索条件
     * @Author tangKai
     * @Date 17:35 2020/1/6
     * @Return org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
     **/
    private NativeSearchQueryBuilder builderBasicQuery(Map<String, String> searchMap) {
        // 封装检索条件
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        // 封装过滤条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (searchMap != null) {
            // 1.获取关键字
            String keywords = searchMap.get("keywords");
            if (!StringUtils.isEmpty(keywords)) {
                builder.withQuery(QueryBuilders.matchQuery("name", keywords));
            }

            // 2.根据商品分类过滤
            String category = searchMap.get("category");
            if (!StringUtils.isEmpty(category)) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("categoryName", category));
            }
            // 3.根据品牌过滤
            String brand = searchMap.get("brand");
            if (!StringUtils.isEmpty(brand)) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("brandName", brand));
            }
            // 4.根据规格过滤
            Set<String> keys = searchMap.keySet();
            for (String key : keys) {
                // 根据规格查询
                if (key.startsWith("spec_")) {
                    searchMap.get(key).replace("\\", "");
                    boolQueryBuilder
                        .must(QueryBuilders.matchQuery("specMap." + key.substring(5) + ".keyword", searchMap.get(key)));
                }
            }
            // 5.价格区间段过滤
            String price = searchMap.get("price");
            if (!StringUtils.isEmpty(price)) {
                String[] priceArray = price.split("-");
                // [min - max]
                // >=,大于等于
                boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(priceArray[0]));
                if (priceArray.length == 2) {
                    // <=,小于等于
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").lte(priceArray[1]));
                }
            }

            // 6.结果排序
            String sortRule = searchMap.get("sortRule");        // 排序规则
            String sortField = searchMap.get("sortField");      // 排序字段
            if (!StringUtils.isEmpty(sortField)) {
                builder.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule)));
            }
        }
        // 添加过滤条件
        builder.withFilter(boolQueryBuilder);

        // 添加分页条件
        String pageNum = searchMap.get("pageNum");
        if (StringUtils.isEmpty(pageNum)) {
            pageNum = "1";              // 默认第一页
        }
        int page = Integer.parseInt(pageNum);   // 当前页码
        int size = 5;                           // 默认第一页
        Pageable pageable = PageRequest.of(page - 1, size);

        builder.withPageable(pageable);
        return builder;
    }

    //    /**
//     * @Description 统计分类列表
//     * @Author tangKai
//     * @Date 11:29 2020/1/14
//     * @Return java.util.List<java.lang.String>
//     **/
//    private List<String> searchCategoryList(NativeSearchQueryBuilder builder) {
//        // 聚合查询
//        builder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
//        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
//        // 分组结果集
//        Aggregations aggregations = aggregatedPage.getAggregations();
//        // 处理分组结果集
//        StringTerms stringTerms = aggregations.get("skuCategory");
//        List<Bucket> buckets = stringTerms.getBuckets();
//        ArrayList<String> list = new ArrayList<>();
//        for (Bucket bucket : buckets) {
//            list.add(bucket.getKeyAsString());
//        }
//        return list;
//    }
//
//
//    /**
//     * @Description 统计品牌列表
//     * @Author tangKai
//     * @Date 15:13 2020/1/14
//     * @Return java.util.List<java.lang.String>
//     **/
//    private List<String> searchBrandList(NativeSearchQueryBuilder builder) {
//        // 聚合查询
//        builder.addAggregation(AggregationBuilders.terms("skuBrand").field("brandName"));
//        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
//        // 分组结果集
//        Aggregations aggregations = aggregatedPage.getAggregations();
//        // 处理分组结果集合
//        StringTerms stringTerms = aggregations.get("skuBrand");
//        List<Bucket> buckets = stringTerms.getBuckets();
//        ArrayList<String> list = new ArrayList<>();
//        for (Bucket bucket : buckets) {
//            list.add(bucket.getKeyAsString());
//        }
//        return list;
//    }
//
//
//    /**
//     * @Description 查询规格列表
//     * @Author tangKai
//     * @Date 15:41 2020/1/14
//     * @Return java.util.Map<java.lang.String,java.util.Set<java.lang.String>>
//     **/
//    private Map<String, Set<String>> searchSpecMap(NativeSearchQueryBuilder builder) {
//        // 添加分组条件
//        builder.addAggregation(AggregationBuilders.terms("skuSpec").field("spec.keyword").size(10000));
//        // 根据条件查询
//        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
//        // 获取分组结果集
//        Aggregations aggregations = aggregatedPage.getAggregations();
//        // 处理分组结果集
//        StringTerms stringTerms = aggregations.get("skuSpec");
//        List<Bucket> buckets = stringTerms.getBuckets();
//        List<String> list = new ArrayList<>();
//        for (Bucket bucket : buckets) {
//            list.add(bucket.getKeyAsString());
//        }
//        // 将集合数据转换成map中
//        // {"颜色":"红色","网络":"移动3G"}
//        // 对结果进一步处理
//        Map<String, Set<String>> map = pullMap(list);
//        return map;
//    }

}
