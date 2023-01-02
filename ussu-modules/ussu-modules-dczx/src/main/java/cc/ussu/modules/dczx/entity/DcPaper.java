package cc.ussu.modules.dczx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * paper
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DcPaper implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 试卷id
     */
    @TableId(type = IdType.INPUT)
    private String paperId;

    /**
     * 试卷名称
     */
    private String paperName;

    private Date createTime;

    private Long interfaceLogId;

}
