package cn.ussu.modules.ecps.item.service;

import cn.ussu.modules.ecps.item.entity.EbItem;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品，包含手机和号卡，通过在类目表中预置的手机类目和号卡类目来区分。裸机：手机机体，不包含任何通信服务和绑定的费用的机器 服务类
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
public interface IEbItemService extends IService<EbItem> {

    EbItem detail(Integer id);

    void add(EbItem p);

    void edit(EbItem p);

    void del(String ids);

}
