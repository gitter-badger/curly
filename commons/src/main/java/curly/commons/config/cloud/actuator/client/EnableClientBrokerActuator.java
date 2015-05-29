package curly.commons.config.cloud.actuator.client;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Joao Pedro Evangelista
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ClientActuatorConfiguration.class)
public @interface EnableClientBrokerActuator {
}
