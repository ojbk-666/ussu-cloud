package cc.ussu.modules.sheep.mapper;

import cc.ussu.modules.sheep.entity.JdDayBeans;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 每日京豆收支 Mapper 接口
 * </p>
 *
 * @author mp-generator
 * @since 2022-04-01 11:13:57
 */
@Master
@Mapper
public interface JdDayBeansMapper extends BaseMapper<JdDayBeans> {

    /**
     * 统计账号的最近x天京豆收入
     *
     * @param start
     * @param end
     * @param jdUserId
     * @return
     */
    List<Map> groupByJdUserId(@Param("start") Date start, @Param("end") Date end, @Param("jdUserId") String jdUserId);

}
