package cn.ussu.modules.dczx.service;

import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.modules.dczx.entity.DcPaper;
import cn.ussu.modules.dczx.model.param.DcPaperParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * paper 服务类
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
public interface IDcPaperService extends IService<DcPaper> {

    /**
     * 分页查询
     */
    ReturnPageInfo<DcPaper> findPage(DcPaperParam param);

    /**
     * 新增
     */
    // void addOne(DcPaper obj);

    /**
     * 修改
     */
    // void updateOne(DcPaper obj);

}
