package cn.ussu.modules.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.constants.StrConstants;
import cn.ussu.common.core.constants.SwaggerConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.core.exception.RequestEmptyException;
import cn.ussu.common.security.annotation.PermCheck;
import cn.ussu.modules.system.entity.SysArea;
import cn.ussu.modules.system.model.param.SysAreaParam;
import cn.ussu.modules.system.service.ISysAreaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 省市区区域表 控制器
 * </p>
 *
 * @author liming
 * @since 2020-05-17
 */
@RestController
@RequestMapping("/sys-area")
// @InsertLog(code = "sys-area", name = "区域管理")
public class SysAreaController extends BaseAdminController {

    @Autowired
    private ISysAreaService service;

    /*
     * 分页查询
     */
    @ApiOperation(value = SwaggerConstants.list)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(name = "limit", value = "分页大小", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(name = "map", value = "查询参数", dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
    })
    @PermCheck("system:area:select")
    @GetMapping
    public Object list(SysAreaParam param) {
        return service.findPage(param);
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    @ApiOperation(value = SwaggerConstants.all)
    @PermCheck("system:area:select")
    @GetMapping("/all")
    public Object listAll() {
        QueryWrapper<SysArea> qw = new QueryWrapper<>();
        qw.orderByAsc(StrConstants.id, "pinyin");
        List<SysArea> list = service.list(qw);
        return JsonResult.ok().data(list);
    }

    /**
     * 获取指定节点下的节点，省下面的所有市
     */
    @ApiOperation(value = "获取指定节点下的节点")
    @PermCheck("system:area:select")
    @GetMapping("/{parentId}")
    public Object listAllByParentId(@PathVariable("parentId") @ApiParam(name = "parentId", value = "上级节点id") Integer pid) {
        if (pid == null) throw new RequestEmptyException();
        // List<Map<String, Object>> list = service.findCascaderByParendId(pid);
        List<?> list = service.findCascaderByParendId(pid);
        return JsonResult.ok().data(list);
    }

    /**
     * 搜索接口，返回的mername
     */
    @ApiOperation(value = "搜索接口，返回的是mername")
    @PermCheck("system:area:select")
    @GetMapping("/query/{name}")
    public Object findCascader(@PathVariable("name") @ApiParam(name = "name", value = "要搜索的名称", required = true, type = SwaggerConstants.paramType_path) String queryMerName) {
        QueryWrapper<SysArea> qw = new QueryWrapper<>();
        qw.like("mername", queryMerName).orderByAsc("pinyin");
        List<SysArea> list = service.list(qw);
        List<Map> collect = list.stream().map(a -> {
            Map<String, Object> m = BeanUtil.beanToMap(a);
            m.put(StrConstants.value, a.getPath());
            m.put("label", a.getMername());
            return m;
        }).collect(Collectors.toList());
        return JsonResult.ok().data(collect);
    }

    /**
     * 新增
     */
    @ApiOperation(value = SwaggerConstants.add)
    @PutMapping
    @PermCheck("system:area:add")
    public Object add(@ApiParam SysArea obj) {
        obj.insert();
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = SwaggerConstants.edit)
    @PostMapping
    @PermCheck("system:area:edit")
    public Object edit(@ApiParam SysArea obj) {
        if (0 == obj.getId()) throw new RequestEmptyException();
        // if (StrUtil.isBlank(obj.getId())) throw new RequestEmptyException();
        obj.updateById();
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = SwaggerConstants.delete)
    @DeleteMapping("/{id}")
    @PermCheck("system:area:delete")
    public Object delete(@PathVariable("id") @ApiParam(name = "id", value = "要删除的主键id", required = true, type = SwaggerConstants.paramType_path) String id) {
        List<String> idList = splitCommaList(id, true);
        service.removeByIds(idList);
        return JsonResult.ok();
    }

}
