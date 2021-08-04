package cn.ussu.modules.ecps.order.controller;

import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.datasource.util.DefaultPageFactory;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.ecps.order.entity.EbOrder;
import cn.ussu.modules.ecps.order.service.IEbOrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 订单。包括实体商品和虚拟商品的订单 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-08-03
 */
@RestController
@RequestMapping("/order")
public class EbOrderController extends BaseAdminController {

    @Autowired
    private IEbOrderService service;

    /**
     * 分页
     */
    @GetMapping
    public Object page(EbOrder p) {
        LambdaQueryWrapper<EbOrder> qw = new LambdaQueryWrapper<>();
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
     * 新增
     */
    @PutMapping
    public JsonResult add(@RequestBody EbOrder obj) {
        service.add(obj);
        return JsonResult.ok();
    }

    /**
     * 创建订单
     */
    @PutMapping("/submitOrder")
    public JsonResult submitOrder(@RequestBody EbOrder order) {
        EbOrder r = service.submitOrder(order, order.getCartIdStr(), SecurityUtils.getUserId());
        return JsonResult.ok().data(r);
    }

    /**
     * 修改
     */
    @PostMapping
    public JsonResult edit(@RequestBody EbOrder obj) {
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
