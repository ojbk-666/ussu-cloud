package cc.ussu.modules.system.service;

import cc.ussu.modules.system.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 通过角色id获取
     */
    List<SysUserRole> getByRoleId(String roleId);

    /**
     * 通过用户id获取
     */
    List<SysUserRole> getByUserId(String userId);

    /**
     * 通过角色id删除关联关系
     */
    void deleteByRoleIds(List<String> roleIds);

    /**
     * 通过用户id删除关联关系
     */
    void deleteByUserIds(List<String> userIds);

}
