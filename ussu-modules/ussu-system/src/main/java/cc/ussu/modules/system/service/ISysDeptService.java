package cc.ussu.modules.system.service;

import cc.ussu.modules.system.entity.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 部门 服务类
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
public interface ISysDeptService extends IService<SysDept> {

    String ROOT_PARENT_ID = "0";
    String ZERO = "0";

    List<SysDept> renderToTree(List<SysDept> list);

    /**
     * 新增
     */
    void add(SysDept p);

    /**
     * 编辑
     */
    void edit(SysDept p);
}
