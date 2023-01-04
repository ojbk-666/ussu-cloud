package cc.ussu.modules.system.service.impl;

import cc.ussu.common.core.constants.StrConstants;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.system.entity.SysRole;
import cc.ussu.modules.system.entity.SysRoleMenu;
import cc.ussu.modules.system.entity.SysUserRole;
import cc.ussu.modules.system.mapper.SysRoleMapper;
import cc.ussu.modules.system.service.ISysRoleMenuService;
import cc.ussu.modules.system.service.ISysRoleService;
import cc.ussu.modules.system.service.ISysUserRoleService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Master
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Override
    public boolean checkNameExist(SysRole p) {
        LambdaQueryWrapper<SysRole> qw = Wrappers.lambdaQuery(SysRole.class)
                .eq(SysRole::getRoleName, p.getRoleName())
                .ne(StrUtil.isNotBlank(p.getId()), SysRole::getId, p.getId());
        return super.count(qw) > 0;
    }

    @Override
    public boolean checkCodeExist(SysRole p) {
        LambdaQueryWrapper<SysRole> qw = Wrappers.lambdaQuery(SysRole.class)
                .eq(SysRole::getRoleCode, p.getRoleCode())
                .ne(StrUtil.isNotBlank(p.getId()), SysRole::getId, p.getId());
        return super.count(qw) > 0;
    }

    @Override
    @Transactional
    public void add(SysRole p) {
        Assert.notBlank(p.getRoleName(), "角色名不能为空");
        Assert.notBlank(p.getRoleCode(), "角色编码不能为空");
        Assert.isFalse(checkNameExist(p), "已存在角色名：{}", p.getRoleName());
        super.save(p);
        List<String> menuIds = p.getMenuIds();
        if (CollUtil.isNotEmpty(menuIds)) {
            List<SysRoleMenu> roleMenuList = menuIds.stream().map(menuId -> new SysRoleMenu().setRoleId(p.getId()).setMenuId(menuId)).collect(Collectors.toList());
            sysRoleMenuService.saveBatch(roleMenuList);
        }
    }

    @Override
    @Transactional
    public void edit(SysRole p) {
        Assert.notBlank(p.getId(), "id不能为空");
        Assert.notBlank(p.getRoleName(), "角色名不能为空");
        Assert.notBlank(p.getRoleCode(), "角色编码不能为空");
        Assert.isFalse(checkCodeExist(p), "已存在角色编码：{}", p.getRoleCode());
        super.updateById(p);
        List<String> menuIds = p.getMenuIds();
        if (CollUtil.isNotEmpty(menuIds)) {
            sysRoleMenuService.remove(Wrappers.lambdaQuery(SysRoleMenu.class).eq(SysRoleMenu::getRoleId, p.getId()));
            if (CollUtil.isNotEmpty(p.getMenuIds())) {
                List<SysRoleMenu> roleMenuList = p.getMenuIds().stream().map(menuId -> new SysRoleMenu().setRoleId(p.getId()).setMenuId(menuId)).collect(Collectors.toList());
                sysRoleMenuService.saveBatch(roleMenuList);
            }
        }
    }

    /**
     * 通过用户id获取关联的角色信息
     */
    @Override
    public Collection<SysRole> listByUserId(String userId) {
        if (StrUtil.isBlank(userId)) {
            return new LinkedHashSet<>();
        }
        if (SecurityUtil.isSuperAdmin(userId)) {
            return CollUtil.newHashSet(super.list(Wrappers.lambdaQuery(SysRole.class).eq(SysRole::getDisableFlag, StrConstants.CHAR_FALSE)));
        }
        List<SysUserRole> userRoleList = sysUserRoleService.list(Wrappers.lambdaQuery(SysUserRole.class).eq(SysUserRole::getUserId, userId));
        List<String> roleIdList = userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(roleIdList)) {
            List<SysRole> list = super.list(Wrappers.lambdaQuery(SysRole.class).eq(SysRole::getDisableFlag, StrConstants.CHAR_FALSE).in(SysRole::getId, roleIdList));
            return CollUtil.newHashSet(list);
        }
        return new LinkedHashSet<>();
    }
}
