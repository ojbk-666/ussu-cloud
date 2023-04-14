package cc.ussu.modules.ecps.order.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseAdminController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.modules.ecps.order.entity.EbOrderLog;
import cc.ussu.modules.ecps.order.service.IEbOrderLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 订单日志	Name	Code	Comment	Default Value	Data Type	Length	 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-08-03
 */
@RestController
@RequestMapping("/order-log")
public class EbOrderLogController extends BaseAdminController {

    @Autowired
    private IEbOrderLogService service;

    /**
     * 分页
     */
    @GetMapping
    public JsonResult page(EbOrderLog p) {
        LambdaQueryWrapper<EbOrderLog> qw = new LambdaQueryWrapper<>();
        IPage page = service.page(MybatisPlusUtil.getPage(), qw);
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
    public JsonResult add(@RequestBody EbOrderLog obj) {
        service.add(obj);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    public JsonResult edit(@RequestBody EbOrderLog obj) {
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
