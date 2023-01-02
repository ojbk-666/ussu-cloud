package cc.ussu.modules.dczx.service.impl;

import cc.ussu.modules.dczx.entity.DcUserStudyPlan;
import cc.ussu.modules.dczx.mapper.DcUserStudyPlanMapper;
import cc.ussu.modules.dczx.model.vo.AllStudyPlanVO;
import cc.ussu.modules.dczx.model.vo.DczxLoginResultVo;
import cc.ussu.modules.dczx.model.vo.StudyPlanVO;
import cc.ussu.modules.dczx.service.DczxService;
import cc.ussu.modules.dczx.service.IDcUserStudyPlanService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 学习计划 服务实现类
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-09 21:41:09
 */
@Slave
@Service
public class DcUserStudyPlanServiceImpl extends ServiceImpl<DcUserStudyPlanMapper, DcUserStudyPlan> implements IDcUserStudyPlanService {

    @Autowired
    private DczxService dczxService;

    /**
     * 根据用户获取
     *
     * @param userid
     */
    @Override
    public List<DcUserStudyPlan> listByUserid(String userid) {
        if (StrUtil.isBlank(userid)) {
            return new ArrayList<>();
        }
        return this.list(Wrappers.lambdaQuery(DcUserStudyPlan.class).eq(DcUserStudyPlan::getUserid, userid));
    }

    /**
     * 通过userid和课程id更新
     *
     * @param p
     */
    @Override
    public boolean updateByUseridAndCourseId(DcUserStudyPlan p) {
        return super.update(p, Wrappers.lambdaQuery(DcUserStudyPlan.class)
                .eq(DcUserStudyPlan::getUserid, p.getUserid())
                .eq(DcUserStudyPlan::getCourseId, p.getCourseId()));
    }

    /**
     * 更新用户学习计划
     *
     * @param username
     * @param password
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public List<DcUserStudyPlan> updateUserStudyPlan(String username, String password, String createBy) {
        DczxLoginResultVo loginResultVo = dczxService.login(username, password);
        List<AllStudyPlanVO> allStudyPlan = dczxService.getAllStudyPlan(loginResultVo);
        Date now = new Date();
        List<DcUserStudyPlan> result = new ArrayList<>();
        if (CollUtil.isNotEmpty(allStudyPlan)) {
            for (AllStudyPlanVO item : allStudyPlan) {
                List<StudyPlanVO> list = item.getCourseList();
                if (CollUtil.isNotEmpty(list)) {
                    for (StudyPlanVO vo : list) {
                        DcUserStudyPlan dcUserStudyPlan = BeanUtil.toBean(vo, DcUserStudyPlan.class);
                        dcUserStudyPlan.setUserid(username)
                            .setButtonList(JSONUtil.toJsonStr(vo.getButtonList()))
                            .setCurrentFlag(item.getIsCurrent())
                            .setBeginTime(item.getBeginTime())
                            .setEndTime(item.getEndTime())
                            .setTermName(item.getTermName())
                            .setCreateBy(createBy)
                            .setUpdateTime(now)
                            .setDelFlag(false);
                        // 尝试更新
                        if (!this.updateByUseridAndCourseId(dcUserStudyPlan)) {
                            this.save(dcUserStudyPlan);
                        }
                        result.add(dcUserStudyPlan);
                    }
                }
            }
        }
        return result;
    }
}
