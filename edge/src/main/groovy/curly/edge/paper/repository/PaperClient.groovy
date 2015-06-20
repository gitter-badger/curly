package curly.edge.paper.repository

import curly.edge.paper.domain.Paper
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * @author Joao Pedro Evangelista
 */
@FeignClient("paperclip")
@RequestMapping("/papers")
interface PaperClient {

    @RequestMapping(
            value = "/{item}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Paper> findByItem(@PathVariable("item") String item)
}
