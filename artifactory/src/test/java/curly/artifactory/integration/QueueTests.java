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
import curly.artifact.model.Artifact;
import curly.artifactory.ArtifactoryTestHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

/**
 * @author João Evangelista
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ArtifactoryApplication.class)
public class QueueTests {

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void testSendAndReceiveOnQueue() throws Exception {
		Artifact artifact = ArtifactoryTestHelper.createArtifact(mongoTemplate);

		amqpTemplate.convertAndSend("tag.queue", artifact.getTags());
		@SuppressWarnings("unchecked") Set<Tag> convert = (Set<Tag>) amqpTemplate.receiveAndConvert("tag.queue");
		System.out.println("----------------------------------------------------------------------------------------------------->" + convert);
		Assert.assertEquals("Must be same size", convert.size(), artifact.getTags().size());

	}
}

