/*
 *        Copyright 2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
