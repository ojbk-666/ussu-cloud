package cc.ussu.modules.ecps.order.service;

import cc.ussu.modules.ecps.order.entity.EbOrderDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 3-10是商品的冗余数据	11-26是营销案的冗余数据	营销案的时候，根据SKU存多条，每条SK 服务类
 * </p>
 *
 * @author liming
 * @since 2021-08-03
 */
public interface IEbOrderDetailService extends IService<EbOrderDetail> {

    EbOrderDetail detail(Integer id);

    void add(EbOrderDetail p);

    void edit(EbOrderDetail p);

    void del(String ids);

}
