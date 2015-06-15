package curly.formula.config

import org.springframework.amqp.core.{BindingBuilder, Queue, TopicExchange}
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
}
