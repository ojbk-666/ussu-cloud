package cn.ussu.modules.ecps.skill.service;

import cn.ussu.modules.ecps.skill.entity.EbSeckillSku;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liming
 * @since 2021-08-11
 */
public interface IEbSeckillSkuService extends IService<EbSeckillSku> {

    EbSeckillSku detail(Integer id);

    void add(EbSeckillSku p);

    void edit(EbSeckillSku p);

    void del(String ids);

}
