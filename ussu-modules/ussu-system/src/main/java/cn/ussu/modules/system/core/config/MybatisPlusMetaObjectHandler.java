package cn.ussu.modules.system.core.config;

import cn.ussu.common.core.constants.StrConstants;
import cn.ussu.common.security.entity.LoginUser;
import cn.ussu.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义填充处理器
 *
 * @author liming
 * @date 2019-09-13 09:32
 */
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    // private String dataScopeName = StrUtil.toCamelCase(DataScope.scopeName);

    /**
     * 插入时自动填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        if (metaObject.hasSetter(StrConstants.createTime) && getFieldValByName(StrConstants.createTime, metaObject) == null) {
            this.strictInsertFill(metaObject, StrConstants.createTime, LocalDateTime.class, now);
        }
        if (metaObject.hasSetter(StrConstants.delFlag) && getFieldValByName(StrConstants.delFlag, metaObject) == null) {
            this.strictInsertFill(metaObject, StrConstants.delFlag, Boolean.class, false);
        }
        if (metaObject.hasSetter(StrConstants.version) && getFieldValByName(StrConstants.version, metaObject) == null) {
            this.strictInsertFill(metaObject, StrConstants.version, Integer.class, 1);
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null) {
            if (metaObject.hasSetter(StrConstants.createBy) && getFieldValByName(StrConstants.createBy, metaObject) == null) {
                this.strictInsertFill(metaObject, StrConstants.createBy, String.class, loginUser.getSysUser().getId());
            }
        }
    }

    /**
     * 更新时自动填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter(StrConstants.updateTime)) {
            this.strictUpdateFill(metaObject, StrConstants.updateTime, LocalDateTime.class, LocalDateTime.now());
        }
        // 从当前请求获取当前登录用户的id
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null) {
            if (metaObject.hasSetter(StrConstants.updateBy)) {
                this.strictUpdateFill(metaObject, StrConstants.updateBy, String.class, loginUser.getSysUser().getId());
            }
        }
    }

}
