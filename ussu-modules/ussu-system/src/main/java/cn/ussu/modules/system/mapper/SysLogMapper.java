package cn.ussu.modules.system.mapper;

import cn.ussu.modules.system.entity.SysLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统日志 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
public interface SysLogMapper extends BaseMapper<SysLog> {

    // IPage<Map> findPage(Page page, @Param("param") Map param);

    List<Map<String, Object>> queryCountGroupBy(@Param("column") String column);

}
