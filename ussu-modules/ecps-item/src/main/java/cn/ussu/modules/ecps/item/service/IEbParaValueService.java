package cn.ussu.modules.ecps.item.service;

import cn.ussu.modules.ecps.item.entity.EbParaValue;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
public interface IEbParaValueService extends IService<EbParaValue> {

    EbParaValue detail(Integer id);

    void add(EbParaValue p);

    void edit(EbParaValue p);

    void del(String ids);

}
