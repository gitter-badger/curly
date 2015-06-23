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
package curly.artifactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import curly.artifact.ArtifactoryApplication;
import curly.artifact.model.Artifact;
import curly.artifact.model.Category;
import curly.artifact.model.Language;
import curly.artifact.model.Tag;
import curly.commons.github.OctoUser;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Joao Pedro Evangelista
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@OAuth2ContextConfiguration
@SpringApplicationConfiguration(classes = {ArtifactoryApplication.class})
@WebIntegrationTest
public abstract class ArtifactoryTestHelper {


	public static String json(Object o, @NotNull HttpMessageConverter<Object> httpMessageConverter) {
		MockHttpOutputMessage message = new MockHttpOutputMessage();
		try {
			httpMessageConverter.write(o, jsonMediaType(), message);
		} catch (IOException ignore) {
		}
		return message.getBodyAsString();
	}

	@NotNull
	public static MediaType jsonMediaType() {
		return new MediaType(MediaType.APPLICATION_JSON.getType(),
				MediaType.APPLICATION_JSON.getSubtype(),
				Charset.forName("utf-8"));
	}

	@NotNull
	public static Artifact createArtifact(@NotNull MongoTemplate mongoTemplate) {
		mongoTemplate.findAllAndRemove(new Query(), Artifact.class);
		Artifact artifact = new Artifact();
		Set<Language> languages = new HashSet<>(0);
		Set<Tag> tags = new HashSet<>(0);
		tags.add(new Tag("document"));
		tags.add(new Tag("nosql"));
		languages.add(new Language("java"));
		languages.add(new Language("groovy"));
		languages.add(new Language("ruby"));
		languages.add(new Language("scala"));
		languages.add(new Language("javascript"));
		artifact.setName("curly");
		artifact.setAuthor("joaoevangelista");
		artifact.setCategory(new Category("database"));
		artifact.setHomePage("http://example.com");
		artifact.setIncubation(LocalDate.now().toString());
		artifact.setLanguages(languages);
		artifact.setTags(tags);
		mongoTemplate.insert(artifact);
		Assert.assertTrue(artifact.getId() != null);
		try {
			System.out.println(new ObjectMapper().writeValueAsString(artifact));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return artifact;
	}

	@NotNull
	public OctoUser octoUser() {
		return new OctoUser("evangelistajoaop@gmail.com", true, 6969, 0, 1, 10, "http://example.com/example.jpg", "",
				"", "", "", "", "joaoevangelista", "Joao Pedro Evangelista", OctoUser.TYPE_USER, "http://example.com");
	}
}
