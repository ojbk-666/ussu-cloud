package cc.ussu.modules.dczx.service;

import cc.ussu.modules.dczx.entity.DcFeedback;
import cc.ussu.modules.dczx.entity.vo.DcFeedbackReplyVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IDcFeedbackService extends IService<DcFeedback> {

    /**
     * 回复反馈
     */
    void reply(DcFeedbackReplyVO vo);

}
