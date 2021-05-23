package cn.ussu.modules.system.model.param;

import cn.ussu.modules.system.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUserParam extends SysUser {

    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;

    private String oldPassword;
    private String newPassword;

}
