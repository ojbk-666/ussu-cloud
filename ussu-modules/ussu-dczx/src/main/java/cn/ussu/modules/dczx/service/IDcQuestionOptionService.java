package cn.ussu.modules.dczx.service;

import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.modules.dczx.entity.DcQuestionOption;
import cn.ussu.modules.dczx.model.param.DcQuestionOptionParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 题目的选项 服务类
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
public interface IDcQuestionOptionService extends IService<DcQuestionOption> {

    /**
     * 分页查询
     */
    ReturnPageInfo<DcQuestionOption> findPage(DcQuestionOptionParam param);

    /**
     * 新增
     */
    // void addOne(DcQuestionOption obj);

    /**
     * 修改
     */
    // void updateOne(DcQuestionOption obj);

}
