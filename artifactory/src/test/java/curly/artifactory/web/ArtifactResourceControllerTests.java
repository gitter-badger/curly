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
package curly.artifactory.web;

import curly.artifact.model.Artifact;
import curly.artifact.web.ArtifactResourceAssembler;
import curly.artifactory.ArtifactoryTestHelper;
import curly.commons.web.hateoas.MediaTypes;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Joao Pedro Evangelista
 */
public class ArtifactResourceControllerTests extends ArtifactoryTestHelper {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private ArtifactResourceAssembler assembler;

	private MockMvc mockMvc;

	private Artifact artifact;

	private MappingJackson2HttpMessageConverter messageConverter;

	@Value("${server.port}")
	private Integer port;

	@Before
	public void setUp() throws Exception {
		this.messageConverter = new MappingJackson2HttpMessageConverter();
		this.artifact = createArtifact(mongoTemplate);
		this.mockMvc = webAppContextSetup(webApplicationContext)
			/*	.apply(
						documentationConfiguration()
								.uris().withHost("localhost").withPort(port).withScheme(" http")
								.and()
								.snippets().withEncoding(StandardCharsets.UTF_8.name()))

			*/.alwaysDo(print())
				//		.alwaysDo(document("artifactory"))
				.alwaysExpect(status().is2xxSuccessful())
				.build();
	}

	@Test
	public void testArtifactResources() throws Exception {
		mockMvc.perform(
				asyncDispatch(
						mockMvc.perform(get("/artifacts"))
								.andExpect(request().asyncStarted())
								.andReturn()));

	}

	@Test
	public void testArtifactResource() throws Exception {
		String id = artifact.getId();
		Artifact byId = mongoTemplate.findById(id, Artifact.class);
		mockMvc.perform(
				asyncDispatch(
						mockMvc.perform(get("/artifacts/{id}", id))
								.andExpect(status().isOk())
								.andExpect(request().asyncStarted())
								.andExpect(request().asyncResult(ResponseEntity.ok(assembler.toResource(byId))))
								.andReturn()))
				.andExpect(content().contentType(MediaTypes.HAL_JSON));
	}

	@Test
	public void testSaveResource() throws Exception {
		mockMvc.perform(
				asyncDispatch(
						mockMvc.perform(post("/artifacts")
								.contentType(MediaType.APPLICATION_JSON)
								.content(json(artifact, messageConverter))
								.principal(octoUser()))
								.andExpect(request().asyncStarted())
								.andExpect(request().asyncResult(new ResponseEntity<>(HttpStatus.CREATED)))
								.andReturn()));
	}

	@Test
	public void testDeleteResource() throws Exception {
		String id = artifact.getId();
		mockMvc.perform(
				asyncDispatch(
						mockMvc.perform(delete("/artifacts/{id}", id)
								.principal(octoUser()))
								.andExpect(request().asyncStarted())
								.andExpect(request().asyncResult(new ResponseEntity<>(HttpStatus.NO_CONTENT)))
								.andReturn()));
	}
}

