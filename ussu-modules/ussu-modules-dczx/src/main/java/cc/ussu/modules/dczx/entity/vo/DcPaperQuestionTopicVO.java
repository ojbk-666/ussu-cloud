package cc.ussu.modules.dczx.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Data
public class DcPaperQuestionTopicVO implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DAN_XUAN = "001";
    public static final String DUO_XUAN = "002";
    public static final String PAN_DUAN = "004";
    public static final String MING_CI_JIE_SHI = "10";
    public static final String JIAN_DA = "2";

    private String topicId;

    private String topicTypeCd;

    @TableId(type = IdType.INPUT)
    private String fullTopicTypeCd;

    /**
     * 题型名称
     */
    private String questionTypeNm;

}