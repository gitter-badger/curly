package carroll.commons.config.cache.annotation;

import carroll.commons.config.cache.RedisCacheConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Joao Pedro Evangelista
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RedisCacheConfig.class)
public @interface EnableRedisCache {
}
