package cn.ussu.modules.ecps.member.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.ecps.member.entity.EbUser;
import cn.ussu.modules.ecps.member.service.IEbUserService;
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
 * @since 2021-07-29
 */
@RestController
@RequestMapping("/user")
public class EbUserController extends BaseAdminController {

    @Autowired
    private IEbUserService service;

    /**
     * 分页
     */
    @GetMapping
    public Object page(EbUser p) {
        LambdaQueryWrapper<EbUser> qw = new LambdaQueryWrapper<>();
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
    public JsonResult add(@RequestBody EbUser obj) {
        service.add(obj);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    public JsonResult edit(@RequestBody EbUser obj) {
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
