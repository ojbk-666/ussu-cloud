package cn.ussu.modules.ecps.member.controller;

import cn.hutool.core.lang.Assert;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.datasource.util.DefaultPageFactory;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.ecps.member.entity.EbShipAddr;
import cn.ussu.modules.ecps.member.service.IEbShipAddrService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 收货地址 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-07-29
 */
@RestController
@RequestMapping("/ship-addr")
public class EbShipAddrController extends BaseAdminController {

    @Autowired
    private IEbShipAddrService service;

    /**
     * 分页
     */
    @GetMapping
    public Object page(EbShipAddr p) {
        LambdaQueryWrapper<EbShipAddr> qw = new LambdaQueryWrapper<>();
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

    @GetMapping("/allByUserId/{userId}")
    public List<EbShipAddr> allByUserId(@PathVariable String userId) {
        Assert.notBlank(userId);
        return service.list(Wrappers.lambdaQuery(EbShipAddr.class).eq(EbShipAddr::getEbUserId, userId));
    }

    /**
     * 新增
     */
    @PutMapping
    public JsonResult add(@RequestBody EbShipAddr obj) {
        service.add(obj);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    public JsonResult edit(@RequestBody EbShipAddr obj) {
        service.edit(obj);
        return JsonResult.ok();
    }

    /**
     * 设置默认
     */
    @PostMapping("/default/{id}")
    public JsonResult setDefault(@PathVariable Integer id) {
        LambdaQueryWrapper<EbShipAddr> qw = Wrappers.lambdaQuery(EbShipAddr.class)
                .eq(EbShipAddr::getEbUserId, SecurityUtils.getUserId());
        service.update(new EbShipAddr().setDefaultAddr(0), qw);
        new EbShipAddr().setShipAddrId(id).setDefaultAddr(1).updateById();
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
