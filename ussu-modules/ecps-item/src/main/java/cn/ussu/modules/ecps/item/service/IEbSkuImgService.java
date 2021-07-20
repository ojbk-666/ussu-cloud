package cn.ussu.modules.ecps.item.service;

import cn.ussu.modules.ecps.item.entity.EbSkuImg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
public interface IEbSkuImgService extends IService<EbSkuImg> {

    EbSkuImg detail(Integer id);

    void add(EbSkuImg p);

    void edit(EbSkuImg p);

    void del(String ids);

    List<EbSkuImg> getBySkuId(Integer skuId);

}
