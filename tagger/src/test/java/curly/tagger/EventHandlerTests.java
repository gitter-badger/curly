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

import com.google.common.collect.ImmutableSet;
import curly.tagger.model.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

/**
 * Integration tests for
 * {@link curly.tagger.listener.EventHandler}
 * {@link curly.tagger.listener.DefaultTagEventListener}
 *
 * @author João Evangelista
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TaggerApplication.class)
public class EventHandlerTests {

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEventHandling() throws Exception {
		long before = count();
		amqpTemplate.convertAndSend("tag.queue", createTag());
		Thread.sleep(5000); //wait some time to check again
		long after = count();
		System.out.println("Before: " + before);
		System.out.println("After: " + after);
		Assert.assertTrue("Counts must differ", after > before);
	}


	private long count() {
		return mongoTemplate.count(new Query(), Tag.class);
	}

	private Set<Tag> createTag() {
		Tag tag = new Tag(null, "sometag");
		Tag tag2 = new Tag(null, "sometag2");
		return ImmutableSet.<Tag>builder().add(tag, tag2).build();

	}
}
