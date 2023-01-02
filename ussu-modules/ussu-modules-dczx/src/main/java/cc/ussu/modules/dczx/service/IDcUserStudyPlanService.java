package cc.ussu.modules.dczx.service;

import cc.ussu.modules.dczx.entity.DcUserStudyPlan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 学习计划 服务类
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-09 21:41:09
 */
public interface IDcUserStudyPlanService extends IService<DcUserStudyPlan> {

    /**
     * 根据用户获取
     */
    List<DcUserStudyPlan> listByUserid(String userid);

    /**
     * 通过userid和课程id更新
     */
    boolean updateByUseridAndCourseId(DcUserStudyPlan p);

    /**
     * 更新用户学习计划
     */
    List<DcUserStudyPlan> updateUserStudyPlan(String username, String password, String createBy);

}
