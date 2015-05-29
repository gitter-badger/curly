package curly.commons.config.cloud.actuator;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

/**
 * @author João Pedro Evangelista
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
		ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier
public @interface ActuatorBroker {
}
