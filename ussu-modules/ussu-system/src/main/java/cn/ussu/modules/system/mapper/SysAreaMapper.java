package cn.ussu.modules.system.mapper;

import cn.ussu.modules.system.entity.SysArea;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 省市区区域表 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-05-17
 */
public interface SysAreaMapper extends BaseMapper<SysArea> {

    // IPage<Map> findPage(Page page, @Param("param") Map param);

    List<Map<String, Object>> findCascaderByParendId(@Param("pid") Integer pid);

}
