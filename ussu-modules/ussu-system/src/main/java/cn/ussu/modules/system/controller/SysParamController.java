package cn.ussu.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.constants.SwaggerConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.core.exception.RequestEmptyException;
import cn.ussu.common.log.annotation.InsertLog;
import cn.ussu.common.security.annotation.PermCheck;
import cn.ussu.modules.system.entity.SysParam;
import cn.ussu.modules.system.model.param.SysParamParam;
import cn.ussu.modules.system.service.ISysParamService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统参数表 前端控制器
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
@RestController
@RequestMapping("/sys-param")
public class SysParamController extends BaseAdminController {

    @Autowired
    private ISysParamService service;

    /**
     * 分页查询
     */
    @ApiOperation(value = SwaggerConstants.list)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(name = "limit", value = "分页大小", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(value = "查询参数", dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
    })
    @PermCheck("system:param:select")
    @GetMapping
    public JsonResult list(SysParamParam sysParamParam) {
        return JsonResult.ok().data(service.findPage(sysParamParam));
    }

    /**
     * 新增
     */
    @ApiOperation(value = SwaggerConstants.add)
    @InsertLog("新增系统参数")
    @PermCheck("system:param:add")
    @PutMapping
    public Object add(@ApiParam @RequestBody SysParam sysParam) {
        sysParam.insert();
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = SwaggerConstants.edit)
    @InsertLog("修改系统参数")
    @PermCheck("system:param:edit")
    @PostMapping
    public Object edit(@ApiParam @RequestBody SysParam sysParam) {
        if (StrUtil.isBlank(sysParam.getId())) throw new RequestEmptyException();
        sysParam.updateById();
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = SwaggerConstants.delete)
    @InsertLog("删除系统参数")
    @PermCheck("system:param:delete")
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") @ApiParam(name = "id", value = SwaggerConstants.paramDesc_delete, required = true, type = SwaggerConstants.paramType_path) String id) {
        List<String> idList = splitCommaList(id, true);
        service.removeByIds(idList);
        return JsonResult.ok();
    }

}
