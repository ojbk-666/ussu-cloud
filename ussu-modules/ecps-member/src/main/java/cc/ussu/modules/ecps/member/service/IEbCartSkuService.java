package cc.ussu.modules.ecps.member.service;

import cc.ussu.modules.ecps.member.entity.EbCartSku;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liming
 * @since 2021-07-30
 */
public interface IEbCartSkuService extends IService<EbCartSku> {

    EbCartSku detail(Integer id);

    void add(EbCartSku p);

    void edit(EbCartSku p);

    void del(String ids);

    /**
     * 商品加入购物车
     */
    Integer addSkuToCartBySkuId(Integer skuId);

}
