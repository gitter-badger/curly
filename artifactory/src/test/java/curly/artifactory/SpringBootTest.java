package curly.artifactory;

import curly.ArtifactoryApplication;
import curly.artifact.Artifact;
import curly.artifact.Category;
import curly.artifact.Language;
import curly.artifact.Type;
import curly.commons.github.OctoUser;
import org.bson.types.ObjectId;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

    public OctoUser octoUser() {
        return new OctoUser("evangelistajoaop@gmail.com", true, 6969, 0, 1, 10, "http://example.com/example.jpg", "",
                "", "", "", "", "joaoevangelista", "Joao Pedro Evangelista", OctoUser.TYPE_USER, "http://example.com");
    }


    public Artifact createArtifact(MongoTemplate mongoTemplate) {
        String id = new ObjectId().toHexString();
        Artifact artifact = new Artifact();
        artifact.setName("A lovers project");
        Set<Language> languages = new HashSet<>(0);
        Set<Type> types = new HashSet<>(0);
        types.add(new Type("document"));
        types.add(new Type("nosql"));
        languages.add(new Language("java"));
        languages.add(new Language("groovy"));
        languages.add(new Language("ruby"));
        artifact.setId(id);
        artifact.setAuthor("joaoevangelista");
        artifact.setCategory(new Category("database"));
        artifact.setHomePage("http://example.com");
        artifact.setIncubation(LocalDate.now().toString());
        artifact.setLanguages(languages);
        artifact.setTypes(types);
        artifact.setOwner(new ObjectId().toHexString());
        mongoTemplate.insert(artifact);
        return artifact;
    }

}
