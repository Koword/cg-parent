package com.changgou.goods.dao;
import com.changgou.goods.pojo.Brand;
import java.util.List;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:shenkunlin
 * @Description:Brand的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface BrandMapper extends Mapper<Brand> {

    /**
     * 根据分类id查询品牌集合
     * @param categoryId
     * @return
     */
    @Select("SELECT tb.* FROM tb_category_brand tcb,tb_brand tb WHERE tcb.category_id = #{categoryId} AND tb.id = tcb.brand_id")
    List<Brand> findByCategory(Integer categoryId);


}
