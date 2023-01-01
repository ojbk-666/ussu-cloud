package cc.ussu.modules.ecps.member.service;

import cc.ussu.modules.ecps.member.entity.EbUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liming
 * @since 2021-07-29
 */
public interface IEbUserService extends IService<EbUser> {

    EbUser detail(Integer id);

    void add(EbUser p);

    void edit(EbUser p);

    void del(String ids);

}
