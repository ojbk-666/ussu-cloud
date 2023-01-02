package cc.ussu.modules.dczx.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class DcFeedbackReplyVO implements Serializable {

    private static final long serialVersionUID = 679821247476789929L;
    @NotNull(message = "id不能为空")
    private Long id;

    @NotBlank(message = "回复内容不能为空")
    private String replyContent;

    /**
     * 是否邮件回复
     */
    private Boolean emailReply = false;

}
