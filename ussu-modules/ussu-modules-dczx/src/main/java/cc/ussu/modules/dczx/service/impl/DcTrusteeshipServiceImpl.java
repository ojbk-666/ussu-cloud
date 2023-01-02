package cc.ussu.modules.dczx.service.impl;

import cc.ussu.modules.dczx.constants.DczxConstants;
import cc.ussu.modules.dczx.entity.DcTask;
import cc.ussu.modules.dczx.entity.DcTrusteeship;
import cc.ussu.modules.dczx.entity.DcUserStudyPlan;
import cc.ussu.modules.dczx.entity.vo.DcTrusteeshipVO;
import cc.ussu.modules.dczx.mapper.DcTrusteeshipMapper;
import cc.ussu.modules.dczx.service.IDcTaskService;
import cc.ussu.modules.dczx.service.IDcTrusteeshipService;
import cc.ussu.modules.dczx.service.IDcUserStudyPlanService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 托管列表 服务实现类
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-09 20:32:44
 */
@Slf4j
@Slave
@Service
public class DcTrusteeshipServiceImpl extends ServiceImpl<DcTrusteeshipMapper, DcTrusteeship> implements IDcTrusteeshipService {

    @Autowired
    private IDcUserStudyPlanService userStudyPlanService;
    @Autowired
    private IDcTaskService dcTaskService;

    /**
     * 更新学习计划
     */
    private void updateStudyPlan(DcTrusteeshipVO p) {
        List<DcUserStudyPlan> dcUserStudyPlans = userStudyPlanService.updateUserStudyPlan(p.getUsername(), p.getPassword(), p.getCreateBy());
        if (CollUtil.isNotEmpty(dcUserStudyPlans)) {
            List<DcUserStudyPlan> notFinishedList = new ArrayList<>();
            for (DcUserStudyPlan dcUserStudyPlan : dcUserStudyPlans) {
                if (DcUserStudyPlan.CURRENT_FLAG_TRUE.equals(dcUserStudyPlan.getCurrentFlag()) && !"100".equals(dcUserStudyPlan.getStudyProgress())) {
                    notFinishedList.add(dcUserStudyPlan);
                }
            }
        }
    }

    @Async
    @Override
    public void createTaskAsync(String username, String password, List<DcUserStudyPlan> notFinishedList) {
        if (CollUtil.isNotEmpty(notFinishedList)) {
            // 创建任务
            for (DcUserStudyPlan item : notFinishedList) {
                DcTask dcTask = new DcTask().setCourseId(item.getCourseId())
                        .setTaskType(DcTask.TYPE_VIDEO).setDcUsername(username);
                try {
                    dcTaskService.submitTask(dcTask, false);
                } catch (Exception e) {
                    log.info("提交任务异常：{}", e.getMessage());
                }
            }
        }
    }

    @Override
    public DcTrusteeship getByUsername(String username) {
        Assert.notBlank(username, "username不能为空");
        DcTrusteeship one = getOne(Wrappers.lambdaQuery(DcTrusteeship.class).eq(DcTrusteeship::getUsername, username));
        return one;
    }

    private void checkExists(DcTrusteeship trusteeship) {
        long c = count(Wrappers.lambdaQuery(DcTrusteeship.class)
                .eq(DcTrusteeship::getUsername, trusteeship.getUsername())
                .ne(StrUtil.isNotBlank(trusteeship.getId()), DcTrusteeship::getId, trusteeship.getId()));
        Assert.isFalse(c > 0, "该账号已添加，请勿重复添加");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(DcTrusteeshipVO p) {
        // 保存主表
        DcTrusteeship dcTrusteeship = BeanUtil.toBean(p, DcTrusteeship.class);
        // 校验是否已有该账号
        checkExists(dcTrusteeship);
        super.save(dcTrusteeship);
        // 学习计划
        updateStudyPlan(p);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(DcTrusteeshipVO p) {
        // 保存主表
        DcTrusteeship dcTrusteeship = BeanUtil.toBean(p, DcTrusteeship.class);
        checkExists(dcTrusteeship);
        super.updateById(dcTrusteeship);
        updateStudyPlan(p);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delIds(List<String> idList) {
        Assert.notEmpty(idList, "参数不能为空");
        List<DcTrusteeship> list = super.listByIds(idList);
        List<String> usernameList = list.stream().map(DcTrusteeship::getUsername).collect(Collectors.toList());
        // 检查是否有正在执行任务的账号
        List<DcTask> taskList = dcTaskService.list(Wrappers.lambdaQuery(DcTask.class).in(DcTask::getDcUsername, usernameList));
        boolean b = taskList.stream().anyMatch(r -> !DczxConstants.TASK_STATUS_FINISHED.equals(r.getStatus()));
        Assert.isFalse(b, "无法删除！有未完成的任务");
        super.removeByIds(idList);
    }
}
