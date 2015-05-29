package curly.commons.cloud.actuator.client;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.actuator.config.ActuatorBroker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Joao Pedro Evangelista
 */

@Configuration
@EnableConfigurationProperties(AmqpClientActuatorProperties.class)
public class ClientActuatorConfiguration {

	@Autowired
	private AmqpClientActuatorProperties properties;

	@Bean
	@ConditionalOnMissingBean
	MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}


	@Bean
	@ActuatorBroker
	@ConditionalOnMissingBean(value = {Queue.class}, name = "actuatorQueue")
	public Queue actuatorQueue() {
		return new Queue(properties.getQueue(), false);
	}

	@Bean
	@ActuatorBroker
	@ConditionalOnMissingBean(value = {TopicExchange.class}, name = "actuatorExchange")
	public TopicExchange actuatorExchange() {
		return new TopicExchange(properties.getExchange());
	}

	@Bean
	public Binding actuatorBinding(@ActuatorBroker Queue queue, @ActuatorBroker TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(properties.getQueue());
	}

}
