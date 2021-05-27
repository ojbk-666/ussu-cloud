package cn.ussu.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.constants.StrConstants;
import cn.ussu.common.core.entity.ReturnPageInfo;
import cn.ussu.common.core.exception.RequestEmptyException;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.system.core.util.DefaultPageFactory;
import cn.ussu.modules.system.entity.SysMenu;
import cn.ussu.modules.system.entity.SysRole;
import cn.ussu.modules.system.entity.SysRoleMenu;
import cn.ussu.modules.system.mapper.SysRoleMapper;
import cn.ussu.modules.system.model.param.SysRoleParam;
import cn.ussu.modules.system.service.ISysMenuService;
import cn.ussu.modules.system.service.ISysRoleMenuService;
import cn.ussu.modules.system.service.ISysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    @Override
    public ReturnPageInfo<SysRole> findPage(SysRoleParam param) {
        LambdaQueryWrapper<SysRole> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(SysRole::getRoleSort);
        if (param != null) {
            String roleName = param.getRoleName();
            String roleCode = param.getRoleCode();
            Integer status = param.getStatus();
            qw.eq(status != null, SysRole::getStatus, status)
                    .like(StrUtil.isNotBlank(roleName), SysRole::getRoleName, roleName)
                    .like(StrUtil.isNotBlank(roleCode), SysRole::getRoleCode, roleCode);
        }
        IPage iPage = this.sysRoleMapper.selectPage(DefaultPageFactory.getPage(), qw);
        return DefaultPageFactory.createReturnPageInfo(iPage);
    }

    @Override
    @Transactional
    public boolean addOne(SysRole sysRole) {
        sysRole.setStatus(1);
        boolean b = sysRole.insert();
        List<String> menuIds = sysRole.getMenuIds();
        if (CollUtil.isEmpty(menuIds)) return b;
        List<SysRoleMenu> sysRoleMenuList = menuIds.stream().map(item -> new SysRoleMenu().setRoleId(sysRole.getId()).setMenuId(item)).collect(Collectors.toList());
        boolean b1 = sysRoleMenuService.saveBatch(sysRoleMenuList);
        return b && b1;
    }

    @Override
    @Transactional
    public boolean updateOne(SysRole sysRole) {
        if (StrUtil.isBlank(sysRole.getId())) {
            throw new RequestEmptyException();
        }
        boolean b = sysRole.updateById();
        LambdaQueryWrapper<SysRoleMenu> rqw = new LambdaQueryWrapper<>();
        rqw.eq(SysRoleMenu::getRoleId, sysRole.getId());
        boolean b2 = sysRoleMenuService.remove(rqw);
        List<String> menuIds = sysRole.getMenuIds();
        if (CollUtil.isEmpty(menuIds)) return b;
        List<SysRoleMenu> sysRoleMenuList = menuIds.stream().map(item -> new SysRoleMenu().setRoleId(sysRole.getId()).setMenuId(item)).collect(Collectors.toList());
        boolean b1 = sysRoleMenuService.saveBatch(sysRoleMenuList);
        return b && b2 && b1;
    }

    @Override
    public List<SysMenu> findPermsByRoleId(String roleId) {
        // 获取所有菜单
        List<SysMenu> menus = sysMenuService.list();
        // 获取已有菜单
        List<SysMenu> menusByRole = sysMenuService.findByRoleId(roleId);
        menus.removeAll(menusByRole);
        // 设置选中值
        menusByRole.stream().forEach(m -> m.setChecked(true));
        menus.addAll(menusByRole);
        return menus;
    }

    @Override
    @Transactional
    public boolean updateRolePerms(String roleId, String menus) {
        if (StrUtil.isBlank(roleId)) throw new RequestEmptyException();
        // 删除
        boolean b1 = sysRoleMenuService.removeById(roleId);
        // 添加
        if (StrUtil.isBlank(menus)) return b1;
        List<SysRoleMenu> rms = new ArrayList<>();
        for (String menuId : menus.split(StrConstants.COMMA)) {
            rms.add(new SysRoleMenu().setRoleId(roleId).setMenuId(menuId));
        }
        boolean b = sysRoleMenuService.saveBatch(rms);
        return b1 && b;
    }

    @Transactional
    @Override
    public boolean updateRoleDataScopes(SysRole sysRole, String depts) {
        // 更新角色
        boolean b = new SysRole().setId(sysRole.getId()).setDataScopeType(sysRole.getDataScopeType()).updateById();
        // 更新数据权限
        // boolean b1 = new SysRoleDept().setRoleId(sysRole.getId()).deleteById();
        if (sysRole.getDataScopeType() == 40 && StrUtil.isNotBlank(depts)) {
            depts = depts.replaceAll(",,", StrConstants.COMMA).replaceAll(" ", "");
            for (String deptId : depts.split(StrConstants.COMMA)) {
                // new SysRoleDept().setRoleId(sysRole.getId()).setDeptId(deptId).insert();
            }
        }
        // return b && b1;
        return b;
    }

    @Override
    public List<SysRole> findAll(Map param) {
        // DataScope deptDataScope = SecurityUtils.getDeptDataScope();
        List<SysRole> list = this.sysRoleMapper.findAll(param);
        return list;
    }

    @Override
    public List<SysRole> findListByUserId(String userId) {
        if (StrUtil.isBlank(userId)) throw new IllegalArgumentException("获取用户角色列表出错，用户id为空");
        List<SysRole> list = null;
        // 如果是超级管理员
        if (SecurityUtils.isSuperAdmin(userId)) {
            // list = super.list();
            list = new ArrayList<>();
            List<String> allPerm = sysMenuService.list().stream().map(item -> item.getPerm()).filter(StrUtil::isNotBlank).collect(Collectors.toList());
            list.add(new SysRole().setRoleName("超级管理员").setPerms(allPerm));
        } else {
            list = this.sysRoleMapper.findByUserId(userId);
            for (SysRole sysRole : list) {
                List<SysMenu> menuList = sysMenuService.findByRoleId(sysRole.getId());
                sysRole.setPerms(menuList.stream().map(SysMenu::getPerm).collect(Collectors.toList()));
            }
        }
        return list;
    }

    /*@Override
    public Set<String> findDeptIdsByRoleIds(Collection<String> roleIds) {
        Collection<SysRoleDept> list = this.sysRoleDeptService.listByIds(roleIds);
        List<String> deptIdList = CollectionUtil.getFieldValues(list, "deptId", String.class);
        return new HashSet<>(deptIdList);
    }*/
}
