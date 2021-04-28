package cn.ussu.common.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 检查权限
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PermCheck {

    /**
     * 并，必须满足所有权限
     */
    String[] value() default {};

    /**
     * 检查角色，暂不使用
     */
    @Deprecated
    String[] roles() default {};

}
