package cc.ussu.modules.system.mapper;

import cc.ussu.modules.system.entity.SysRole;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Master
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<String> getRoleNameListByUserId(@Param("userId") String userId);

}
