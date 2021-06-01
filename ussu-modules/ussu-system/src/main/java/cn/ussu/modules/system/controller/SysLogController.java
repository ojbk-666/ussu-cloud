package cn.ussu.modules.system.controller;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.constants.StrConstants;
import cn.ussu.common.core.constants.SwaggerConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.core.exception.RequestEmptyException;
import cn.ussu.common.security.annotation.PermCheck;
import cn.ussu.modules.system.entity.SysLog;
import cn.ussu.modules.system.model.param.SysLogParam;
import cn.ussu.modules.system.service.ISysLogService;
import cn.ussu.modules.system.third.amap.GaodeIpLocationResponse;
import cn.ussu.modules.system.third.amap.GaodeIpLocationService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
@RestController
@RequestMapping("/sys-log")
public class SysLogController extends BaseAdminController {

    @Autowired
    private ISysLogService sysLogService;
    @Autowired
    private GaodeIpLocationService gaodeIpLocationService;

    @ApiOperation(value = SwaggerConstants.list)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(name = "limit", value = "分页大小", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(value = "查询参数", dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
    })
    @PermCheck("system:log:select")
    @GetMapping
    public Object list(SysLogParam param) {
        return JsonResult.ok().data(sysLogService.findPage(param));
    }

    /**
     * 写入日志
     */
    @PutMapping(StrConstants.SLASH_remote)
    public JsonResult recordLog(@RequestBody SysLog sysLog) {
        if (StrUtil.isBlank(sysLog.getId())) {
            sysLog.setId(IdWorker.getIdStr());
        }
        if (StrUtil.isNotBlank(sysLog.getRemoteIp()) && !NetUtil.isInnerIP(sysLog.getRemoteIp())) {
            GaodeIpLocationResponse gaodeIpLocationResponse = gaodeIpLocationService.getLocation(sysLog.getRemoteIp());
            if (gaodeIpLocationResponse.success() && !"[]".equals(gaodeIpLocationResponse.getAdcode())) {
                sysLog.setLocationAdcode(gaodeIpLocationResponse.getAdcode())
                        .setLocationStr(gaodeIpLocationResponse.getProvince() + gaodeIpLocationResponse.getCity());
            }
        }
        sysLog.insert();
        return JsonResult.ok();
    }

    @ApiOperation(value = SwaggerConstants.detail)
    @GetMapping("/{id}")
    public Object detail(@PathVariable("id") String id) {
        if (StrUtil.isBlank(id)) throw new RequestEmptyException();
        SysLog byId = sysLogService.getById(id);
        return JsonResult.ok().data(byId);
    }

    @DeleteMapping("/{id}")
    @PermCheck("@pc.check('sys-log:delete')")
    public Object delete(@PathVariable("id") @ApiParam(name = "id", value = SwaggerConstants.paramDesc_delete, required = true, type = SwaggerConstants.paramType_path) String id) {
        if (StrUtil.isBlank(id)) throw new RequestEmptyException();
        boolean b = this.sysLogService.removeById(id);
        return JsonResult.ok();
    }

}
