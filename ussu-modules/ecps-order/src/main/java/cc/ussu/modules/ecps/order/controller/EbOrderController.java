package cc.ussu.modules.ecps.order.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseAdminController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.ecps.order.entity.EbOrder;
import cc.ussu.modules.ecps.order.mapper.EbOrderMapper;
import cc.ussu.modules.ecps.order.service.IEbOrderService;
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
    @Autowired
    private EbOrderMapper orderMapper;

    /**
     * 分页
     */
    @GetMapping
    public JsonResult page(EbOrder p) {
        IPage page = orderMapper.findPage(MybatisPlusUtil.getPage(), p);
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 详情
     */
    @GetMapping("/{id}")
    public JsonResult detail(@PathVariable Integer id) {
        return JsonResult.ok(service.detail(id));
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
    public EbOrder submitOrder(@RequestBody EbOrder order) {
        EbOrder r = service.submitOrder(order, order.getCartIdStr(), SecurityUtil.getUserId());
        return r;
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
