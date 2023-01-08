package cc.ussu.common.swagger.annotation;

import cc.ussu.common.swagger.config.SwaggerAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfig.class})
public @interface EnableCustomSwagger2 {

}
