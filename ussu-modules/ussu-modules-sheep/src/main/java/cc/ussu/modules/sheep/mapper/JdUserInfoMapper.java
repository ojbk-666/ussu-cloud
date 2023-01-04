package cc.ussu.modules.sheep.mapper;

import cc.ussu.modules.sheep.entity.JdUserInfo;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Master
@Mapper
public interface JdUserInfoMapper extends BaseMapper<JdUserInfo> {
}
