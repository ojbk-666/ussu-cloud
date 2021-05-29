package cn.ussu.modules.system.controller;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.constants.SwaggerConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.log.annotation.InsertLog;
import cn.ussu.common.security.annotation.PermCheck;
import cn.ussu.modules.system.entity.SysDept;
import cn.ussu.modules.system.model.param.SysDeptParam;
import cn.ussu.modules.system.parser.SysDeptTreeSelectParser;
import cn.ussu.modules.system.service.ISysDeptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门 控制器
 * </p>
 *
 * @author liming
 * @since 2020-05-16
 */
@RestController
@RequestMapping("/sys-dept")
public class SysDeptController extends BaseAdminController {

    @Autowired
    private ISysDeptService service;

    /**
     * 获取所有，不构造树结构
     */
    @ApiOperation(value = "获取所有部门，不构造树结构")
    @PermCheck("system:dept:select")
    @GetMapping("/all")
    public Object listAll(String name) {
        LambdaQueryWrapper<SysDept> qw = new LambdaQueryWrapper<>();
        qw.like(StrUtil.isNotBlank(name), SysDept::getName, name);
        qw.orderByAsc(SysDept::getParentId, SysDept::getSort, SysDept::getCreateTime);
        List<SysDept> list = service.list(qw);
        return JsonResult.ok().data(list);
    }

    /**
     * 获取所有 不包含指定
     */
    @PermCheck("system:dept:select")
    @GetMapping("/all/exclude")
    public Object listAllExclude(String ids) {
        if (ids == null) {
            ids = StrUtil.EMPTY;
        }
        List<String> idList = splitCommaList(ids, false);
        LambdaQueryWrapper<SysDept> qw = new LambdaQueryWrapper<>();
        for (String id : idList) {
            qw.notLike(SysDept::getPath, StrUtil.SLASH + id);
        }
        qw.orderByAsc(SysDept::getParentId, SysDept::getSort, SysDept::getCreateTime);
        List<SysDept> list = service.list(qw);
        // List<SysDept> result = list.stream().filter(item -> idList.contains(item.getId())).collect(Collectors.toList());
        return JsonResult.ok().data(list);
    }

    /**
     * treeselect
     */
    @PermCheck("system:dept:select")
    @GetMapping("/treeselect")
    public Object treeselect() {
        List<SysDept> list = service.list();
        List<Tree<String>> result = TreeUtil.build(list, "0", new SysDeptTreeSelectParser());
        return JsonResult.ok().data(result);
    }

    /**
     * 获取某个部门的直接子部门
     *
     * @param pid 上级部门
     * @return
     */
    @ApiOperation(value = "获取某个部门的直接子部门")
    @PermCheck("system:dept:select")
    @GetMapping("/sub/{pid}")
    public Object getSubDeptByPId(@PathVariable("pid") @ApiParam(name = "pid", value = "上级id", required = true, type = SwaggerConstants.paramType_path) String pid) {
        List<Map> list = service.findSubDeptByParentId(pid);
        return JsonResult.ok().data(list);
    }

    @PermCheck("system:dept:select")
    @GetMapping("/sub")
    public Object getSubDept(String parentId) {
        // if (StrUtil.isBlank(parentId)) parentId = StrConstants.NUM_0;
        if (StrUtil.isBlank(parentId)) {
            // parentId = getUser().getDeptId();
            // if (SecurityUtils.isSuperAdmin()) parentId = StrConstants.NUM_0;
        }
        List<Map> list = service.findSubDeptByParentId(parentId);
        return JsonResult.ok().data(list);
    }

    /*
     * 分页查询
     */
    @ApiOperation(value = SwaggerConstants.list)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(name = "limit", value = "分页大小", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(value = "查询参数", dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
    })
    @PermCheck("system:dept:select")
    @GetMapping
    public Object list(SysDeptParam param) {
        return JsonResult.ok().data(service.findPage(param));
    }

    /**
     * 新增
     */
    @ApiOperation(value = SwaggerConstants.add)
    @InsertLog("新增部门")
    @PutMapping
    @PermCheck("system:dept:add")
    public Object add(@ApiParam @RequestBody SysDept obj) {
        service.addOne(obj);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = SwaggerConstants.edit)
    @InsertLog("修改部门")
    @PostMapping
    @PermCheck("system:dept:edit")
    public Object edit(@ApiParam @RequestBody SysDept obj) {
        checkReqParamThrowException(obj.getId());
        this.service.updateOne(obj);
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = SwaggerConstants.delete)
    @InsertLog("删除部门")
    @DeleteMapping("/{id}")
    @PermCheck("system:dept:delete")
    public Object delete(@PathVariable("id") @ApiParam(name = "id", value = SwaggerConstants.paramDesc_delete, required = true, type = SwaggerConstants.paramType_path) String id) {
        List<String> idList = splitCommaList(id, true);
        int deleted = service.deleteMany(idList);
        return JsonResult.ok().data(deleted);
    }

}
