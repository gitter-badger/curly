package curly.formula.controller

import java.io.IOException

import curly.formula.{Category, FormulaApplication}
import org.junit.runner.RunWith
import org.junit.{Before, Test}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.{SpringApplicationConfiguration, WebIntegrationTest}
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.{MediaType, ResponseEntity}
import org.springframework.mock.http.MockHttpOutputMessage
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders._
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.{request, _}
import org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import org.springframework.web.context.WebApplicationContext

/**
 *
 * @author João Evangelista
 */
@DirtiesContext
@WebIntegrationTest
@OAuth2ContextConfiguration
@RunWith(classOf[SpringJUnit4ClassRunner])
@SpringApplicationConfiguration(classes = Array(classOf[FormulaApplication]))
class CategoryControllerTests {

  @Autowired private var wac: WebApplicationContext = null
  @Autowired private var mongoTemplate: MongoTemplate = null
  private var mockMvc: MockMvc = null
  private var category: Category = null


  @Before def setup(): Unit = {

    mongoTemplate.findAllAndRemove(new Query(), classOf[Category])
    this.mockMvc = webAppContextSetup(wac).build()
    this.category = createCat
  }

  def createCat = {
    val c = new Category("full-stack")
    mongoTemplate.save(c)
    c
  }

  @Test
  @throws[Exception]
  def testGet(): Unit = {
    mockMvc.perform(asyncDispatch(mockMvc.perform(get("/categories/{category}", category.name))
      .andExpect(request().asyncStarted())
      .andExpect(request().asyncResult(ResponseEntity.ok(category)))
      .andDo(print())
      .andReturn()))
      .andDo(print())
      .andExpect(status().is2xxSuccessful())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(toJson(category)))
  }

  def toJson(o: AnyRef): String = {
    val message: MockHttpOutputMessage = new MockHttpOutputMessage
    try {
      new MappingJackson2HttpMessageConverter().write(o, MediaType.APPLICATION_JSON, message)
    }
    catch {
      case ignore: IOException =>

    }
    message.getBodyAsString
  }

  @Test
  @throws[Exception]
  def testSearch(): Unit = {
    mockMvc.perform(asyncDispatch(mockMvc.perform(get("/categories/search").param("q", "full"))
      .andExpect(request().asyncStarted())
      .andExpect(request().asyncResult(ResponseEntity.ok(Set(category))))
      .andDo(print())
      .andReturn()))
      .andDo(print())
      .andExpect(status().is2xxSuccessful())
  }

}
