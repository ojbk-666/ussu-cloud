package cc.ussu.modules.system.controller;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.constants.SystemLogConstants;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.system.entity.SysDept;
import cc.ussu.modules.system.service.ISysDeptService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 部门 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@SystemLog(serviceName = ServiceNameConstants.SERVICE_SYSETM, group = "部门管理")
@RestController
@RequestMapping("${ussu.mapping-prefix.system}/sys-dept")
public class SysDeptController extends BaseController {

    private static final String PERM_PREFIX = "system:dept:";

    @Autowired
    private ISysDeptService service;

    public boolean hasChildren(String id) {
        return service.count(Wrappers.lambdaQuery(SysDept.class).eq(SysDept::getParentId, id)) > 0;
    }

    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/page")
    public JsonResult page(SysDept p) {
        LambdaQueryWrapper<SysDept> qw = Wrappers.lambdaQuery(SysDept.class)
                .eq(StrUtil.isNotBlank(p.getParentId()), SysDept::getParentId, p.getParentId())
                .like(StrUtil.isNotBlank(p.getName()), SysDept::getName, p.getName())
                .like(StrUtil.isNotBlank(p.getFullName()), SysDept::getFullName, p.getFullName());
        IPage<SysDept> iPage = service.page(MybatisPlusUtil.getPage(), qw);
        for (SysDept dept : iPage.getRecords()) {
            dept.setHasChildren(hasChildren(dept.getId()));
        }
        return MybatisPlusUtil.getResult(iPage);
    }

    /**
     * 获取直接子节点
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/children")
    public JsonResult<List<SysDept>> children(SysDept p) {
        if (StrUtil.isBlank(p.getId())) {
            p.setId("0");
        }
        LambdaQueryWrapper<SysDept> qw = Wrappers.lambdaQuery(SysDept.class).eq(SysDept::getParentId, p.getId());
        List<SysDept> list = service.list(qw);
        List<SysDept> children = new ArrayList<>();
        for (SysDept dept : list) {
            dept.setChildren(children).setHasChildren(hasChildren(dept.getId()));
        }
        return JsonResult.ok(list);
    }

    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/list")
    public JsonResult<List<SysDept>> list(SysDept p) {
        LambdaQueryWrapper<SysDept> qw = Wrappers.lambdaQuery(SysDept.class)
                .like(StrUtil.isNotBlank(p.getName()), SysDept::getName, p.getName())
                .like(StrUtil.isNotBlank(p.getFullName()), SysDept::getFullName, p.getFullName());
        List<SysDept> list = service.list(qw);
        return JsonResult.ok(list);
    }

    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/list/exclude/{id}")
    public JsonResult listExclude(@PathVariable String id) {
        LambdaQueryWrapper<SysDept> qw = Wrappers.lambdaQuery(SysDept.class).ne(SysDept::getId, id);
        return JsonResult.ok(service.list(qw));
    }

    /**
     * 树
     */
    @GetMapping("/treeselect")
    public JsonResult<List<SysDept>> treeselect() {
        List<SysDept> list = service.list();
        return JsonResult.ok(service.renderToTree(list));
    }

    /**
     * 单个详情
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/{id}")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.ok(service.getById(id));
    }

    @SystemLog(name = SystemLogConstants.INSERT)
    @PermCheck(PERM_PREFIX + ADD)
    @PutMapping({"", "/add"})
    public JsonResult add(@Validated @RequestBody SysDept p) {
        service.add(p);
        return JsonResult.ok();
    }

    @SystemLog(name = SystemLogConstants.UPDATE)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping({"", "/edit"})
    public JsonResult edit(@Validated @RequestBody SysDept p) {
        service.edit(p);
        return JsonResult.ok();
    }

    @SystemLog(name = SystemLogConstants.DELETE)
    @PermCheck(PERM_PREFIX + DELETE)
    @DeleteMapping({"/{ids}", "/del/{ids}"})
    public JsonResult del(@PathVariable String ids) {
        List<String> idList = CollUtil.removeBlank(StrUtil.split(ids, StrUtil.COMMA));
        Assert.notEmpty(idList, "id不能为空");
        service.removeByIds(idList);
        return JsonResult.ok();
    }

}

