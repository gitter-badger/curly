package curly.formula.service

import curly.formula.Category
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._

/**
 * @author João Evangelista
 */
@Service
class DefaultCategoryService @Autowired()(val repository: CategoryRepository) extends CategoryService {

  val logger = LoggerFactory.getLogger(classOf[DefaultCategoryService])

  override def find(name: String): Option[Category] = Option(repository.findByName(name))

  override def save(names: Set[Category]): Unit = {
    names.foreach(f => {
      try repository.save(f) catch {
        case ex: DuplicateKeyException => logger.warn("key {} already exists and cannot be replaced!!", f.name)
        case e: Exception => throw new IllegalStateException(e)
      }
    })
  }

  override def query(name: String): Option[Set[Category]] = Option(repository.findByNameLike(name).asScala.toSet)
}
