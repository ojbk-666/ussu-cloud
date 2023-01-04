package cc.ussu.modules.system.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.constants.SystemLogConstants;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.system.entity.SysMenu;
import cc.ussu.modules.system.entity.SysRoleMenu;
import cc.ussu.modules.system.service.ISysMenuService;
import cc.ussu.modules.system.service.ISysRoleMenuService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单 前端控制器
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.system}/sys-menu")
public class SysMenuController extends BaseController {

    private static final String PERM_PREFIX = "system:menu:";
    private static final String SYSTEM_LOG_GROUP = "菜单管理";

    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    /**
     * 获取列表
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/list")
    public JsonResult<List<SysMenu>> list(SysMenu p) {
        LambdaQueryWrapper<SysMenu> qw = Wrappers.lambdaQuery(SysMenu.class)
                .orderByAsc(SysMenu::getType).orderByAsc(SysMenu::getSort)
                .eq(StrUtil.isNotBlank(p.getDisableFlag()), SysMenu::getDisableFlag, p.getDisableFlag())
                .like(StrUtil.isNotBlank(p.getName()), SysMenu::getName, p.getName());
        return JsonResult.ok(sysMenuService.list(qw));
    }

    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/{id}")
    public JsonResult<SysMenu> detail(@PathVariable String id) {
        return JsonResult.ok(sysMenuService.getById(id));
    }

    /**
     * 树
     */
    @GetMapping("/treeselect")
    public JsonResult<List<SysMenu>> treeselect() {
        List<SysMenu> list = sysMenuService.list(Wrappers.lambdaQuery(SysMenu.class).orderByAsc(SysMenu::getSort));
        List<SysMenu> menuList = sysMenuService.renderToTree(list);
        return JsonResult.ok(menuList);
    }

    /**
     * 角色已有菜单id和菜单树
     */
    // @PermCheck(PERM_PREFIX + SELECT)
    // @GetMapping("/roleMenuTreeselect/{roleId}")
    public Object roleMenuTreeselect(@PathVariable String roleId) {
        // 获取角色对应菜单
        List<SysRoleMenu> roleMenuList = sysRoleMenuService.list(Wrappers.lambdaQuery(SysRoleMenu.class).eq(SysRoleMenu::getRoleId, roleId));
        JsonResult<List<SysMenu>> jr = treeselect();
        Map<String, Object> map = new HashMap<>();
        map.put("treeselect", jr.getData());
        map.put("roleMenuIds", roleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList()));
        return JsonResult.ok(map);
    }

    /**
     * 更改状态
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.CHANGE_STATUS)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping("/changeStatus")
    public JsonResult changeStatus(@RequestBody SysMenu p) {
        sysMenuService.updateById(new SysMenu().setId(p.getId()).setDisableFlag(p.getDisableFlag()));
        return JsonResult.ok();
    }

    /**
     * 添加
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.INSERT)
    @PermCheck(PERM_PREFIX + ADD)
    @PutMapping({"", "/add"})
    public JsonResult add(@Validated @RequestBody SysMenu p) {
        // Assert.notBlank(p.getName(), "名称不能为空");
        sysMenuService.save(p);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.UPDATE)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping({"", "/edit"})
    public JsonResult edit(@Validated @RequestBody SysMenu p) {
        Assert.notBlank(p.getId(), "id不能为空");
        // Assert.notBlank(p.getName(), "名称不能为空");
        sysMenuService.updateById(p);
        return JsonResult.ok();
    }

    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.DELETE)
    @PermCheck(PERM_PREFIX + DELETE)
    @DeleteMapping({"/{ids}", "/del/{ids}"})
    public JsonResult delete(@PathVariable String ids) {
        List<String> idList = StrUtil.split(ids, StrUtil.COMMA);
        idList = CollUtil.removeBlank(idList);
        Assert.notEmpty(idList, "id不能为空");
        sysMenuService.removeByIds(idList);
        return JsonResult.ok();
    }

}

