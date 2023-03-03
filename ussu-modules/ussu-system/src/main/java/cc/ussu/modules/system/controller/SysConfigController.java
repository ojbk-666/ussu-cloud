package cc.ussu.modules.system.controller;

import cc.ussu.common.core.constants.StrConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.system.entity.SysConfig;
import cc.ussu.modules.system.entity.vo.SysConfigGroupVO;
import cc.ussu.modules.system.entity.vo.SysConfigVO;
import cc.ussu.modules.system.service.ISysConfigService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统配置表 前端控制器
 * </p>
 *
 * @author mp-generator
 * @since 2023-03-01 14:45:52
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.system}/sys-config")
public class SysConfigController extends BaseController {

    private static final String PERM_PREFIX = "system:sys-config";
    public static final String SYSTEM_LOG_GROUP = "系统配置表";

    @Autowired
    private ISysConfigService service;

    /**
     * 分页
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/group/page")
    public JsonResult groupPage(String keyword) {
        LambdaQueryWrapper<SysConfig> qw = Wrappers.lambdaQuery(SysConfig.class)
                .eq(SysConfig::getGroupFlag, StrConstants.CHAR_TRUE)
                .and(StrUtil.isNotBlank(keyword),
                        q -> q.like(SysConfig::getGroupName, keyword).or().like(SysConfig::getGroupCode, keyword));
        IPage<SysConfig> page = service.page(MybatisPlusUtil.getPage(), qw);
        List<SysConfigGroupVO> groupList = BeanUtil.copyToList(page.getRecords(), SysConfigGroupVO.class);
        return MybatisPlusUtil.getResult(page.getTotal(), groupList);
    }

    /**
     * 添加分组
     */
    @PermCheck(PERM_PREFIX + ADD)
    @PutMapping("/group/add")
    public JsonResult groupAdd(@RequestBody SysConfigGroupVO vo) {
        service.addGroup(vo);
        return JsonResult.ok();
    }

    /**
     * 修改分组
     */
    @PermCheck(PERM_PREFIX + ADD)
    @PostMapping("/group/edit")
    public JsonResult groupEdit(@RequestBody SysConfigGroupVO vo) {
        service.editGroup(vo);
        return JsonResult.ok();
    }

    /**
     * 删除分组
     */
    @PermCheck(PERM_PREFIX + ADD)
    @DeleteMapping("/group/{ids}")
    public JsonResult groupDelete(@PathVariable String ids) {
        List<String> idList = splitCommaList(ids);
        Assert.notEmpty(idList, "id不能为空");
        service.removeByIds(idList);
        return JsonResult.ok();
    }

    /**
     * 分页
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/page")
    public JsonResult page(SysConfigVO query, String keyword) {
        LambdaQueryWrapper<SysConfig> qw = Wrappers.lambdaQuery(SysConfig.class)
                .eq(SysConfig::getGroupFlag, StrConstants.CHAR_FALSE)
                .eq(StrUtil.isNotBlank(query.getGroupCode()), SysConfig::getGroupCode, query.getGroupCode())
                .and(StrUtil.isNotBlank(keyword),
                        q -> q.like(SysConfig::getLabel, keyword).or().like(SysConfig::getCode, keyword));
        IPage<SysConfig> page = service.page(MybatisPlusUtil.getPage(), qw);
        List<SysConfigVO> voList = BeanUtil.copyToList(page.getRecords(), SysConfigVO.class);
        return MybatisPlusUtil.getResult(page.getTotal(), voList);
    }

    /**
     * 单个详情
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping({"/{id}", "/detail/{id}"})
    public JsonResult<SysConfig> detail(@PathVariable String id) {
        return JsonResult.ok(service.getById(id));
    }

    /**
     * 添加
     */
    @PermCheck(PERM_PREFIX + ADD)
    @PutMapping("/add")
    public JsonResult add(@Validated @RequestBody SysConfigVO p) {
        service.addData(p);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping("/edit")
    public JsonResult edit(@Validated @RequestBody SysConfigVO p) {
        Assert.notBlank(p.getId(), "id不能为空");
        service.editData(p);
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @PermCheck(PERM_PREFIX + DELETE)
    @DeleteMapping({"/{ids}", "/del/{ids}"})
    public JsonResult del(@PathVariable String ids) {
        List<String> idList = splitCommaList(ids);
        Assert.notEmpty(idList, "id不能为空");
        service.removeByIds(idList);
        return JsonResult.ok();
    }

    /**
     * 修改状态
     */
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping("/changeStatus")
    public void changeStatus(@RequestBody SysConfigVO p) {
        Assert.notBlank(p.getId(), "id不能为空");
        Assert.notBlank(p.getDisableFlag(), "状态不能为空");
        SysConfig sysConfig = new SysConfig().setId(p.getId()).setDisableFlag(p.getDisableFlag());
        service.updateById(sysConfig);
    }

    /**
     * 刷新缓存
     */
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping("/refresh-cache")
    public void refreshCache() {
        service.refreshCache();
    }

}

