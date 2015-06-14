package curly.formula.command

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import curly.commons.logging.annotation.Loggable
import curly.formula.Category
import curly.formula.service.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service

/**
 * @author João Evangelista
 */
@Service
class DefaultInsertCommand @Autowired()(val service: CategoryService)
  extends InsertCommand {
  @Loggable
  @Retryable
  @HystrixCommand
  override def save(tags: Set[Category]): Unit = {
    service.save(tags)
  }
}
