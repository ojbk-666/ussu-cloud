package cc.ussu.modules.system.controller;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.constants.SystemLogConstants;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.system.entity.SysPost;
import cc.ussu.modules.system.service.ISysPostService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 岗位 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-12-31 19:24:31
 */
@Slf4j
@SystemLog(serviceName = ServiceNameConstants.SERVICE_SYSETM, group = "岗位管理")
@RestController
@RequestMapping("${ussu.mapping-prefix.system}/sys-post")
public class SysPostController extends BaseController {

    private static final String PERM_PREFIX = "system:post:";

    @Autowired
    private ISysPostService service;

    /**
     * 分页
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/page")
    public Object page(SysPost p) {
        LambdaQueryWrapper<SysPost> qw = Wrappers.lambdaQuery(SysPost.class)
                .orderByAsc(SysPost::getPostSort)
                .eq(StrUtil.isNotBlank(p.getDisableFlag()), SysPost::getDisableFlag, p.getDisableFlag())
                .like(StrUtil.isNotBlank(p.getPostCode()), SysPost::getPostCode, p.getPostCode())
                .like(StrUtil.isNotBlank(p.getPostName()), SysPost::getPostName, p.getPostName());
        Page<SysPost> page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 详情
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping({"/{id}", "/detail/{id}"})
    public JsonResult<SysPost> detail(@PathVariable String id) {
        return JsonResult.ok(service.getById(id));
    }

    /**
     * 添加
     */
    @SystemLog(name = SystemLogConstants.INSERT)
    @PermCheck(PERM_PREFIX + ADD)
    @PutMapping({"", "/add"})
    public JsonResult add(@Validated @RequestBody SysPost p) {
        // Assert.notBlank(p.getPostCode(), "岗位编码不能为空");
        // Assert.notBlank(p.getPostName(), "岗位名称不能为空");
        Assert.isFalse(service.checkCodeExist(p), "已存在岗位编码：{}", p.getPostCode());
        service.save(p);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @SystemLog(name = SystemLogConstants.UPDATE)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping({"", "/edit"})
    public JsonResult edit(@Validated @RequestBody SysPost p) {
        Assert.notBlank(p.getId(), "id不能为空");
        // Assert.notBlank(p.getPostCode(), "岗位编码不能为空");
        // Assert.notBlank(p.getPostName(), "岗位名称不能为空");
        Assert.isFalse(service.checkCodeExist(p), "已存在岗位编码：{}", p.getPostCode());
        service.updateById(p);
        return JsonResult.ok();
    }

    /**
     * 修改状态
     */
    @SystemLog(name = SystemLogConstants.CHANGE_STATUS)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping("/changeStatus")
    public JsonResult changeStatus(@RequestBody SysPost p) {
        Assert.notBlank(p.getId(), "id不能为空");
        Assert.notBlank(p.getDisableFlag(), "状态不能为空");
        SysPost sp = new SysPost().setId(p.getId()).setDisableFlag(p.getDisableFlag());
        service.updateById(sp);
        return JsonResult.ok();
    }

    /**
     * 删除
     */
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

