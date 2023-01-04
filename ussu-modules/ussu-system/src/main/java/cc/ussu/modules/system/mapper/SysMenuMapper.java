package cc.ussu.modules.system.mapper;

import cc.ussu.modules.system.entity.SysMenu;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 系统菜单 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Master
public interface SysMenuMapper extends BaseMapper<SysMenu> {

}
