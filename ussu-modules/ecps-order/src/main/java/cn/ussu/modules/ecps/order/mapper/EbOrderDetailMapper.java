package cn.ussu.modules.ecps.order.mapper;

import cn.ussu.modules.ecps.order.entity.EbOrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 3-10是商品的冗余数据	11-26是营销案的冗余数据	营销案的时候，根据SKU存多条，每条SK Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2021-08-03
 */
public interface EbOrderDetailMapper extends BaseMapper<EbOrderDetail> {

    List<EbOrderDetail> findByOrderId(@Param("orderId") Integer orderId);

}
