package cc.ussu.modules.ecps.member.service;

import cc.ussu.modules.ecps.member.entity.EbShipAddr;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 收货地址 服务类
 * </p>
 *
 * @author liming
 * @since 2021-07-29
 */
public interface IEbShipAddrService extends IService<EbShipAddr> {

    EbShipAddr detail(Integer id);

    void add(EbShipAddr p);

    void edit(EbShipAddr p);

    void del(String ids);

}
