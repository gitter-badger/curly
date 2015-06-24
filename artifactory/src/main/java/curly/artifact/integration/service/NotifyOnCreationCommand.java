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
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author João Evangelista
 */
@Slf4j
@Component
public class NotifyOnCreationCommand implements EventEmitter<Artifact> {

	private final AmqpTemplate amqpTemplate;

	private final NotifierClient notifierClient;

	@Autowired
	public NotifyOnCreationCommand(AmqpTemplate amqpTemplate, NotifierClient notifierClient) {
		Assert.notNull(amqpTemplate, "AmqpTemplate, must be not null!");
		Assert.notNull(notifierClient, "NotifierClient, must be not null!");
		this.amqpTemplate = amqpTemplate;
		this.notifierClient = notifierClient;
	}

	@Override
	@Loggable
	@Retryable
	@HystrixCommand(fallbackMethod = "emitFallback")
	public void emit(Artifact artifact) {
		log.info("Emitting artifact to notification system");
		amqpTemplate.convertAndSend("artifactory.notification.queue", artifact);
	}

	@Retryable
	@HystrixCommand
	public void emitFallback(Object object) {
		if (object instanceof Artifact) {
			ResponseEntity<?> responseEntity = notifierClient.postNotification(((Artifact) object));
			log.info("Fallback emitted event through HTTP response is {}", responseEntity.getStatusCode().value());
		}

	}


}