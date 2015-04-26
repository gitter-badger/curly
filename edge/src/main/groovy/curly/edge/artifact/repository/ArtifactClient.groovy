package curly.edge.artifact.repository

import curly.commons.web.hateoas.MediaTypes
import curly.edge.artifact.Artifact
import curly.edge.artifact.PagedArtifact
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
            produces = MediaTypes.HAL_JSON)
    ResponseEntity<PagedArtifact> findOne(@PathVariable("id") String id)

    @RequestMapping(
            value = "/arts?page={page}&size={size}",
            method = RequestMethod.GET,
            produces = MediaTypes.HAL_JSON)
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
