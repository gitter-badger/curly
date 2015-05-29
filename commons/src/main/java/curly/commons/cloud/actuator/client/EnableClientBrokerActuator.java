package curly.commons.cloud.actuator.client;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Imports default Queue and Exchange to communicate with rabbitMQ
 * To use the client for now, you need to provide a listener implementation
 *
 * @author Joao Pedro Evangelista
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ClientActuatorConfiguration.class)
public @interface EnableClientBrokerActuator {
}
