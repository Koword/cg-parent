package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.SkuMapper;
import com.changgou.goods.dao.SpuMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.goods.service.SpuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.IdWorker;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

/****
 * @Author:shenkunlin
 * @Description:Spu业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
@AllArgsConstructor
public class SpuServiceImpl implements SpuService {


    SpuMapper spuMapper;


    IdWorker idWorker;

    CategoryMapper categoryMapper;

    BrandMapper brandMapper;

    SkuMapper skuMapper;


    /**
     * Spu条件+分页查询
     *
     * @param spu 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Spu> findPage(Spu spu, int page, int size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索条件构建
        Example example = createExample(spu);
        //执行搜索
        return new PageInfo<Spu>(spuMapper.selectByExample(example));
    }

    /**
     * Spu分页查询
     */
    @Override
    public PageInfo<Spu> findPage(int page, int size) {
        //静态分页
        PageHelper.startPage(page, size);
        //分页查询
        return new PageInfo<Spu>(spuMapper.selectAll());
    }

    /**
     * Spu条件查询
     */
    @Override
    public List<Spu> findList(Spu spu) {
        //构建查询条件
        Example example = createExample(spu);
        //根据构建的条件查询数据
        return spuMapper.selectByExample(example);
    }


    /**
     * Spu构建查询对象
     */
    public Example createExample(Spu spu) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (spu != null) {
            // 主键
            if (!StringUtils.isEmpty(spu.getId())) {
                criteria.andEqualTo("id", spu.getId());
            }
            // 货号
            if (!StringUtils.isEmpty(spu.getSn())) {
                criteria.andEqualTo("sn", spu.getSn());
            }
            // SPU名
            if (!StringUtils.isEmpty(spu.getName())) {
                criteria.andLike("name", "%" + spu.getName() + "%");
            }
            // 副标题
            if (!StringUtils.isEmpty(spu.getCaption())) {
                criteria.andEqualTo("caption", spu.getCaption());
            }
            // 品牌ID
            if (!StringUtils.isEmpty(spu.getBrandId())) {
                criteria.andEqualTo("brandId", spu.getBrandId());
            }
            // 一级分类
            if (!StringUtils.isEmpty(spu.getCategory1Id())) {
                criteria.andEqualTo("category1Id", spu.getCategory1Id());
            }
            // 二级分类
            if (!StringUtils.isEmpty(spu.getCategory2Id())) {
                criteria.andEqualTo("category2Id", spu.getCategory2Id());
            }
            // 三级分类
            if (!StringUtils.isEmpty(spu.getCategory3Id())) {
                criteria.andEqualTo("category3Id", spu.getCategory3Id());
            }
            // 模板ID
            if (!StringUtils.isEmpty(spu.getTemplateId())) {
                criteria.andEqualTo("templateId", spu.getTemplateId());
            }
            // 运费模板id
            if (!StringUtils.isEmpty(spu.getFreightId())) {
                criteria.andEqualTo("freightId", spu.getFreightId());
            }
            // 图片
            if (!StringUtils.isEmpty(spu.getImage())) {
                criteria.andEqualTo("image", spu.getImage());
            }
            // 图片列表
            if (!StringUtils.isEmpty(spu.getImages())) {
                criteria.andEqualTo("images", spu.getImages());
            }
            // 售后服务
            if (!StringUtils.isEmpty(spu.getSaleService())) {
                criteria.andEqualTo("saleService", spu.getSaleService());
            }
            // 介绍
            if (!StringUtils.isEmpty(spu.getIntroduction())) {
                criteria.andEqualTo("introduction", spu.getIntroduction());
            }
            // 规格列表
            if (!StringUtils.isEmpty(spu.getSpecItems())) {
                criteria.andEqualTo("specItems", spu.getSpecItems());
            }
            // 参数列表
            if (!StringUtils.isEmpty(spu.getParaItems())) {
                criteria.andEqualTo("paraItems", spu.getParaItems());
            }
            // 销量
            if (!StringUtils.isEmpty(spu.getSaleNum())) {
                criteria.andEqualTo("saleNum", spu.getSaleNum());
            }
            // 评论数
            if (!StringUtils.isEmpty(spu.getCommentNum())) {
                criteria.andEqualTo("commentNum", spu.getCommentNum());
            }
            // 是否上架,0已下架，1已上架
            if (!StringUtils.isEmpty(spu.getIsMarketable())) {
                criteria.andEqualTo("isMarketable", spu.getIsMarketable());
            }
            // 是否启用规格
            if (!StringUtils.isEmpty(spu.getIsEnableSpec())) {
                criteria.andEqualTo("isEnableSpec", spu.getIsEnableSpec());
            }
            // 是否删除,0:未删除，1：已删除
            if (!StringUtils.isEmpty(spu.getIsDelete())) {
                criteria.andEqualTo("isDelete", spu.getIsDelete());
            }
            // 审核状态，0：未审核，1：已审核，2：审核不通过
            if (!StringUtils.isEmpty(spu.getStatus())) {
                criteria.andEqualTo("status", spu.getStatus());
            }
        }
        return example;
    }

    /**
     * 删除
     */
    @Override
    public void delete(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);

        // 物理删除的前提，先逻辑删除后才能物理删除
        if (!"1".equals(spu.getIsDelete())){
            throw new RuntimeException("此商品不能删除!");
        }
        spuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Spu
     */
    @Override
    public void update(Spu spu) {
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 增加Spu
     */
    @Override
    public void add(Spu spu) {
        spuMapper.insert(spu);
    }

    /**
     * 根据ID查询Spu
     */
    @Override
    public Spu findById(Long id) {
        return spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Spu全部数据
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }


    /**
     * 保存商品
     */
    @Override
    public void saveOrUpdateGoods(Goods goods) {
        // 增加Spu
        Spu spu = goods.getSpu();
        if (spu.getId() == null) {
            // 增加
            spu.setId(idWorker.nextId());   // spu的主键
            spu.setIsMarketable("0");       // 未上架
            spu.setIsDelete("0");           // 未删除
            spu.setStatus("0");             // 未审核
            spuMapper.insertSelective(spu);
        } else {
            // 更新
            spuMapper.updateByPrimaryKeySelective(spu);
            // 删除该Spu的Sku（先删除之后再插入）
            Sku sku = new Sku();
            sku.setSpuId(spu.getId());
            skuMapper.delete(sku);
        }

        // 增加sku
        Date date = new Date();
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());

        // 获取Sku集合
        List<Sku> skuList = goods.getSkuList();

        // 循环将数据加入到数据库
        if (skuList != null && skuList.size() > 0) {
            for (Sku sku : skuList) {
                sku.setId(idWorker.nextId());       // sku主键
                sku.setSpuId(spu.getId());          // spu的id
                // sku的名称 = spu名称 + spu副标题 + 规格
                String name = spu.getName() + spu.getCaption();
                String spec = sku.getSpec();
                if (!StringUtils.isEmpty(spec)) {
                    Map<String, String> map = JSON.parseObject(spec, Map.class);
                    Set<Entry<String, String>> entries = map.entrySet();
                    for (Entry<String, String> entry : entries) {
                        name += " " + entry.getValue();
                    }
                }
                sku.setName(name);

                // SpuId
                sku.setSpuId(spu.getId());
                // 创建日期
                sku.setCreateTime(date);
                // 修改时间
                sku.setUpdateTime(date);
                // 商品分类ID
                sku.setCategoryId(spu.getCategory3Id());
                // 分类名字
                sku.setCategoryName(category.getName());
                // 品牌名字
                sku.setBrandName(brand.getName());
                // 审核状态
                sku.setStatus("1");

                // 增加
                skuMapper.insertSelective(sku);
            }
        }
    }


    /**
     * 根据Spu的ID查找Spu以及对应的Sku集合
     */
    @Override
    public Goods findGoodsById(Long spuId) {
        // 查询Spu
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        // 查找List<Sku>
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);

        // 封装Goods
        Goods goods = new Goods();
        goods.setSkuList(skuList);
        goods.setSpu(spu);
        return goods;
    }


    /**
     * 商品审核
     */
    @Override
    public void audit(Long spuId) {
        // 查询商品
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        // -------判断该商品是否被删除------
        if (spu.getIsDelete().equalsIgnoreCase("1")) {
            throw new RuntimeException("被商品已被删除!");
        }

        // ------实现上架和审核------

        // 审核通过
        spu.setStatus("1");
        // 上架
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKey(spu);
    }


    /**
     * 商品下架
     */
    @Override
    public void pull(Long spuId) {
        // 查询spu
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        // -------判断该商品是否被删除------
        if (spu.getIsDelete().equalsIgnoreCase("1")) {
            throw new RuntimeException("被商品已被删除!");
        }

        // 下架
        spu.setIsMarketable("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }


    /**
     * 商品上架
     */
    @Override
    public void put(Long spuId) {
        // 查询商品
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        // 判断该商品是否被删除
        if ("1".equalsIgnoreCase(spu.getIsDelete())) {
            throw new RuntimeException("删除的商品不能上架!");
        }

        // 判断该商品是否审核通过
        if (!"1".equalsIgnoreCase(spu.getStatus())) {
            throw new RuntimeException("不是已审核的商品!");
        }

        // 上架商品
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }


    /**
     * 上架或下级
     */
    @Override
    public boolean isShow(Long spuId, String isMarketable) {
        // 查询商品
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        if ("1".equals(isMarketable)) {            // 上架
            if ("1".equalsIgnoreCase(spu.getIsDelete()) || !"1".equals(spu.getStatus())) {
                throw new RuntimeException("该商品不能上架");
            }
            spu.setIsMarketable(isMarketable);
            spuMapper.updateByPrimaryKeySelective(spu);
            return true;

        } else {                                            // 下架
            if ("1".equals(spu.getIsDelete())) {
                throw new RuntimeException("已删除的商品不能下架!");
            }
            // 上下架操作
            spu.setIsMarketable(isMarketable);
            spuMapper.updateByPrimaryKeySelective(spu);
            return false;
        }
    }


    /**
     * 批量上架
     */
    @Override
    public int putMany(Long[] ids) {
        Spu spu = new Spu();
        // 上架
        spu.setIsMarketable("1");

        // 批量修改
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));       // id

        // 已经下架的
        criteria.andEqualTo("isMarketable", "0");
        // 审核通过的
        criteria.andEqualTo("status", "1");
        // 非删除的
        criteria.andEqualTo("isDelete", "0");
        // 执行批量上架
        return spuMapper.updateByExampleSelective(spu, example);
    }



    /**
     * 批量下架
     * @param ids
     * @return
     */
    @Override
    public int pullMany(Long... ids) {
        Spu spu = new Spu();
        // 下架
        spu.setIsMarketable("0");

        // 批量修改
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));

        // 已经上架的
        criteria.andEqualTo("isMarketable","1");
        // 非删除的
        criteria.andEqualTo("isDelete","0");
        // 执行批量下架
        return spuMapper.updateByExampleSelective(spu,example);
    }



    /**
     * 逻辑删除
     * @param spuId
     */
    @Override
    public void logicDelete(Long spuId) {
        // 查询spu
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        // 判断是否下架
        if (!"0".equals(spu.getIsMarketable())){
            throw new RuntimeException("没有下架的商品不能删除!");
        }

        // 删除
        spu.setIsDelete("1");
        // 设置成未审核状态
        spu.setStatus("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }


    /**
     * 找回商品
     * @param spuId
     */
    @Override
    public void restore(Long spuId) {
        // 查询spu
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        // 判断是否是删除的商品
        if (!"1".equals(spu.getIsDelete())){
            throw new RuntimeException("此商品未删除!");
        }

        // 未删除
        spu.setIsDelete("0");
        // 未审核
        spu.setStatus("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }


}
