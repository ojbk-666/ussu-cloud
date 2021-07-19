package cn.ussu.modules.ecps.item.controller;

import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.datasource.util.DefaultPageFactory;
import cn.ussu.modules.ecps.item.entity.EbSku;
import cn.ussu.modules.ecps.item.service.IEbSkuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Object page(EbSku p) {
        LambdaQueryWrapper<EbSku> qw = new LambdaQueryWrapper<>();
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
     * 删除
     */
    @DeleteMapping("/{ids}")
    public JsonResult delete(@PathVariable String ids) {
        service.del(ids);
        return JsonResult.ok();
    }

}
