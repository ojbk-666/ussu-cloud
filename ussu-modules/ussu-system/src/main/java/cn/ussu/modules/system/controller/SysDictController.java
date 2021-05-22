package cn.ussu.modules.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.constants.SwaggerConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.modules.system.core.util.DictUtil;
import cn.ussu.modules.system.entity.SysDict;
import cn.ussu.modules.system.entity.SysDictType;
import cn.ussu.modules.system.model.param.SysDictParam;
import cn.ussu.modules.system.service.ISysDictService;
import cn.ussu.modules.system.service.ISysDictTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author liming
 * @since 2020-05-07
 */
@RestController
@RequestMapping("/sys-dict")
// @InsertLog(code = "sys-dict", name = "数据字典")
public class SysDictController extends BaseAdminController {

    @Autowired
    private ISysDictService service;
    @Autowired
    private ISysDictTypeService sysDictTypeService;

    @ApiOperation(value = SwaggerConstants.list)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(name = "limit", value = "分页大小", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(value = "查询参数", dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
    })
    @PreAuthorize("@pc.check('sys-dict:select')")
    @GetMapping
    public Object list(SysDictParam param) {
        return JsonResult.ok().data(service.findPage(param));
    }

    /**
     * 根据类型代码获取字典列表
     */
    @GetMapping("/typecode/{typeCode}")
    public Object getListByTypeCode(@PathVariable String typeCode) {
        LambdaQueryWrapper<SysDict> qw = new LambdaQueryWrapper<>();
        qw.eq(SysDict::getTypeCode, typeCode).orderByAsc(SysDict::getDictSort, SysDict::getDictValue);
        List<SysDict> list = service.list(qw);
        List<Map<String, Object>> result = new LinkedList<>();
        for (SysDict sysDict : list) {
            Map<String, Object> item = BeanUtil.beanToMap(sysDict);
            // 尝试转int
            Integer dictValue = MapUtil.getInt(item, "dictValue");
            if (dictValue != null) {
                item.put("dictValue", dictValue);
            }
            result.add(item);
        }
        return JsonResult.ok().data(result);
    }

    /**
     * 新增
     */
    @ApiOperation(value = SwaggerConstants.add)
    @PutMapping
    @PreAuthorize("@pc.check('sys-dict:add')")
    public Object add(@ApiParam @RequestBody SysDict obj) {
        checkReqParamThrowException(obj.getTypeCode());
        // 重复检查
        LambdaQueryWrapper<SysDict> qw = new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getDictValue, obj.getDictValue())
                .eq(SysDict::getTypeCode, obj.getTypeCode());
        int count = service.count(qw);
        if (count > 0) {
            return JsonResult.error("字典值已存在");
        }
        // 获取类型
        SysDictType type = sysDictTypeService.getOne(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getTypeCode, obj.getTypeCode()), true);
        obj.setTypeId(type.getId()).insert();
        // DictUtil.addDict(obj);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = SwaggerConstants.edit)
    @PostMapping
    @PreAuthorize("@pc.check('sys-dict:edit')")
    public Object edit(@ApiParam @RequestBody SysDict obj) {
        checkReqParamThrowException(obj.getId());
        // 重复检查
        LambdaQueryWrapper<SysDict> qw = new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getDictValue, obj.getDictValue())
                .eq(SysDict::getTypeCode, obj.getTypeCode())
                .ne(SysDict::getId, obj.getId());
        int count = service.count(qw);
        if (count > 0) {
            return JsonResult.error("字典值已存在");
        }
        SysDict old = new SysDict().setId(obj.getId()).selectById();
        obj.setTypeCode(null).setTypeId(null).updateById();
        // DictUtil.updateDict(obj, old.getDictValue());
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = SwaggerConstants.delete)
    @DeleteMapping("/{id}")
    @PreAuthorize("@pc.check('sys-dict:delete')")
    public Object delete(@PathVariable("id") @ApiParam(name = "id", value = SwaggerConstants.paramDesc_delete, required = true, type = SwaggerConstants.paramType_path) String id) {
        List<String> idList = splitCommaList(id, true);
        Collection<SysDict> oldList = service.listByIds(idList);
        service.removeByIds(idList);
        for (SysDict old : oldList) {
            DictUtil.deleteDict(old);
        }
        return JsonResult.ok();
    }

}
