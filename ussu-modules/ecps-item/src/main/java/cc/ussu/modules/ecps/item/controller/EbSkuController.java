package cc.ussu.modules.ecps.item.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseAdminController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.modules.ecps.item.entity.EbSku;
import cc.ussu.modules.ecps.item.service.IEbSkuService;
import cc.ussu.modules.ecps.order.entity.EbOrder;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 最小销售单元，包括实体商品、虚拟商品（如号卡、套卡、话费等）	将要增加的字段：	STOCK_IN 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
@RestController
@RequestMapping("/sku")
public class EbSkuController extends BaseAdminController {

    @Autowired
    private IEbSkuService service;

    /**
     * 分页
     */
    @GetMapping
    public JsonResult page(EbSku p) {
        LambdaQueryWrapper<EbSku> qw = new LambdaQueryWrapper<>();
        IPage page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 获取单个sku详情
     */
    @GetMapping("/simpleDetail/{skuId}")
    public EbSku simpleSkuDetail(@PathVariable Integer skuId) {
        return service.getById(skuId);
    }

    /**
     * 获取多个sku详情
     */
    @GetMapping("/simpleDetailList/{skuIds}")
    public List<EbSku> simpleDetailList(@PathVariable String skuIds) {
        List<Integer> skuIdList = Arrays.stream(StrUtil.splitToInt(skuIds, StrPool.COMMA)).boxed().collect(Collectors.toList());
        return service.listByIds(skuIdList);
    }

    /**
     * 详情
     */
    @GetMapping("/{id}")
    public JsonResult<EbSku> detail(@PathVariable Integer id) {
        return JsonResult.ok(service.detail(id, false));
    }

    @GetMapping("/detail2/{skuId}")
    public EbSku detail2(@PathVariable Integer skuId) {
        EbSku ebSku = service.detail2(skuId, true);
        return ebSku;
    }

    /**
     * 新增
     */
    @PutMapping
    public JsonResult add(@RequestBody EbSku obj) {
        service.add(obj);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    public JsonResult edit(@RequestBody EbSku obj) {
        service.edit(obj);
        return JsonResult.ok();
    }

    /**
     * 商品上架
     */
    @PostMapping("/up/{skuIds}")
    public JsonResult up(@PathVariable String skuIds) {
        service.upSkuBySkuId(skuIds);
        return JsonResult.ok();
    }

    /**
     * 更新sku库存
     */
    @PostMapping("/updateStock")
    public JsonResult updateSkuStock(EbSku sku) {
        service.updateStock(sku);
        return JsonResult.ok();
    }

    /**
     * 取消订单时恢复库存
     */
    @PostMapping("/rollbackStock")
    public JsonResult rollbackSkuStock(@RequestBody EbOrder order) {
        service.rollbackStock(order);
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
