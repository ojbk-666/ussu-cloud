package cc.ussu.modules.system.controller;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.constants.StrConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.constants.SystemLogConstants;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.system.entity.SysMenu;
import cc.ussu.modules.system.entity.SysRole;
import cc.ussu.modules.system.entity.SysRoleMenu;
import cc.ussu.modules.system.entity.SysUserRole;
import cc.ussu.modules.system.entity.vo.RoleAuthUserVO;
import cc.ussu.modules.system.service.ISysMenuService;
import cc.ussu.modules.system.service.ISysRoleMenuService;
import cc.ussu.modules.system.service.ISysRoleService;
import cc.ussu.modules.system.service.ISysUserRoleService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@SystemLog(serviceName = ServiceNameConstants.SERVICE_SYSETM, group = "角色管理")
@RestController
@RequestMapping("${ussu.mapping-prefix.system}/sys-role")
public class SysRoleController extends BaseController {

    public static final String PERM_PREFIX = "system:role:";
    @Autowired
    private ISysRoleService service;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    /**
     * 分页
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/page")
    public JsonResult page(SysRole p) {
        LambdaQueryWrapper<SysRole> qw = Wrappers.lambdaQuery(SysRole.class)
                .eq(StrUtil.isNotBlank(p.getDisableFlag()), SysRole::getDisableFlag, p.getDisableFlag())
                .like(StrUtil.isNotBlank(p.getRoleName()), SysRole::getRoleName, p.getRoleName())
                .like(StrUtil.isNotBlank(p.getRoleCode()), SysRole::getRoleCode, p.getRoleCode());
        Page page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 单个详情
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/{id}")
    public JsonResult<SysRole> detail(@PathVariable String id) {
        return JsonResult.ok(service.getById(id));
    }

    /**
     * 修改状态
     */
    @SystemLog(name = SystemLogConstants.CHANGE_STATUS)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping("/changeStatus")
    public JsonResult changeStatus(@RequestBody SysRole p) {
        Assert.notBlank(p.getId(), "id不能为空");
        Assert.notBlank(p.getDisableFlag(), "状态不能为空");
        service.updateById(new SysRole().setId(p.getId()).setDisableFlag(p.getDisableFlag()));
        return JsonResult.ok();
    }

    /**
     * 取消授权用户
     */
    @SystemLog(name = "取消授权用户")
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping("/authUser/cancel")
    public JsonResult cancelAuthUser(@RequestBody RoleAuthUserVO p) {
        String roleId = p.getRoleId();
        Assert.notBlank(roleId, "角色id不能为空");
        Set<String> userIds = p.getUserIds();
        Assert.notEmpty(userIds, "userIds不能为空");
        sysUserRoleService.remove(Wrappers.lambdaQuery(SysUserRole.class).eq(SysUserRole::getRoleId, roleId).in(SysUserRole::getUserId, userIds));
        return JsonResult.ok();
    }

    /**
     * 授权用户
     */
    @SystemLog(name = "授权用户")
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping("/authUser/confirm")
    public JsonResult confirmAuthUser(@RequestBody RoleAuthUserVO p) {
        String roleId = p.getRoleId();
        Assert.notBlank(roleId, "角色id不能为空");
        Set<String> userIds = p.getUserIds();
        Assert.notEmpty(userIds, "userIds不能为空");
        List<SysUserRole> userRoleList = userIds.stream().map(uid -> new SysUserRole().setRoleId(roleId).setUserId(uid)).collect(Collectors.toList());
        sysUserRoleService.saveBatch(userRoleList);
        return JsonResult.ok();
    }

    /**
     * 角色关联的菜单
     */
    @GetMapping("/role-menu/{id}")
    public JsonResult<Map<String, Object>> getMenuList(@PathVariable String id) {
        // 获取所有未禁用菜单
        SysRole role = service.getById(id);
        Assert.notNull(role, "角色不存在");
        Set<String> menuIdList = sysRoleMenuService.list(Wrappers.lambdaQuery(SysRoleMenu.class).eq(SysRoleMenu::getRoleId, role.getId()))
                .stream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());
        List<SysMenu> menuList = sysMenuService.list(Wrappers.lambdaQuery(SysMenu.class)
                .orderByAsc(SysMenu::getType).orderByAsc(SysMenu::getSort).eq(SysMenu::getDisableFlag, StrConstants.CHAR_FALSE));
        Map<String, Object> m = new HashMap<>();
        List<Map<String, Object>> menuListChecked = menuList.stream().map(menu -> {
            Map<String, Object> map = BeanUtil.beanToMap(menu);
            map.put("checked", CollUtil.contains(menuIdList, menu.getId()));
            return map;
        }).collect(Collectors.toList());
        m.put("menuList", menuList);    //所有菜单
        m.put("menuListChecked", menuListChecked);  //带有选中字段的所有菜单
        m.put("menuIdListChecked", menuIdList); //选中的菜单id集合
        return JsonResult.ok(m);
    }

    /**
     * 授权菜单
     */
    @SystemLog(name = "授权菜单")
    @PermCheck(PERM_PREFIX + "menuIds")
    @PutMapping("/role-menu/{roleId}")
    public JsonResult editRoleMenu(@PathVariable String roleId, @RequestBody Set<String> menuIdList) {
        sysRoleMenuService.remove(Wrappers.lambdaQuery(SysRoleMenu.class).eq(SysRoleMenu::getRoleId, roleId));
        if (CollUtil.isNotEmpty(menuIdList)) {
            sysRoleMenuService.saveBatch(menuIdList.stream().map(menuId -> new SysRoleMenu().setRoleId(roleId).setMenuId(menuId)).collect(Collectors.toList()));
        }
        return JsonResult.ok();
    }

    /**
     * 新增
     */
    @SystemLog(name = SystemLogConstants.INSERT)
    @PermCheck(PERM_PREFIX + ADD)
    @PutMapping({"", "/add"})
    public JsonResult add(@RequestBody SysRole p) {
        service.add(p);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @SystemLog(name = SystemLogConstants.UPDATE)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping({"", "/edit"})
    public JsonResult edit(@RequestBody SysRole p) {
        service.edit(p);
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
        sysUserRoleService.deleteByRoleIds(idList);
        return JsonResult.ok();
    }

}

