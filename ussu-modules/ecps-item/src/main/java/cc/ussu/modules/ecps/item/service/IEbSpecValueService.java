package cc.ussu.modules.ecps.item.service;

import cc.ussu.modules.ecps.item.entity.EbSpecValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 规格值（与价格有关） 服务类
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
public interface IEbSpecValueService extends IService<EbSpecValue> {

    EbSpecValue detail(Integer id);

    void add(EbSpecValue p);

    void edit(EbSpecValue p);

    void del(String ids);

    List<EbSpecValue> getBySkuId(Integer skuId);

}
