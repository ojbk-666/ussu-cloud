package cc.ussu.common.core.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 生产环境 ProdEnvironment
 */
public class ProdEnvCondition implements Condition {

    public static final String ENV_NAME = "prod";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        return environment.acceptsProfiles(Profiles.of(ENV_NAME));
    }
}
