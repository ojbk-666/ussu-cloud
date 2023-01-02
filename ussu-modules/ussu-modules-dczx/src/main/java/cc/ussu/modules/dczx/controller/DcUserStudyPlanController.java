package cc.ussu.modules.dczx.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.vo.SelectVO;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.dczx.entity.DcTask;
import cc.ussu.modules.dczx.entity.DcTrusteeship;
import cc.ussu.modules.dczx.entity.DcUserStudyPlan;
import cc.ussu.modules.dczx.entity.vo.DcUserStudyPlanVO;
import cc.ussu.modules.dczx.service.IDcTrusteeshipService;
import cc.ussu.modules.dczx.service.IDcUserStudyPlanService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 学习计划 前端控制器
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-09 21:41:09
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.dczx}/dc-user-study-plan")
public class DcUserStudyPlanController extends BaseController {

    @Autowired
    private IDcUserStudyPlanService service;
    @Autowired
    private IDcTrusteeshipService trusteeshipService;

    /**
     * 分页
     */
    @PermCheck("dczx:dc-user-study-plan:select")
    @GetMapping("/page")
    public Object page(DcUserStudyPlanVO p) {
        LambdaQueryWrapper<DcUserStudyPlan> qw = Wrappers.lambdaQuery(DcUserStudyPlan.class)
                .orderByAsc(DcUserStudyPlan::getBeginTime)
                .eq(SecurityUtil.isNotSuperAdmin(), DcUserStudyPlan::getCreateBy, SecurityUtil.getLoginUser().getUserId())
                .eq(StrUtil.isNotBlank(p.getCurrentFlag()), DcUserStudyPlan::getCurrentFlag, p.getCurrentFlag())
                .eq(StrUtil.isNotBlank(p.getUserid()), DcUserStudyPlan::getUserid, p.getUserid())
                .like(StrUtil.isNotBlank(p.getCourseName()), DcUserStudyPlan::getCourseName, p.getCourseName());
        IPage<DcUserStudyPlan> iPage = service.page(MybatisPlusUtil.getPage(), qw);
        List<DcUserStudyPlanVO> list = iPage.getRecords().stream().map(r -> BeanUtil.toBean(r, DcUserStudyPlanVO.class).setButtonList(null)).collect(Collectors.toList());
        return MybatisPlusUtil.getResult(iPage.getTotal(), list);
    }

    /**
     * 单个详情
     */
    @PermCheck("dczx:dc-user-study-plan:select")
    @GetMapping({"/{id}", "/detail/{id}"})
    public JsonResult<DcUserStudyPlanVO> detail(@PathVariable String id) {
        DcUserStudyPlan one = service.getById(id);
        DcUserStudyPlanVO vo = BeanUtil.toBean(one, DcUserStudyPlanVO.class);
        return JsonResult.ok(vo);
    }

    /**
     * 获取可选课程
     *
     * @param tid      托管账号id
     * @param taskType 类型
     */
    @GetMapping("/select-course/{tid}/{taskType}")
    public JsonResult<List<SelectVO>> getSelectList(@PathVariable String tid, @PathVariable String taskType) {
        Assert.notBlank(tid);
        DcTrusteeship one = trusteeshipService.getById(tid);
        Assert.notNull(one, "账号不存在");
        if (SecurityUtil.isNotSuperAdmin()) {
            String userId = SecurityUtil.getLoginUser().getUserId();
            Assert.isTrue(userId.equals(one.getCreateBy()), "没有权限");
        }
        List<DcUserStudyPlan> list = service.list(Wrappers.lambdaQuery(DcUserStudyPlan.class).eq(DcUserStudyPlan::getUserid, one.getUsername()));
        List<SelectVO> selectVOList = list.stream().map(r -> {
            String buttonList = r.getButtonList();
            JSONObject button = JSONUtil.parseObj(buttonList);
            boolean disabled = false;
            if (DcTask.TYPE_HOMEWORK.equals(taskType)) {
                disabled = "0".equals(button.getStr("homework"));
            } else if (DcTask.TYPE_COMP_HOMEWORK.equals(taskType)) {
                disabled = "0".equals(button.getStr("compHomework"));
            } else if (DcTask.TYPE_VIDEO.equals(taskType)) {
                disabled = "0".equals(button.getStr("studyStatus"));
            }
            SelectVO<String> selectVo = new SelectVO<String>().setLabel(r.getCourseName()).setValue(r.getCourseId()).setDisabled(disabled);
            return selectVo;
        }).collect(Collectors.toList());
        return JsonResult.ok(selectVOList);
    }

    /**
     * 刷新学习计划
     */
    @SystemLog(group = "东财在线-学习计划", name = "刷新学习计划")
    @PermCheck("dczx:dc-user-study-plan:refresh")
    @PostMapping("/refresh-user-study-plan/{trusteeshipId}")
    public JsonResult refreshUserStudyPlan(@PathVariable String trusteeshipId) {
        DcTrusteeship trusteeship = trusteeshipService.getById(trusteeshipId);
        Assert.notNull(trusteeship, "未找到该账号");
        if (SecurityUtil.isNotSuperAdmin()) {
            String createBy = trusteeship.getCreateBy();
            Assert.isTrue(SecurityUtil.getLoginUser().getUserId().equals(createBy), "没有操作权限");
        }
        service.updateUserStudyPlan(trusteeship.getUsername(), trusteeship.getPassword(), trusteeship.getCreateBy());
        return JsonResult.ok();
    }

}

