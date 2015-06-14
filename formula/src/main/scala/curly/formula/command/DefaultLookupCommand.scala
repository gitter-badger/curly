package curly.formula.command

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.command.ObservableResult
import curly.formula.Category
import curly.formula.service.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import rx.Observable

/**
 * @author João Evangelista
 */
@Service
class DefaultLookupCommand @Autowired()(val service: CategoryService) extends LookupCommand {

  @HystrixCommand
  @Retryable(maxAttempts = 1)
  override def get(name: String): Observable[Option[Category]] = new ObservableResult[Option[Category]] {
    override def invoke(): Option[Category] = service.find(name)
  }

  @HystrixCommand
  @Retryable(maxAttempts = 1)
  override def like(name: String): Observable[Option[Set[Category]]] = new ObservableResult[Option[Set[Category]]] {
    override def invoke(): Option[Set[Category]] = service.query(name)
  }

}
