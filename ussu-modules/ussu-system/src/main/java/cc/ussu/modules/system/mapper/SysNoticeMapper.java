package cc.ussu.modules.system.mapper;

import cc.ussu.modules.system.entity.SysNotice;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 通知公告表 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2022-01-18 15:31:26
 */
@Master
@Mapper
public interface SysNoticeMapper extends BaseMapper<SysNotice> {

}
