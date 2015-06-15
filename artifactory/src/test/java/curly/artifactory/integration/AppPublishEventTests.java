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
package curly.artifactory.integration;

import curly.artifact.ArtifactoryApplication;
import curly.artifact.integration.event.CreatedArtifactEvent;
import curly.artifact.model.Artifact;
import curly.artifactory.ArtifactoryTestHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

/**
 * @author João Evangelista
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ArtifactoryApplication.class)
public class AppPublishEventTests {

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;
	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	@SuppressWarnings("unchecked")
	public void testTagPublishEvent() throws Exception {
		Artifact artifact = ArtifactoryTestHelper.createArtifact(mongoTemplate);
		applicationEventPublisher.publishEvent(new CreatedArtifactEvent(artifact));
		Object o = amqpTemplate.receiveAndConvert("tag.queue");
		if (o instanceof Set) {
			Set<Tag> tags = (Set<Tag>) o;
			System.out.println("\n--------------->\n" + tags);
			Assert.assertEquals("must be equals", tags.size(), artifact.getTags().size());

		} else {
			System.err.println("not processable instance");
			Assert.assertTrue(false);
		}
	}

	@Test
	public void testPublishArtifactToNotifier() throws Exception {
		Artifact artifact = ArtifactoryTestHelper.createArtifact(mongoTemplate);
		applicationEventPublisher.publishEvent(new CreatedArtifactEvent(artifact));
		Object o = amqpTemplate.receiveAndConvert("artifactory.notification.queue");
		System.out.println(o);
		Assert.assertTrue("not processable instance", o instanceof Artifact);

		Artifact received = ((Artifact) o);
		System.out.println("\n--------------->\n" + received);
		Assert.assertEquals("must be equals", received, artifact);


	}
}
