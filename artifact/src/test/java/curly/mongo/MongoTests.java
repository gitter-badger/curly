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
@SuppressWarnings("ObjectAllocationInLoop")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ArtifactApplication.class})
public class MongoTests {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void testInsert() throws Exception {
        for (int i = 0; i < 10; i++) {
            Artifact artifact = new Artifact();
            Set<Language> languages = new HashSet<>(4);
            Set<Type> types = new HashSet<>(4);
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
