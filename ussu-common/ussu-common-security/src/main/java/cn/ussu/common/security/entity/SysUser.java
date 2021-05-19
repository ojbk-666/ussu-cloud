package cn.ussu.common.security.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

    private static final long serialVersionUID = -5417331628802144001L;

    private String id;
    private String account;
    private String password;
    private String name;
    private String nickName;
    private String avatar;
    private String sex;
    private String email;
    private String phone;
    private String userType;
    private String deptId;

    private SysDept sysDept;
    private List<SysRole> roleList;

}
