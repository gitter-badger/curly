package carroll.commons.github;

import java.lang.annotation.*;

/**
 * @author Joao Pedro Evangelista
 * @since 04/04/2015
 */
@Documented
@Inherited
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface GitHubAuthentication {
}
