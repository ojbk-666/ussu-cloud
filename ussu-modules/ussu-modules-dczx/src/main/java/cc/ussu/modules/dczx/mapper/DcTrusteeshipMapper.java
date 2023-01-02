package cc.ussu.modules.dczx.mapper;

import cc.ussu.modules.dczx.entity.DcTrusteeship;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 托管列表 Mapper 接口
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-09 20:32:44
 */
@Slave
@Repository
@Mapper
public interface DcTrusteeshipMapper extends BaseMapper<DcTrusteeship> {

}
