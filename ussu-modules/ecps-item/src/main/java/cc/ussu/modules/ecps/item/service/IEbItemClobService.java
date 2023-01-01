package cc.ussu.modules.ecps.item.service;

import cc.ussu.modules.ecps.item.entity.EbItemClob;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
public interface IEbItemClobService extends IService<EbItemClob> {

    EbItemClob detail(Integer id);

    void add(EbItemClob p);

    void edit(EbItemClob p);

    void del(String ids);

    EbItemClob getByItemId(Integer itemId);

}
