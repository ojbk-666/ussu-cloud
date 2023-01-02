package cc.ussu.modules.dczx.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.constants.SystemLogConstants;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.dczx.entity.DcFeedback;
import cc.ussu.modules.dczx.entity.vo.DcFeedbackReplyVO;
import cc.ussu.modules.dczx.service.IDcFeedbackService;
import cc.ussu.system.api.RemoteSystemNoticeService;
import cc.ussu.system.api.vo.SendNoticeVO;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("${ussu.mapping-prefix.dczx}/dc-feedback")
public class DcFeedbackController {

    private static final String SYSTEM_LOG_GROUP = "东财在线插件反馈";

    @Autowired
    private IDcFeedbackService dcFeedbackService;
    @Autowired
    private RemoteSystemNoticeService sysNoticeService;

    @PermCheck("dczx:dc-feedback:select")
    @GetMapping("/page")
    public Object page(DcFeedback p) {
        LambdaQueryWrapper<DcFeedback> qw = Wrappers.lambdaQuery(DcFeedback.class)
                .orderByDesc(DcFeedback::getCreateTime)
                .eq(SecurityUtil.isNotSuperAdmin(), DcFeedback::getCreateBy, SecurityUtil.getLoginUser().getUserId())
                .eq(StrUtil.isNotBlank(p.getEmail()), DcFeedback::getEmail, p.getEmail());
        IPage iPage = dcFeedbackService.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(iPage);
    }

    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.INSERT)
    @PermCheck(("dczx:dc-feedback:add"))
    @PutMapping
    public JsonResult add(@Validated @RequestBody DcFeedback p) {
        p.setId(null).setCreateTime(new Date()).setCreateBy(SecurityUtil.getLoginUser().getUserId());
        dcFeedbackService.save(p);
        sysNoticeService.noticeSuperAdmin(new SendNoticeVO().setTitle("有新的东财在线反馈").setContent(p.getContent()));
        return JsonResult.ok();
    }

    /**
     * 回复
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = "回复")
    @PermCheck("dczx:dc-feedback:reply")
    @PostMapping("/reply")
    public JsonResult reply(@Validated @RequestBody DcFeedbackReplyVO vo) {
        dcFeedbackService.reply(vo);
        return JsonResult.ok();
    }

}
