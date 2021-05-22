package cn.ussu.modules.system.mapper;

import cn.ussu.common.core.constants.StrConstants;
import cn.ussu.modules.system.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户id获取用户的角色列表
     *
     * @param userId
     * @return
     */
    List<SysRole> findByUserId(@Param("userId") String userId);

    IPage<SysRole> findPage(Page defaultPage, @Param(StrConstants.param) Map param);

    List<SysRole> findAll(@Param(StrConstants.param) Map param);
}
