package curly.formula.service

import java.util.{Set => JSet}

import curly.formula.Category
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author João Evangelista
 */
trait CategoryRepository extends MongoRepository[Category, String] {

  def findByName(name: String): Category

  def findByNameLike(name: String): JSet[Category]

}
