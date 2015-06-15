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
