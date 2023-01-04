package cc.ussu.modules.system.service.impl;

import cc.ussu.modules.system.entity.SysRoleMenu;
import cc.ussu.modules.system.mapper.SysRoleMenuMapper;
import cc.ussu.modules.system.service.ISysRoleMenuService;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Master
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

}
