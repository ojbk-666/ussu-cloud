package cn.ussu.modules.ecps.member.controller;

import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.datasource.util.DefaultPageFactory;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.ecps.member.entity.EbCartSku;
import cn.ussu.modules.ecps.member.service.IEbCartSkuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-07-30
 */
@RestController
@RequestMapping("/cart")
public class EbCartSkuController extends BaseAdminController {

    @Autowired
    private IEbCartSkuService service;

    /**
     * 分页
     */
    @GetMapping
    public Object page(EbCartSku p) {
        LambdaQueryWrapper<EbCartSku> qw = new LambdaQueryWrapper<>();
        IPage page = service.page(DefaultPageFactory.getPage(), qw);
        return DefaultPageFactory.createReturnPageInfo(page);
    }

    /**
     * 详情
     */
    @GetMapping("/{id}")
    public Object detail(@PathVariable Integer id) {
        return service.detail(id);
    }

    /**
     * 获取个人购物车列表
     */
    @GetMapping("/user")
    public List<EbCartSku> allByUserId() {
        String userId = SecurityUtils.getUserId();
        return service.list(Wrappers.lambdaQuery(EbCartSku.class).eq(EbCartSku::getEbUserId, userId));
    }

    /**
     * 获取购物车数量
     */
    @GetMapping("/amount")
    public JsonResult getCartAmount() {
        return JsonResult.ok().data(service.count(Wrappers.lambdaQuery(EbCartSku.class)
                .eq(EbCartSku::getEbUserId, SecurityUtils.getUserId())));
    }

    /**
     * 新增
     */
    @PutMapping
    public JsonResult add(@RequestBody EbCartSku obj) {
        service.add(obj);
        return JsonResult.ok();
    }

    /**
     * sku加入购物车
     */
    @PutMapping("/add/{skuId}")
    public JsonResult addSkuToCartBySkuId(@PathVariable Integer skuId) {
        Integer cartSkuId = service.addSkuToCartBySkuId(skuId);
        return JsonResult.ok().data(cartSkuId);
    }

    @PostMapping("/updateNum")
    public JsonResult updateCartQuantity(@RequestBody EbCartSku cartSku) {
        new EbCartSku().setCartSkuId(cartSku.getCartSkuId())
                .setQuantity(cartSku.getQuantity())
                .updateById();
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    public JsonResult edit(@RequestBody EbCartSku obj) {
        service.edit(obj);
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    public JsonResult delete(@PathVariable String ids) {
        service.del(ids);
        return JsonResult.ok();
    }

}
