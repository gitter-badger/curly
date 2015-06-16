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
package curly.tagger;

import curly.tagger.model.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@OAuth2ContextConfiguration
@SpringApplicationConfiguration(classes = {TaggerApplication.class})
@WebIntegrationTest
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
