package cc.ussu.modules.sheep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 京东用户信息
 * </p>
 *
 * @author mp-generator
 * @since 2022-06-02 13:55:06
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName("jd_user_info")
public class JdUserInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 京东用户id
     */
    @TableId(value = "jd_user_id", type = IdType.INPUT)
    private String jdUserId;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 头像
     */
    @TableField("head_image_url")
    private String headImageUrl;

    @TableField("account_type")
    private String accountType;

    @TableField("level_name")
    private String levelName;

    @TableField("user_level")
    private String userLevel;

    /**
     * 京豆总数量
     */
    @TableField("bean_num")
    private Integer beanNum;

    /**
     * 优惠券数量
     */
    @TableField("coupon_num")
    private Integer couponNum;

    /**
     * 红包总额
     */
    @TableField("red_balance")
    private Float redBalance;

    /**
     * 是否是plus
     */
    @TableField("is_plus_vip")
    private String isPlusVip;

    /**
     * 是否实名认证
     */
    @TableField("is_real_name_auth")
    private String isRealNameAuth;

    /**
     * 小白守约分
     */
    @TableField("xb_keep_score")
    private Integer xbKeepScore;

    /**
     * 登录IP
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 最后时间
     */
    @TableField("last_time")
    private Date lastTime;

    /**
     * QQ
     */
    @TableField("qq")
    private String qq;

    /**
     * 注册IP
     */
    @TableField("reg_ip")
    private String regIp;

    /**
     * 注册日期
     */
    @TableField("reg_time")
    private Date regTime;

    /**
     * 性别
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 京享值文本
     */
    @TableField("uclass")
    private String uclass;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

}
