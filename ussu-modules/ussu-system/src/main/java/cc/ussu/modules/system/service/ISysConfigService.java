package cc.ussu.modules.system.service;

import cc.ussu.modules.system.entity.SysConfig;
import cc.ussu.modules.system.entity.vo.SysConfigGroupVO;
import cc.ussu.modules.system.entity.vo.SysConfigVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统配置表 服务类
 * </p>
 *
 * @author mp-generator
 * @since 2023-03-01 14:45:52
 */
public interface ISysConfigService extends IService<SysConfig> {

    /**
     * 刷新缓存
     */
    void refreshCache();

    /**
     * 通过分组编码查询分组信息
     */
    SysConfigGroupVO getByGroupCode(String groupCode);

    /**
     * 检查分组编码是否存在
     *
     * @return 是否存在
     */
    boolean existGroupByGroupCode(String groupCode, String id);

    /**
     * 检查分组下编码是否存在
     *
     * @return 是否存在
     */
    boolean existDataByGroupCodeAndCode(String groupCode, String code, String id);

    /**
     * 添加分组
     */
    void addGroup(SysConfigGroupVO vo);

    /**
     * 修改分组
     */
    void editGroup(SysConfigGroupVO vo);

    /**
     * 添加数据
     */
    void addData(SysConfigVO vo);

    /**
     * 修改数据
     */
    void editData(SysConfigVO vo);
}
