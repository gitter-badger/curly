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
import org.bson.types.ObjectId
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult
import rx.Observable

import javax.inject.Inject

import static curly.commons.rx.RxResult.defer
import static org.springframework.http.HttpStatus.BAD_REQUEST
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
        isValid(item)

        defer command.getByItem(item).map({
            it.<ResourceNotFoundException> orElseThrow({
                new ResourceNotFoundException("Resource for item $item cannot been found!")
            })
        }).map({
            ResponseEntity.ok(it)
        })
    }

    static def isValid(String item) {
        if (!ObjectId.isValid(item)) return defer(Observable.just(new ResponseEntity(BAD_REQUEST)))
    }

    @Loggable
    @RequestMapping(value = "/owner/{item}", method = RequestMethod.GET, produces = JSON)
    DeferredResult<ResponseEntity<Paper>> getOneByItemAndOwner(@PathVariable String item,
                                                               @GitHubAuthentication OctoUser octoUser) {
        isValid(item)
        defer command.getByOwner(item, Optional.ofNullable(octoUser)).map({
            it.orElseThrow({ new ResourceNotFoundException("Resource for item $item cannot been found!") })
        }).map({
            ResponseEntity.ok(it)
        })
    }


}
