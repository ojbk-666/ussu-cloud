package cc.ussu.modules.dczx.service.impl;

import cc.ussu.modules.dczx.entity.DcAnnouncement;
import cc.ussu.modules.dczx.mapper.DcAnnouncementMapper;
import cc.ussu.modules.dczx.service.IDcAnnouncementService;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 东财在线插件公告 服务实现类
 * </p>
 *
 * @author mp-generator
 * @since 2022-11-22 15:12:34
 */
@Slave
@Service
public class DcAnnouncementServiceImpl extends ServiceImpl<DcAnnouncementMapper, DcAnnouncement> implements IDcAnnouncementService {

}
