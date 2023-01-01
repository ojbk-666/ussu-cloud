package cc.ussu.modules.ecps.order.service;

import cc.ussu.modules.ecps.order.entity.EbOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单。包括实体商品和虚拟商品的订单 服务类
 * </p>
 *
 * @author liming
 * @since 2021-08-03
 */
public interface IEbOrderService extends IService<EbOrder> {

    EbOrder detail(Integer id);

    void add(EbOrder p);

    void edit(EbOrder p);

    void del(String ids);

    /**
     * 提交订单
     */
    EbOrder submitOrder(EbOrder order, String cartIds, String userId);

    /**
     * 关闭订单，回滚库存
     */
    boolean closeOrder(EbOrder order);

}
