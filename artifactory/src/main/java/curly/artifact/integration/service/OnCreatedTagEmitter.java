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
package curly.artifact.integration.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import curly.artifact.model.Artifact;
import curly.commons.logging.annotation.Loggable;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Tries to emmit the artifact who created a tag using amqp,
 * if it fails for some reason, will fallback sending using HTTP
 *
 * @author João Evangelista
 */
@Service
public class OnCreatedTagEmitter implements EventEmitter<Artifact> {

	private final AmqpTemplate amqpTemplate;
	private final TaggerClient taggerClient;

	@Autowired
	public OnCreatedTagEmitter(AmqpTemplate amqpTemplate, TaggerClient taggerClient) {
		Assert.notNull(amqpTemplate, "AmqpTemplate, must be not null!");
		Assert.notNull(taggerClient, "TaggerClient, must be not null!");
		this.amqpTemplate = amqpTemplate;
		this.taggerClient = taggerClient;
	}

	@Override
	@Loggable
	@Retryable
	@HystrixCommand(fallbackMethod = "emitEventFallback")
	public void emit(Artifact artifact) {
		amqpTemplate.convertAndSend("tag.queue", artifact.getTags());
	}

	@Loggable
	@Retryable
	@HystrixCommand
	public void emitEventFallback(Object source) {
		if (source instanceof Artifact) {
			taggerClient.postEvent(((Artifact) source).getTags());
		}
	}
}
