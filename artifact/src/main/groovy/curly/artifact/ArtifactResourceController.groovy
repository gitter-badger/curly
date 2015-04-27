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
package curly.artifact

import curly.commons.web.OperationIncompletedException
import curly.commons.web.hateoas.MediaTypes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.ExposesResourceFor
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import rx.schedulers.Schedulers

import javax.validation.constraints.NotNull

import static curly.commons.rx.RxResult.defer
/**
 * @author João Pedro Evangelista
 */
@RestController
@RequestMapping("/arts")
@ExposesResourceFor(Artifact)
class ArtifactResourceController {

    private @Autowired
    @NotNull
    ArtifactCommand command

    private @Autowired
    @NotNull
    ArtifactResourceAssembler resourceAssembler

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaTypes.HAL_JSON)
    def artifacts(@PageableDefault(20) Pageable pageable,
                  PagedResourcesAssembler<Artifact> assembler) {
        defer this.command.artifacts(pageable)
                .map { it.or { new ResourceNotFoundException() } }
                .map { assembler.toResource(it, resourceAssembler) }
                .map { ResponseEntity.ok(it) },
                Schedulers.computation()
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaTypes.HAL_JSON)
    def artifact(@PathVariable("id") String id) {
        defer this.command.artifact(id)
                .map { it.or { new ResourceNotFoundException() } }
                .map { resourceAssembler.toResource(it) }
                .map { ResponseEntity.ok(it) },
                Schedulers.computation()
    }


    @RequestMapping(
            method = [RequestMethod.POST, RequestMethod.PUT],
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaTypes.HAL_JSON)
    def insertArtifact(@RequestBody Artifact artifact) {
        defer this.command.save(artifact)
                .map { it.or({ new OperationIncompletedException() }) }
                .map { resourceAssembler.toResource(it) }
                .map { ResponseEntity.ok(it) },
                Schedulers.computation()
    }

    //todo delete

    //todo by methods
}
