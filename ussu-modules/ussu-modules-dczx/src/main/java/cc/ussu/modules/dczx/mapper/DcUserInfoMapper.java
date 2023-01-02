package cc.ussu.modules.dczx.mapper;

import cc.ussu.modules.dczx.entity.DcUserInfo;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 学生基本信息 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2022-03-07 16:06:24
 */
@Slave
@Mapper
public interface DcUserInfoMapper extends BaseMapper<DcUserInfo> {

}
