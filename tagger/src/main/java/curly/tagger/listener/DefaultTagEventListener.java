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

import curly.commons.config.reactor.Reactor;
import curly.tagger.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.bus.Event;
import reactor.bus.EventBus;

import javax.inject.Inject;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * @author João Evangelista
 */
@Slf4j
@Component
public class DefaultTagEventListener {

	private final EventBus eventBus;

	@Inject
	DefaultTagEventListener(@Reactor EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@RabbitListener(queues = "tag.queue")
	public void onReceive(@Payload Message<Set<Tag>> message) {
		if (message != null) {
			Set<TagMessage> messages = message.getPayload().stream().map(TagMessage::from).collect(toSet());
			log.info("Received messages {} on tag.queue, notifying Bus...", messages);
			eventBus.notify("tag.bus", Event.wrap(messages));
		} else {
			log.error("Cannot use NULL message!!");
		}

	}


}
