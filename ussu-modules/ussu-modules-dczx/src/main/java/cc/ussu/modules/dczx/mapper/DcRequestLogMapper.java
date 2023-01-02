package cc.ussu.modules.dczx.mapper;

import cc.ussu.modules.dczx.entity.DcRequestLog;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 东财接口访问日志 Mapper 接口
 * </p>
 *
 * @author mp-generator
 * @since 2022-06-03 14:16:34
 */
@Slave
@Mapper
public interface DcRequestLogMapper extends BaseMapper<DcRequestLog> {

}
