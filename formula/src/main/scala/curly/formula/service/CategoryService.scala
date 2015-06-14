package curly.formula.service

import curly.formula.Category

/**
 * @author João Evangelista
 */
trait CategoryService {
  def find(name: String): Option[Category]

  def query(name: String): Option[Set[Category]]

  def save(tags: Set[Category]): Unit

}
