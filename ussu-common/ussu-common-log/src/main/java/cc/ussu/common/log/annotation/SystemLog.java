package cc.ussu.common.log.annotation;

import java.lang.annotation.*;

/**
 * 操作日志
 */
@Deprecated
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SystemLog {

    /**
     * 是否保存参数
     */
    boolean saveParam() default true;

    /**
     * 是否保存结果
     */
    boolean saveResult() default true;

    /**
     * 分组（大类）
     */
    String group() default "";

    /**
     * 名称（小类）
     */
    String name() default "";

    /**
     * 服务名
     */
    String serviceName() default "";

}
