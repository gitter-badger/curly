package curly.paperclip
import curly.commons.github.OctoUser
import curly.paperclip.paper.Paper
import org.junit.runner.RunWith
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.mock.http.MockHttpOutputMessage
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.nio.charset.Charset
/**
 * @author Joao Pedro Evangelista
 */
@WebIntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@OAuth2ContextConfiguration
@SpringApplicationConfiguration(classes = [PaperclipApplication.class])
public abstract class SpringBootTestAdapter {


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
                MediaType.APPLICATION_JSON.getSubtype(),
                Charset.forName("utf-8"));
    }

    public static OctoUser octoUser() {
        return new OctoUser("evangelistajoaop@gmail.com", true, 6969, 0, 1, 10, "http://example.com/example.jpg", "",
                "", "", "", "", "joaoevangelista", "Joao Pedro Evangelista", OctoUser.TYPE_USER, "http://example.com");
    }

    static Paper paper(MongoTemplate mongoTemplate) {
        Paper paper = new Paper("6969", "1234", "http://example.com/1234/PAPER.md")
        mongoTemplate.insert(paper)
        paper
    }
}
