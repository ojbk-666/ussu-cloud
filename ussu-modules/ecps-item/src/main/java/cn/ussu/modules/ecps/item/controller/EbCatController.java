package cn.ussu.modules.ecps.item.controller;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.datasource.util.DefaultPageFactory;
import cn.ussu.modules.ecps.item.entity.EbCat;
import cn.ussu.modules.ecps.item.service.IEbCatService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品类目。	1. 电商一期只支持两种商品，即手机和号卡。促销活动作为一种规则配置到上述两种商品上。二期会增加 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-07-13
 */
@RestController
@RequestMapping("/eb-cat")
public class EbCatController extends BaseAdminController {

    @Autowired
    private IEbCatService ebCatService;

    @GetMapping
    public Object findPage() {
        Page page = ebCatService.page(DefaultPageFactory.getPage());
        return DefaultPageFactory.createReturnPageInfo(page);
    }

    @GetMapping("/all")
    public JsonResult all(String catName) {
        LambdaQueryWrapper<EbCat> qw = new LambdaQueryWrapper<>();
        qw.like(StrUtil.isNotBlank(catName), EbCat::getCatName, catName);
        return JsonResult.ok().data(ebCatService.list(qw));
    }

    @GetMapping("/list/tree")
    public List<EbCat> listTree() {
        return ebCatService.listTree();
    }

    @PutMapping
    public JsonResult add(@RequestBody EbCat ebCat) {
        ebCatService.add(ebCat);
        return JsonResult.ok();
    }

    @CacheEvict(value = "item", key = "'catTree'", allEntries = true)
    @PostMapping
    public JsonResult edit(@RequestBody EbCat ebCat) {

        return JsonResult.ok();
    }

    @CacheEvict(value = "item", key = "'catTree'", allEntries = true)
    @DeleteMapping
    public JsonResult delete(String catIds) {
        ebCatService.del(catIds);
        return JsonResult.ok();
    }

}
