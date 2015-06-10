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

import curly.commons.github.GitHubAuthentication
import curly.commons.github.OctoUser
import curly.commons.logging.annotation.Loggable
import curly.commons.web.ApiErrors
import org.bson.types.ObjectId
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.async.DeferredResult
import rx.Observable

import javax.inject.Inject

import static curly.commons.rx.RxResult.defer
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE as JSON

/**
 * @author Joao Pedro Evangelista
 */
@RestController
@RequestMapping("/papers")
class PaperResourceController {

    private final PaperCommand command

    @Inject
    PaperResourceController(PaperCommand command) {
        this.command = command
    }

    @Loggable
    @RequestMapping(value = "/{item}", method = RequestMethod.GET, produces = JSON)
    DeferredResult<ResponseEntity<Paper>> getOneByItem(@PathVariable String item) {
        if (!ObjectId.isValid(item)) return defer(Observable.just(new ResponseEntity(BAD_REQUEST)))

        defer command.getByItem(item).map({
            it.<ResourceNotFoundException> orElseThrow({
                new ResourceNotFoundException("Resource for item $item cannot been found!")
            })
        }).map({
            ResponseEntity.ok(it)
        })
    }

    static def isValid(String item) {

    }

    @Loggable
    @RequestMapping(value = "/owner/{item}", method = RequestMethod.GET, produces = JSON)
    DeferredResult<ResponseEntity<Paper>> getOneByItemAndOwner(@PathVariable String item,
                                                               @GitHubAuthentication OctoUser octoUser) {
        if (!ObjectId.isValid(item)) return defer(Observable.just(new ResponseEntity(BAD_REQUEST)))
        defer command.getByOwner(item, Optional.ofNullable(octoUser)).map({
            it.orElseThrow({ new ResourceNotFoundException("Resource for item $item cannot been found!") })
        }).map({
            ResponseEntity.ok(it)
        })
    }

    @Loggable
    @RequestMapping(value = "/{item}", method = RequestMethod.DELETE)
    DeferredResult<ResponseEntity<?>> delete(@PathVariable String item,
                                             @GitHubAuthentication OctoUser octoUser) {
        if (!ObjectId.isValid(item)) return defer(Observable.just(new ResponseEntity(BAD_REQUEST)))
        command.delete(item, Optional.ofNullable(octoUser))
        defer Observable.just(new ResponseEntity(NO_CONTENT))
    }

    @Loggable
    @RequestMapping(method = [RequestMethod.PUT, RequestMethod.POST])
    DeferredResult<ResponseEntity<?>> save(@RequestBody Paper paper,
                                           @GitHubAuthentication OctoUser octoUser,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return defer(Observable.just(new ResponseEntity(new ApiErrors(BAD_REQUEST, bindingResult), BAD_REQUEST)))
        }
        command.save(paper, Optional.ofNullable(octoUser))
        defer Observable.just(new ResponseEntity(CREATED))
    }
}