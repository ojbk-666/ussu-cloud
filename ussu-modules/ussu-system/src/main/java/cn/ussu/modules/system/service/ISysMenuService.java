package cn.ussu.modules.system.service;

import cn.ussu.modules.system.entity.SysMenu;
import cn.ussu.modules.system.model.vo.RouterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统菜单 服务类
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenu> findByRoleId(String roleId);

    /**
     * 获取用户所有菜单
     * @param userId
     * @return
     */
    List<SysMenu> findMenusByUserId(String userId);

    /**
     * 获取用户菜单树
     */
    List<SysMenu> findMenuTreeByUserId(String userId);

    List<RouterVo> buildMenus(List<SysMenu> tree);

    /**
     * 获取用户权限列表
     *
     * @param userId
     * @return
     */
    List<String> findPermsByUserId(String userId);

    /**
     * 删除菜单，同时会删除子菜单，权限 和角色菜单关联关系
     */
    void deleteMenuAndRef(String id);
}
