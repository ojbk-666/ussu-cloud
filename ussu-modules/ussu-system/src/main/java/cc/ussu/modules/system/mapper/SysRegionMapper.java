package cc.ussu.modules.system.mapper;

import cc.ussu.modules.system.entity.SysRegion;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 省市区区域表 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2022-01-23 17:45:37
 */
@Master
@Mapper
public interface SysRegionMapper extends BaseMapper<SysRegion> {

    /**
     * 获取所有
     */
    List<SysRegion> getAll();

}
