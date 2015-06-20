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
package curly.edge.artifact.repository

import curly.edge.artifact.domain.Artifact
import curly.edge.artifact.domain.PagedArtifact
import curly.edge.artifact.web.ArtifactResource
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * @author João Pedro Evangelista
 */
@FeignClient("artifact")
interface ArtifactClient {

    @RequestMapping(
            value = "/arts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ArtifactResource> findOne(@PathVariable("id") String id)

    @RequestMapping(
            value = "/arts?page={page}&size={size}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PagedArtifact> findAll(@PathVariable("page") int page, @PathVariable("size") int size)

    @RequestMapping(
            value = "/arts",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    void save(@RequestBody Artifact artifact)

    @RequestMapping(
            value = "/arts/{id}",
            method = RequestMethod.DELETE)
    void delete(@PathVariable("id") String id)


}
