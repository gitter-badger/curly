package curly.formula.controller

import curly.formula.command.{InsertCommand, LookupCommand}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

/**
 * @author João Evangelista
 */
@RestController
@RestController(Array("/categories"))
class CategoryController @Autowired()(val insertCommand: InsertCommand, val lookupCommand: LookupCommand) {


}
