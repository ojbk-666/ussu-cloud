package cc.ussu.common.core.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 开发环境
 */
public class DevEnvCondition implements Condition {

    public static final String ENV_NAME = "dev";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        return environment.acceptsProfiles(Profiles.of(ENV_NAME));
    }
}
