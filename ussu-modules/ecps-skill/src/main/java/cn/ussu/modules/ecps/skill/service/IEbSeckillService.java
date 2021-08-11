package cn.ussu.modules.ecps.skill.service;

import cn.ussu.modules.ecps.skill.entity.EbSeckill;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liming
 * @since 2021-08-11
 */
public interface IEbSeckillService extends IService<EbSeckill> {

    EbSeckill detail(Integer id);

    void add(EbSeckill p);

    void edit(EbSeckill p);

    void del(String ids);

}
