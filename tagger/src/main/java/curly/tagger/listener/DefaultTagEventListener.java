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
package curly.tagger.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import curly.commons.config.reactor.Reactor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.bus.Event;
import reactor.bus.EventBus;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author João Evangelista
 */
@Slf4j
@Component
public class DefaultTagEventListener {

	private final EventBus eventBus;
	private final ObjectMapper objectMapper;

	@Inject
	DefaultTagEventListener(@Reactor EventBus eventBus, ObjectMapper objectMapper) {
		Assert.notNull(eventBus, "EventBus must be not null!");
		Assert.notNull(objectMapper, "ObjectMapper must be not null!");
		this.eventBus = eventBus;
		this.objectMapper = objectMapper;
	}

	@RabbitListener(queues = "tag.queue")
	public void onReceive(@Nullable Message message) throws IOException {
		if (message != null) {
			TagMessage[] values = objectMapper.readValue(message.getBody(), TagMessage[].class);
			Set<TagMessage> tagMessages = new HashSet<>(Arrays.asList(values));
			log.info("Received messages {} on tag.queue, notifying Bus...", tagMessages);
			eventBus.notify("tag.bus", Event.wrap(tagMessages));
		} else {
			log.warn("Cannot process null message occurred on " + LocalDateTime.now());
		}
	}
}



