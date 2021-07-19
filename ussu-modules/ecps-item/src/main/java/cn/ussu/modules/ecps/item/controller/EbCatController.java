package cn.ussu.modules.ecps.item.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.datasource.util.DefaultPageFactory;
import cn.ussu.modules.ecps.item.entity.EbCat;
import cn.ussu.modules.ecps.item.service.IEbCatService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public JsonResult listTree() {
        return JsonResult.ok().data(ebCatService.listTree());
    }

    @PutMapping
    public JsonResult add(@RequestBody EbCat ebCat) {
        String parentIdPath = null;
        if (ebCat.getParentId() == null) {
            ebCat.setParentId(0);
        } else {
            Integer parentId = ebCat.getParentId();
            String idPath = new EbCat().setCatId(parentId).selectById().getIdPath();
            parentIdPath = idPath;
        }
        ebCat.insert();
        if (parentIdPath == null) {
            ebCat.setIdPath(ebCat.getCatId() + "");
        } else {
            ebCat.setIdPath(parentIdPath + StrPool.COMMA + ebCat.getCatId());
        }
        ebCat.updateById();
        return JsonResult.ok();
    }

    @PostMapping
    public JsonResult edit(@RequestBody EbCat ebCat) {
        Assert.notNull(ebCat.getCatId());
        if (ebCat.getParentId() == null) {
            ebCat.setParentId(0);
            ebCat.setIdPath(ebCat.getCatId() + "");
        } else {
            ebCat.setIdPath(new EbCat()
                    .setCatId(ebCat.getParentId())
                    .selectById()
                    .getIdPath() + StrPool.COMMA + ebCat.getCatId());
        }
        ebCat.updateById();
        return JsonResult.ok();
    }

    @DeleteMapping
    public JsonResult delete(@RequestBody Integer[] catIds) {
        return JsonResult.ok();
    }

}
