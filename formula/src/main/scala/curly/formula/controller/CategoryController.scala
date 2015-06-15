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
package curly.formula.controller

import curly.commons.rx.RxResult
import curly.formula.command.{InsertCommand, LookupCommand}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestMethod, RestController}
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
