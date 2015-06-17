package curly.translator

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.util.HashMap

/**
 * @author João Evangelista
 */

public data Service open class ColorsLoader {

    private val langMap: HashMap<String, String>

    init {
        val file = ClassPathResource("colors.json", this.javaClass.getClassLoader()).getFile()
        langMap = ObjectMapper().readValue(file, javaClass<HashMap<String, String>>())
    }

    fun getColorMap(): Map<String, String> {
        return langMap
    }

}
