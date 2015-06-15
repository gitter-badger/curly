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

import curly.formula.{Category, FormulaApplication}
import org.junit.runner.RunWith
import org.junit.{Assert, Test}
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
 * @author João Evangelista
 */
@RunWith(classOf[SpringJUnit4ClassRunner])
@SpringApplicationConfiguration(classes = Array(classOf[FormulaApplication]))
class EventHandlerTests {
  @Autowired private var amqpTemplate: AmqpTemplate = null
  @Autowired private var mongoTemplate: MongoTemplate = null


  @Test
  @throws[Exception]
  def testHandling() = {
    val before = count
    amqpTemplate.convertAndSend("category.queue", Category(null, "full-stack"))
    Thread.sleep(5000) // wait some time to process it
    val after = count
    Assert.assertTrue("The number of items before action must be lower than after", before < after)
  }

  private def count = mongoTemplate.count(new Query(), classOf[Category])

}
