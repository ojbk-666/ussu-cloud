package cc.ussu.modules.system.mapper;

import cc.ussu.modules.system.entity.SysUserPost;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户与岗位关联表 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2021-12-31 20:05:21
 */
@Master
public interface SysUserPostMapper extends BaseMapper<SysUserPost> {

}
