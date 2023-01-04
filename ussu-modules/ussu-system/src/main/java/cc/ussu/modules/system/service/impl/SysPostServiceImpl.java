package cc.ussu.modules.system.service.impl;

import cc.ussu.modules.system.entity.SysPost;
import cc.ussu.modules.system.mapper.SysPostMapper;
import cc.ussu.modules.system.service.ISysPostService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 岗位 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-12-31 19:24:31
 */
@Master
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements ISysPostService {

    @Override
    public boolean checkCodeExist(SysPost p) {
        LambdaQueryWrapper<SysPost> qw = Wrappers.lambdaQuery(SysPost.class).eq(SysPost::getPostCode, p.getPostCode())
                .ne(StrUtil.isNotBlank(p.getId()), SysPost::getId, p.getId());
        return super.count(qw) > 0;
    }

}
