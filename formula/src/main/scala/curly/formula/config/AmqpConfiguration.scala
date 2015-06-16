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
package curly.formula.config

import curly.commons.reactor.DispatcherFactory
import curly.formula.listener.CategoryEventListener
import org.springframework.amqp.core.{BindingBuilder, Queue, TopicExchange}
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.{Jackson2JsonMessageConverter, MessageConverter}
import org.springframework.context.annotation.{Bean, Configuration}

/**
 * @author João Evangelista
 */
@Configuration
class AmqpConfiguration {

  @Bean def categoryQueue() = new Queue("category.queue", false)

  @Bean def artifactoryExchange() = new TopicExchange("artifactory-exchange")


  @Bean def categoryBinding(categoryQueue: Queue, artifactoryExchange: TopicExchange) = BindingBuilder
    .bind(categoryQueue)
    .to(artifactoryExchange)
    .`with`(categoryQueue.getName)

  @Bean def messageConverter: MessageConverter = new Jackson2JsonMessageConverter

  @Bean def rabbitTemplate(connectionFactory: ConnectionFactory, messageConverter: MessageConverter): RabbitTemplate = {
    val rabbitTemplate: RabbitTemplate = new RabbitTemplate(connectionFactory)
    rabbitTemplate.setMessageConverter(messageConverter)
    rabbitTemplate
  }

  @Bean def rabbitListenerContainerFactory(connectionFactory: ConnectionFactory): SimpleRabbitListenerContainerFactory = {
    val smpContainer = new SimpleRabbitListenerContainerFactory
    smpContainer.setTaskExecutor(DispatcherFactory.workQueueDispatcher(classOf[CategoryEventListener]))
    smpContainer.setMaxConcurrentConsumers(16)
    smpContainer.setConnectionFactory(connectionFactory: ConnectionFactory)
    smpContainer
  }
}
