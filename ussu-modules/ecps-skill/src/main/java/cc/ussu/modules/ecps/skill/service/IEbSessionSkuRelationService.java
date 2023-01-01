package cc.ussu.modules.ecps.skill.service;

import cc.ussu.modules.ecps.skill.entity.EbSessionSkuRelation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liming
 * @since 2021-08-11
 */
public interface IEbSessionSkuRelationService extends IService<EbSessionSkuRelation> {

    EbSessionSkuRelation detail(Integer id);

    void add(EbSessionSkuRelation p);

    void edit(EbSessionSkuRelation p);

    void del(String ids);

}
