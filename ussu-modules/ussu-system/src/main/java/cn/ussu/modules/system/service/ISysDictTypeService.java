package cn.ussu.modules.system.service;

import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.modules.system.entity.SysDictType;
import cn.ussu.modules.system.model.param.SysDictTypeParam;
import cn.ussu.modules.system.model.result.SysDictTypeResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字典类型表 服务类
 * </p>
 *
 * @author liming
 * @since 2020-05-07
 */
public interface ISysDictTypeService extends IService<SysDictType> {

    ReturnPageInfo<SysDictType> findPage(SysDictTypeParam param);

    void deleteDictTypeAndData(String id);

    List<SysDictTypeResult> listAllSysDictTypeResultList();

}
