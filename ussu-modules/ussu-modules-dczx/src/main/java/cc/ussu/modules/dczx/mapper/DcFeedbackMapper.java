package cc.ussu.modules.dczx.mapper;

import cc.ussu.modules.dczx.entity.DcFeedback;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Slave
@Mapper
public interface DcFeedbackMapper extends BaseMapper<DcFeedback> {
}
