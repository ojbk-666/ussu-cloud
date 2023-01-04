package cc.ussu.modules.system.service;

import cc.ussu.modules.system.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
public interface ISysRoleService extends IService<SysRole> {

    boolean checkNameExist(SysRole p);

    boolean checkCodeExist(SysRole p);

    void add(SysRole p);

    void edit(SysRole p);

    /**
     * 通过用户id获取关联的角色信息
     */
    Collection<SysRole> listByUserId(String userId);
}
