package cc.ussu.modules.system.mapper;

import cc.ussu.modules.system.entity.SysUser;
import cc.ussu.modules.system.entity.vo.SelectUserParamVO;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Master
public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<SysUser> getPageList(Page page, @Param("p") SysUser p);

    IPage<SysUser> getSelectUserList(Page page, @Param("p") SelectUserParamVO p);

}
