package curly.paperclip

import curly.paperclip.paper.Paper
import curly.paperclip.paper.RawPaper
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
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
class PaperclipControllerTests extends SpringBootTestAdapter {

    private Paper paper;
    private RawPaper rawPaper = new RawPaper("6969", "1234", "#{urly\n" +
            "\n" +
            "![Logo](https://raw.githubusercontent.com/joaoevangelista/curly/master/src/logo60.png)\n" +
            "\n" +
            "[![Build Status](https://travis-ci.org/joaoevangelista/curly.svg)](https://travis-ci.org/joaoevangelista/curly)\n" +
            "\n" +
            "[![forthebadge](http://forthebadge.com/images/badges/compatibility-betamax.svg)](http://forthebadge.com)\n" +
            "\n" +
            "[![forthebadge](http://forthebadge.com/images/badges/built-with-love.svg)](http://forthebadge.com)\n" +
            "\n" +
            "[![forthebadge](http://forthebadge.com/images/badges/gluten-free.svg)](http://forthebadge.com)")

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WebApplicationContext applicationContext;

    private MockMvc mockMvc;

    private MappingJackson2HttpMessageConverter messageConverter;

    @Before
    public void setUp() throws Exception {
        messageConverter = new MappingJackson2HttpMessageConverter()
        paper = paper(mongoTemplate)
        mockMvc = webAppContextSetup(applicationContext).build()
    }

    @Test
    public void testContext() throws Exception {
    }

    @Test
    public void testGetPaper() throws Exception {
        mockMvc.perform(
                asyncDispatch(
                        mockMvc.perform(get("/artifact/{artifact}", paper.artifact()))
                                .andExpect(request().asyncStarted())
                                .andReturn()
                )
        )
                .andExpect(status().isOk())
                .andExpect(content().json(json(paper, messageConverter)))
    }
}
