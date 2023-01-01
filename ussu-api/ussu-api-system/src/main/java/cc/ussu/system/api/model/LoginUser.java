package cc.ussu.system.api.model;

import cc.ussu.system.api.vo.SysUserVO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 账号
     */
    private String account;

    /**
     * 登陆时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 角色编码集
     */
    private Set<String> roleCodeList;

    /**
     * 权限集
     */
    private Set<String> permissions;

    private SysUserVO user;

}
