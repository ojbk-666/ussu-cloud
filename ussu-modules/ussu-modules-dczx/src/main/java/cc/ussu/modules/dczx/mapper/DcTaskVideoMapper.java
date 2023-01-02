package cc.ussu.modules.dczx.mapper;

import cc.ussu.modules.dczx.entity.DcTaskVideo;
import cc.ussu.modules.dczx.entity.vo.DcTaskVideoQueryVO;
import cc.ussu.modules.dczx.entity.vo.DcTaskVideoVO;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 视频信息 Mapper 接口
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-02 14:01:34
 */
@Slave
@Repository
public interface DcTaskVideoMapper extends BaseMapper<DcTaskVideo> {

    List<DcTaskVideoVO> findPage(DcTaskVideoQueryVO query);

}
