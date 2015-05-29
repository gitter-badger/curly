package curly.commons.config.cloud.actuator.emitter;

import org.springframework.cloud.actuator.emitter.ActuatorEmitterConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Joao Pedro Evangelista
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({EmitterBrokerConfiguration.class, ActuatorEmitterConfiguration.class})
public @interface EnableEmitterActuatorBroker {
}
