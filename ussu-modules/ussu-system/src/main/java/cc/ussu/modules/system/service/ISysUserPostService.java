package cc.ussu.modules.system.service;

import cc.ussu.modules.system.entity.SysUserPost;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户与岗位关联表 服务类
 * </p>
 *
 * @author liming
 * @since 2021-12-31 20:05:21
 */
public interface ISysUserPostService extends IService<SysUserPost> {

    /**
     * 通过用户id获取
     */
    List<SysUserPost> getByUserId(String userId);

    /**
     * 通过岗位id获取
     */
    List<SysUserPost> getByPostId(String postId);

}
