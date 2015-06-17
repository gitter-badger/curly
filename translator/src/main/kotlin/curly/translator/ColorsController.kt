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
