package cc.ussu.modules.system.service;

import cc.ussu.modules.system.entity.SysMenu;
import cc.ussu.modules.system.entity.vo.RouterVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统菜单 服务类
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 构造成树
     */
    List<SysMenu> renderToTree(List<SysMenu> menuList);

    /**
     * 获取用户权限集合
     */
    Set<String> listActionByUserId(String userId);

    /**
     * 获取用户菜单列表
     */
    List<SysMenu> listByUserId(String userId, boolean isTree);

    /**
     * 构建树
     *
     * @param list 树结构的list
     */
    List<RouterVO> buildRouter(List<SysMenu> list);

}
