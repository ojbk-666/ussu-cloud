package cn.ussu.modules.system.mapper;

import cn.ussu.modules.system.entity.SysDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-05-07
 */
@Repository
public interface SysDictMapper extends BaseMapper<SysDict> {

    SysDict findByTypeId(@Param("typeId") String typeId);

}
