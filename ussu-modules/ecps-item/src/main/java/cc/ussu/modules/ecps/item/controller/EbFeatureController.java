package cc.ussu.modules.ecps.item.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseAdminController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.modules.ecps.item.entity.EbFeature;
import cc.ussu.modules.ecps.item.mapper.EbFeatureMapper;
import cc.ussu.modules.ecps.item.service.IEbFeatureService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public JsonResult page(EbFeature p) {
        LambdaQueryWrapper<EbFeature> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(EbFeature::getFeatureSort, EbFeature::getCatId)
                .orderByDesc(EbFeature::getFeatureId)
                .like(StrUtil.isNotBlank(p.getFeatureName()), EbFeature::getFeatureName, p.getFeatureName())
                .eq(p.getCatId() != null, EbFeature::getCatId, p.getCatId())
                .eq(p.getGroupId() != null, EbFeature::getGroupId, p.getGroupId());
        // IPage page = service.page(MybatisPlusUtil.getPage(), qw);
        IPage page = featureMapper.findPage(MybatisPlusUtil.getPage(), p);
        return MybatisPlusUtil.getResult(page);
    }

    @GetMapping("/catId/{catId}")
    public List<EbFeature> listByCatId(@PathVariable Integer catId, EbFeature p) {
        LambdaQueryWrapper<EbFeature> qw = Wrappers.lambdaQuery(EbFeature.class)
                .orderByAsc(EbFeature::getFeatureSort, EbFeature::getGroupId)
                .orderByDesc(EbFeature::getFeatureId)
                .eq(EbFeature::getCatId, catId)
                .eq(p.getIsSpec() != null, EbFeature::getIsSpec, p.getIsSpec())
                .eq(p.getIsSelect() != null, EbFeature::getIsSelect, p.getIsSelect())
                .like(StrUtil.isNotBlank(p.getFeatureName()), EbFeature::getFeatureName, p.getFeatureName());
        return service.list(qw);
    }

    /**
     * 详情
     */
    @GetMapping("/{id}")
    public JsonResult<EbFeature> detail(@PathVariable Integer id) {
        return JsonResult.ok(service.detail(id));
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
