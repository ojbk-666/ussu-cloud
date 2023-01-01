package cc.ussu.modules.ecps.item.service;

import cc.ussu.modules.ecps.item.entity.EbFeature;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品属性	预置的手机参数（请将预置可选值补充完整）	1.      型号             服务类
 * </p>
 *
 * @author liming
 * @since 2021-07-16
 */
public interface IEbFeatureService extends IService<EbFeature> {

    EbFeature detail(Integer id);

    void add(EbFeature p);

    void edit(EbFeature p);

    void del(String ids);

}
