package cc.ussu.modules.system.controller;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.datasource.vo.PageInfoVO;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.constants.SystemLogConstants;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.system.entity.SysRegion;
import cc.ussu.modules.system.entity.vo.RegionSelectVO;
import cc.ussu.modules.system.service.ISysRegionService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 省市区区域表 前端控制器
 * </p>
 *
 * @author liming
 * @since 2022-01-23 17:45:37
 */
@SystemLog(serviceName = ServiceNameConstants.SERVICE_SYSETM, group = "省市区管理")
@RestController
@RequestMapping("${ussu.mapping-prefix.system}/sys-region")
public class SysRegionController extends BaseController {

    private static final String PERM_PREFIX = "system:region:";
    @Autowired
    private ISysRegionService service;

    /**
     * 分页
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/page")
    public JsonResult<PageInfoVO<SysRegion>> page() {
        LambdaQueryWrapper<SysRegion> qw = Wrappers.lambdaQuery(SysRegion.class);
        Page<SysRegion> page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 获取子集
     */
    @GetMapping("/children")
    public JsonResult<List<SysRegion>> children(SysRegion p) {
        if (StrUtil.isBlank(p.getId())) {
            p.setId("0");
            p.setId("100000");
        }
        List<SysRegion> children = service.getChildren(p.getId());
        return JsonResult.ok(children);
    }

    @GetMapping({"/select/{id}", "/select/"})
    public JsonResult<List<RegionSelectVO>> selectChildren(@PathVariable(required = false) String id) {
        id = StrUtil.blankToDefault(id, "100000");
        List<SysRegion> list = service.getChildren(id);
        List<RegionSelectVO> collect = list.stream().map(r -> BeanUtil.toBean(r, RegionSelectVO.class)).collect(Collectors.toList());
        return JsonResult.ok(collect);
    }

    /**
     * 单个详情
     */
    // @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping({"/{id}", "/detail/{id}"})
    public JsonResult<SysRegion> detail(@PathVariable String id) {
        return JsonResult.ok(service.getById(id));
    }

    /**
     * 添加
     */
    @SystemLog(name = SystemLogConstants.INSERT)
    @PermCheck(PERM_PREFIX + ADD)
    @PutMapping({"", "/add"})
    public JsonResult add(@Validated @RequestBody SysRegion p) {
        service.add(p);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @SystemLog(name = SystemLogConstants.UPDATE)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping({"", "/edit"})
    public JsonResult edit(@Validated @RequestBody SysRegion p) {
        Assert.notBlank(p.getId(), "id不能为空");
        service.edit(p);
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @SystemLog(name = SystemLogConstants.DELETE)
    @PermCheck(PERM_PREFIX + DELETE)
    @DeleteMapping({"/{ids}", "/del/{ids}"})
    public JsonResult del(@PathVariable String ids) {
        List<String> idList = splitCommaList(ids);
        Assert.notEmpty(idList, "id不能为空");
        service.removeByIds(idList);
        return JsonResult.ok();
    }

}

