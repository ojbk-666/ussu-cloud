package cc.ussu.modules.ecps.member.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseAdminController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.ecps.member.entity.EbCartSku;
import cc.ussu.modules.ecps.member.service.IEbCartSkuService;
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
        IPage page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
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
        String userId = SecurityUtil.getUserId();
        return service.list(Wrappers.lambdaQuery(EbCartSku.class).eq(EbCartSku::getEbUserId, userId));
    }

    /**
     * 获取购物车数量
     */
    @GetMapping("/amount")
    public JsonResult getCartAmount() {
        return JsonResult.ok(service.count(Wrappers.lambdaQuery(EbCartSku.class)
            .eq(EbCartSku::getEbUserId, SecurityUtil.getUserId())));
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
        return JsonResult.ok(cartSkuId);
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
