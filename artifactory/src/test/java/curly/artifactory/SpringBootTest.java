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

import curly.ArtifactoryApplication;
import curly.artifact.Artifact;
import curly.artifact.Category;
import curly.artifact.Language;
import curly.artifact.Type;
import curly.commons.github.OctoUser;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Joao Pedro Evangelista
 */
@RunWith(SpringJUnit4ClassRunner.class)
@OAuth2ContextConfiguration
@SpringApplicationConfiguration(classes = {ArtifactoryApplication.class})
@WebIntegrationTest
public abstract class SpringBootTest {


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

    public OctoUser octoUser() {
        return new OctoUser("evangelistajoaop@gmail.com", true, 6969, 0, 1, 10, "http://example.com/example.jpg", "",
                "", "", "", "", "joaoevangelista", "Joao Pedro Evangelista", OctoUser.TYPE_USER, "http://example.com");
    }

    public Artifact createArtifact(MongoTemplate mongoTemplate) {
        Artifact artifact = new Artifact();
        Set<Language> languages = new HashSet<>(0);
        Set<Type> types = new HashSet<>(0);
        types.add(new Type("document"));
        types.add(new Type("nosql"));
        languages.add(new Language("java"));
        languages.add(new Language("groovy"));
        languages.add(new Language("ruby"));
        artifact.setName("A lovers project");
        artifact.setAuthor("joaoevangelista");
        artifact.setCategory(new Category("database"));
        artifact.setHomePage("http://example.com");
        artifact.setIncubation(LocalDate.now().toString());
        artifact.setLanguages(languages);
        artifact.setTypes(types);
        mongoTemplate.insert(artifact);
        return artifact;
    }
}
