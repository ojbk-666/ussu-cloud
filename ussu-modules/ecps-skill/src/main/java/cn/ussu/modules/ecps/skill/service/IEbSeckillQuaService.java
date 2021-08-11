package cn.ussu.modules.ecps.skill.service;

import cn.ussu.modules.ecps.skill.entity.EbSeckillQua;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liming
 * @since 2021-08-11
 */
public interface IEbSeckillQuaService extends IService<EbSeckillQua> {

    EbSeckillQua detail(Integer id);

    void add(EbSeckillQua p);

    void edit(EbSeckillQua p);

    void del(String ids);

}
