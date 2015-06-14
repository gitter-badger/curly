package curly.formula.command

import curly.formula.Category

/**
 * @author João Evangelista
 */
trait InsertCommand {
  def save(tags: Set[Category])

}
