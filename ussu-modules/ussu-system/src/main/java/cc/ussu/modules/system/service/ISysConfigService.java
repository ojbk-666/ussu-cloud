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

    SysConfigGroupVO getByGroupCode(String groupCode);

    void addGroup(SysConfigGroupVO vo);

    void editGroup(SysConfigGroupVO vo);

    void addData(SysConfigVO vo);

    void editData(SysConfigVO vo);
}
