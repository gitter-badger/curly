package curly.artifact

import curly.commons.web.hateoas.MediaTypes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityLinks
import org.springframework.hateoas.ExposesResourceFor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
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
    EntityLinks entityLinks


    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaTypes.HAL_JSON)
    //DeferredResult<ResponseEntity<PagedResources<Artifact>>> todo
    def artifacts(@PageableDefault(20) Pageable pageable,
                  PagedResourcesAssembler<Artifact> assembler) {
        defer this.command.artifacts(pageable)
                .map { it.orElseThrow { new ResourceNotFoundException() } }
                .map { assembler.toResource(it) }
                .map { ResponseEntity.ok(it) },
                Schedulers.computation()
    }
}
