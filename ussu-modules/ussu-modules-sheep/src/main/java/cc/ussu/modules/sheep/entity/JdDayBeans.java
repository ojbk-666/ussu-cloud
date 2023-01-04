package cc.ussu.modules.sheep.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 每日京豆收支
 * </p>
 *
 * @author mp-generator
 * @since 2022-04-01 11:13:57
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("jd_day_beans")
public class JdDayBeans implements Serializable{

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 京东用户名
     */
    @TableField("jd_user_id")
    private String jdUserId;

    /**
     * 昵称
     */
    @TableField(exist = false)
    private String nickname;

    /**
     * 所属日期
     */
    @TableField("create_date")
    private Date createDate;

    /**
     * 收入京豆
     */
    @TableField("income_bean")
    private Integer incomeBean;

    /**
     * 支出京豆
     */
    @TableField("out_bean")
    private Integer outBean;

}
