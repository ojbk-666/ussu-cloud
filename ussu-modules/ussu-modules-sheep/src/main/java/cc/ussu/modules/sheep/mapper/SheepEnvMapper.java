package cc.ussu.modules.sheep.mapper;

import cc.ussu.modules.sheep.entity.SheepEnv;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 环境变量 Mapper 接口
 * </p>
 *
 * @author mp-generator
 * @since 2022-10-16 09:40:01
 */
@Master
@Repository
@Mapper
public interface SheepEnvMapper extends BaseMapper<SheepEnv> {

}
