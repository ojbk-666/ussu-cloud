package cn.ussu.modules.ecps.order.mapper;

import cn.ussu.modules.ecps.order.entity.EbOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单。包括实体商品和虚拟商品的订单 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2021-08-03
 */
public interface EbOrderMapper extends BaseMapper<EbOrder> {

    IPage findPage(Page page, @Param("p") EbOrder p);

}
