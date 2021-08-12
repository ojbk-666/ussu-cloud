package cn.ussu.modules.ecps.skill.service;

import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.ecps.skill.entity.EbSeckillSession;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liming
 * @since 2021-08-11
 */
public interface IEbSeckillSessionService extends IService<EbSeckillSession> {

    EbSeckillSession detail(Integer id);

    void add(EbSeckillSession p);

    void edit(EbSeckillSession p);

    void del(String ids);

    /**
     * 上架当天的秒杀商品
     */
    void uploadSkillSku();

    /**
     * 秒杀商品
     */
    JsonResult seckillSku(Integer sku, String randomCode, Integer num);

}
