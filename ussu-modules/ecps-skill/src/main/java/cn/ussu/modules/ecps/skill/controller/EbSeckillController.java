package cn.ussu.modules.ecps.skill.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.ecps.skill.entity.EbSeckill;
import cn.ussu.modules.ecps.skill.service.IEbSeckillService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import cn.ussu.common.datasource.util.DefaultPageFactory;
import com.baomidou.mybatisplus.core.metadata.IPage;

import org.springframework.web.bind.annotation.RestController;
import cn.ussu.common.core.base.BaseAdminController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-08-11
 */
@RestController
@RequestMapping("/seckill")
public class EbSeckillController extends BaseAdminController {

    @Autowired
    private IEbSeckillService service;

    /**
     * 分页
     */
    @GetMapping
    public Object page(EbSeckill p) {
        LambdaQueryWrapper<EbSeckill> qw = new LambdaQueryWrapper<>();
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
    public JsonResult add(@RequestBody EbSeckill obj) {
        service.add(obj);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    public JsonResult edit(@RequestBody EbSeckill obj) {
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
