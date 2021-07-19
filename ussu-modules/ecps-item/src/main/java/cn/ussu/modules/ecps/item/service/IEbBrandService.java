package cn.ussu.modules.ecps.item.service;

import cn.ussu.modules.ecps.item.entity.EbBrand;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 品牌 服务类
 * </p>
 *
 * @author liming
 * @since 2021-07-14
 */
public interface IEbBrandService extends IService<EbBrand> {

    void add(EbBrand brand);

    EbBrand detail(Integer brandId);

    void edit(EbBrand brand);

    void del(String ids);

}
