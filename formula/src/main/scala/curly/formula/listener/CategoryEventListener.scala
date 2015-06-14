package curly.formula.listener

import curly.commons.config.reactor.Reactor
import curly.formula.Category
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import reactor.bus.{Event, EventBus}

/**
 * @author João Evangelista
 */
@Component
class CategoryEventListener @Autowired()(@Reactor val eventBus: EventBus) {

  val logger = LoggerFactory.getLogger(classOf[CategoryEventListener])

  @RabbitListener(queues = Array("category.queue"))
  def onReceive(@Payload message: Message[Set[Category]]): Unit = {
    Option(message) match {
      case Some(x) => eventBus.notify("category.bus", Event.wrap(x))
      case None => logger.error("Cannot use NULL message!!")
    }
  }
}
