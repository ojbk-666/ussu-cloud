package cc.ussu.modules.dczx.service.impl;

import cc.ussu.modules.dczx.entity.DcUserInfo;
import cc.ussu.modules.dczx.mapper.DcUserInfoMapper;
import cc.ussu.modules.dczx.service.IDcUserInfoService;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生基本信息 服务实现类
 * </p>
 *
 * @author liming
 * @since 2022-03-07 16:06:24
 */
@Slave
@Service
public class DcUserInfoServiceImpl extends ServiceImpl<DcUserInfoMapper, DcUserInfo> implements IDcUserInfoService {

}
