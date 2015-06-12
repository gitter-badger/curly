package curly.tagger.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author João Evangelista
 */
@Configuration
public class AmqpConfiguration {

	@Bean
	Queue tagQueue() {
		return new Queue("tag.queue", false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("tag-exchange");
	}

	@Bean
	Binding binding(TopicExchange exchange, Queue tagQueue) {
		return BindingBuilder.bind(tagQueue).to(exchange).with(tagQueue.getName());
	}
}
