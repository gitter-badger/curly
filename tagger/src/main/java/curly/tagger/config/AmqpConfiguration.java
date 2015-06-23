/*
 *        Copyright 2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package curly.tagger.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author João Evangelista
 */
@Configuration
public class AmqpConfiguration {

	@NotNull
	@Bean
	Queue tagQueue() {
		return new Queue("tag.queue", false);
	}

	@NotNull
	@Bean
	TopicExchange artifactoryExchange() {
		return new TopicExchange("artifactory-exchange");
	}

	@Bean
	Binding binding(@NotNull TopicExchange artifactoryExchange, @NotNull Queue tagQueue) {
		return BindingBuilder.bind(tagQueue).to(artifactoryExchange).with(tagQueue.getName());
	}

	@NotNull
	@Bean
	MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@NotNull
	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);
		return rabbitTemplate;
	}
}
