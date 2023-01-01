package cc.ussu.modules.ecps.item.mapper;

import cc.ussu.modules.ecps.item.entity.EbFeature;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品属性	预置的手机参数（请将预置可选值补充完整）	1.      型号             Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2021-07-16
 */
public interface EbFeatureMapper extends BaseMapper<EbFeature> {

    void updateGroupIdNullByGroupIdAndCatId(@Param("groupId") Integer groupId, @Param("catId") Integer catId);

    IPage<EbFeature> findPage(Page page, @Param("p") EbFeature p);

}
