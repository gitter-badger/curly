package curly.formula.command

import curly.formula.Category
import rx.Observable

/**
 * @author João Evangelista
 */
trait LookupCommand {

  def get(tag: String): Observable[Option[Category]]

  def like(tag: String): Observable[Option[Set[Category]]]
}
