package cc.ussu.modules.dczx.mapper;

import cc.ussu.modules.dczx.entity.DcInterfaceLog;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 接口日志 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Slave
public interface DcInterfaceLogMapper extends BaseMapper<DcInterfaceLog> {

    // 查询用户首次调用接口的时间
    List<DcInterfaceLog> groupByUserid(@Param("list") List<String> accountList);

}
