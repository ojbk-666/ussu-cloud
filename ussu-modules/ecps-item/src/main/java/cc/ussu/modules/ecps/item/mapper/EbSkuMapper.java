package cc.ussu.modules.ecps.item.mapper;

import cc.ussu.modules.ecps.item.entity.EbSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 最小销售单元，包括实体商品、虚拟商品（如号卡、套卡、话费等）	将要增加的字段：	STOCK_IN Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
public interface EbSkuMapper extends BaseMapper<EbSku> {

    void rollbackStock(@Param("skuId") Integer skuId, @Param("num") Integer num);

}
