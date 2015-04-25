package carroll.commons.config.web.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import java.lang.annotation.*;

/**
 * @author João Pedro Evangelista
 * @since 09/03/15
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(RepositoryRestMvcConfiguration.class)
public @interface EnableRepositoryRestMvc {
}
