package cc.ussu.modules.dczx.mapper;

import cc.ussu.modules.dczx.entity.DcTask;
import cc.ussu.modules.dczx.entity.vo.DcTaskVO;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 任务 Mapper 接口
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-01 15:52:07
 */
@Slave
@Repository
public interface DcTaskMapper extends BaseMapper<DcTask> {

    List<DcTaskVO> findPage(DcTaskVO query);

}
