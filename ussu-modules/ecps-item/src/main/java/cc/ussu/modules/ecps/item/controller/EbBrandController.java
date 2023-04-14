package cc.ussu.modules.ecps.item.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseAdminController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.modules.ecps.item.entity.EbBrand;
import cc.ussu.modules.ecps.item.entity.EbCatbrand;
import cc.ussu.modules.ecps.item.service.IEbBrandService;
import cc.ussu.modules.ecps.item.service.IEbCatbrandService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 品牌 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-07-14
 */
@RestController
@RequestMapping("/brand")
public class EbBrandController extends BaseAdminController {

    @Autowired
    private IEbBrandService brandService;
    @Autowired
    private IEbCatbrandService catbrandService;

    /**
     * 分页
     */
    @GetMapping
    public JsonResult page(EbBrand ebBrand) {
        LambdaQueryWrapper<EbBrand> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(EbBrand::getBrandSort)
                .orderByDesc(EbBrand::getBrandId)
                .like(StrUtil.isNotBlank(ebBrand.getBrandName()), EbBrand::getBrandName, ebBrand.getBrandName());
        IPage page = brandService.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 根据分类获取
     */
    @GetMapping("/catId/{catId}")
    public List<EbBrand> listByCatId(@PathVariable Integer catId, EbBrand p) {
        LambdaQueryWrapper<EbCatbrand> qw1 = Wrappers.lambdaQuery(EbCatbrand.class)
                .eq(EbCatbrand::getCatId, catId);
        List<Integer> brandIdList = catbrandService.list(qw1).stream().map(EbCatbrand::getBrandId).collect(Collectors.toList());
        if (CollUtil.isEmpty(brandIdList)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<EbBrand> qw = Wrappers.lambdaQuery(EbBrand.class);
        qw.orderByAsc(EbBrand::getBrandSort)
                .orderByDesc(EbBrand::getBrandId)
                .like(StrUtil.isNotBlank(p.getBrandName()), EbBrand::getBrandName, p.getBrandName())
                .in(EbBrand::getBrandId, brandIdList);
        return brandService.list(qw);
    }

    /**
     * 详情
     */
    @GetMapping("/{brandId}")
    public JsonResult detail(@PathVariable Integer brandId) {
        return JsonResult.ok(brandService.detail(brandId));
    }

    /**
     * 新增
     */
    @PutMapping
    public JsonResult add(@RequestBody EbBrand brand) {
        brandService.add(brand);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    public JsonResult edit(@RequestBody EbBrand brand) {
        brandService.edit(brand);
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    public JsonResult delete(@PathVariable String ids) {
        brandService.del(ids);
        return JsonResult.ok();
    }

}
