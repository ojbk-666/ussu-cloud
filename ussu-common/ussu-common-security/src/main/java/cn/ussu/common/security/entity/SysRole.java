package cn.ussu.common.security.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRole implements Serializable {

    private static final long serialVersionUID = -1602037037122155236L;

    private String id;
    private String roleCode;
    private String roleName;
    private List<String> perms;

}
