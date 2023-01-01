package cc.ussu.system.api.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class SysUserVO implements Serializable {

    private static final long serialVersionUID = -5602227103947624444L;

    /**
     * 主键
     */
    private String id;
    /**
     * 所在部门id
     */
    private String deptId;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 登录名
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别：1，男；2，女
     */
    private String sex;

    public SysUserVO setSex(String sex) {
        this.sex = sex;
        if (sex != null) {
            if ("1".equals(sex)) {
                this.sexName = "男";
            } else if ("2".equals(sex)) {
                this.sexName = "女";
            } else {
                this.sexName = "未知";
            }
        }
        return this;
    }
    /**
     * 性别：1，男；2，女
     */
    private String sexName;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 最后登陆IP
     */
    private String loginIp;

    /**
     * 最后登陆时间
     */
    private Date lastLoginTime;

    /**
     * 是否禁用
     */
    private String disableFlag;

    /**
     * 角色id
     */
    private Set<String> roleIdList;
    /**
     * 角色编码
     */
    private Set<String> roleCodeList;

    /**
     * 角色名称
     */
    private Set<String> roleNameList;

    /**
     * 岗位id
     */
    private Set<String> postIdList;

    /**
     * 岗位编码
     */
    private Set<String> postCodeList;

    /**
     * 岗位名称
     */
    private Set<String> postNameList;

}
