package cc.ussu.modules.system.service.impl;

import cc.ussu.modules.system.entity.SysUserRole;
import cc.ussu.modules.system.mapper.SysUserRoleMapper;
import cc.ussu.modules.system.service.ISysUserRoleService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Master
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    /**
     * 通过角色id获取
     *
     * @param roleId
     */
    @Override
    public List<SysUserRole> getByRoleId(String roleId) {
        if (StrUtil.isBlank(roleId)) {
            return new ArrayList<>();
        }
        return list(Wrappers.lambdaQuery(SysUserRole.class).eq(SysUserRole::getRoleId, roleId));
    }

    /**
     * 通过用户id获取
     *
     * @param userId
     */
    @Override
    public List<SysUserRole> getByUserId(String userId) {
        if (StrUtil.isBlank(userId)) {
            return new ArrayList<>();
        }
        return list(Wrappers.lambdaQuery(SysUserRole.class).eq(SysUserRole::getUserId, userId));
    }

    /**
     * 通过角色id删除关联关系
     *
     * @param roleIds
     */
    @Transactional
    @Override
    public void deleteByRoleIds(List<String> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            super.remove(Wrappers.lambdaQuery(SysUserRole.class).in(SysUserRole::getRoleId, roleIds));
        }
    }

    /**
     * 通过用户id删除关联关系
     *
     * @param userIds
     */
    @Transactional
    @Override
    public void deleteByUserIds(List<String> userIds) {
        if (CollUtil.isNotEmpty(userIds)) {
            super.remove(Wrappers.lambdaQuery(SysUserRole.class).in(SysUserRole::getUserId, userIds));
        }
    }
}
