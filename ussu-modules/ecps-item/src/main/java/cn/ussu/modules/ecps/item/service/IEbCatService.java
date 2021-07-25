package cn.ussu.modules.ecps.item.service;

import cn.ussu.modules.ecps.item.entity.EbCat;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品类目。	1. 电商一期只支持两种商品，即手机和号卡。促销活动作为一种规则配置到上述两种商品上。二期会增加 服务类
 * </p>
 *
 * @author liming
 * @since 2021-07-13
 */
public interface IEbCatService extends IService<EbCat> {

    List<EbCat> listTree();

    void add(EbCat p);

    void edit(EbCat p);

    void del(String ids);

}
