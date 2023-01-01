package cc.ussu.modules.ecps.item.service;

import cc.ussu.modules.ecps.item.entity.EbFeatureGroup;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liming
 * @since 2021-07-16
 */
public interface IEbFeatureGroupService extends IService<EbFeatureGroup> {

    EbFeatureGroup detail(Integer id);

    void add(EbFeatureGroup p);

    void edit(EbFeatureGroup p);

    void del(String ids);

    void link(EbFeatureGroup p);
}
