package cc.ussu.modules.system.controller;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.constants.StrConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.datasource.vo.PageInfoVO;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.constants.SystemLogConstants;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.system.entity.SysDictData;
import cc.ussu.modules.system.entity.vo.SysDictDataSelectVO;
import cc.ussu.modules.system.service.ISysDictDataService;
import cc.ussu.modules.system.service.ISysDictTypeService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-12-31 20:31:02
 */
@SystemLog(serviceName = ServiceNameConstants.SERVICE_SYSETM, group = "字典数据管理")
@RestController
@RequestMapping("${ussu.mapping-prefix.system}/sys-dict-data")
public class SysDictDataController extends BaseController {

    private static final String PERM_PREFIX = "system:dict-data:";

    @Autowired
    private ISysDictDataService service;
    @Autowired
    private ISysDictTypeService sysDictTypeService;

    /**
     * 分页
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/page")
    public JsonResult<PageInfoVO<SysDictData>> page(String keyword, String dictType) {
        LambdaQueryWrapper<SysDictData> qw = Wrappers.lambdaQuery(SysDictData.class)
            .orderByAsc(SysDictData::getCreateTime)
            .orderByAsc(SysDictData::getDictType)
            .eq(SysDictData::getDelFlag, StrConstants.CHAR_FALSE)
            .eq(StrUtil.isNotBlank(dictType), SysDictData::getDictType, dictType)
            .and(StrUtil.isNotBlank(keyword), qa -> qa.or(q -> q.like(SysDictData::getDictLabel, keyword))
                .or(q -> q.like(SysDictData::getDictCode, keyword))
                .or(q -> q.like(SysDictData::getRemark, keyword))
            );
        IPage<SysDictData> page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/list")
    public JsonResult<List<SysDictData>> list(SysDictData p) {
        LambdaQueryWrapper<SysDictData> qw = Wrappers.lambdaQuery(SysDictData.class)
            .orderByAsc(SysDictData::getCreateTime)
            .orderByAsc(SysDictData::getDictType)
            .eq(StrUtil.isNotBlank(p.getDictType()), SysDictData::getDictType, p.getDictType())
            .eq(StrUtil.isNotBlank(p.getDisableFlag()), SysDictData::getDisableFlag, p.getDisableFlag())
            .eq(StrUtil.isNotBlank(p.getDictType()), SysDictData::getDictType, p.getDictType())
            .eq(StrUtil.isNotBlank(p.getDictValue()), SysDictData::getDictValue, p.getDictValue())
            .like(StrUtil.isNotBlank(p.getDictLabel()), SysDictData::getDictLabel, p.getDictLabel());
        return JsonResult.ok(service.list(qw));
    }

    @GetMapping("/type/{type}")
    public JsonResult<List<SysDictData>> listByType(@PathVariable String type) {
        List<SysDictData> list = service.list(Wrappers.lambdaQuery(SysDictData.class)
            .orderByAsc(SysDictData::getCreateTime)
            .eq(SysDictData::getDisableFlag, StrConstants.CHAR_FALSE)
            .eq(SysDictData::getDictType, type));
        return JsonResult.ok(list);
    }

    @GetMapping({"/type/select/{type}", "/type/select/"})
    public JsonResult<List<SysDictDataSelectVO>> listSelectByType(@PathVariable(required = false) String type) {
        if (StrUtil.isBlank(type)) {
            return JsonResult.ok();
        }
        List<SysDictDataSelectVO> collect = service.list(Wrappers.lambdaQuery(SysDictData.class)
                .orderByAsc(SysDictData::getCreateTime)
                .eq(SysDictData::getDisableFlag, StrConstants.CHAR_FALSE)
                .eq(SysDictData::getDictType, type))
            .stream().map(r -> new SysDictDataSelectVO().setLabel(r.getDictLabel()).setValue(r.getDictValue())).collect(Collectors.toList());
        return JsonResult.ok(collect);
    }

    /**
     * 详情
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping({"/{id}", "/detail/{id}"})
    public JsonResult<SysDictData> detail(@PathVariable String id) {
        return JsonResult.ok(service.getById(id));
    }

    /**
     * 添加
     */
    @SystemLog(name = SystemLogConstants.INSERT)
    @PermCheck(PERM_PREFIX + ADD)
    @PutMapping({"", "/add"})
    public JsonResult add(@Validated @RequestBody SysDictData p) {
        Assert.isTrue(service.checkTypeExist(p), "不存在类型编码：{}", p.getDictType());
        Assert.isFalse(service.checkCodeExist(p), "已存在该编码：{}", p.getDictType());
        service.save(p);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @SystemLog(name = SystemLogConstants.UPDATE)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping({"", "/edit"})
    public JsonResult edit(@Validated @RequestBody SysDictData p) {
        Assert.notBlank(p.getId(), "id不能为空");
        Assert.isTrue(service.checkTypeExist(p), "不存在类型编码：{}", p.getDictType());
        Assert.isFalse(service.checkCodeExist(p), "已存在编码：{}", p.getDictType());
        service.updateById(p);
        return JsonResult.ok();
    }

    /**
     * 修改状态
     */
    @SystemLog(name = SystemLogConstants.CHANGE_STATUS)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping("/changeStatus")
    public JsonResult changeStatus(@RequestBody SysDictData p) {
        Assert.notBlank(p.getId(), "id不能为空");
        Assert.notBlank(p.getDisableFlag(), "状态不能为空");
        SysDictData sdd = new SysDictData().setId(p.getId()).setDisableFlag(p.getDisableFlag());
        service.updateById(sdd);
        return JsonResult.ok();
    }

    /**
     * 刷新缓存
     */
    @SystemLog(name = "刷新缓存")
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping("/refresh-cache")
    public JsonResult refreshCache() {
        service.refreshCache();
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

