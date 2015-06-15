package curly.formula.command

import curly.formula.Category
import rx.Observable

/**
 * @author João Evangelista
 */
trait LookupCommand {

  def get(name: String): Observable[Option[Category]]

  def like(name: String): Observable[Option[Set[Category]]]
}
