package curly.mongo;

import curly.ArtifactApplication;
import curly.artifact.Artifact;
import curly.artifact.Category;
import curly.artifact.Language;
import curly.artifact.Type;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author João Pedro Evangelista
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ArtifactApplication.class})
public class MongoTests {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void testInsert() throws Exception {
        for (int i = 0; i < 10; i++) {
            Artifact artifact = new Artifact();
            Set<Language> languages = new HashSet<>();
            Set<Type> types = new HashSet<>();
            types.add(new Type("document"));
            types.add(new Type("nosql"));
            languages.add(new Language("java"));
            languages.add(new Language("groovy"));
            languages.add(new Language("ruby"));
            artifact.setName("Curly");
            artifact.setAuthor("joaoevangelista");
            artifact.setCategory(new Category("database"));
            artifact.setHomePage("http://example.com");
            artifact.setIncubation(LocalDate.now().toString());
            artifact.setLanguages(languages);
            artifact.setTypes(types);
            artifact.setOwner(new ObjectId().toHexString());
            mongoTemplate.insert(artifact);

        }
        System.out.println(mongoTemplate.findAll(Artifact.class));
    }
}
