package cc.ussu.modules.ecps.order.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseAdminController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.modules.ecps.order.entity.EbOrderDetail;
import cc.ussu.modules.ecps.order.service.IEbOrderDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 3-10是商品的冗余数据	11-26是营销案的冗余数据	营销案的时候，根据SKU存多条，每条SK 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-08-03
 */
@RestController
@RequestMapping("/order-detail")
public class EbOrderDetailController extends BaseAdminController {

    @Autowired
    private IEbOrderDetailService service;

    /**
     * 分页
     */
    @GetMapping
    public Object page(EbOrderDetail p) {
        LambdaQueryWrapper<EbOrderDetail> qw = new LambdaQueryWrapper<>();
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
     * 新增
     */
    @PutMapping
    public JsonResult add(@RequestBody EbOrderDetail obj) {
        service.add(obj);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    public JsonResult edit(@RequestBody EbOrderDetail obj) {
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
