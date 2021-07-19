package cn.ussu.modules.ecps.item.mapper;

import cn.ussu.modules.ecps.item.entity.EbCat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 商品类目。	1. 电商一期只支持两种商品，即手机和号卡。促销活动作为一种规则配置到上述两种商品上。二期会增加 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2021-07-13
 */
public interface EbCatMapper extends BaseMapper<EbCat> {

}
