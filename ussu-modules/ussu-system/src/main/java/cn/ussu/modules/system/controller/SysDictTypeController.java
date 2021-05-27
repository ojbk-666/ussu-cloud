package cn.ussu.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.constants.SwaggerConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.core.exception.RequestEmptyException;
import cn.ussu.common.security.annotation.PermCheck;
import cn.ussu.modules.system.entity.SysDict;
import cn.ussu.modules.system.entity.SysDictType;
import cn.ussu.modules.system.model.param.SysDictTypeParam;
import cn.ussu.modules.system.service.ISysDictService;
import cn.ussu.modules.system.service.ISysDictTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 字典类型表 前端控制器
 * </p>
 *
 * @author liming
 * @since 2020-05-07
 */
@RestController
@RequestMapping("/sys-dict-type")
// @InsertLog(code = "sys-dict", name = "数据字典")
public class SysDictTypeController extends BaseAdminController {

    @Autowired
    private ISysDictTypeService sysDictTypeService;
    @Autowired
    private ISysDictService sysDictService;

    @ApiOperation(value = SwaggerConstants.list)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(name = "limit", value = "分页大小", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(value = "查询参数", dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
    })
    @PermCheck("system:dicttype:select")
    @GetMapping
    public Object list(SysDictTypeParam param) {
        return JsonResult.ok().data(sysDictTypeService.findPage(param));
    }

    /**
     * 获取所有
     */
    @GetMapping("/all")
    @PermCheck("system:dicttype:select")
    public Object all() {
        return JsonResult.ok().data(sysDictTypeService.list());
    }

    /**
     * 获取单个详情
     */
    @GetMapping("/{id}")
    @PermCheck("system:dicttype:select")
    public Object getOne(@PathVariable String id) {
        return JsonResult.ok().data(new SysDictType().setId(id).selectById());
    }

    /**
     * 新增
     */
    @ApiOperation(value = SwaggerConstants.add)
    @PutMapping
    @PermCheck("system:dicttype:edit")
    public Object add(@ApiParam @RequestBody SysDictType obj) {
        LambdaQueryWrapper<SysDictType> qw = new LambdaQueryWrapper<>();
        if (StrUtil.isBlank(obj.getTypeCode())) {
            throw new RequestEmptyException("类型代码不能为空");
        }
        qw.eq(SysDictType::getTypeCode, obj.getTypeCode());
        int count = sysDictTypeService.count(qw);
        if (count > 0) {
            return JsonResult.error("类型代码不能重复");
        }
        obj.insert();
        // DictUtil.addDictType(obj.getTypeCode());
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = SwaggerConstants.edit)
    @PermCheck("system:dicttype:edit")
    @PostMapping
    public Object edit(@ApiParam @RequestBody SysDictType obj) {
        checkReqParamThrowException(obj.getId());
        if (StrUtil.isBlank(obj.getTypeCode())) {
            throw new RequestEmptyException("类型代码不能为空");
        }
        LambdaQueryWrapper<SysDictType> qw = new LambdaQueryWrapper<>();
        qw.eq(SysDictType::getTypeCode, obj.getTypeCode()).ne(SysDictType::getId, obj.getId());
        int count = sysDictTypeService.count(qw);
        if (count > 0) {
            return JsonResult.error("类型代码不能重复");
        }
        SysDictType old = new SysDictType().setId(obj.getId()).selectById();
        obj.updateById();
        // 更新所有 typecode
        SysDict sysDict = new SysDict().setTypeCode(obj.getTypeCode());
        QueryWrapper<SysDict> qw1 = new QueryWrapper<>();
        qw1.eq("type_id", obj.getId());
        sysDict.update(qw1);
        // DictUtil.refreshDictType(old.getTypeCode(), obj.getTypeCode());
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = SwaggerConstants.delete)
    @PermCheck("system:dicttype:delete")
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") @ApiParam(name = "id", value = SwaggerConstants.paramDesc_delete, required = true, type = SwaggerConstants.paramType_path) String id) {
        sysDictTypeService.deleteDictTypeAndData(id);
        return JsonResult.ok();
    }

}
