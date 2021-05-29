package cn.ussu.common.log.annotation;

import java.lang.annotation.*;

/**
 * 标记需要记录日志的方法
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface InsertLog {

    /**
     * 日志名称：log_name
     */
    String value() default "";

}
