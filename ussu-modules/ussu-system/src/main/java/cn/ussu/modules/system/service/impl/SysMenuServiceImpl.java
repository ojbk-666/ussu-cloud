package cn.ussu.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.constants.StrConstants;
import cn.ussu.common.core.exception.RequestEmptyException;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.system.core.constants.MenuConstants;
import cn.ussu.modules.system.entity.SysMenu;
import cn.ussu.modules.system.entity.SysRoleMenu;
import cn.ussu.modules.system.mapper.SysMenuMapper;
import cn.ussu.modules.system.mapper.SysRoleMenuMapper;
import cn.ussu.modules.system.model.vo.MetaVo;
import cn.ussu.modules.system.model.vo.RouterVo;
import cn.ussu.modules.system.service.ISysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    // @Autowired
    // private UssuProperties ussuProperties;

    @Override
    public List<SysMenu> findByRoleId(String roleId) {
        return sysMenuMapper.findByRoleId(roleId);
    }

    @Override
    public List<SysMenu> findMenusByUserId(String userId) {
        if (StrUtil.isBlank(userId)) throw new RequestEmptyException();
        if (SecurityUtils.isSuperAdmin()) userId = null;
        return sysMenuMapper.findMenuListByUserId(userId, true);
    }

    @Override
    public List<SysMenu> findMenuTreeByUserId(String userId) {
        List<SysMenu> list = findMenusByUserId(userId);
        return list.stream().map(item -> {
            // 开始递归
            item.setChildren(recursionSysMenu(item, list));
            return item;
        })
                .collect(Collectors.toList())
                .stream()
                .filter(item -> "0".equals(item.getParentId()))
                .collect(Collectors.toList());
    }

    /**
     * 递归
     *
     * @param r
     * @param list
     * @return
     */
    public List<SysMenu> recursionSysMenu(SysMenu r, List<SysMenu> list) {
        List<SysMenu> child = new LinkedList<>();
        for (SysMenu item : list) {
            if (item.getParentId().equals(r.getId())) {
                child.add(item);
            }
        }
        if (child.size() != 0) {
            for (SysMenu item : child) {
                item.setChildren(recursionSysMenu(item, list));
            }
        }
        return child;
    }

    @Override
    public List<RouterVo> buildMenus(List<SysMenu> tree) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : tree) {
            RouterVo router = new RouterVo();
            router.setHidden(0==menu.getShowFlag());
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon(), 0 == menu.getCacheFlag()));
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && MenuConstants.TYPE_DIR.equals(menu.getType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMeunFrame(menu)) {
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StrUtil.upperFirst(menu.getPath()));
                children.setMeta(new MetaVo(menu.getName(), menu.getIcon(), 0 == menu.getCacheFlag()));
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
        if (isMeunFrame(menu)) {
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
        String routerPath = menu.getPath();
        // 非外链并且是一级目录（类型为目录）
        if ("0".equals(menu.getParentId()) &&MenuConstants.TYPE_DIR.equals(menu.getType()) && 0 == menu.getIframeFlag()) {
            routerPath = "/" + menu.getPath();
        } else if (isMeunFrame(menu)) {
            // 非外链并且是一级目录（类型为菜单）
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
        String component = StrConstants.Layout;
        if (StrUtil.isNotEmpty(menu.getComponent()) && !isMeunFrame(menu)) {
            component = menu.getComponent();
        } else if (StrUtil.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = StrConstants.ParentView;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMeunFrame(SysMenu menu) {
        return "0".equals(menu.getParentId()) && MenuConstants.TYPE_MENU.equals(menu.getType()) && 1== menu.getIframeFlag();
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {
        return !"0".equals(menu.getParentId()) && MenuConstants.TYPE_DIR.equals(menu.getType());
    }

    @Override
    public List<String> findPermsByUserId(String userId) {
        if (StrUtil.isBlank(userId)) throw new RequestEmptyException();
        if (SecurityUtils.isSuperAdmin()) userId = null;
        List<SysMenu> menuListByUserId = sysMenuMapper.findMenuListByUserId(userId, false);
        List<SysMenu> collect = menuListByUserId.stream().filter(m -> 20 == m.getType() && StrUtil.isNotBlank(m.getPerm())).collect(Collectors.toList());
        return CollectionUtil.getFieldValues(collect, "perm", String.class);
    }

    @Override
    @Transactional
    public void deleteMenuAndRef(String id) {
        if (StrUtil.isBlank(id)) throw new RequestEmptyException();
        sysMenuMapper.deleteById(id);
        QueryWrapper<SysMenu> qw = new QueryWrapper<>();
        qw.eq("parent_id", id);
        sysMenuMapper.delete(qw);
        QueryWrapper<SysRoleMenu> qw1 = new QueryWrapper<>();
        qw1.eq("menu_id", id);
        sysRoleMenuMapper.delete(qw1);
    }
}
