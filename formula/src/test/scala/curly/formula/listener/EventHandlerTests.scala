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
