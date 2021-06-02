package cn.ussu.modules.dczx.service;

import cn.ussu.common.core.entity.ReturnPageInfo;
import cn.ussu.modules.dczx.entity.DcInterfaceLog;
import cn.ussu.modules.dczx.model.param.DcInterfaceLogParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 接口日志 服务类
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
public interface IDcInterfaceLogService extends IService<DcInterfaceLog> {

    /**
     * 分页查询
     */
    ReturnPageInfo<DcInterfaceLog> findPage(DcInterfaceLogParam param);

    /**
     * 新增
     */
    // void addOne(DcInterfaceLog obj);

    /**
     * 修改
     */
    // void updateOne(DcInterfaceLog obj);

}
