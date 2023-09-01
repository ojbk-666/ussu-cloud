package cc.ussu.common.core.function;

import java.io.Serializable;
import java.util.function.Function;

@Deprecated
@FunctionalInterface
public interface FieldFunction<T, R> extends Function<T, R>, Serializable {
}
