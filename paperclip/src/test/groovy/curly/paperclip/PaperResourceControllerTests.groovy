package curly.paperclip

import curly.paperclip.paper.Paper
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

/**
 * @author Joao Pedro Evangelista
 */
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
        mockMvc = webAppContextSetup(applicationContext).build()
        println paper
    }


    @Test
    public void testGetOneByItem() throws Exception {
        mockMvc.perform(
                asyncDispatch(
                        mockMvc.perform(get("/papers/{id}", paper.item))
                                .andExpect(request().asyncStarted())
                                .andExpect(request().asyncResult(ResponseEntity.ok(paper)))
                                .andReturn()
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(json(paper, new MappingJackson2HttpMessageConverter())))

    }
}
