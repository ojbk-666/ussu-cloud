package cc.ussu.modules.system.entity;

import cn.hutool.core.lang.RegexPool;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统用户
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 所在部门
     */
    @TableField("dept_id")
    private String deptId;

    /**
     * 登录名
     */
    @NotBlank(message = "用户名不能为空")
    @TableField("account")
    private String account;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 姓名
     */
    @NotBlank(message = "昵称不能为空")
    @TableField("nick_name")
    private String nickName;

    /**
     * 性别：1，男；2，女
     */
    @Pattern(regexp = "[12]", message = "性别只能取1男 2女")
    @TableField("sex")
    private Integer sex;

    /**
     * 邮箱
     */
    @Email
    @TableField("email")
    private String email;

    /**
     * 手机
     */
    @Pattern(regexp = RegexPool.MOBILE, message = "手机号格式不正确")
    @TableField("phone")
    private String phone;

    /**
     * 用户类型
     */
    @TableField("user_type")
    private String userType;

    /**
     * 用户排序
     */
    @TableField("user_sort")
    private Integer userSort;

    /**
     * 用户头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 最后登陆IP
     */
    @TableField("login_ip")
    private String loginIp;

    /**
     * 最后登陆时间
     */
    @TableField("last_login_time")
    private Date lastLoginTime;

    /**
     * 是否禁用
     */
    @TableField("disable_flag")
    private String disableFlag;

    /**
     * 账号状态：10 20 30
     */
    @TableField("state")
    private Integer state;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @PastOrPresent
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @PastOrPresent
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 是否删除：1，是；0，否
     */
    @JsonIgnore
    @TableField("del_flag")
    @TableLogic
    private String delFlag;

    /**
     * 乐观锁
     */
    @TableField("version")
    @Version
    private Integer version;

    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private Integer ext5;
    private Integer ext6;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private SysDept dept;

    @TableField(exist = false)
    private List<String> roleIds;

    @TableField(exist = false)
    private List<String> postIds;

    /**
     * 角色名称集合
     */
    @TableField(exist = false)
    private List<String> roleNameList;

}
