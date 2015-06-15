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
package curly.artifact.integration.config;

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
	Queue notifierQueue() {
		return new Queue("artifactory.notification.queue", false);
	}

	@Bean
	Queue categoryQueue() {
		return new Queue("category.queue", false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("artifactory-exchange");
	}


	@Bean
	Binding tagBinding(TopicExchange exchange, Queue tagQueue) {
		return BindingBuilder.bind(tagQueue).to(exchange).with(tagQueue.getName());
	}

	@Bean
	Binding notifierBinding(TopicExchange exchange, Queue notifierQueue) {
		return BindingBuilder.bind(notifierQueue).to(exchange).with(notifierQueue.getName());
	}

	@Bean
	Binding categoryBinding(TopicExchange exchange, Queue categoryQueue) {
		return BindingBuilder.bind(categoryQueue).to(exchange).with(categoryQueue.getName());
	}
}
