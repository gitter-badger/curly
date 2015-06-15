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

import com.fasterxml.jackson.databind.ObjectMapper
import curly.commons.config.reactor.Reactor
import curly.formula.Category
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.bus.{Event, EventBus}
/**
 * @author João Evangelista
 */
@Component
class CategoryEventListener @Autowired()(@Reactor val eventBus: EventBus, objectMapper: ObjectMapper) {

  val logger = LoggerFactory.getLogger(classOf[CategoryEventListener])

  @RabbitListener(queues = Array("category.queue"))
  def onReceive(message: Message): Unit = {
    Option(message) match {
      case Some(x) =>
        val o = objectMapper.readValue(x.getBody, classOf[Category])
        eventBus.notify("category.bus", Event.wrap(o))
      case None => logger.error("Cannot use NULL message!!")
    }
  }
}
