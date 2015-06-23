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
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author João Evangelista
 */
@Slf4j
@Service
public class OnCreatedCategoryEmitter implements EventEmitter<Artifact> {

	private final AmqpTemplate amqpTemplate;

	private final CategoryClient categoryClient;

	@Autowired
	public OnCreatedCategoryEmitter(AmqpTemplate amqpTemplate, CategoryClient categoryClient) {
		Assert.notNull(amqpTemplate, "AmqpTemplate, must be not null!");
		Assert.notNull(categoryClient, "CategoryClient, must be not null!");
		this.amqpTemplate = amqpTemplate;
		this.categoryClient = categoryClient;
	}


	@Override
	@Loggable
	@Retryable
	@HystrixCommand(fallbackMethod = "emitFallback")
	public void emit(@NotNull Artifact artifact) {
		log.info("Emitting event of source {}", artifact);
		amqpTemplate.convertAndSend("category.queue", artifact.getCategory());
	}


	@Loggable
	@Retryable
	@HystrixCommand
	public void emitFallback(Object source) {
		Artifact artifact = (Artifact) source;
		log.info("Fallback starting, sending via HTTP, source {}", artifact);
		categoryClient.postEvent(artifact.getCategory());
	}
}
