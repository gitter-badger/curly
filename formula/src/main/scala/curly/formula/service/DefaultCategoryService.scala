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

  override def save(category: Category): Unit = {
    try repository.save(category) catch {
      case ex: DuplicateKeyException => logger.warn("key {} already exists and cannot be replaced!!", category.name)
        case e: Exception => throw new IllegalStateException(e)
      }
  }

  override def query(name: String): Option[Set[Category]] = Option(repository.findByNameLike(name).asScala.toSet)
}
