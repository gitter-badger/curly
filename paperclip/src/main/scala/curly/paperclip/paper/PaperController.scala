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
package curly.paperclip.paper

import curly.commons.github.{GitHubAuthentication, OctoUser}
import curly.commons.rx.RxResult._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation._
import org.springframework.web.context.request.async.DeferredResult
import rx.lang.scala.JavaConversions._

/**
 * @author Joao Pedro Evangelista
 */
@RestController
@RequestMapping(value = Array("/paperclip"))
class PaperController @Autowired()(val command: PaperCommand) {


  /**
   * Get the raw content of a file matching the artifact id
   * @param artifact the artifact request it's paper
   * @return raw content or Status 404 if not found
   */
  @RequestMapping(value = Array("/artifact/{artifact}"), method = Array(RequestMethod.GET))
  def getPaper(artifact: String@PathVariable("artifact")): DeferredResult[_ <: ResponseEntity[RawPaper]] = {
    defer(toJavaObservable(toScalaObservable(command.getPaperByArtifact(artifact)).map {
      case Some(o) => ResponseEntity.ok(o)
      case None => new ResponseEntity[RawPaper](HttpStatus.NOT_FOUND)
    }))
  }

  /**
   *
   * @param artifact the requested artifact
   * @param octoUser the current logged user
   * @return the found entity
   */
  @Secured(value = Array("ROLE_USER"))
  @RequestMapping(value = Array("/owner/artifact/{artifact}"), method = Array(RequestMethod.GET))
  def getPaperForOwner(artifact: String@PathVariable("artifact"), octoUser: OctoUser@GitHubAuthentication): DeferredResult[_ <: ResponseEntity[RawPaper]] = {
    defer(toJavaObservable(toScalaObservable(command.getPaperForOwner(artifact, Option(octoUser))).map {
      case Some(o) => ResponseEntity.ok(o)
      case None => new ResponseEntity[RawPaper](HttpStatus.NOT_FOUND)
    }))
  }

  @Secured(value = Array("ROLE_USER"))
  @RequestMapping(method = Array(RequestMethod.PATCH, RequestMethod.PUT))
  def savePaper(rawPaper: RawPaper@RequestBody, octoUser: OctoUser@GitHubAuthentication): DeferredResult[_ <: ResponseEntity[_]] = {
    ???
  }

  //todo post content process save (reactive) akka?
}
