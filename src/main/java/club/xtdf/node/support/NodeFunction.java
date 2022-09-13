package club.xtdf.node.tree.support;

import java.io.Serializable;
import java.util.function.Function;

/**
 * node接口
 *
 * @param <T>
 * @param <R>
 * @author yangyi
 */
@FunctionalInterface
public interface NodeFunction<T, R> extends Function<T, R>, Serializable {
}
