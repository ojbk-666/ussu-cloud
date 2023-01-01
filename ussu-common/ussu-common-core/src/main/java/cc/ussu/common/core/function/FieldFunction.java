package cc.ussu.common.core.function;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface FieldFunction<T, R> extends Function<T, R>, Serializable {
}
