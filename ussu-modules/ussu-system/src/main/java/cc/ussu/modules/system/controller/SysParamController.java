package cc.ussu.modules.system.controller;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.constants.SystemLogConstants;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.system.entity.SysParam;
import cc.ussu.modules.system.service.ISysParamService;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统参数表 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@SystemLog(serviceName = ServiceNameConstants.SERVICE_SYSETM, group = "参数管理")
@RestController
@RequestMapping("${ussu.mapping-prefix.system}/sys-param")
public class SysParamController extends BaseController {

    private static final String PERM_PREFIX = "system:param:";

    @Autowired
    private ISysParamService service;

    /**
     * 分页
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/list")
    public Object page(SysParam p) {
        LambdaQueryWrapper<SysParam> qw = Wrappers.lambdaQuery(SysParam.class)
                .orderByAsc(SysParam::getParamName)
                .eq(StrUtil.isNotBlank(p.getDisableFlag()), SysParam::getDisableFlag, p.getDisableFlag())
                .like(StrUtil.isNotBlank(p.getParamName()), SysParam::getParamName, p.getParamName())
                .like(StrUtil.isNotBlank(p.getParamKey()), SysParam::getParamKey, p.getParamKey());
        Page page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 单个详情
     */
    @GetMapping({"/{id}", "/detail/{id}"})
    public JsonResult<SysParam> detail(@PathVariable String id) {
        return JsonResult.ok(service.getById(id));
    }

    /**
     * 添加
     */
    @SystemLog(name = SystemLogConstants.INSERT)
    @PermCheck(PERM_PREFIX + ADD)
    @PutMapping({"", "/add"})
    public JsonResult add(@Validated @RequestBody SysParam p) {
        // Assert.notBlank(p.getParamName(), "参数名不能为空");
        // Assert.notBlank(p.getParamKey(), "参数键不能为空");
        // Assert.notBlank(p.getParamValue(), "参数值不能为空");
        Assert.isFalse(service.checkNameExist(p), "已存在参数名：{}", p.getParamName());
        service.save(p);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @SystemLog(name = SystemLogConstants.UPDATE)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping({"", "/edit"})
    public JsonResult edit(@Validated @RequestBody SysParam p) {
        Assert.notBlank(p.getId(), "参数名id不能为空");
        // Assert.notBlank(p.getParamName(), "参数名不能为空");
        // Assert.notBlank(p.getParamKey(), "参数键不能为空");
        // Assert.notBlank(p.getParamValue(), "参数值不能为空");
        Assert.isFalse(service.checkKeyExist(p), "已存在参数键：{}", p.getParamKey());
        service.updateById(p);
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

    /**
     * 更改状态
     */
    @SystemLog(name = SystemLogConstants.CHANGE_STATUS)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping("/changeStatus")
    public JsonResult changeStatus(@RequestBody SysParam p) {
        Assert.notBlank(p.getId(), "id不能为空");
        Assert.notBlank(p.getDisableFlag(), "状态不能为空");
        service.updateById(new SysParam().setId(p.getId()).setDisableFlag(p.getDisableFlag()));
        return JsonResult.ok();
    }

}

