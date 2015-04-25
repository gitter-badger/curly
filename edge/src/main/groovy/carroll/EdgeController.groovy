package carroll

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController


@RestController
class EdgeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    def hello(Pageable pageable) {
        println pageable.pageNumber
        "Olá"
    }
}
