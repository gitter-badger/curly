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

import javax.validation.Valid

import curly.commons.rx.RxResult.defer
import curly.formula.Category
import curly.formula.command.{InsertCommand, LookupCommand}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.http.{HttpStatus, MediaType, ResponseEntity}
import org.springframework.web.bind.annotation._
import rx.lang.scala.JavaConversions._

/**
 * @author João Evangelista
 */
@RestController
@RequestMapping(Array("/categories"))
class CategoryController @Autowired()(val insertCommand: InsertCommand, val lookupCommand: LookupCommand) {

  @RequestMapping(value = Array("/{category}"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def get(@PathVariable category: String) = {
    defer(toJavaObservable(toScalaObservable(lookupCommand.get(category)).map {
      case Some(c) => ResponseEntity.ok(c)
      case None => throw new ResourceNotFoundException()
    }))
  }

  @RequestMapping(value = Array("/search"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def search(@RequestParam(value = "q", defaultValue = "") part: String) = {
    defer(toJavaObservable(toScalaObservable(lookupCommand.like(part)).map {
      case Some(c) => ResponseEntity.ok(c)
      case None => throw new ResourceNotFoundException()
    }))
  }

  @RequestMapping(method = Array(RequestMethod.POST), consumes = Array(MediaType.APPLICATION_JSON_VALUE))
  def save(@Valid @RequestBody category: Category) = {
    insertCommand.save(category)
    defer(rx.Observable.just(new ResponseEntity[Any](HttpStatus.CREATED)))
  }


}
