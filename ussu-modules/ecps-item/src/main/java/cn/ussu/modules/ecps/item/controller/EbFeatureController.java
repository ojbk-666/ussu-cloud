package cn.ussu.modules.ecps.item.controller;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.datasource.util.DefaultPageFactory;
import cn.ussu.modules.ecps.item.entity.EbFeature;
import cn.ussu.modules.ecps.item.mapper.EbFeatureMapper;
import cn.ussu.modules.ecps.item.service.IEbFeatureService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 商品属性	预置的手机参数（请将预置可选值补充完整）	1.      型号             前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-07-16
 */
@RestController
@RequestMapping("/feature")
public class EbFeatureController extends BaseAdminController {

    @Autowired
    private IEbFeatureService service;
    @Autowired
    private EbFeatureMapper featureMapper;

    /**
     * 分页
     */
    @GetMapping
    public Object page(EbFeature p) {
        LambdaQueryWrapper<EbFeature> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(EbFeature::getFeatureSort, EbFeature::getCatId)
                .orderByDesc(EbFeature::getFeatureId)
                .like(StrUtil.isNotBlank(p.getFeatureName()), EbFeature::getFeatureName, p.getFeatureName())
                .eq(p.getCatId() != null, EbFeature::getCatId, p.getCatId())
                .eq(p.getGroupId() != null, EbFeature::getGroupId, p.getGroupId());
        // IPage page = service.page(DefaultPageFactory.getPage(), qw);
        IPage page = featureMapper.findPage(DefaultPageFactory.getPage(), p);
        return DefaultPageFactory.createReturnPageInfo(page);
    }

    @GetMapping("/catId/{catId}")
    public Object listByCatId(@PathVariable Integer catId, EbFeature p) {
        LambdaQueryWrapper<EbFeature> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(EbFeature::getFeatureSort, EbFeature::getGroupId)
                .orderByDesc(EbFeature::getFeatureId)
                .eq(EbFeature::getCatId, catId)
                .eq(p.getIsSpec() != null, EbFeature::getIsSpec, p.getIsSpec());
        return service.list(qw);
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
    public JsonResult add(@RequestBody EbFeature obj) {
        service.add(obj);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    public JsonResult edit(@RequestBody EbFeature obj) {
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
