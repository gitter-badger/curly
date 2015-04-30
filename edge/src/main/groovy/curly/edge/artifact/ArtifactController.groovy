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
package curly.edge.artifact

import curly.commons.web.hateoas.MediaTypes
import curly.commons.web.hateoas.PageProcessor
import curly.edge.artifact.repository.ArtifactClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.ExposesResourceFor
import org.springframework.hateoas.PagedResources
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult

import static curly.commons.rx.RxResult.defer

@RequestMapping("/artifacts")
@ExposesResourceFor(Artifact)
@RestController
class ArtifactController {

    @Autowired ArtifactClient client;

    @Autowired ArtifactResourceAssembler assembler

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaTypes.HAL_JSON)
    DeferredResult<ResponseEntity<PagedResources<ArtifactResource>>> getAll(
            @PageableDefault(20) Pageable pageable, PagedResourcesAssembler<Artifact> pagedResourcesAssembler) {
        defer(rx.Observable.just(client.findAll(pageable.pageNumber, pageable.pageSize))
                .filter({ res -> res.statusCode.is2xxSuccessful() })
                .map({ r -> pagedResourcesAssembler.toResource(PageProcessor.toPage(r.body), assembler) })
                .map({ ResponseEntity.ok(it) }))
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaTypes.HAL_JSON)
    DeferredResult<ResponseEntity<ArtifactResource>> getOne(@PathVariable("id") String id) {
        defer(rx.Observable.just(client.findOne(id)).firstOrDefault(ResponseEntity.ok(null)))
    }
}
