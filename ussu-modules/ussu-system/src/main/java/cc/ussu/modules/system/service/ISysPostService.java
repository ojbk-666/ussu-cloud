package cc.ussu.modules.system.service;

import cc.ussu.modules.system.entity.SysPost;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 岗位 服务类
 * </p>
 *
 * @author liming
 * @since 2021-12-31 19:24:31
 */
public interface ISysPostService extends IService<SysPost> {

    boolean checkCodeExist(SysPost p);
}
