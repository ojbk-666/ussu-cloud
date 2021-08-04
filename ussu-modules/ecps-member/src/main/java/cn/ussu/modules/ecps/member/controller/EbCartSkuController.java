package cn.ussu.modules.ecps.member.controller;

import cn.hutool.core.util.NumberUtil;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.datasource.util.DefaultPageFactory;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.ecps.item.entity.EbItem;
import cn.ussu.modules.ecps.item.entity.EbSku;
import cn.ussu.modules.ecps.item.entity.EbSpecValue;
import cn.ussu.modules.ecps.member.entity.EbCartSku;
import cn.ussu.modules.ecps.member.feign.RemoteSkuService;
import cn.ussu.modules.ecps.member.service.IEbCartSkuService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    @Autowired
    private RemoteSkuService remoteSkuService;

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
    public JsonResult allByUserId() {
        String userId = SecurityUtils.getUserId();
        return JsonResult.ok().data(service.list(Wrappers.lambdaQuery(EbCartSku.class).eq(EbCartSku::getEbUserId, userId)));
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
        // 检查是否已有
        LambdaQueryWrapper<EbCartSku> qw = Wrappers.lambdaQuery(EbCartSku.class)
                .eq(EbCartSku::getEbUserId, SecurityUtils.getUserId()).eq(EbCartSku::getSkuId, skuId);
        EbCartSku one = service.getOne(qw);
        if (one != null) {
            new EbCartSku().setCartSkuId(one.getCartSkuId())
                    .setQuantity(one.getQuantity() + 1)
                    .setTotalPrice(NumberUtil.mul(one.getPrice(), one.getQuantity() + 1))
                    .updateById();
        } else {
            JsonResult jr = remoteSkuService.getSkuBySkuId(skuId);
            EbSku sku = jr.getData(EbSku.class);
            EbItem item = sku.getItem();
            Map<String, Object> skuSpec = getNewHashMap();
            for (EbSpecValue spec : sku.getSpecList()) {
                skuSpec.put(spec.getFeatureName(), spec.getSpecValue());
            }
            new EbCartSku().setEbUserId(SecurityUtils.getUserId())
                    .setItemId(item.getItemId())
                    .setItemNo(item.getItemNo())
                    .setItemName(item.getItemName())
                    .setSkuId(sku.getSkuId())
                    .setSku(sku.getSku())
                    .setSkuName(sku.getSkuName())
                    .setSkuImg(sku.getSkuImg())
                    .setSkuSpec(JSON.toJSONString(skuSpec))
                    .setMarketPrice(sku.getMarketPrice())
                    .setPrice(sku.getSkuPrice())
                    .setQuantity(1)
                    .setTotalPrice(NumberUtil.mul(sku.getSkuPrice(), 1))
                    .insert();
        }
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
