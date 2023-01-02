package cc.ussu.modules.dczx.service.impl;

import cc.ussu.modules.dczx.entity.DcPaper;
import cc.ussu.modules.dczx.mapper.DcPaperMapper;
import cc.ussu.modules.dczx.service.IDcPaperService;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * paper 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Slave
@Service
public class DcPaperServiceImpl extends ServiceImpl<DcPaperMapper, DcPaper> implements IDcPaperService {

}
