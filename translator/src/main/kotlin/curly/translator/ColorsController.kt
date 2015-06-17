package curly.translator

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.HashMap

/**
 * @author João Evangelista
 */
public RestController open class ColorsController {

    private val colors: ColorsLoader

    Autowired constructor(colors: ColorsLoader) {
        this.colors = colors
    }

    RequestMapping(value = array("/colors"), method = array(RequestMethod.POST))
    fun get(RequestBody lang: Set<String>): Map<String, String> {
        var output: MutableMap<String, String> = HashMap()
        lang.forEach { val color = colors.getColorMap().get(it.toLowerCase()); output.put(it, color!!) }
        return output
    }

}
