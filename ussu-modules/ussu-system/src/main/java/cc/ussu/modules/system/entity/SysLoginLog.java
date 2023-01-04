package cc.ussu.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户会话记录/登录日志 表
 * </p>
 *
 * @author liming
 * @since 2022-01-01 17:31:29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_login_log")
public class SysLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String RESULT_SUCCESS = "1";
    public static final String RESULT_FAILED = "0";

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 账号
     */
    @TableField("account")
    private String account;

    /**
     * uuid
     */
    @TableField("uuid")
    private String uuid;

    /**
     * 登录时的ip
     */
    @TableField("login_ip")
    private String loginIp;

    @TableField("ip_location")
    private String ipLocation;

    /**
     * 登录结果
     */
    @TableField("result")
    private String result;

    /**
     * 登录时使用的设备名称
     */
    @TableField("device_name")
    private String deviceName;

    /**
     * 登录时使用的浏览器
     */
    @TableField("browser")
    private String browser;

    /**
     * 详细的ua信息
     */
    @TableField("user_agent")
    private String userAgent;

    /**
     * 登录时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

}
