package cn.ussu.modules.system.mapper;

import cn.ussu.modules.system.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统菜单 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> findMenuListByUserId(@Param("userId") String userId, @Param("onlyMenu") boolean onlyMenu);

    /**
     * 根据角色获取角色已有权限
     */
    List<SysMenu> findByRoleId(@Param("roleId") String roleId);
}
