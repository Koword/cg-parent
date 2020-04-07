package com.changgou.search.dao;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Description
 * @Author tangkai
 * @Date 16:45 2020/1/6
 **/
public interface SkuInfoMapper extends ElasticsearchRepository<SkuInfo, Integer> {

}
