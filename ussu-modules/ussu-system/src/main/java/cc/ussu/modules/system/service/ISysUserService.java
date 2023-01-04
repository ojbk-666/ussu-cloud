package cc.ussu.modules.system.service;

import cc.ussu.modules.system.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 检查是否存在
     *
     * @param p
     * @return 已存在返回true
     */
    boolean checkExistAccount(SysUser p);

    SysUser getByAccount(String account);

    void add(SysUser p);

    void edit(SysUser p);

    void del(String ids);

    void authRole(SysUser p);

    /**
     * 导入用户
     */
    void importUser(List<SysUser> sysUserList);
}
