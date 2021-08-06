package cn.ussu.modules.ecps.item.service;

import cn.ussu.modules.ecps.item.entity.EbSku;
import cn.ussu.modules.ecps.order.entity.EbOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 最小销售单元，包括实体商品、虚拟商品（如号卡、套卡、话费等）	将要增加的字段：	STOCK_IN 服务类
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
public interface IEbSkuService extends IService<EbSku> {

    EbSku detail(Integer id, boolean includeItem);

    EbSku detail2(Integer skuId, boolean includeItem);

    void add(EbSku p);

    void edit(EbSku p);

    void del(String ids);

    List<EbSku> getByItemId(Integer itemId);

    void upSkuBySkuId(String skuId);

    /**
     * 更新商品库存
     */
    void updateStock(EbSku sku);

    /**
     * 回滚库存
     */
    void rollbackStock(EbOrder order);
}
