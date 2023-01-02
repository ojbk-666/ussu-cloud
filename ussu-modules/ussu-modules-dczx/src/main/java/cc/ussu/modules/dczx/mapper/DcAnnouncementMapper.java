package cc.ussu.modules.dczx.mapper;

import cc.ussu.modules.dczx.entity.DcAnnouncement;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 东财在线插件公告 Mapper 接口
 * </p>
 *
 * @author mp-generator
 * @since 2022-11-22 15:12:34
 */
@Slave
@Repository
@Mapper
public interface DcAnnouncementMapper extends BaseMapper<DcAnnouncement> {

}
