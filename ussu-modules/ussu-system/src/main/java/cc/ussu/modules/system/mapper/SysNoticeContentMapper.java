package cc.ussu.modules.system.mapper;

import cc.ussu.modules.system.entity.SysNoticeContent;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Master
@Repository
public interface SysNoticeContentMapper extends BaseMapper<SysNoticeContent> {
}
