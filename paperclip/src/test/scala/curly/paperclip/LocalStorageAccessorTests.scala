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
package curly.paperclip

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders._
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

/**
 * @author Joao Pedro Evangelista
 */
class LocalStorageAccessorTests extends SpringBootTests {

  val mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
  @Autowired() var wac: WebApplicationContext = null

  @Test
  def testRead(): Unit = {
    mockMvc.perform(
      asyncDispatch(
        mockMvc.perform(put("/pickpocket")
          .principal(octoUser))
          .andReturn()
      )
    )
  }

}