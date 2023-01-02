package cc.ussu.modules.dczx.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("dc_feedback")
public class DcFeedback implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String email;

    private String content;

    private String replyContent;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private String createBy;

}
