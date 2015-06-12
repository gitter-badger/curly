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
