package cc.ussu.modules.system.entity.vo;

import cc.ussu.modules.system.entity.SysUser;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 人员选择的请求参数封装
 */
@Data
@ToString
public class SelectUserParamVO extends SysUser {

    /**
     * 包含的角色id
     */
    private String includeRoleIds;
    private List<String> inIds;

    public void setIncludeRoleIds(String includeRoleIds) {
        this.includeRoleIds = includeRoleIds;
        this.inIds = split(includeRoleIds);
    }

    /**
     * 排除的角色id
     */
    private String excludeRoleIds;
    private List<String> exIds;

    public void setExcludeRoleIds(String excludeRoleIds) {
        this.excludeRoleIds = excludeRoleIds;
        this.exIds = split(excludeRoleIds);
    }

    /**
     * 包含的角色code
     */
    private String includeRoleCodes;
    private List<String> inCodes;

    public void setIncludeRoleCodes(String includeRoleCodes) {
        this.includeRoleCodes = includeRoleCodes;
        this.inCodes = split(includeRoleCodes);
    }

    /**
     * 排除的角色code
     */
    private String excludeRoleCodes;
    private List<String> exCodes;

    public void setExcludeRoleCodes(String excludeRoleCodes) {
        this.excludeRoleCodes = excludeRoleCodes;
        this.exCodes = split(excludeRoleCodes);
    }

    private List<String> split(String str) {
        return CollUtil.removeBlank(StrUtil.split(str, StrPool.COMMA));
    }

}
