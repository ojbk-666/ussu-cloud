package cc.ussu.modules.system.controller;

import cc.ussu.common.core.constants.StrConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.constants.SystemLogConstants;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.system.entity.SysDictType;
import cc.ussu.modules.system.service.ISysDictTypeService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 字典类型表 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-12-31 20:31:02
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.system}/sys-dict-type")
public class SysDictTypeController extends BaseController {

    private static final String PERM_PREFIX = "system:dict-type:";
    private static final String SYSTEM_LOG_GROUP = "字典类型管理";

    @Autowired
    private ISysDictTypeService service;

    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping({"", "/list"})
    public JsonResult page(String keyword) {
        LambdaQueryWrapper<SysDictType> qw = Wrappers.lambdaQuery(SysDictType.class)
                .orderByAsc(SysDictType::getDictType)
                .eq(SysDictType::getDelFlag, false)
                .and(StrUtil.isNotBlank(keyword), qa -> qa.or(q -> q.like(SysDictType::getDictName, keyword))
                        .or(q -> q.like(SysDictType::getDictType, keyword))
                        .or(q -> q.like(SysDictType::getRemark, keyword))
                );
        Page<SysDictType> page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 获取全部
     */
    @GetMapping("/all")
    public JsonResult<List<SysDictType>> all() {
        List<SysDictType> list = service.list(Wrappers.lambdaQuery(SysDictType.class).eq(SysDictType::getDelFlag, false)
            .eq(SysDictType::getDisableFlag, StrConstants.CHAR_FALSE));
        return JsonResult.ok(list);
    }

    /**
     * 详情
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping({"/{id}", "/detail/{id}"})
    public JsonResult<SysDictType> detail(@PathVariable String id) {
        return JsonResult.ok(service.getById(id));
    }

    /**
     * 添加
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.INSERT)
    @PermCheck(PERM_PREFIX + ADD)
    @PutMapping({"", "/add"})
    public JsonResult add(@RequestBody SysDictType p) {
        // Assert.notBlank(p.getDictType(), "编码不能为空");
        // Assert.notBlank(p.getDictName(), "名称不能为空");
        Assert.isFalse(service.checkTypeExist(p), "已存在编码：{}", p.getDictType());
        service.save(p);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.UPDATE)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping({"", "/edit"})
    public JsonResult edit(@RequestBody SysDictType p) {
        Assert.notBlank(p.getId(), "id不能为空");
        // Assert.notBlank(p.getDictType(), "编码不能为空");
        // Assert.notBlank(p.getDictName(), "名称不能为空");
        Assert.isFalse(service.checkTypeExist(p), "已存在编码：{}", p.getDictType());
        service.updateById(p);
        return JsonResult.ok();
    }

    /**
     * 修改状态
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.CHANGE_STATUS)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping("/changeStatus")
    public JsonResult changeStatus(@RequestBody SysDictType p) {
        Assert.notBlank(p.getId(), "id不能为空");
        Assert.notBlank(p.getDisableFlag(), "状态不能为空");
        SysDictType sdt = new SysDictType().setId(p.getId()).setDisableFlag(p.getDisableFlag());
        service.updateById(sdt);
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.DELETE)
    @PermCheck(PERM_PREFIX + DELETE)
    @DeleteMapping({"/{ids}", "/del/{ids}"})
    public JsonResult del(@PathVariable String ids) {
        List<String> idList = CollUtil.removeBlank(StrUtil.split(ids, StrUtil.COMMA));
        Assert.notEmpty(idList, "id不能为空");
        service.removeByIds(idList);
        return JsonResult.ok();
    }

}

