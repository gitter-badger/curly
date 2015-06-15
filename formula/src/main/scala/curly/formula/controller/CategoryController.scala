package curly.formula.controller

import curly.commons.rx.RxResult
import curly.formula.command.{InsertCommand, LookupCommand}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.{PathVariable, RequestMethod, RequestMapping, RestController}
import rx.lang.scala.JavaConversions._

/**
 * @author João Evangelista
 */
@RestController
@RequestMapping(Array("/categories"))
class CategoryController @Autowired()(val insertCommand: InsertCommand, val lookupCommand: LookupCommand) {

  @RequestMapping(value = Array("/{category}"), method = Array(RequestMethod.GET))
  def get(@PathVariable category: String) = {
    RxResult.defer(toJavaObservable(toScalaObservable(lookupCommand.get(category)).map {
      case Some(c) => ResponseEntity.ok(c)
      case None => throw new ResourceNotFoundException()
    }))
  }


}
