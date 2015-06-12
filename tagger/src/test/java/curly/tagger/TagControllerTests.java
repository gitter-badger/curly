package curly.tagger;

import curly.tagger.model.Tag;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for {@link curly.tagger.controller.TagController}
 *
 * @author João Evangelista
 */
public class TagControllerTests extends TagHelper {

	private Tag tag;
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private MongoTemplate mongoTemplate;

	private static String withControllerPath(String fragment) {
		return "/tags" + fragment;
	}

	@Before
	public void setUp() throws Exception {
		this.tag = createTag(mongoTemplate);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.alwaysDo(print())
				.alwaysExpect(status().is2xxSuccessful())
				.build();
	}

	@Test
	public void testGet() throws Exception {
		mockMvc.perform(asyncDispatch(mockMvc.perform(get(withControllerPath("/{tag}"), "love"))
				.andExpect(request().asyncStarted())
				.andExpect(request().asyncResult(ResponseEntity.ok(tag)))
				.andReturn()))
				.andExpect(content().contentType(jsonMediaType()))
				.andExpect(content().json(json(tag, new MappingJackson2HttpMessageConverter())));
	}

	@Test
	public void testSearch() throws Exception {
		mockMvc.perform(
				asyncDispatch(
						mockMvc.perform(get(withControllerPath("/search"))
								.param("q", "lov"))
								.andExpect(request().asyncStarted())
								.andReturn()
				));

	}
}
