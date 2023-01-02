package cc.ussu.modules.dczx.mapper;

import cc.ussu.modules.dczx.entity.DcUserStudyPlan;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 学习计划 Mapper 接口
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-09 21:41:09
 */
@Slave
@Repository
@Mapper
public interface DcUserStudyPlanMapper extends BaseMapper<DcUserStudyPlan> {

}
