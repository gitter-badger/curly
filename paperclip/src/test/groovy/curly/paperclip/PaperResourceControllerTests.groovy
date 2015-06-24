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
import curly.paperclip.paper.model.Paper
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
/**
 * @author Joao Pedro Evangelista
 */
@DirtiesContext
class PaperResourceControllerTests extends SpringBootTestAdapter {

    private MockMvc mockMvc

    private @Autowired
    WebApplicationContext applicationContext

    private @Autowired
    MongoTemplate mongoTemplate

    private Paper paper

    @Before
    public void setUp() throws Exception {
        paper = createPaper(mongoTemplate)
        mockMvc = webAppContextSetup(applicationContext)
                .alwaysDo(print())
                .alwaysExpect(status().is2xxSuccessful())
                .build()
    }


    @Test
    public void testGetOneByItem() throws Exception {
        mockMvc.perform(
                asyncDispatch(
                        mockMvc.perform(get("/papers/{id}", paper.item).header("Version", "curly/internal.v1"))
                                .andExpect(request().asyncStarted())
                                .andExpect(request().asyncResult(ResponseEntity.ok(paper)))
                                .andReturn()))
                .andExpect(status().isOk())

    }

    @Test
    public void testGetOneByItemAndOwner() throws Exception {
        mockMvc.perform(asyncDispatch(
                mockMvc.perform(get("/papers/owner/{item}", paper.item).header("Version", "curly/internal.v1")
                        .principal(octoUser()))
                        .andExpect(request().asyncStarted())
                        .andExpect(request().asyncResult(ResponseEntity.ok(paper)))
                        .andReturn()))
                .andExpect(status().isOk())
                .andExpect(content().json(json(paper, new MappingJackson2HttpMessageConverter())))
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(asyncDispatch(
                mockMvc.perform(delete("/papers/{item}", paper.item).header("Version", "curly/internal.v1")
                        .principal(octoUser()))
                        .andExpect(request().asyncStarted())
                        .andExpect(request().asyncResult(new ResponseEntity(HttpStatus.NO_CONTENT)))
                        .andReturn()))
    }

    @Test
    public void testSave() throws Exception {
        mockMvc.perform(asyncDispatch(
                mockMvc.perform(post("/papers").header("Version", "curly/internal.v1")
                        .principal(octoUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(paper, new MappingJackson2HttpMessageConverter())))
                        .andExpect(request().asyncStarted())
                        .andExpect(request().asyncResult(new ResponseEntity(HttpStatus.CREATED)))
                        .andReturn()
        ))

    }
}
