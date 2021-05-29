package cn.ussu.modules.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.constants.StrConstants;
import cn.ussu.common.core.constants.SwaggerConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.core.exception.RequestEmptyException;
import cn.ussu.common.log.annotation.InsertLog;
import cn.ussu.common.security.annotation.PermCheck;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.system.core.util.DictUtil;
import cn.ussu.modules.system.entity.SysMenu;
import cn.ussu.modules.system.entity.SysRoleMenu;
import cn.ussu.modules.system.model.param.SysMenuParam;
import cn.ussu.modules.system.parser.SysMenuNodeParser;
import cn.ussu.modules.system.parser.SysMenuTreeSelectParser;
import cn.ussu.modules.system.service.ISysMenuService;
import cn.ussu.modules.system.service.ISysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单 前端控制器
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
@RestController
@RequestMapping("/sys-menu")
public class SysMenuController extends BaseAdminController {

    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    /**
     * 获取所有菜单，不分页
     */
    @ApiOperation(value = SwaggerConstants.all)
    @PermCheck("system:menu:select")
    @GetMapping(StrConstants.all)
    public Object listAll(SysMenuParam param) {
        LambdaQueryWrapper<SysMenu> qw = new LambdaQueryWrapper<>();
        qw.like(StrUtil.isNotBlank(param.getName()), SysMenu::getName, param.getName())
                .eq(param.getStatus() != null, SysMenu::getStatus, param.getStatus())
                .orderByAsc(SysMenu::getType, SysMenu::getSort, SysMenu::getName);
        List<SysMenu> list = sysMenuService.list(qw);
        return JsonResult.ok().data(list);
    }

    /**
     * 获取用户菜单树
     */
    @ApiOperation(value = "获取用户菜单树")
    @PermCheck("system:menu:select")
    @GetMapping
    public Object listAllByUserId() {
        String userId = SecurityUtils.getUserId();
        List<SysMenu> list = sysMenuService.findMenusByUserId(userId);
        SysMenuNodeParser parser = new SysMenuNodeParser();
        TreeNodeConfig tnc = TreeNodeConfig.DEFAULT_CONFIG.setChildrenKey("subMenus");
        List<Tree<String>> t = TreeUtil.build(list, "0", tnc, parser);
        return JsonResult.ok().data(t);
    }

    /**
     * 获取用户菜单树-多系统
     */
    @ApiOperation(value = "获取用户菜单树")
    @PermCheck("system:menu:select")
    @GetMapping("/multi")
    public Object listAllByUserId2() {
        String userId = SecurityUtils.getUserId();
        List<SysMenu> list = sysMenuService.findMenusByUserId(userId);
        // 多系统
        List<Map<String, String>> menuTypeList = DictUtil.getDataByType("sys-menu-types", StrConstants.id, StrConstants.name);
        List<Map<String, Object>> returnList = new LinkedList<>();
        for (Map<String, String> m : menuTypeList) {
            String id = m.get(StrConstants.id);
            String name = m.get(StrConstants.name);
            Map<String, Object> tempMap = getNewHashMap();
            List<SysMenu> tempMenuList = new ArrayList<>();
            for (SysMenu sysMenu : list) {
                if (id.equals(sysMenu.getSysType())) {
                    tempMenuList.add(sysMenu);
                }
            }
            // 构造为树
            SysMenuNodeParser parser = new SysMenuNodeParser();
            TreeNodeConfig tnc = TreeNodeConfig.DEFAULT_CONFIG.setChildrenKey("subMenus");
            List<Tree<String>> t = TreeUtil.build(tempMenuList, "0", tnc, parser);
            tempMap.put("subMenus", t);
            // 当前菜单系统下没有一个菜单则不显示
            if (CollectionUtil.isEmpty(t)) {
                continue;
            }
            tempMap.put(StrConstants.id, id);
            tempMap.put(StrConstants.name, name);
            returnList.add(tempMap);
        }
        return JsonResult.ok().data(returnList);
    }

    /**
     * 获取菜单数
     */
    @PermCheck("system:menu:select")
    @GetMapping("/treeselect")
    public Object treeselect() {
        List<SysMenu> list = sysMenuService.list();
        List<Tree<String>> tree = TreeUtil.build(list, "0", new SysMenuTreeSelectParser());
        return JsonResult.ok().data(tree);
    }

    /**
     * 获取角色已有菜单的id及菜单树
     */
    @PermCheck("system:menu:select")
    @GetMapping("/roleMenuTreeselect/{roleId}")
    public Object roleTreeselect(@PathVariable String roleId) {
        Map<String, Object> m = getNewHashMap();
        m.put("menus", TreeUtil.build(sysMenuService.list(), "0", new SysMenuTreeSelectParser()));
        LambdaQueryWrapper<SysRoleMenu> qw = new LambdaQueryWrapper<>();
        qw.select(SysRoleMenu::getMenuId).eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> list = sysRoleMenuService.list(qw);
        m.put("checkedKeys", list.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList()));
        return JsonResult.ok().data(m);
    }

    /**
     * 新增
     */
    @ApiOperation(value = SwaggerConstants.add)
    @InsertLog("新增菜单")
    @PermCheck("system:menu:add")
    @PutMapping
    public Object add(@ApiParam @RequestBody SysMenu obj) {
        // 未选择上级则未顶级菜单
        if (StrUtil.isBlank(obj.getParentId())) obj.setParentId("0");
        obj.insert();
        return JsonResult.ok();
    }

    /**
     * 编辑
     */
    @ApiOperation(value = SwaggerConstants.edit)
    @InsertLog("修改菜单")
    @PermCheck("system:menu:edit")
    @PostMapping
    public Object edit(@ApiParam @RequestBody SysMenu obj) {
        if (StrUtil.isBlank(obj.getId())) throw new RequestEmptyException();
        if (StrUtil.isBlank(obj.getParentId()) && StrUtil.isNotBlank(obj.getName())) {
            obj.setParentId("0");
        }
        obj.updateById();
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = SwaggerConstants.delete)
    @InsertLog("删除菜单")
    @PermCheck("system:menu:delete")
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") @ApiParam(name = "id", value = SwaggerConstants.paramDesc_delete, required = true, type = SwaggerConstants.paramType_path) String id) {
        if (StrUtil.isBlank(id)) throw new RequestEmptyException();
        this.sysMenuService.deleteMenuAndRef(id);
        return JsonResult.ok();
    }

}
