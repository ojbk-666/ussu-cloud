package cn.ussu.modules.system.mapper;

import cn.ussu.modules.system.entity.SysAttach;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 附件表 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-05-17
 */
public interface SysAttachMapper extends BaseMapper<SysAttach> {

    IPage<Map> findPage(Page page, @Param("param") Map param);

}
