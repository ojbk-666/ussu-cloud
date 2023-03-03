package cc.ussu.modules.system.mapper;

import cc.ussu.modules.system.entity.SysConfig;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 系统配置表 Mapper 接口
 * </p>
 *
 * @author mp-generator
 * @since 2023-03-01 14:45:52
 */
@Master
@Repository
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {

}
