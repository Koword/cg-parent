package com.changgou.goods.controller;

import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;
import com.changgou.goods.service.SpuService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/****
 * @Author:shenkunlin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/spu")
@CrossOrigin
public class SpuController {

    @Autowired
    private SpuService spuService;

    /***
     * Spu分页条件搜索实现
     * @param spu
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody(required = false) Spu spu, @PathVariable int page,
        @PathVariable int size) {
        //调用SpuService实现分页条件查询Spu
        PageInfo<Spu> pageInfo = spuService.findPage(spu, page, size);
        return new Result(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * Spu分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageInfo> findPage(@PathVariable int page, @PathVariable int size) {
        //调用SpuService实现分页查询Spu
        PageInfo<Spu> pageInfo = spuService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param spu
     * @return
     */
    @PostMapping(value = "/search")
    public Result<List<Spu>> findList(@RequestBody(required = false) Spu spu) {
        //调用SpuService实现条件查询Spu
        List<Spu> list = spuService.findList(spu);
        return new Result<List<Spu>>(true, StatusCode.OK, "查询成功", list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        //调用SpuService实现根据主键删除
        spuService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 修改Spu数据
     * @param spu
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Spu spu, @PathVariable Long id) {
        //设置主键值
        spu.setId(id);
        //调用SpuService实现修改Spu
        spuService.update(spu);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /***
     * 新增Spu数据
     * @param spu
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Spu spu) {
        //调用SpuService实现添加Spu
        spuService.add(spu);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /***
     * 根据ID查询Spu数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Spu> findById(@PathVariable Long id) {
        //调用SpuService实现根据主键查询Spu
        Spu spu = spuService.findById(id);
        return new Result<Spu>(true, StatusCode.OK, "查询成功", spu);
    }

    /***
     * 查询Spu全部数据
     * @return
     */
    @GetMapping
    public Result<List<Spu>> findAll() {
        //调用SpuService实现查询所有Spu
        List<Spu> list = spuService.findAll();
        return new Result<List<Spu>>(true, StatusCode.OK, "查询成功", list);
    }


    /**
     * @Description 保存spu
     * @Author tangKai
     * @Date 17:30 2019/12/30
     * @Param [goods]
     * @Return entity.Result
     **/
    @PostMapping("/save")
    public Result save(@RequestBody Goods goods) {
        spuService.saveOrUpdateGoods(goods);
        return new Result(true, StatusCode.OK, "保存Spu成功!");
    }


    /**
     * @Description 根据Spu的ID查找Spu以及对应的Sku集合
     * @Author tangKai
     * @Date 18:03 2019/12/30
     * @Param [id]
     * @Return entity.Result
     **/
    @GetMapping(value = "/goods/{id}")
    public Result findGoodById(@PathVariable(value = "id") Long id) {
        // 根据ID查询Goods(Spu+Sku)信息
        Goods goods = spuService.findGoodsById(id);
        return new Result<Goods>(true, StatusCode.OK, "据Spu的ID查找Spu以及对应的Sku集合成功!", goods);
    }


    /**
     * @Description 商品审核上架
     * @Author tangKai
     * @Date 10:48 2019/12/31
     * @Param [id]
     * @Return entity.Result
     **/
    @PutMapping(value = "/audit/{id}")
    public Result audit(@PathVariable(value = "id") Long id) {
        spuService.audit(id);
        return new Result(true, StatusCode.OK, "审核通过，并且以上架!");
    }


    /**
     * @Description 下架
     * @Author tangKai
     * @Date 11:14 2019/12/31
     * @Param [id]
     * @Return entity.Result
     **/
    @PutMapping(value = "/pull/{id}")
    public Result pull(@PathVariable(value = "id") Long id) {
        spuService.pull(id);
        return new Result(true, StatusCode.OK, "下架成功!");

    }


    /**
     * @Description 上架
     * @Author tangKai
     * @Date 11:33 2019/12/31
     * @Param [id]
     * @Return entity.Result
     **/
    @PutMapping(value = "/put/{id}")
    public Result put(@PathVariable(value = "id") Long id) {
        spuService.put(id);
        return new Result(true, StatusCode.OK, "上架成功!");
    }


    /**
     * @Description 上架或者下级
     * @Author tangKai
     * @Date 11:55 2019/12/31
     * @Param [id, isMarketable]
     * @Return entity.Result
     **/
    @PutMapping(value = "/isShow/{id}/{isMarketable}")
    public Result isShow(@PathVariable(value = "id") Long id,
        @PathVariable(value = "isMarketable") String isMarketable) {
        boolean isShow = spuService.isShow(id, isMarketable);
        // 根据service 的boolean值来判断上下架：true:上架  false:下架
        if (isShow) {
            return new Result(true, StatusCode.OK, "上架成功!");
        }
        return new Result(true, StatusCode.OK, "下架成功!");
    }


    /**
     * @Description 批量上架
     * @Author tangKai
     * @Date 10:19 2020/1/2
     * @Param [ids]
     * @Return entity.Result
     **/
    @PutMapping(value = "/put/many")
    public Result putMany(@RequestBody Long... ids) {
        int count = spuService.putMany(ids);
        return new Result(true, StatusCode.OK, count + "个商品成功上架!");
    }


    /**
     * @Description 批量下架
     * @Author tangKai
     * @Date 10:19 2020/1/2
     * @Param [ids]
     * @Return entity.Result
     **/
    @PutMapping(value = "/pull/many")
    public Result pillMany(@RequestBody Long... ids) {
        int count = spuService.pullMany(ids);
        return new Result(true, StatusCode.OK, count + "个商品成功下架!");
    }


    /**
     * @Description 逻辑删除商品
     * @Author tangKai
     * @Date 13:42 2020/1/2
     * @Param [id]
     * @Return entity.Result
     **/
    @PutMapping(value = "/logic/delete/{id}")
    public Result logicDelete(@PathVariable(value = "id") Long id) {
        spuService.logicDelete(id);
        return new Result(true, StatusCode.OK, "逻辑删除成功!");
    }


    /**
     * @Description 找回商品
     * @Author tangKai
     * @Date 13:45 2020/1/2
     * @Param [id]
     * @Return entity.Result
     **/
    @PutMapping(value = "/restore/{id}")
    public Result restore(@PathVariable(value = "id") Long id) {
        spuService.restore(id);
        return new Result(true, StatusCode.OK, "找回商品成功!");
    }


}
