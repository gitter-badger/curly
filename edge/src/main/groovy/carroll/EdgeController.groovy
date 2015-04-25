package carroll
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import rx.Observable

@RestController
class EdgeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    Observable<String> hello(Pageable pageable) {
        println pageable.getPageNumber()
        Observable.just("Olá")
    }
}
