package cc.ussu.modules.sheep.service.impl;

import cc.ussu.modules.sheep.entity.JdUserInfo;
import cc.ussu.modules.sheep.mapper.JdUserInfoMapper;
import cc.ussu.modules.sheep.service.IJdUserInfoService;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Master
@Service
public class JdUserInfoServiceImpl extends ServiceImpl<JdUserInfoMapper, JdUserInfo> implements IJdUserInfoService {

}
