package curly.formula.config

import org.springframework.amqp.core.{BindingBuilder, Queue, TopicExchange}
import org.springframework.context.annotation.{Bean, Configuration}

/**
 * @author João Evangelista
 */
@Configuration
class AmqpConfiguration {

  @Bean def categoryQueue() = new Queue("category.queue", false)

  @Bean def categoryExchange() = new TopicExchange("category-exchange")

  @Bean def categoryBinding(categoryQueue: Queue, categoryExchange: TopicExchange) = BindingBuilder
    .bind(categoryQueue)
    .to(categoryExchange)
    .`with`(categoryQueue.getName)
}
