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

import curly.commons.github.OctoUser;
import curly.tagger.model.Tag;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * @author João Evangelista
 */
@RunWith(SpringJUnit4ClassRunner.class)
@OAuth2ContextConfiguration
@SpringApplicationConfiguration(classes = {TaggerApplication.class})
@WebIntegrationTest
public class TagHelper {

	public static String json(Object o, HttpMessageConverter<Object> httpMessageConverter) {
		MockHttpOutputMessage message = new MockHttpOutputMessage();
		try {
			httpMessageConverter.write(o, jsonMediaType(), message);
		} catch (IOException ignore) {
		}
		return message.getBodyAsString();
	}

	public static MediaType jsonMediaType() {
		return new MediaType(MediaType.APPLICATION_JSON.getType(),
				MediaType.APPLICATION_JSON.getSubtype());
	}

	public OctoUser octoUser() {
		return new OctoUser("evangelistajoaop@gmail.com", true, 6969, 0, 1, 10, "http://example.com/example.jpg", "",
				"", "", "", "", "joaoevangelista", "Joao Pedro Evangelista", OctoUser.TYPE_USER, "http://example.com");
	}

	public Tag createTag(MongoTemplate mongoTemplate) {
		mongoTemplate.findAllAndRemove(new Query(), Tag.class);
		Tag tag = new Tag(null, "love");
		mongoTemplate.insert(tag);
		Assert.notNull(tag.getId(), "Must be generate!");
		return tag;
	}
}
