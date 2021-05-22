package cn.ussu.modules.system.mapper;

import cn.ussu.modules.system.entity.SysDictType;
import cn.ussu.modules.system.model.result.SysDictTypeResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 字典类型表 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-05-07
 */
@Repository
public interface SysDictTypeMapper extends BaseMapper<SysDictType> {

    List<SysDictTypeResult> listAllSysDictTypeResult();

}
