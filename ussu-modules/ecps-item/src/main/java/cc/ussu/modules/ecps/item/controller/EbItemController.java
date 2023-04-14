package cc.ussu.modules.ecps.item.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseAdminController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.datasource.vo.PageInfoVO;
import cc.ussu.modules.ecps.item.entity.EbBrand;
import cc.ussu.modules.ecps.item.entity.EbItem;
import cc.ussu.modules.ecps.item.service.IEbBrandService;
import cc.ussu.modules.ecps.item.service.IEbItemService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 商品，包含手机和号卡，通过在类目表中预置的手机类目和号卡类目来区分。裸机：手机机体，不包含任何通信服务和绑定的费用的机器 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
@RestController
@RequestMapping("/item")
public class EbItemController extends BaseAdminController {

    @Autowired
    private IEbItemService service;
    @Autowired
    private IEbBrandService brandService;

    /**
     * 分页
     */
    @GetMapping
    public JsonResult<PageInfoVO<EbItem>> page(EbItem p) {
        LambdaQueryWrapper<EbItem> qw = new LambdaQueryWrapper<>();
        qw.eq(p.getCatId() != null, EbItem::getCatId, p.getCatId())
                .eq(p.getBrandId() != null, EbItem::getBrandId, p.getBrandId())
                .eq(p.getShowStatus() != null, EbItem::getShowStatus, p.getShowStatus())
                .eq(p.getAuditStatus() != null, EbItem::getAuditStatus, p.getAuditStatus())
                .eq(StrUtil.isNotBlank(p.getItemNo()), EbItem::getItemNo, p.getItemNo())
                .like(StrUtil.isNotBlank(p.getItemName()), EbItem::getItemName, p.getItemName());
        IPage<EbItem> page = service.page(MybatisPlusUtil.getPage(), qw);
        for (EbItem item : page.getRecords()) {
            EbBrand brand = new EbBrand().setBrandId(item.getBrandId()).selectById();
            if (brand != null) {
                item.setBrandName(brand.getBrandName());
            }
        }
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 详情
     */
    @GetMapping("/{id}")
    public JsonResult<EbItem> detail(@PathVariable Integer id) {
        return JsonResult.ok(service.detail(id, true));
    }

    /**
     * 新增
     */
    @PutMapping
    public JsonResult add(@RequestBody EbItem obj) {
        service.add(obj);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    public JsonResult edit(@RequestBody EbItem obj) {
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
