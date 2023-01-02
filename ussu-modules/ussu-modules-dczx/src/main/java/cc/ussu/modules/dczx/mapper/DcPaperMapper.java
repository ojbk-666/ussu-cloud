package cc.ussu.modules.dczx.mapper;

import cc.ussu.modules.dczx.entity.DcPaper;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * paper Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Slave
public interface DcPaperMapper extends BaseMapper<DcPaper> {

}
