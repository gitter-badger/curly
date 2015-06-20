package curly.edge.paper.web

import curly.commons.rx.RxResult
import curly.commons.web.hateoas.MediaTypes
import curly.edge.paper.command.PaperService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult
import rx.Observable

/**
 * @author Joao Pedro Evangelista
 */
@RestController
@RequestMapping(value = "/papers")
class PaperController {

    @Autowired
    private PaperService service


    @RequestMapping(value = "/{artifactId}", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON)
    DeferredResult<ResponseEntity<PaperResource>> getByArtifact(@PathVariable("artifactId") String id,
                                                                PaperResourceAssembler assembler) {
        if (!ObjectId.isValid(id)) {
            return RxResult.defer(Observable.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST)))
        }
        RxResult.defer(service.getPaper(id)
                .map({ it.orElseThrow({ new ResourceNotFoundException() }) })
                .map({ ResponseEntity.ok(assembler.toResource(it)) }))

    }

}
