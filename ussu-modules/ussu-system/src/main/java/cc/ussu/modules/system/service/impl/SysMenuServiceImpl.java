package cc.ussu.modules.system.service.impl;

import cc.ussu.common.core.constants.StrConstants;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.system.entity.SysMenu;
import cc.ussu.modules.system.entity.SysRole;
import cc.ussu.modules.system.entity.SysRoleMenu;
import cc.ussu.modules.system.entity.vo.RouterMetaVO;
import cc.ussu.modules.system.entity.vo.RouterVO;
import cc.ussu.modules.system.mapper.SysMenuMapper;
import cc.ussu.modules.system.service.ISysMenuService;
import cc.ussu.modules.system.service.ISysRoleMenuService;
import cc.ussu.modules.system.service.ISysRoleService;
import cc.ussu.modules.system.service.ISysUserRoleService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Master
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private static final String LAYOUT = "Layout";
    private static final String INNER_LINK = "InnerLink";
    private static final String PARENT_VIEW = "ParentView";
    private static final String ZERO = "0";

    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;
    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 构造成树
     */
    @Override
    public List<SysMenu> renderToTree(List<SysMenu> menuList) {
        return getChildPerms(menuList, ZERO);
    }

    /**
     * 获取用户权限集合
     */
    @Override
    public Set<String> listActionByUserId(String userId) {
        List<SysMenu> menuList = listByUserId(userId, false);
        return menuList.stream()
                .filter(m -> SysMenu.TYPE_BUTTON.equals(m.getType()) || SysMenu.TYPE_MENU.equals(m.getType()))
                .filter(m -> StrUtil.isNotBlank(m.getAction())).map(SysMenu::getAction).collect(Collectors.toSet());
    }

    /**
     * 获取用户菜单列表
     */
    @Override
    public List<SysMenu> listByUserId(String userId, boolean isTree) {
        LambdaQueryWrapper<SysMenu> qw = Wrappers.lambdaQuery(SysMenu.class)
                .orderByAsc(SysMenu::getType).orderByAsc(SysMenu::getSort)
                .eq(SysMenu::getDisableFlag, StrConstants.CHAR_FALSE);
        if (SecurityUtil.isNotSuperAdmin(userId)) {
            // 获取角色
            Set<String> roleIdList = sysRoleService.listByUserId(userId).stream().map(SysRole::getId).collect(Collectors.toSet());
            if (CollUtil.isEmpty(roleIdList)) {
                return new ArrayList<>();
            }
            List<SysRoleMenu> roleMenuList = sysRoleMenuService.list(Wrappers.lambdaQuery(SysRoleMenu.class).in(SysRoleMenu::getRoleId, roleIdList));
            List<String> menuIdList = roleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
            if (CollUtil.isEmpty(menuIdList)) {
                return new ArrayList<>();
            }
            qw.in(SysMenu::getId, menuIdList);
        }
        List<SysMenu> menuList = super.list(qw);
        if (isTree) {
            return getChildPerms(menuList, ZERO);
        }
        return menuList;
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, String parentId) {
        List<SysMenu> returnList = new ArrayList<>();
        Iterator<SysMenu> iterator = list.iterator();
        while (iterator.hasNext()) {
            SysMenu t = iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId().equals(parentId)) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = it.next();
            if (n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return CollUtil.isNotEmpty(getChildList(list, t));
    }

    /**
     * 构建路由树
     */
    @Override
    public List<RouterVO> buildRouter(List<SysMenu> menuList) {
        List<RouterVO> routers = new LinkedList<>();
        for (SysMenu menu : menuList) {
            RouterVO router = new RouterVO().setHidden(!menu.getShowFlag())
                    .setName(getRouteName(menu))
                    .setPath(getRouterPath(menu))
                    .setComponent(getComponent(menu))
                    .setMeta(new RouterMetaVO()
                            .setTitle(menu.getName())
                            .setIcon(menu.getIcon())
                            .setNoCache(!menu.getCacheFlag())
                            .setLink(menu.getPath()));
            List<SysMenu> cMenus = menu.getChildren();
            if (CollUtil.isNotEmpty(cMenus) && menu.isDir()) {
                router.setAlwaysShow(true)
                        .setRedirect("noRedirect")
                        .setChildren(buildRouter(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVO> childrenList = new ArrayList<>();
                RouterVO children = new RouterVO();
                children.setPath(menu.getPath())
                        .setComponent(menu.getComponent())
                        .setName(StrUtil.upperFirst(menu.getPath()))
                        .setMeta(new RouterMetaVO()
                                .setTitle(menu.getName())
                                .setIcon(menu.getIcon())
                                .setNoCache(!menu.getCacheFlag())
                                .setLink(menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.parentIsZero() && isInnerLink(menu)) {
                router.setMeta(new RouterMetaVO().setTitle(menu.getName()).setIcon(menu.getIcon()))
                        .setPath("/inner");
                List<RouterVO> childrenList = new ArrayList<>();
                RouterVO children = new RouterVO();
                String routerPath = StrUtil.replace(menu.getPath(), "https://", "").replaceAll("http://", "");
                children.setPath(routerPath)
                        .setComponent(INNER_LINK)
                        .setName(StrUtil.upperFirst(routerPath))
                        .setMeta(new RouterMetaVO()
                                .setIcon(menu.getName())
                                .setIcon(menu.getIcon())
                                .setLink(menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu) {
        String routerName = StrUtil.upperFirst(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StrUtil.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath() == null ? "" : menu.getPath();
        // 内链打开外网方式
        if (!menu.parentIsZero() && isInnerLink(menu)) {
            routerPath = routerPath.replaceAll("http://", StrUtil.EMPTY).replaceAll("https://", StrUtil.EMPTY);
        }
        // 非外链并且是一级目录（类型为目录）
        if (menu.parentIsZero() && menu.isDir() && !menu.getFrameFlag()) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu) {
        String component = LAYOUT;
        if (StrUtil.isNotBlank(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StrUtil.isBlank(menu.getComponent()) && !menu.parentIsZero() && isInnerLink(menu)) {
            component = INNER_LINK;
        } else if (StrUtil.isBlank(menu.getComponent()) && isParentView(menu)) {
            component = PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu) {
        return menu.parentIsZero() && menu.isMenu() && !menu.getFrameFlag();
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu) {
        return !menu.getFrameFlag() && startsWithHttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {
        return !menu.parentIsZero() && menu.isDir();
    }

    private boolean startsWithHttp(String str) {
        return StrUtil.startWith(str, "http://", true) || StrUtil.startWith(str, "https://", true);
    }

}
