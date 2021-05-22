package cn.ussu.modules.system.service;

import cn.ussu.common.core.entity.ReturnPageInfo;
import cn.ussu.modules.system.entity.SysMenu;
import cn.ussu.modules.system.entity.SysRole;
import cn.ussu.modules.system.model.param.SysRoleParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
public interface ISysRoleService extends IService<SysRole> {

    ReturnPageInfo<SysRole> findPage(SysRoleParam param);

    /**
     * 添加
     */
    boolean addOne(SysRole sysRole);

    /**
     * 修改
     */
    boolean updateOne(SysRole sysRole);

    /**
     * 获取角色权限
     */
    List<SysMenu> findPermsByRoleId(String roleId);

    /**
     * 更新角色的权限信心
     */
    boolean updateRolePerms(String roleId, String menus);

    /**
     * 更新角色数据权限
     */
    boolean updateRoleDataScopes(SysRole sysRole, String depts);

    /**
     * 获取权限范围内所有角色
     */
    List<SysRole> findAll(Map param);

    /**
     * 获取用户角色列表
     */
    List<SysRole> findListByUserId(String userId);

    /**
     * 根据角色id获取对应的自定义数据范围
     */
    // Set<String> findDeptIdsByRoleIds(Collection<String> roleIds);
}
