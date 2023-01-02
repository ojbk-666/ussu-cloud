package cc.ussu.modules.dczx.service.impl;

import cc.ussu.modules.dczx.entity.DcFeedback;
import cc.ussu.modules.dczx.entity.vo.DcFeedbackReplyVO;
import cc.ussu.modules.dczx.mapper.DcFeedbackMapper;
import cc.ussu.modules.dczx.service.IDcFeedbackService;
import cc.ussu.system.api.RemoteSystemNoticeService;
import cc.ussu.system.api.vo.SendNoticeVO;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Slave
@Service
public class DcFeedbackServiceImpl extends ServiceImpl<DcFeedbackMapper, DcFeedback> implements IDcFeedbackService {

    @Autowired(required = false)
    private JavaMailSender javaMailSender;
    @Autowired(required = false)
    private MailProperties mailProperties;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private RemoteSystemNoticeService sysNoticeService;

    // @Transactional
    @Override
    public void reply(DcFeedbackReplyVO vo) {
        Long id = vo.getId();
        DcFeedback feedback = super.getById(id);
        Assert.notNull(feedback, "未找到该反馈数据");
        if (BooleanUtil.isTrue(vo.getEmailReply()) && Validator.isEmail(feedback.getEmail())) {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject("东财在线作业、答题助手 插件反馈的回复");
            simpleMailMessage.setFrom(mailProperties.getUsername());
            simpleMailMessage.setTo(feedback.getEmail());
            simpleMailMessage.setReplyTo(feedback.getEmail());
            simpleMailMessage.setText(vo.getReplyContent());
            // javaMailSender.send(simpleMailMessage);
            threadPoolTaskExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    javaMailSender.send(simpleMailMessage);
                }
            });
        }
        DcFeedback u = new DcFeedback().setId(id).setReplyContent(vo.getReplyContent());
        super.updateById(u);
        if (StrUtil.isNotBlank(feedback.getCreateBy())) {
            sysNoticeService.notice(new SendNoticeVO(feedback.getCreateBy(), "反馈信息已回复", vo.getReplyContent()));
        }
    }
}
