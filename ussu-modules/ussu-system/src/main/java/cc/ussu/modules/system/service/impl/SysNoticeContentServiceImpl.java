package cc.ussu.modules.system.service.impl;

import cc.ussu.modules.system.entity.SysNoticeContent;
import cc.ussu.modules.system.mapper.SysNoticeContentMapper;
import cc.ussu.modules.system.service.ISysNoticeContentService;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 通知已读记录 服务实现类
 * </p>
 *
 * @author mp-generator
 * @since 2022-11-11 14:26:02
 */
@Master
@Service
public class SysNoticeContentServiceImpl extends ServiceImpl<SysNoticeContentMapper, SysNoticeContent> implements ISysNoticeContentService {

}
