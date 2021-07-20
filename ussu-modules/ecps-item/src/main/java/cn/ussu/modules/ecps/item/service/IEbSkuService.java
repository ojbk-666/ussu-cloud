package cn.ussu.modules.ecps.item.service;

import cn.ussu.modules.ecps.item.entity.EbSku;
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

    EbSku detail(Integer id);

    void add(EbSku p);

    void edit(EbSku p);

    void del(String ids);

    List<EbSku> getByItemId(Integer itemId);

}
