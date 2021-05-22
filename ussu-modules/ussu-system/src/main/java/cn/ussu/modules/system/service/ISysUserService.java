package cn.ussu.modules.system.service;

import cn.ussu.common.core.entity.ReturnPageInfo;
import cn.ussu.modules.system.entity.SysUser;
import cn.ussu.modules.system.model.param.SysUserParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 通过账号查找
     * @param account
     * @return
     */
    SysUser findByAccount(String account);

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    ReturnPageInfo<SysUser> findPage(SysUserParam param);

    /**
     * 添加
     */
    boolean addOne(SysUser sysUser);

    /**
     * 更新
     */
    boolean updateOne(SysUser sysUser);

    /**
     * 删除
     */
    boolean deleteUser(List<String> userIdList);

}
