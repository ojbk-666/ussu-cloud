package cn.ussu.modules.system.service;

import cn.ussu.common.core.entity.ReturnPageInfo;
import cn.ussu.modules.system.entity.SysDict;
import cn.ussu.modules.system.model.param.SysDictParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author liming
 * @since 2020-05-07
 */
public interface ISysDictService extends IService<SysDict> {

    ReturnPageInfo<SysDict> findPage(SysDictParam param);
}
