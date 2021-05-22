package cn.ussu.modules.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.constants.SwaggerConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.core.exception.RequestEmptyException;
import cn.ussu.modules.system.entity.SysDept;
import cn.ussu.modules.system.entity.SysMenu;
import cn.ussu.modules.system.entity.SysRole;
import cn.ussu.modules.system.entity.SysUserRole;
import cn.ussu.modules.system.model.param.SysRoleParam;
import cn.ussu.modules.system.parser.SysRoleMenuNodeParser;
import cn.ussu.modules.system.service.ISysDeptService;
import cn.ussu.modules.system.service.ISysRoleService;
import cn.ussu.modules.system.service.ISysUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
@Api(value = "角色管理")
@RestController
@RequestMapping("/sys-role")
// @InsertLog(code = "sys-role", name = "角色管理")
public class SysRoleController extends BaseAdminController {

    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysDeptService sysDeptService;
    // @Autowired
    // private ISysRoleDeptService sysRoleDeptService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;

    /**
     * all
     */
    @GetMapping("/all")
    public Object all() {
        return JsonResult.ok().data(sysRoleService.list(new LambdaQueryWrapper<SysRole>().orderByAsc(SysRole::getRoleSort)));
    }

    /**
     * 分页
     */
    @ApiOperation(value = SwaggerConstants.list)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(name = "limit", value = "分页大小", required = true, dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
            , @ApiImplicitParam(value = "查询参数", dataTypeClass = String.class, paramType = SwaggerConstants.paramType_form)
    })
    @PreAuthorize("@pc.check('sys-role:select')")
    @GetMapping
    public Object list(SysRoleParam param) {
        return JsonResult.ok().data(sysRoleService.findPage(param));
    }

    /**
     * 新增
     */
    @ApiOperation(value = SwaggerConstants.add)
    @PutMapping
    @PreAuthorize("@pc.check('sys-role:edit')")
    public Object add(@ApiParam(name = "obj", value = "将请求的参数封装为一个 SysRole 对象") @RequestBody SysRole obj) {
        sysRoleService.addOne(obj);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = SwaggerConstants.edit)
    @PostMapping
    @PreAuthorize("@pc.check('sys-role:edit')")
    public Object edit(@ApiParam @RequestBody SysRole obj) {
        checkReqParamThrowException(obj.getId());
        sysRoleService.updateOne(obj);
        return JsonResult.ok();
    }

    /**
     * 修改状态
     */
    @PostMapping("/changeStatus")
    public Object changeStatus(@RequestBody SysRole sysRole) {
        checkReqParamThrowException(sysRole.getId());
        checkReqParamThrowException(sysRole.getStatus());
        new SysRole().setId(sysRole.getId()).setStatus(sysRole.getStatus()).updateById();
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = SwaggerConstants.delete, notes = "删除角色")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pc.check('sys-role:delete')")
    public Object delete(@PathVariable("id") @ApiParam(name = "id", value = SwaggerConstants.paramDesc_delete, required = true) String id) {
        List<String> idList = splitCommaList(id, true);
        sysRoleService.removeByIds(idList);
        return JsonResult.ok();
    }

    /**
     * 校验角色code是否重复
     */
    @PostMapping("/checkRoleCode")
    @PreAuthorize("@pc.check('sys-role:edit')")
    public Object chechRepeatRoleCode(SysRole role) {
        QueryWrapper<SysRole> qw = new QueryWrapper<>();
        qw.eq("role_code", role.getRoleCode()).ne(StrUtil.isNotBlank(role.getId()), "id", role.getId());
        int count = this.sysRoleService.count(qw);
        if (count > 0) return JsonResult.error("角色代码已存在");
        return JsonResult.ok();
    }

    /**
     * 获取指定角色的权限
     */
    @ApiOperation(value = "根据角色获取该角色的权限")
    @GetMapping("/perms/{id}")
    public Object listPermsByRoleId(@PathVariable("id") @ApiParam(name = "id", value = "角色id", type = SwaggerConstants.paramType_path, required = true) String roleId) {
        checkReqParamThrowException(roleId);
        List<SysMenu> list = sysRoleService.findPermsByRoleId(roleId);
        List<Object> mapList = list.stream().map(m -> {
            m.setIcon(null);
            m.setComponent(null);
            Map<String, Object> map = BeanUtil.beanToMap(m);
            map.put("open", m.getChecked() == null ? false : true);
            return map;
        }).collect(Collectors.toList());
        TreeNodeConfig tnc = TreeNodeConfig.DEFAULT_CONFIG.setChildrenKey("children");
        List<Tree<String>> treeList = TreeUtil.build(list, "0", tnc, new SysRoleMenuNodeParser());
        Map<String, Object> map = getNewHashMap();
        map.put("listData", mapList);  //简单数据
        map.put("treeData", treeList);  //树形结构
        return JsonResult.ok().data(map);
    }

    /**
     * 保存角色权限
     */
    @ApiOperation(value = "保存角色权限")
    @PreAuthorize("@pc.check('sys-role:perms')")
    @PostMapping("/perms/{id}")
    public Object savePerms(@PathVariable("id") @ApiParam(name = "id", value = "角色id", required = true) String roleId,
                            @RequestParam(value = "menus", required = false) @ApiParam(name = "menus", value = "以逗号分隔的权限id", allowEmptyValue = true) String menus) {
        if (StrUtil.isBlank(roleId)) throw new RequestEmptyException();
        sysRoleService.updateRolePerms(roleId, menus);
        return JsonResult.ok();
    }

    /**
     * 获取指定角色的数据权限
     */
    @ApiOperation(value = "根据角色获取该角色的自定义数据权限")
    @PreAuthorize("@pc.check('sys-role:select')")
    @GetMapping("/datascope/{id}")
    public Object listCustomDataScopeByRoleId(@PathVariable("id") @ApiParam(name = "id", value = "角色id", type = SwaggerConstants.paramType_path, required = true) String roleId) {
        checkReqParamThrowException(roleId);
        // 部门
        List<SysDept> deptList = sysDeptService.list();
        // 已有
        // List<SysRoleDept> roleDeptList = sysRoleDeptService.list(new QueryWrapper<SysRoleDept>().eq("role_id", roleId));
        Map<String, Object> map = getNewHashMap();
        map.put("deptList", deptList);
        // map.put("roleDeptList", roleDeptList);
        return JsonResult.ok().data(map);
    }

    /**
     * 保存角色数据权限
     */
    @ApiOperation(value = "保存角色数据权限")
    @PreAuthorize("@pc.check('sys-role:datascope')")
    @PostMapping("/datascope")
    public Object savePerms(SysRole sysRole, @RequestParam(value = "depts", required = false) @ApiParam(name = "depts", value = "以逗号分隔的权限id", allowEmptyValue = true) String depts) {
        checkReqParamThrowException(sysRole.getId());
        boolean b = sysRoleService.updateRoleDataScopes(sysRole, depts);
        if (!b) return JsonResult.error();
        return JsonResult.ok();
    }

    /**
     * 获取权限范围内所有角色
     */
    @GetMapping("/listAll")
    public Object listAll(@RequestParam Map param) {
        List<SysRole> list = this.sysRoleService.findAll(param);
        return JsonResult.ok().data(list);
    }

    /**
     * 获取用户已有角色
     */
    @GetMapping("/getRoleIdsByUserId")
    public Object getRoleIdsByUserId(String userId) {
        checkReqParamThrowException(userId);
        List<SysUserRole> list = sysUserRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        return JsonResult.ok().data(CollectionUtil.getFieldValues(list, "roleId"));
    }

}
