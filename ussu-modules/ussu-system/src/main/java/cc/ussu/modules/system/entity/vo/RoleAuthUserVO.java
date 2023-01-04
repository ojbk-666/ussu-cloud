package cc.ussu.modules.system.entity.vo;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
@Accessors(chain = true)
public class RoleAuthUserVO implements Serializable {

    private static final long serialVersionUID = 3733202848078651285L;

    @NotBlank(message = "角色id不能为空")
    private String roleId;

    @Size(min = 1, message = "userIds不能为空")
    private Set<String> userIds;

    public RoleAuthUserVO setUserIds(Set<String> userIds) {
        Set<String> set = CollUtil.removeBlank(userIds);
        this.userIds = set;
        return this;
    }

}
