package cn.ussu.modules.system.mapper;

import cn.ussu.modules.system.entity.SysDept;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-05-16
 */
public interface SysDeptMapper extends BaseMapper<SysDept> {

    @SqlParser(filter = true)
    List<Map> findSubDeptByParentId(@Param("pid") String pid);

    List<SysDept> listAll2();

    // IPage<Map> findPage(Page page, @Param("param") Map param);

}
