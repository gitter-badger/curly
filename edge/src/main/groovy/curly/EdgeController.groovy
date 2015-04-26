package curly

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import rx.Observable

import static curly.commons.rx.RxResult.defer

@RestController
class EdgeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    def hello(Pageable pageable) {
        println pageable.getPageNumber()
       defer Observable.just("Hello")
    }
}
