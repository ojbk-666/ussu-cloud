package cn.ussu.modules.ecps.item.mapper;

import cn.ussu.modules.ecps.item.entity.EbItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 商品，包含手机和号卡，通过在类目表中预置的手机类目和号卡类目来区分。裸机：手机机体，不包含任何通信服务和绑定的费用的机器 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
public interface EbItemMapper extends BaseMapper<EbItem> {

}
