package curly.commons.cloud.actuator.emitter;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.actuator.properties.AmqpActuatorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author João Pedro Evangelista
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(AmqpActuatorProperties.class)
@ConditionalOnClass({Endpoint.class, AmqpTemplate.class})
public class ActuatorEmitterConfiguration {

	@Bean
	Emitter emitter() {
		return new Emitter();
	}
}
