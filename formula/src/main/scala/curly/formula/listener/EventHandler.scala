package curly.formula.listener

import curly.commons.config.reactor.Reactor
import curly.formula.Category
import curly.formula.command.InsertCommand
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import reactor.bus.EventBus
import reactor.spring.context.annotation.{Consumer, Selector}

/**
 * @author João Evangelista
 */
@Consumer
class EventHandler @Autowired()(val insertCommand: InsertCommand, @Reactor val eventBus: EventBus) {

  val logger = LoggerFactory.getLogger(classOf[EventHandler])

  @Selector("category.bus")
  def handle(message: Category): Unit = {
    logger.info("Initiating processor for tag message received on Event Bus")
    insertCommand.save(message)
  }

}
