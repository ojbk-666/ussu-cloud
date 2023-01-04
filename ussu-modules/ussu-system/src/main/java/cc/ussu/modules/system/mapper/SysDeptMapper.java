package cc.ussu.modules.system.mapper;

import cc.ussu.modules.system.entity.SysDept;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 部门 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Master
public interface SysDeptMapper extends BaseMapper<SysDept> {

    /**
     * 获取id
     */
    @Select("select max(CONVERT(id, SIGNED))+1 from sys_dept")
    String getNextId();

}
