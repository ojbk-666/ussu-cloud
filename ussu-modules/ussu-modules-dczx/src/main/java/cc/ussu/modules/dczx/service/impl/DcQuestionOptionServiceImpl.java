package cc.ussu.modules.dczx.service.impl;

import cc.ussu.modules.dczx.entity.DcQuestionOption;
import cc.ussu.modules.dczx.mapper.DcQuestionOptionMapper;
import cc.ussu.modules.dczx.service.IDcQuestionOptionService;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 题目的选项 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Slave
@Service
public class DcQuestionOptionServiceImpl extends ServiceImpl<DcQuestionOptionMapper, DcQuestionOption> implements IDcQuestionOptionService {

}
