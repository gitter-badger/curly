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
