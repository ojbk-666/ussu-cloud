package cc.ussu.modules.dczx.service;

import cc.ussu.modules.dczx.exception.LoginFailedException;
import cc.ussu.modules.dczx.model.vo.AllStudyPlanVO;
import cc.ussu.modules.dczx.model.vo.CurrentStudyPlanVO;
import cc.ussu.modules.dczx.model.vo.DczxLoginResultVo;

import java.util.List;

public interface DczxService {

    /**
     * 登录
     */
    DczxLoginResultVo login(String username, String password) throws LoginFailedException;

    /**
     * 验证ck有效性
     * @param loginResultVo
     * @return
     */
    boolean verifyCookieExpired(DczxLoginResultVo loginResultVo);

    /**
     * 获取全部学习计划
     */
    List<AllStudyPlanVO> getAllStudyPlan(DczxLoginResultVo loginResultVo);

    /**
     * 获取当前学习计划
     */
    List<CurrentStudyPlanVO> getCurrentStudyPlan(DczxLoginResultVo loginResultVo);

}
