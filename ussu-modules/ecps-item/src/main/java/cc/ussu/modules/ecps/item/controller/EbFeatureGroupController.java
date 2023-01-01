package cc.ussu.modules.ecps.item.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseAdminController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.modules.ecps.item.entity.EbFeature;
import cc.ussu.modules.ecps.item.entity.EbFeatureGroup;
import cc.ussu.modules.ecps.item.service.IEbFeatureGroupService;
import cc.ussu.modules.ecps.item.service.IEbFeatureService;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-07-16
 */
@RestController
@RequestMapping("/feature-group")
public class EbFeatureGroupController extends BaseAdminController {

    @Autowired
    private IEbFeatureGroupService service;
    @Autowired
    private IEbFeatureService featureService;

    /**
     * 分页
     */
    @GetMapping
    public Object page(EbFeatureGroup p) {
        LambdaQueryWrapper<EbFeatureGroup> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(EbFeatureGroup::getGroupSort)
                .orderByDesc(EbFeatureGroup::getGroupId)
                .like(StrUtil.isNotBlank(p.getGroupName()), EbFeatureGroup::getGroupName, p.getGroupName())
                .eq(p.getCatId() != null, EbFeatureGroup::getCatId, p.getCatId());
        IPage<EbFeatureGroup> page = service.page(MybatisPlusUtil.getPage(), qw);
        List<EbFeatureGroup> records = page.getRecords();
        for (EbFeatureGroup item : records) {
            LambdaQueryWrapper<EbFeature> qw1 = new LambdaQueryWrapper<>();
            qw1.select(EbFeature::getFeatureId)
                    .orderByAsc(EbFeature::getFeatureId)
                    .eq(EbFeature::getCatId, item.getCatId())
                    .eq(EbFeature::getGroupId, item.getGroupId());
            List<EbFeature> list = featureService.list(qw1);
            item.setFeatureIdList(list.stream().map(EbFeature::getFeatureId).collect(Collectors.toList()));
        }
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 根据分类id获取组
     */
    @GetMapping("/catId/{catId}")
    public List<EbFeatureGroup> listByCatId(@PathVariable Integer catId, Boolean needFeatureList, EbFeature feature) {
        LambdaQueryWrapper<EbFeatureGroup> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(EbFeatureGroup::getGroupSort)
                .orderByDesc(EbFeatureGroup::getGroupId)
                .eq(EbFeatureGroup::getCatId, catId);
        List<EbFeatureGroup> list = service.list(qw);
        if (BooleanUtil.isTrue(needFeatureList)) {
            for (EbFeatureGroup item : list) {
                LambdaQueryWrapper<EbFeature> qw1 = Wrappers.lambdaQuery(EbFeature.class)
                        .eq(EbFeature::getGroupId, item.getGroupId())
                        .eq(EbFeature::getCatId, catId)
                        .eq(feature.getIsSpec() != null, EbFeature::getIsSpec, feature.getIsSpec());
                List<EbFeature> featureList = featureService.list(qw1);
                item.setFeatureList(featureList);
            }
        }
        return list;
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
    public JsonResult add(@RequestBody EbFeatureGroup obj) {
        service.add(obj);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    public JsonResult edit(@RequestBody EbFeatureGroup obj) {
        service.edit(obj);
        return JsonResult.ok();
    }

    @PostMapping("/link")
    public JsonResult link(@RequestBody EbFeatureGroup p) {
        service.link(p);
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
