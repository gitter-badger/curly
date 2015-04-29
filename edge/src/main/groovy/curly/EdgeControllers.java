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
package curly;

import curly.commons.rx.RxResult;
import curly.commons.web.hateoas.PageProcessor;
import curly.edge.artifact.Artifact;
import curly.edge.artifact.ArtifactResource;
import curly.edge.artifact.ArtifactResourceAssembler;
import curly.edge.artifact.repository.ArtifactClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author João Pedro Evangelista
 */
public class EdgeControllers {

    @Autowired ArtifactClient client;

    DeferredResult<ResponseEntity<PagedResources<ArtifactResource>>> hello(@PageableDefault(20) Pageable pageable, PagedResourcesAssembler<Artifact> assembler) {
        return RxResult.defer(rx.Observable.just(client.findAll(pageable.getPageNumber(), pageable.getPageSize()))
                .filter(res -> res.getStatusCode().is2xxSuccessful())
                .map(e -> assembler.toResource(PageProcessor.toPage(e.getBody()), new ArtifactResourceAssembler()))
                .map(ResponseEntity::ok));

    }
}
