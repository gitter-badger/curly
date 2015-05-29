package curly.commons.cloud.actuator.emitter;

import curly.commons.config.cloud.actuator.ActuatorBroker;
import curly.commons.config.cloud.actuator.AmqpActuatorProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author João Pedro Evangelista
 */
@Configuration
@EnableConfigurationProperties(AmqpActuatorProperties.class)
public class EmitterBrokerConfiguration {

	@Autowired
	private AmqpActuatorProperties amqpActuatorProperties;

	@Bean
	@ActuatorBroker
	@ConditionalOnMissingBean(value = {Queue.class}, name = "actuatorQueue")
	public Queue actuatorQueue() {
		return new Queue(amqpActuatorProperties.getQueue(), false);

	}

	@Bean
	@ActuatorBroker
	@ConditionalOnMissingBean(value = {TopicExchange.class}, name = "actuatorExchange")
	public TopicExchange actuatorExchange() {
		return new TopicExchange(amqpActuatorProperties.getExchange());
	}

	@Bean
	public Binding actuatorBinding(@ActuatorBroker Queue queue, @ActuatorBroker TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(amqpActuatorProperties.getQueue());
	}

	@Bean
	@ConditionalOnMissingBean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

}
