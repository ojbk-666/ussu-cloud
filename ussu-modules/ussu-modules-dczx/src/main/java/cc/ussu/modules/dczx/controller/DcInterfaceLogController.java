package cc.ussu.modules.dczx.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.modules.dczx.entity.DcInterfaceLog;
import cc.ussu.modules.dczx.entity.DcPaperQuestion;
import cc.ussu.modules.dczx.model.vo.homework.ShowHistoryPapermcapiResponse;
import cc.ussu.modules.dczx.service.IDcInterfaceLogService;
import cc.ussu.modules.dczx.thread.SaveDczxPaperQuestionThread;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 接口日志 控制器
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.dczx}/dc-interface-log")
public class DcInterfaceLogController extends BaseController {

    @Autowired
    private IDcInterfaceLogService service;

    /*
     * 分页查询
     */
    @GetMapping("/page")
    public Object list(DcInterfaceLog p) {
        LambdaQueryWrapper<DcInterfaceLog> qw = Wrappers.lambdaQuery(DcInterfaceLog.class)
                .orderByDesc(DcInterfaceLog::getCreateTime).orderByDesc(DcInterfaceLog::getId)
                .like(StrUtil.isNotBlank(p.getUserid()), DcInterfaceLog::getUserid, p.getUserid())
                .like(StrUtil.isNotBlank(p.getRemarks()), DcInterfaceLog::getRemarks, p.getRemarks());
        return MybatisPlusUtil.getResult(service.page(MybatisPlusUtil.getPage(), qw));
    }

    /**
     * 补充数据
     */
    @GetMapping("/fill/{id}")
    public Object fillData(@PathVariable("id") String id) {
        DcInterfaceLog one = service.getById(id);
        if (one != null) {
            if (BooleanUtil.isTrue(one.getResult())) {
                SaveDczxPaperQuestionThread saveDczxPaperQuestionThread = new SaveDczxPaperQuestionThread(one);
                ShowHistoryPapermcapiResponse showHistoryPapermcapiResponse = JSONUtil.toBean(one.getResponseBody(), ShowHistoryPapermcapiResponse.class);
                List<DcPaperQuestion> questionList = saveDczxPaperQuestionThread.analyzeQuestionJson(showHistoryPapermcapiResponse);
                if (CollUtil.isNotEmpty(questionList)) {
                    Date now = new Date();
                    questionList.forEach(r -> {
                        r.setCreateBy(one.getCreateBy()).setCreateTime(now).setInterfaceLogId(one.getId());
                        r.getOptions().forEach(r2 -> r2.setCreateBy(one.getCreateBy()).setCreateTime(now).setInterfaceLogId(one.getId()));
                    });
                    saveDczxPaperQuestionThread.saveQuestion(questionList);
                    saveDczxPaperQuestionThread.saveOption(questionList);
                    saveDczxPaperQuestionThread.saveTopic(questionList);
                    // DynamicDataSourceContextHolder.clear();
                }
            }
        }
        return JsonResult.ok();
    }

}
