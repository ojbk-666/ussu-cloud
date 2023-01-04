package cc.ussu.modules.system.service.impl;

import cc.ussu.modules.system.entity.SysUserPost;
import cc.ussu.modules.system.mapper.SysUserPostMapper;
import cc.ussu.modules.system.service.ISysUserPostService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户与岗位关联表 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-12-31 20:05:21
 */
@Master
@Service
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostMapper, SysUserPost> implements ISysUserPostService {

    /**
     * 通过用户id获取
     *
     * @param userId
     */
    @Override
    public List<SysUserPost> getByUserId(String userId) {
        if (StrUtil.isBlank(userId)) {
            return new ArrayList<>();
        }
        return list(Wrappers.lambdaQuery(SysUserPost.class).eq(SysUserPost::getUserId, userId));
    }

    /**
     * 通过岗位id获取
     *
     * @param postId
     */
    @Override
    public List<SysUserPost> getByPostId(String postId) {
        if (StrUtil.isBlank(postId)) {
            return new ArrayList<>();
        }
        return list(Wrappers.lambdaQuery(SysUserPost.class).eq(SysUserPost::getPostId, postId));
    }
}
