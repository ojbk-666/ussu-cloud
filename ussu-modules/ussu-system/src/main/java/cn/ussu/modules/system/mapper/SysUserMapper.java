package cn.ussu.modules.system.mapper;

import cn.ussu.modules.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    // SysUser findByAccount(String account);

    IPage findPage(Page page, @Param("param") Map param);

}
