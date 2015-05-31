package curly.paperclip.paper

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
        if (!ObjectId.isValid(item)) return defer(Observable.just(new ResponseEntity(BAD_REQUEST)))

        defer command.getByItem(item).map({
            it.<ResourceNotFoundException> orElseThrow({
                new ResourceNotFoundException("Resource for item $item cannot been found!")
            })
        }).map({
            ResponseEntity.ok(it)
        })
    }


}
