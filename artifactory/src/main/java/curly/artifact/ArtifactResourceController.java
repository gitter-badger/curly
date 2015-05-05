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
package curly.artifact;

import curly.commons.github.GitHubAuthentication;
import curly.commons.github.OctoUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

import static curly.commons.rx.RxResult.defer;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author João Pedro Evangelista
 */
@Slf4j
@Controller
@RequestMapping("/arts")
class ArtifactResourceController {

    private final ArtifactService artifactService;

    @Autowired
    ArtifactResourceController(ArtifactService artifactService) {
        this.artifactService = artifactService;

    }

    @ResponseBody
    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<HttpEntity<PagedArtifact>> artifactResources(@PageableDefault(20) Pageable pageable) {
        log.trace("Querying resources with page {}, size {}", pageable.getPageNumber(), pageable.getPageSize());
        artifactService.findAll(pageable).forEach(System.out::println);
        return defer(artifactService.findAll(pageable)
                .map(o -> o.<ResourceNotFoundException>orElseThrow(ResourceNotFoundException::new))
                .map(PagedArtifact::new)
                        .map(ResponseEntity::ok)
        );
    }

    @ResponseBody
    @RequestMapping(
            value = "/{id}",
            method = GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<HttpEntity<Artifact>> artifactResource(@PathVariable("id") String id) {
        log.trace("Querying single resource based on id {}", id);
        return defer(artifactService.findOne(id)
                .map(o -> o.<ResourceNotFoundException>orElseThrow(ResourceNotFoundException::new))
                .map(ResponseEntity::ok));
    }


    @RequestMapping(method = {POST, PUT})
    public Callable<HttpEntity<?>> saveResource(@RequestBody Artifact artifact,
                                                @GitHubAuthentication OctoUser octoUser) {
        log.debug("Performing save operations based on user {}", octoUser.getId());
        artifactService.save(artifact, octoUser);
        return () -> ResponseEntity.ok().build();
    }

    @RequestMapping(
            value = "/{id}",
            method = DELETE)
    public Callable<HttpEntity<?>> deleteResource(@PathVariable("id") String id,
                                                  @GitHubAuthentication OctoUser octoUser) {
        log.trace("Performing deletion operations based on user {}", octoUser.getId());
        artifactService.delete(id, octoUser);
        return () -> ResponseEntity.noContent().build();
    }

}
